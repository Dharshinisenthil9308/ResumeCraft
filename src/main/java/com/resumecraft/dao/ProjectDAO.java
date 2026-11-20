package com.resumecraft.dao;

import com.resumecraft.model.Project;
import com.resumecraft.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Project CRUD operations.
 */
public class ProjectDAO {

    public boolean addProject(Project proj) throws SQLException {
        String sql = "INSERT INTO projects (user_id, project_name, description, technologies, project_url, start_date, end_date) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proj.getUserId());
            ps.setString(2, proj.getProjectName());
            ps.setString(3, proj.getDescription());
            ps.setString(4, proj.getTechnologies());
            ps.setString(5, proj.getProjectUrl());
            ps.setDate(6, proj.getStartDate());
            ps.setDate(7, proj.getEndDate());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Project> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM projects WHERE user_id = ? ORDER BY start_date DESC";
        List<Project> list = new ArrayList<>();
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

    public boolean updateProject(Project proj) throws SQLException {
        String sql = "UPDATE projects SET project_name=?, description=?, technologies=?, project_url=?, start_date=?, end_date=? WHERE project_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, proj.getProjectName());
            ps.setString(2, proj.getDescription());
            ps.setString(3, proj.getTechnologies());
            ps.setString(4, proj.getProjectUrl());
            ps.setDate(5, proj.getStartDate());
            ps.setDate(6, proj.getEndDate());
            ps.setInt(7, proj.getProjectId());
            ps.setInt(8, proj.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteProject(int projectId, int userId) throws SQLException {
        String sql = "DELETE FROM projects WHERE project_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Project mapRow(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setProjectId(rs.getInt("project_id"));
        p.setUserId(rs.getInt("user_id"));
        p.setProjectName(rs.getString("project_name"));
        p.setDescription(rs.getString("description"));
        p.setTechnologies(rs.getString("technologies"));
        p.setProjectUrl(rs.getString("project_url"));
        p.setStartDate(rs.getDate("start_date"));
        p.setEndDate(rs.getDate("end_date"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
