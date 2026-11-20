package com.resumecraft.servlet;

import com.resumecraft.dao.ProjectDAO;
import com.resumecraft.model.Project;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * CRUD operations for project entries.
 * GET  — list all projects for the logged-in user.
 * POST — add / update / delete a project based on the "action" parameter.
 */
public class ProjectServlet extends HttpServlet {

    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = getSessionUserId(req);
        if (userId == -1) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            List<Project> projectList = projectDAO.getByUserId(userId);
            req.setAttribute("projectList", projectList);
            req.getRequestDispatcher("/jsp/resume/builder.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load project data.");
            req.getRequestDispatcher("/jsp/common/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = getSessionUserId(req);
        if (userId == -1) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "add";

        try {
            switch (action) {
                case "add":
                    addProject(req, userId);
                    req.getSession().setAttribute("success", "Project added successfully.");
                    break;
                case "update":
                    updateProject(req, userId);
                    req.getSession().setAttribute("success", "Project updated successfully.");
                    break;
                case "delete":
                    int projectId = Integer.parseInt(req.getParameter("projectId"));
                    projectDAO.deleteProject(projectId, userId);
                    req.getSession().setAttribute("success", "Project deleted successfully.");
                    break;
                default:
                    req.getSession().setAttribute("error", "Invalid action.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "Invalid data provided.");
        } catch (IllegalArgumentException e) {
            req.getSession().setAttribute("error", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/projects");
    }

    private void addProject(HttpServletRequest req, int userId) throws SQLException {
        Project proj = new Project();
        proj.setUserId(userId);
        proj.setProjectName(req.getParameter("projectName"));
        proj.setDescription(req.getParameter("description"));
        proj.setTechnologies(req.getParameter("technologies"));
        proj.setProjectUrl(req.getParameter("projectUrl"));
        proj.setStartDate(parseDate(req.getParameter("startDate")));
        proj.setEndDate(parseDate(req.getParameter("endDate")));

        // Validation
        if (proj.getProjectName() == null || proj.getProjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name is required.");
        }
        projectDAO.addProject(proj);
    }

    private void updateProject(HttpServletRequest req, int userId) throws SQLException {
        Project proj = new Project();
        proj.setProjectId(Integer.parseInt(req.getParameter("projectId")));
        proj.setUserId(userId);
        proj.setProjectName(req.getParameter("projectName"));
        proj.setDescription(req.getParameter("description"));
        proj.setTechnologies(req.getParameter("technologies"));
        proj.setProjectUrl(req.getParameter("projectUrl"));
        proj.setStartDate(parseDate(req.getParameter("startDate")));
        proj.setEndDate(parseDate(req.getParameter("endDate")));
        projectDAO.updateProject(proj);
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return Date.valueOf(dateStr.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private int getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) return -1;
        return (int) session.getAttribute("userId");
    }
}
