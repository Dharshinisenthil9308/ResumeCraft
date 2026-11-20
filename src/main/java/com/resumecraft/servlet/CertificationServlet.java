package com.resumecraft.servlet;

import com.resumecraft.dao.CertificationDAO;
import com.resumecraft.model.Certification;

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
 * CRUD operations for certification entries.
 * GET  — list all certifications for the logged-in user.
 * POST — add / update / delete a certification based on the "action" parameter.
 */
public class CertificationServlet extends HttpServlet {

    private final CertificationDAO certificationDAO = new CertificationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = getSessionUserId(req);
        if (userId == -1) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            List<Certification> certificationList = certificationDAO.getByUserId(userId);
            req.setAttribute("certificationList", certificationList);
            req.getRequestDispatcher("/jsp/resume/builder.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load certification data.");
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
                    addCertification(req, userId);
                    req.getSession().setAttribute("success", "Certification added successfully.");
                    break;
                case "update":
                    updateCertification(req, userId);
                    req.getSession().setAttribute("success", "Certification updated successfully.");
                    break;
                case "delete":
                    int certId = Integer.parseInt(req.getParameter("certificationId"));
                    certificationDAO.deleteCertification(certId, userId);
                    req.getSession().setAttribute("success", "Certification deleted successfully.");
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
        resp.sendRedirect(req.getContextPath() + "/certifications");
    }

    private void addCertification(HttpServletRequest req, int userId) throws SQLException {
        Certification cert = new Certification();
        cert.setUserId(userId);
        cert.setCertName(req.getParameter("certName"));
        cert.setIssuingOrg(req.getParameter("issuingOrg"));
        cert.setIssueDate(parseDate(req.getParameter("issueDate")));
        cert.setExpiryDate(parseDate(req.getParameter("expiryDate")));
        cert.setCredentialUrl(req.getParameter("credentialUrl"));

        // Validation
        if (cert.getCertName() == null || cert.getCertName().trim().isEmpty()) {
            throw new IllegalArgumentException("Certification name is required.");
        }
        if (cert.getIssuingOrg() == null || cert.getIssuingOrg().trim().isEmpty()) {
            throw new IllegalArgumentException("Issuing organization is required.");
        }
        certificationDAO.addCertification(cert);
    }

    private void updateCertification(HttpServletRequest req, int userId) throws SQLException {
        Certification cert = new Certification();
        cert.setCertificationId(Integer.parseInt(req.getParameter("certificationId")));
        cert.setUserId(userId);
        cert.setCertName(req.getParameter("certName"));
        cert.setIssuingOrg(req.getParameter("issuingOrg"));
        cert.setIssueDate(parseDate(req.getParameter("issueDate")));
        cert.setExpiryDate(parseDate(req.getParameter("expiryDate")));
        cert.setCredentialUrl(req.getParameter("credentialUrl"));
        certificationDAO.updateCertification(cert);
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
