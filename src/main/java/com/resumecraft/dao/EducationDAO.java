package com.resumecraft.dao;

import com.resumecraft.model.Education;
import com.resumecraft.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Education CRUD operations.
 */
public class EducationDAO {

    public boolean addEducation(Education edu) throws SQLException {
        String sql = "INSERT INTO education (user_id, institution, degree, field_of_study, start_date, end_date, grade, description) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, edu.getUserId());
            ps.setString(2, edu.getInstitution());
            ps.setString(3, edu.getDegree());
            ps.setString(4, edu.getFieldOfStudy());
            ps.setDate(5, edu.getStartDate());
            ps.setDate(6, edu.getEndDate());
            ps.setString(7, edu.getGrade());
            ps.setString(8, edu.getDescription());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Education> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM education WHERE user_id = ? ORDER BY start_date DESC";
        List<Education> list = new ArrayList<>();
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

    public boolean updateEducation(Education edu) throws SQLException {
        String sql = "UPDATE education SET institution=?, degree=?, field_of_study=?, start_date=?, end_date=?, grade=?, description=? WHERE education_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, edu.getInstitution());
            ps.setString(2, edu.getDegree());
            ps.setString(3, edu.getFieldOfStudy());
            ps.setDate(4, edu.getStartDate());
            ps.setDate(5, edu.getEndDate());
            ps.setString(6, edu.getGrade());
            ps.setString(7, edu.getDescription());
            ps.setInt(8, edu.getEducationId());
            ps.setInt(9, edu.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteEducation(int educationId, int userId) throws SQLException {
        String sql = "DELETE FROM education WHERE education_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, educationId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Education mapRow(ResultSet rs) throws SQLException {
        Education e = new Education();
        e.setEducationId(rs.getInt("education_id"));
        e.setUserId(rs.getInt("user_id"));
        e.setInstitution(rs.getString("institution"));
        e.setDegree(rs.getString("degree"));
        e.setFieldOfStudy(rs.getString("field_of_study"));
        e.setStartDate(rs.getDate("start_date"));
        e.setEndDate(rs.getDate("end_date"));
        e.setGrade(rs.getString("grade"));
        e.setDescription(rs.getString("description"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        return e;
    }
}
