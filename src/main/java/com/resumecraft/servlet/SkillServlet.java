package com.resumecraft.servlet;

import com.resumecraft.dao.SkillDAO;
import com.resumecraft.model.Skill;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * CRUD operations for skill entries.
 * GET  — list all skills for the logged-in user.
 * POST — add / update / delete a skill based on the "action" parameter.
 */
public class SkillServlet extends HttpServlet {

    private final SkillDAO skillDAO = new SkillDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = getSessionUserId(req);
        if (userId == -1) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            List<Skill> skillList = skillDAO.getByUserId(userId);
            req.setAttribute("skillList", skillList);
            req.getRequestDispatcher("/jsp/resume/builder.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load skills data.");
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
                    addSkill(req, userId);
                    req.getSession().setAttribute("success", "Skill added successfully.");
                    break;
                case "update":
                    updateSkill(req, userId);
                    req.getSession().setAttribute("success", "Skill updated successfully.");
                    break;
                case "delete":
                    int skillId = Integer.parseInt(req.getParameter("skillId"));
                    skillDAO.deleteSkill(skillId, userId);
                    req.getSession().setAttribute("success", "Skill deleted successfully.");
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
        resp.sendRedirect(req.getContextPath() + "/skills");
    }

    private void addSkill(HttpServletRequest req, int userId) throws SQLException {
        Skill skill = new Skill();
        skill.setUserId(userId);
        skill.setSkillName(req.getParameter("skillName"));
        skill.setProficiency(req.getParameter("proficiency"));
        skill.setCategory(req.getParameter("category"));

        // Validation
        if (skill.getSkillName() == null || skill.getSkillName().trim().isEmpty()) {
            throw new IllegalArgumentException("Skill name is required.");
        }
        // Set defaults if not provided
        if (skill.getProficiency() == null || skill.getProficiency().trim().isEmpty()) {
            skill.setProficiency("Intermediate");
        }
        if (skill.getCategory() == null || skill.getCategory().trim().isEmpty()) {
            skill.setCategory("Technical");
        }
        skillDAO.addSkill(skill);
    }

    private void updateSkill(HttpServletRequest req, int userId) throws SQLException {
        Skill skill = new Skill();
        skill.setSkillId(Integer.parseInt(req.getParameter("skillId")));
        skill.setUserId(userId);
        skill.setSkillName(req.getParameter("skillName"));
        skill.setProficiency(req.getParameter("proficiency"));
        skill.setCategory(req.getParameter("category"));
        skillDAO.updateSkill(skill);
    }

    private int getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) return -1;
        return (int) session.getAttribute("userId");
    }
}
