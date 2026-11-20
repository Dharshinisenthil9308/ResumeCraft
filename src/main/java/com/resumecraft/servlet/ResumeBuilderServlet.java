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
 * Resume builder — select template, choose sections, create/edit/delete resume.
 * GET  — shows the builder form (with all user data) for creating or editing a resume.
 * POST — creates, updates, or deletes a resume based on the "action" parameter.
 */
public class ResumeBuilderServlet extends HttpServlet {

    private final ResumeDAO resumeDAO               = new ResumeDAO();
    private final ResumeSectionDAO sectionDAO        = new ResumeSectionDAO();
    private final EducationDAO educationDAO          = new EducationDAO();
    private final ExperienceDAO experienceDAO        = new ExperienceDAO();
    private final SkillDAO skillDAO                  = new SkillDAO();
    private final ProjectDAO projectDAO              = new ProjectDAO();
    private final CertificationDAO certificationDAO  = new CertificationDAO();

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
            // Load all user data for the builder form
            req.setAttribute("educationList", educationDAO.getByUserId(userId));
            req.setAttribute("experienceList", experienceDAO.getByUserId(userId));
            req.setAttribute("skillList", skillDAO.getByUserId(userId));
            req.setAttribute("projectList", projectDAO.getByUserId(userId));
            req.setAttribute("certificationList", certificationDAO.getByUserId(userId));

            // If editing an existing resume, load its config
            String resumeIdParam = req.getParameter("resumeId");
            if (resumeIdParam != null && !resumeIdParam.trim().isEmpty()) {
                int resumeId = Integer.parseInt(resumeIdParam);
                Resume resume = resumeDAO.getById(resumeId, userId);
                if (resume != null) {
                    req.setAttribute("resume", resume);
                    req.setAttribute("resumeSections", sectionDAO.getByResumeId(resumeId));
                }
            }
            req.getRequestDispatcher("/jsp/resume/builder.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load resume builder.");
            req.getRequestDispatcher("/jsp/common/500.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid resume ID.");
            req.getRequestDispatcher("/jsp/resume/builder.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int userId = (int) session.getAttribute("userId");

        String action = req.getParameter("action");
        if (action == null) action = "create";

        try {
            switch (action) {
                case "create": {
                    Resume resume = new Resume();
                    resume.setUserId(userId);
                    String title = req.getParameter("resumeTitle");
                    resume.setResumeTitle(
                        title != null && !title.trim().isEmpty() ? title.trim() : "My Resume");
                    String template = req.getParameter("templateName");
                    resume.setTemplateName(
                        template != null && !template.trim().isEmpty() ? template.trim() : "classic");
                    resume.setPublic("on".equals(req.getParameter("isPublic")));

                    int resumeId = resumeDAO.createResume(resume);
                    if (resumeId > 0) {
                        saveSections(req, resumeId);
                        resp.sendRedirect(
                            req.getContextPath() + "/resume/preview?resumeId=" + resumeId);
                    } else {
                        session.setAttribute("error", "Failed to create resume.");
                        resp.sendRedirect(req.getContextPath() + "/resume/build");
                    }
                    break;
                }
                case "update": {
                    int resumeId = Integer.parseInt(req.getParameter("resumeId"));
                    Resume resume = resumeDAO.getById(resumeId, userId);
                    if (resume == null) {
                        session.setAttribute("error", "Resume not found.");
                        resp.sendRedirect(req.getContextPath() + "/dashboard");
                        return;
                    }
                    String title = req.getParameter("resumeTitle");
                    resume.setResumeTitle(
                        title != null && !title.trim().isEmpty() ? title.trim() : resume.getResumeTitle());
                    String template = req.getParameter("templateName");
                    resume.setTemplateName(
                        template != null && !template.trim().isEmpty() ? template.trim() : resume.getTemplateName());
                    resume.setPublic("on".equals(req.getParameter("isPublic")));
                    resumeDAO.updateResume(resume);

                    // Replace sections: delete old, save new
                    sectionDAO.deleteByResumeId(resumeId);
                    saveSections(req, resumeId);

                    session.setAttribute("success", "Resume updated successfully.");
                    resp.sendRedirect(
                        req.getContextPath() + "/resume/preview?resumeId=" + resumeId);
                    break;
                }
                case "delete": {
                    int resumeId = Integer.parseInt(req.getParameter("resumeId"));
                    resumeDAO.deleteResume(resumeId, userId);
                    session.setAttribute("success", "Resume deleted successfully.");
                    resp.sendRedirect(req.getContextPath() + "/dashboard");
                    break;
                }
                default:
                    resp.sendRedirect(req.getContextPath() + "/dashboard");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Database error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/resume/build");
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid resume ID.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }

    /**
     * Saves resume section preferences from a multi-select "sections" parameter.
     * Each selected value (e.g. "education", "experience") becomes a ResumeSection row.
     */
    private void saveSections(HttpServletRequest req, int resumeId) throws SQLException {
        String[] sectionTypes = req.getParameterValues("sections");
        if (sectionTypes != null) {
            int order = 1;
            for (String type : sectionTypes) {
                ResumeSection section = new ResumeSection();
                section.setResumeId(resumeId);
                section.setSectionType(type.trim());
                section.setSectionOrder(order++);
                section.setVisible(true);
                sectionDAO.addSection(section);
            }
        }
    }
}
