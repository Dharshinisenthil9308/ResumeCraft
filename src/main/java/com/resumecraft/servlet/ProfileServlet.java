package com.resumecraft.servlet;

import com.resumecraft.dao.UserDAO;
import com.resumecraft.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Profile management — view and update personal details
 * (name, phone, address, links, summary).
 */
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

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
            User user = userDAO.getUserById(userId);
            if (user == null) {
                session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            req.setAttribute("user", user);
            req.getRequestDispatcher("/jsp/dashboard/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load profile.");
            req.getRequestDispatcher("/jsp/common/500.jsp").forward(req, resp);
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

        // Validate required field
        String fullName = req.getParameter("fullName");
        if (fullName == null || fullName.trim().isEmpty()) {
            req.setAttribute("error", "Full name is required.");
            doGet(req, resp);
            return;
        }

        try {
            User user = new User();
            user.setUserId(userId);
            user.setFullName(fullName.trim());
            user.setPhone(req.getParameter("phone"));
            user.setAddress(req.getParameter("address"));
            user.setLinkedinUrl(req.getParameter("linkedinUrl"));
            user.setGithubUrl(req.getParameter("githubUrl"));
            user.setPortfolioUrl(req.getParameter("portfolioUrl"));
            user.setSummary(req.getParameter("summary"));

            boolean updated = userDAO.updateProfile(user);
            if (updated) {
                session.setAttribute("userFullName", fullName.trim());
                session.setAttribute("success", "Profile updated successfully.");
            } else {
                session.setAttribute("error", "Failed to update profile.");
            }
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "A system error occurred.");
            doGet(req, resp);
        }
    }
}
