package com.resumecraft.servlet;

import com.resumecraft.dao.*;
import com.resumecraft.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Resume preview — gathers all user data and resume config,
 * then renders the resume with the chosen template.
 */
public class ResumePreviewServlet extends HttpServlet {

    private final ResumeDAO resumeDAO             = new ResumeDAO();
    private final UserDAO userDAO                 = new UserDAO();
    private final EducationDAO eduDAO             = new EducationDAO();
    private final ExperienceDAO expDAO            = new ExperienceDAO();
    private final SkillDAO skillDAO               = new SkillDAO();
    private final ProjectDAO projectDAO           = new ProjectDAO();
    private final CertificationDAO certDAO        = new CertificationDAO();
    private final ResumeSectionDAO sectionDAO     = new ResumeSectionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int userId = (int) session.getAttribute("userId");

        String resumeIdParam = req.getParameter("resumeId");
        if (resumeIdParam == null || resumeIdParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        try {
            int resumeId = Integer.parseInt(resumeIdParam);
            Resume resume = resumeDAO.getById(resumeId, userId);
            if (resume == null) {
                session.setAttribute("error", "Resume not found.");
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }

            // Load all data
            User user = userDAO.getUserById(userId);
            List<Education> educationList       = eduDAO.getByUserId(userId);
            List<Experience> experienceList     = expDAO.getByUserId(userId);
            List<Skill> skillList               = skillDAO.getByUserId(userId);
            List<Project> projectList           = projectDAO.getByUserId(userId);
            List<Certification> certificationList = certDAO.getByUserId(userId);
            List<ResumeSection> sections        = sectionDAO.getByResumeId(resumeId);

            // Set all as request attributes for the JSP template
            req.setAttribute("resume", resume);
            req.setAttribute("user", user);
            req.setAttribute("educationList", educationList);
            req.setAttribute("experienceList", experienceList);
            req.setAttribute("skillList", skillList);
            req.setAttribute("projectList", projectList);
            req.setAttribute("certificationList", certificationList);
            req.setAttribute("resumeSections", sections);

            // Forward to preview page (which applies the template)
            req.getRequestDispatcher("/jsp/resume/preview.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load resume preview.");
            req.getRequestDispatcher("/jsp/common/500.jsp").forward(req, resp);
        }
    }
}
