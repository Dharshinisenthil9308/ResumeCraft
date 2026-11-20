package com.resumecraft.dao;

import com.resumecraft.model.Experience;
import com.resumecraft.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Experience CRUD operations.
 */
public class ExperienceDAO {

    public boolean addExperience(Experience exp) throws SQLException {
        String sql = "INSERT INTO experience (user_id, company_name, job_title, location, start_date, end_date, is_current, description) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, exp.getUserId());
            ps.setString(2, exp.getCompanyName());
            ps.setString(3, exp.getJobTitle());
            ps.setString(4, exp.getLocation());
            ps.setDate(5, exp.getStartDate());
            ps.setDate(6, exp.getEndDate());
            ps.setBoolean(7, exp.isCurrent());
            ps.setString(8, exp.getDescription());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Experience> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM experience WHERE user_id = ? ORDER BY start_date DESC";
        List<Experience> list = new ArrayList<>();
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

    public boolean updateExperience(Experience exp) throws SQLException {
        String sql = "UPDATE experience SET company_name=?, job_title=?, location=?, start_date=?, end_date=?, is_current=?, description=? WHERE experience_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exp.getCompanyName());
            ps.setString(2, exp.getJobTitle());
            ps.setString(3, exp.getLocation());
            ps.setDate(4, exp.getStartDate());
            ps.setDate(5, exp.getEndDate());
            ps.setBoolean(6, exp.isCurrent());
            ps.setString(7, exp.getDescription());
            ps.setInt(8, exp.getExperienceId());
            ps.setInt(9, exp.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteExperience(int experienceId, int userId) throws SQLException {
        String sql = "DELETE FROM experience WHERE experience_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, experienceId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Experience mapRow(ResultSet rs) throws SQLException {
        Experience e = new Experience();
        e.setExperienceId(rs.getInt("experience_id"));
        e.setUserId(rs.getInt("user_id"));
        e.setCompanyName(rs.getString("company_name"));
        e.setJobTitle(rs.getString("job_title"));
        e.setLocation(rs.getString("location"));
        e.setStartDate(rs.getDate("start_date"));
        e.setEndDate(rs.getDate("end_date"));
        e.setCurrent(rs.getBoolean("is_current"));
        e.setDescription(rs.getString("description"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        return e;
    }
}
