package com.resumecraft.dao;

import com.resumecraft.model.ResumeSection;
import com.resumecraft.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ResumeSection CRUD operations.
 * Manages which sections appear in a resume and their order.
 * All queries use PreparedStatement and try-with-resources.
 */
public class ResumeSectionDAO {

    // --- CREATE ---
    public boolean addSection(ResumeSection section) throws SQLException {
        String sql = "INSERT INTO resume_sections (resume_id, section_type, section_order, is_visible) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, section.getResumeId());
            ps.setString(2, section.getSectionType());
            ps.setInt(3, section.getSectionOrder());
            ps.setBoolean(4, section.isVisible());
            return ps.executeUpdate() > 0;
        }
    }

    // --- READ by resume ---
    public List<ResumeSection> getByResumeId(int resumeId) throws SQLException {
        String sql = "SELECT * FROM resume_sections WHERE resume_id = ? ORDER BY section_order ASC";
        List<ResumeSection> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resumeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // --- DELETE all sections for a resume (used before re-saving) ---
    public boolean deleteByResumeId(int resumeId) throws SQLException {
        String sql = "DELETE FROM resume_sections WHERE resume_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resumeId);
            return ps.executeUpdate() > 0;
        }
    }

    // --- Row Mapper ---
    private ResumeSection mapRow(ResultSet rs) throws SQLException {
        ResumeSection s = new ResumeSection();
        s.setSectionId(rs.getInt("section_id"));
        s.setResumeId(rs.getInt("resume_id"));
        s.setSectionType(rs.getString("section_type"));
        s.setSectionOrder(rs.getInt("section_order"));
        s.setVisible(rs.getBoolean("is_visible"));
        return s;
    }
}
