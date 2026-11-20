package com.resumecraft.servlet;

import com.resumecraft.dao.ExperienceDAO;
import com.resumecraft.model.Experience;

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
 * CRUD operations for experience entries.
 * GET  — list all experience entries for the logged-in user.
 * POST — add / update / delete an experience entry based on the "action" parameter.
 */
public class ExperienceServlet extends HttpServlet {

    private final ExperienceDAO experienceDAO = new ExperienceDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = getSessionUserId(req);
        if (userId == -1) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            List<Experience> experienceList = experienceDAO.getByUserId(userId);
            req.setAttribute("experienceList", experienceList);
            req.getRequestDispatcher("/jsp/resume/builder.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load experience data.");
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
                    addExperience(req, userId);
                    req.getSession().setAttribute("success", "Experience added successfully.");
                    break;
                case "update":
                    updateExperience(req, userId);
                    req.getSession().setAttribute("success", "Experience updated successfully.");
                    break;
                case "delete":
                    int experienceId = Integer.parseInt(req.getParameter("experienceId"));
                    experienceDAO.deleteExperience(experienceId, userId);
                    req.getSession().setAttribute("success", "Experience deleted successfully.");
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
        resp.sendRedirect(req.getContextPath() + "/experience");
    }

    private void addExperience(HttpServletRequest req, int userId) throws SQLException {
        Experience exp = new Experience();
        exp.setUserId(userId);
        exp.setCompanyName(req.getParameter("companyName"));
        exp.setJobTitle(req.getParameter("jobTitle"));
        exp.setLocation(req.getParameter("location"));
        exp.setStartDate(parseDate(req.getParameter("startDate")));
        exp.setEndDate(parseDate(req.getParameter("endDate")));
        exp.setCurrent("on".equals(req.getParameter("isCurrent"))
                    || "true".equals(req.getParameter("isCurrent")));
        exp.setDescription(req.getParameter("description"));

        // Validation
        if (exp.getCompanyName() == null || exp.getCompanyName().trim().isEmpty()) {
            throw new IllegalArgumentException("Company name is required.");
        }
        if (exp.getJobTitle() == null || exp.getJobTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Job title is required.");
        }
        if (exp.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
        experienceDAO.addExperience(exp);
    }

    private void updateExperience(HttpServletRequest req, int userId) throws SQLException {
        Experience exp = new Experience();
        exp.setExperienceId(Integer.parseInt(req.getParameter("experienceId")));
        exp.setUserId(userId);
        exp.setCompanyName(req.getParameter("companyName"));
        exp.setJobTitle(req.getParameter("jobTitle"));
        exp.setLocation(req.getParameter("location"));
        exp.setStartDate(parseDate(req.getParameter("startDate")));
        exp.setEndDate(parseDate(req.getParameter("endDate")));
        exp.setCurrent("on".equals(req.getParameter("isCurrent"))
                    || "true".equals(req.getParameter("isCurrent")));
        exp.setDescription(req.getParameter("description"));
        experienceDAO.updateExperience(exp);
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
