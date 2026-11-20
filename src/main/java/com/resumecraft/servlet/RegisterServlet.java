package com.resumecraft.servlet;

import com.resumecraft.dao.UserDAO;
import com.resumecraft.model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles user registration (GET = show form, POST = create account).
 * Validates inputs, checks for duplicate emails, hashes passwords with BCrypt.
 */
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // --- Input Validation ---
        if (fullName == null || fullName.trim().isEmpty()) {
            setErrorAndForward(req, resp, "Full name is required.");
            return;
        }
        if (email == null || email.trim().isEmpty()
                || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            setErrorAndForward(req, resp, "A valid email address is required.");
            return;
        }
        if (password == null || password.length() < 6) {
            setErrorAndForward(req, resp, "Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            setErrorAndForward(req, resp, "Passwords do not match.");
            return;
        }

        try {
            // Check duplicate email
            if (userDAO.emailExists(email.trim())) {
                setErrorAndForward(req, resp, "An account with this email already exists.");
                return;
            }

            // Create user with hashed password
            User user = new User();
            user.setFullName(fullName.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));

            boolean created = userDAO.registerUser(user);
            if (created) {
                req.getSession().setAttribute("success",
                        "Account created successfully! Please log in.");
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                setErrorAndForward(req, resp, "Registration failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            setErrorAndForward(req, resp, "A system error occurred. Please try again later.");
        }
    }

    /** Helper: set error message and forward back to register form. */
    private void setErrorAndForward(HttpServletRequest req, HttpServletResponse resp,
                                     String message) throws ServletException, IOException {
        req.setAttribute("error", message);
        req.getRequestDispatcher("/jsp/auth/register.jsp").forward(req, resp);
    }
}
