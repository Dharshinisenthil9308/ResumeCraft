package com.resumecraft.servlet;

import com.resumecraft.dao.EducationDAO;
import com.resumecraft.model.Education;

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
 * CRUD operations for education entries.
 * GET  — list all education entries for the logged-in user.
 * POST — add / update / delete an education entry based on the "action" parameter.
 */
public class EducationServlet extends HttpServlet {

    private final EducationDAO educationDAO = new EducationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = getSessionUserId(req);
        if (userId == -1) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            List<Education> educationList = educationDAO.getByUserId(userId);
            req.setAttribute("educationList", educationList);
            req.getRequestDispatcher("/jsp/resume/builder.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load education data.");
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
                    addEducation(req, userId);
                    req.getSession().setAttribute("success", "Education added successfully.");
                    break;
                case "update":
                    updateEducation(req, userId);
                    req.getSession().setAttribute("success", "Education updated successfully.");
                    break;
                case "delete":
                    int educationId = Integer.parseInt(req.getParameter("educationId"));
                    educationDAO.deleteEducation(educationId, userId);
                    req.getSession().setAttribute("success", "Education deleted successfully.");
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
        resp.sendRedirect(req.getContextPath() + "/education");
    }

    private void addEducation(HttpServletRequest req, int userId) throws SQLException {
        Education edu = new Education();
        edu.setUserId(userId);
        edu.setInstitution(req.getParameter("institution"));
        edu.setDegree(req.getParameter("degree"));
        edu.setFieldOfStudy(req.getParameter("fieldOfStudy"));
        edu.setStartDate(parseDate(req.getParameter("startDate")));
        edu.setEndDate(parseDate(req.getParameter("endDate")));
        edu.setGrade(req.getParameter("grade"));
        edu.setDescription(req.getParameter("description"));

        // Validation
        if (edu.getInstitution() == null || edu.getInstitution().trim().isEmpty()) {
            throw new IllegalArgumentException("Institution is required.");
        }
        if (edu.getDegree() == null || edu.getDegree().trim().isEmpty()) {
            throw new IllegalArgumentException("Degree is required.");
        }
        if (edu.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
        educationDAO.addEducation(edu);
    }

    private void updateEducation(HttpServletRequest req, int userId) throws SQLException {
        Education edu = new Education();
        edu.setEducationId(Integer.parseInt(req.getParameter("educationId")));
        edu.setUserId(userId);
        edu.setInstitution(req.getParameter("institution"));
        edu.setDegree(req.getParameter("degree"));
        edu.setFieldOfStudy(req.getParameter("fieldOfStudy"));
        edu.setStartDate(parseDate(req.getParameter("startDate")));
        edu.setEndDate(parseDate(req.getParameter("endDate")));
        edu.setGrade(req.getParameter("grade"));
        edu.setDescription(req.getParameter("description"));
        educationDAO.updateEducation(edu);
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
