package com.resumecraft.dao;

import com.resumecraft.model.User;
import com.resumecraft.util.DBConnection;

import java.sql.*;

/**
 * Data Access Object for User CRUD operations.
 */
public class UserDAO {

    // --- CREATE ---
    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, password_hash) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            return ps.executeUpdate() > 0;
        }
    }

    // --- READ by email (for login) ---
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // --- READ by ID ---
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // --- UPDATE profile ---
    public boolean updateProfile(User user) throws SQLException {
        String sql = "UPDATE users SET full_name=?, phone=?, address=?, linkedin_url=?, "
                   + "github_url=?, portfolio_url=?, summary=? WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getLinkedinUrl());
            ps.setString(5, user.getGithubUrl());
            ps.setString(6, user.getPortfolioUrl());
            ps.setString(7, user.getSummary());
            ps.setInt(8, user.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    // --- Check if email exists ---
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // --- Row Mapper ---
    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setPhone(rs.getString("phone"));
        u.setAddress(rs.getString("address"));
        u.setLinkedinUrl(rs.getString("linkedin_url"));
        u.setGithubUrl(rs.getString("github_url"));
        u.setPortfolioUrl(rs.getString("portfolio_url"));
        u.setSummary(rs.getString("summary"));
        u.setProfilePhoto(rs.getString("profile_photo"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        return u;
    }
}
