package com.resumecraft.servlet;

import com.resumecraft.dao.*;
import com.resumecraft.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * Generates and serves a downloadable HTML document of the resume.
 * Streams a fully self-contained HTML page with inline CSS as an attachment.
 */
public class ResumeDownloadServlet extends HttpServlet {

    private final ResumeDAO resumeDAO         = new ResumeDAO();
    private final UserDAO userDAO             = new UserDAO();
    private final EducationDAO eduDAO         = new EducationDAO();
    private final ExperienceDAO expDAO        = new ExperienceDAO();
    private final SkillDAO skillDAO           = new SkillDAO();
    private final ProjectDAO projectDAO       = new ProjectDAO();
    private final CertificationDAO certDAO    = new CertificationDAO();

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
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Resume not found.");
                return;
            }

            User user = userDAO.getUserById(userId);
            List<Education> educationList       = eduDAO.getByUserId(userId);
            List<Experience> experienceList     = expDAO.getByUserId(userId);
            List<Skill> skillList               = skillDAO.getByUserId(userId);
            List<Project> projectList           = projectDAO.getByUserId(userId);
            List<Certification> certList        = certDAO.getByUserId(userId);

            // --- Stream the HTML attachment ---
            resp.setContentType("text/html; charset=UTF-8");
            resp.setHeader("Content-Disposition",
                    "attachment; filename=\"" + sanitize(resume.getResumeTitle()) + ".html\"");

            PrintWriter out = resp.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'><head><meta charset='UTF-8'>");
            out.println("<title>" + esc(resume.getResumeTitle()) + "</title>");
            out.println("<style>");
            out.println("body{font-family:'Segoe UI',Arial,sans-serif;max-width:800px;"
                    + "margin:0 auto;padding:40px;color:#333;line-height:1.6;}");
            out.println("h1{color:#2c3e50;border-bottom:3px solid #3498db;padding-bottom:10px;}");
            out.println("h2{color:#2c3e50;border-bottom:1px solid #bdc3c7;padding-bottom:5px;"
                    + "margin-top:25px;}");
            out.println(".contact{color:#7f8c8d;margin-bottom:20px;}");
            out.println(".entry{margin-bottom:15px;} .entry h3{margin:0;color:#34495e;} "
                    + ".entry .meta{color:#7f8c8d;font-size:0.9em;}");
            out.println(".skills-grid{display:flex;flex-wrap:wrap;gap:8px;} "
                    + ".skill-tag{background:#ecf0f1;padding:4px 12px;border-radius:15px;"
                    + "font-size:0.9em;}");
            out.println("</style></head><body>");

            // --- Header ---
            out.println("<h1>" + esc(user.getFullName()) + "</h1>");
            out.println("<div class='contact'>");
            if (user.getEmail() != null) out.print(esc(user.getEmail()));
            if (user.getPhone() != null && !user.getPhone().isEmpty())
                out.print(" | " + esc(user.getPhone()));
            if (user.getAddress() != null && !user.getAddress().isEmpty())
                out.print(" | " + esc(user.getAddress()));
            out.println("</div>");

            // --- Summary ---
            if (user.getSummary() != null && !user.getSummary().isEmpty()) {
                out.println("<h2>Professional Summary</h2>");
                out.println("<p>" + esc(user.getSummary()) + "</p>");
            }

            // --- Education ---
            if (!educationList.isEmpty()) {
                out.println("<h2>Education</h2>");
                for (Education edu : educationList) {
                    out.println("<div class='entry'>");
                    out.println("<h3>" + esc(edu.getDegree()) + " &mdash; "
                            + esc(edu.getInstitution()) + "</h3>");
                    out.println("<div class='meta'>");
                    if (edu.getFieldOfStudy() != null && !edu.getFieldOfStudy().isEmpty())
                        out.print(esc(edu.getFieldOfStudy()) + " | ");
                    out.print(edu.getStartDate() + " – "
                            + (edu.getEndDate() != null ? edu.getEndDate() : "Present"));
                    if (edu.getGrade() != null && !edu.getGrade().isEmpty())
                        out.print(" | Grade: " + esc(edu.getGrade()));
                    out.println("</div>");
                    if (edu.getDescription() != null && !edu.getDescription().isEmpty())
                        out.println("<p>" + esc(edu.getDescription()) + "</p>");
                    out.println("</div>");
                }
            }

            // --- Experience ---
            if (!experienceList.isEmpty()) {
                out.println("<h2>Experience</h2>");
                for (Experience exp : experienceList) {
                    out.println("<div class='entry'>");
                    out.println("<h3>" + esc(exp.getJobTitle()) + " &mdash; "
                            + esc(exp.getCompanyName()) + "</h3>");
                    out.println("<div class='meta'>");
                    if (exp.getLocation() != null && !exp.getLocation().isEmpty())
                        out.print(esc(exp.getLocation()) + " | ");
                    out.print(exp.getStartDate() + " – "
                            + (exp.isCurrent() ? "Present"
                                : (exp.getEndDate() != null ? exp.getEndDate() : "N/A")));
                    out.println("</div>");
                    if (exp.getDescription() != null && !exp.getDescription().isEmpty())
                        out.println("<p>" + esc(exp.getDescription()) + "</p>");
                    out.println("</div>");
                }
            }

            // --- Skills ---
            if (!skillList.isEmpty()) {
                out.println("<h2>Skills</h2><div class='skills-grid'>");
                for (Skill s : skillList) {
                    out.println("<span class='skill-tag'>" + esc(s.getSkillName())
                            + " (" + esc(s.getProficiency()) + ")</span>");
                }
                out.println("</div>");
            }

            // --- Projects ---
            if (!projectList.isEmpty()) {
                out.println("<h2>Projects</h2>");
                for (Project p : projectList) {
                    out.println("<div class='entry'>");
                    out.println("<h3>" + esc(p.getProjectName()) + "</h3>");
                    if (p.getTechnologies() != null && !p.getTechnologies().isEmpty())
                        out.println("<div class='meta'>Technologies: "
                                + esc(p.getTechnologies()) + "</div>");
                    if (p.getDescription() != null && !p.getDescription().isEmpty())
                        out.println("<p>" + esc(p.getDescription()) + "</p>");
                    if (p.getProjectUrl() != null && !p.getProjectUrl().isEmpty())
                        out.println("<p><a href='" + esc(p.getProjectUrl()) + "'>"
                                + esc(p.getProjectUrl()) + "</a></p>");
                    out.println("</div>");
                }
            }

            // --- Certifications ---
            if (!certList.isEmpty()) {
                out.println("<h2>Certifications</h2>");
                for (Certification c : certList) {
                    out.println("<div class='entry'>");
                    out.println("<h3>" + esc(c.getCertName()) + "</h3>");
                    out.print("<div class='meta'>" + esc(c.getIssuingOrg()));
                    if (c.getIssueDate() != null)
                        out.print(" | Issued: " + c.getIssueDate());
                    out.println("</div></div>");
                }
            }

            // --- Links ---
            StringBuilder links = new StringBuilder();
            if (user.getLinkedinUrl() != null && !user.getLinkedinUrl().isEmpty())
                links.append("<a href='").append(esc(user.getLinkedinUrl()))
                     .append("'>LinkedIn</a> ");
            if (user.getGithubUrl() != null && !user.getGithubUrl().isEmpty())
                links.append("<a href='").append(esc(user.getGithubUrl()))
                     .append("'>GitHub</a> ");
            if (user.getPortfolioUrl() != null && !user.getPortfolioUrl().isEmpty())
                links.append("<a href='").append(esc(user.getPortfolioUrl()))
                     .append("'>Portfolio</a>");
            if (links.length() > 0) {
                out.println("<h2>Links</h2><p>" + links + "</p>");
            }

            out.println("</body></html>");
            out.flush();

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to generate resume.");
        }
    }

    /** HTML-escape helper to prevent XSS in the generated document. */
    private String esc(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                   .replace("\"", "&quot;").replace("'", "&#39;");
    }

    /** Sanitise a string for use as a filename. */
    private String sanitize(String name) {
        if (name == null) return "resume";
        return name.replaceAll("[^a-zA-Z0-9\\-_ ]", "").trim().replace(" ", "_");
    }
}
