package com.resumecraft.servlet;

import com.resumecraft.dao.*;
import com.resumecraft.model.Resume;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Dashboard — shows user's saved resumes and quick stats
 * (counts of education, experience, skills, projects entries).
 */
public class DashboardServlet extends HttpServlet {

    private final ResumeDAO resumeDAO         = new ResumeDAO();
    private final EducationDAO educationDAO   = new EducationDAO();
    private final ExperienceDAO experienceDAO = new ExperienceDAO();
    private final SkillDAO skillDAO           = new SkillDAO();
    private final ProjectDAO projectDAO       = new ProjectDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int userId = (int) session.getAttribute("userId");

        try {
            List<Resume> resumes = resumeDAO.getByUserId(userId);
            req.setAttribute("resumes", resumes);
            req.setAttribute("resumeCount", resumes.size());
            req.setAttribute("educationCount", educationDAO.getByUserId(userId).size());
            req.setAttribute("experienceCount", experienceDAO.getByUserId(userId).size());
            req.setAttribute("skillCount", skillDAO.getByUserId(userId).size());
            req.setAttribute("projectCount", projectDAO.getByUserId(userId).size());
            req.getRequestDispatcher("/jsp/dashboard/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load dashboard data.");
            req.getRequestDispatcher("/jsp/common/500.jsp").forward(req, resp);
        }
    }
}
