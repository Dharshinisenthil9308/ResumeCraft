package com.resumecraft.dao;

import com.resumecraft.model.Skill;
import com.resumecraft.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Skill CRUD operations.
 */
public class SkillDAO {

    public boolean addSkill(Skill skill) throws SQLException {
        String sql = "INSERT INTO skills (user_id, skill_name, proficiency, category) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, skill.getUserId());
            ps.setString(2, skill.getSkillName());
            ps.setString(3, skill.getProficiency());
            ps.setString(4, skill.getCategory());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Skill> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM skills WHERE user_id = ? ORDER BY category, skill_name";
        List<Skill> list = new ArrayList<>();
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
    public boolean updateSkill(Skill skill) throws SQLException {
        String sql = "UPDATE skills SET skill_name=?, proficiency=?, category=? WHERE skill_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, skill.getSkillName());
            ps.setString(2, skill.getProficiency());
            ps.setString(3, skill.getCategory());
            ps.setInt(4, skill.getSkillId());
            ps.setInt(5, skill.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    // --- DELETE ---
    public boolean deleteSkill(int skillId, int userId) throws SQLException {
        String sql = "DELETE FROM skills WHERE skill_id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, skillId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Skill mapRow(ResultSet rs) throws SQLException {
        Skill s = new Skill();
        s.setSkillId(rs.getInt("skill_id"));
        s.setUserId(rs.getInt("user_id"));
        s.setSkillName(rs.getString("skill_name"));
        s.setProficiency(rs.getString("proficiency"));
        s.setCategory(rs.getString("category"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        return s;
    }
}
