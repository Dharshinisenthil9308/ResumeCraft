package com.resumecraft.dao;

import com.resumecraft.model.Resume;
import com.resumecraft.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Resume CRUD operations.
 */
public class ResumeDAO {

    public int createResume(Resume resume) throws SQLException {
        String sql = "INSERT INTO resumes (user_id, resume_title, template_name) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, resume.getUserId());
            ps.setString(2, resume.getResumeTitle());
            ps.setString(3, resume.getTemplateName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public List<Resume> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM resumes WHERE user_id = ? ORDER BY updated_at DESC";
        List<Resume> list = new ArrayList<>();
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

    public Resume getById(int resumeId, int userId) throws SQLException {
        String sql = "SELECT * FROM resumes WHERE resume_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resumeId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // --- UPDATE ---
    public boolean updateResume(Resume resume) throws SQLException {
        String sql = "UPDATE resumes SET resume_title=?, template_name=?, is_public=? WHERE resume_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resume.getResumeTitle());
            ps.setString(2, resume.getTemplateName());
            ps.setBoolean(3, resume.isPublic());
            ps.setInt(4, resume.getResumeId());
            ps.setInt(5, resume.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    // --- DELETE ---
    public boolean deleteResume(int resumeId, int userId) throws SQLException {
        String sql = "DELETE FROM resumes WHERE resume_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resumeId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Resume mapRow(ResultSet rs) throws SQLException {
        Resume r = new Resume();
        r.setResumeId(rs.getInt("resume_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setResumeTitle(rs.getString("resume_title"));
        r.setTemplateName(rs.getString("template_name"));
        r.setPublic(rs.getBoolean("is_public"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        r.setUpdatedAt(rs.getTimestamp("updated_at"));
        return r;
    }
}
