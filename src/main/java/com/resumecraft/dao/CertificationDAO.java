package com.resumecraft.dao;

import com.resumecraft.model.Certification;
import com.resumecraft.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Certification CRUD operations.
 * All queries use PreparedStatement and try-with-resources.
 */
public class CertificationDAO {

    // --- CREATE ---
    public boolean addCertification(Certification cert) throws SQLException {
        String sql = "INSERT INTO certifications (user_id, cert_name, issuing_org, issue_date, expiry_date, credential_url) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cert.getUserId());
            ps.setString(2, cert.getCertName());
            ps.setString(3, cert.getIssuingOrg());
            ps.setDate(4, cert.getIssueDate());
            ps.setDate(5, cert.getExpiryDate());
            ps.setString(6, cert.getCredentialUrl());
            return ps.executeUpdate() > 0;
        }
    }

    // --- READ all by user ---
    public List<Certification> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM certifications WHERE user_id = ? ORDER BY issue_date DESC";
        List<Certification> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // --- UPDATE ---
    public boolean updateCertification(Certification cert) throws SQLException {
        String sql = "UPDATE certifications SET cert_name=?, issuing_org=?, issue_date=?, expiry_date=?, credential_url=? WHERE certification_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cert.getCertName());
            ps.setString(2, cert.getIssuingOrg());
            ps.setDate(3, cert.getIssueDate());
            ps.setDate(4, cert.getExpiryDate());
            ps.setString(5, cert.getCredentialUrl());
            ps.setInt(6, cert.getCertificationId());
            ps.setInt(7, cert.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    // --- DELETE ---
    public boolean deleteCertification(int certificationId, int userId) throws SQLException {
        String sql = "DELETE FROM certifications WHERE certification_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, certificationId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // --- Row Mapper ---
    private Certification mapRow(ResultSet rs) throws SQLException {
        Certification c = new Certification();
        c.setCertificationId(rs.getInt("certification_id"));
        c.setUserId(rs.getInt("user_id"));
        c.setCertName(rs.getString("cert_name"));
        c.setIssuingOrg(rs.getString("issuing_org"));
        c.setIssueDate(rs.getDate("issue_date"));
        c.setExpiryDate(rs.getDate("expiry_date"));
        c.setCredentialUrl(rs.getString("credential_url"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
