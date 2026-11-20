package com.resumecraft.servlet;

import com.resumecraft.dao.UserDAO;
import com.resumecraft.model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles user login (GET = show form, POST = authenticate).
 * Verifies credentials with BCrypt, creates an HTTP session on success.
 */
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // --- Input Validation ---
        if (email == null || email.trim().isEmpty()) {
            setErrorAndForward(req, resp, "Email is required.");
            return;
        }
        if (password == null || password.isEmpty()) {
            setErrorAndForward(req, resp, "Password is required.");
            return;
        }

        try {
            User user = userDAO.getUserByEmail(email.trim().toLowerCase());

            if (user == null || !BCrypt.checkpw(password, user.getPasswordHash())) {
                setErrorAndForward(req, resp, "Invalid email or password.");
                return;
            }

            // Authentication successful — create session
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userFullName", user.getFullName());
            session.setAttribute("userEmail", user.getEmail());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (SQLException e) {
            e.printStackTrace();
            setErrorAndForward(req, resp, "A system error occurred. Please try again later.");
        }
    }

    /** Helper: set error message and forward back to login form. */
    private void setErrorAndForward(HttpServletRequest req, HttpServletResponse resp,
                                     String message) throws ServletException, IOException {
        req.setAttribute("error", message);
        req.getRequestDispatcher("/jsp/auth/login.jsp").forward(req, resp);
    }
}
