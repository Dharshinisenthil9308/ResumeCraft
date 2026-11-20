package com.resumecraft.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing a work/internship experience entry.
 */
public class Experience {
    private int experienceId;
    private int userId;
    private String companyName;
    private String jobTitle;
    private String location;
    private Date startDate;
    private Date endDate;
    private boolean isCurrent;
    private String description;
    private Timestamp createdAt;

    public Experience() {}

    public int getExperienceId() { return experienceId; }
    public void setExperienceId(int experienceId) { this.experienceId = experienceId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public boolean isCurrent() { return isCurrent; }
    public void setCurrent(boolean current) { isCurrent = current; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
