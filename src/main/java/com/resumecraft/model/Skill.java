package com.resumecraft.model;

import java.sql.Timestamp;

/**
 * Model class representing a skill entry.
 */
public class Skill {
    private int skillId;
    private int userId;
    private String skillName;
    private String proficiency;   // Beginner | Intermediate | Advanced | Expert
    private String category;      // Technical | Soft Skill | Language | Tool | Other
    private Timestamp createdAt;

    public Skill() {}

    public int getSkillId() { return skillId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getProficiency() { return proficiency; }
    public void setProficiency(String proficiency) { this.proficiency = proficiency; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
