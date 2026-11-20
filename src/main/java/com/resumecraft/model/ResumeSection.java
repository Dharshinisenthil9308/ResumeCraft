package com.resumecraft.model;

/**
 * Model class representing a resume section configuration.
 * Maps which sections are included in a resume and their display order.
 */
public class ResumeSection {
    private int sectionId;
    private int resumeId;
    private String sectionType; // education, experience, skills, projects, certifications, summary
    private int sectionOrder;
    private boolean isVisible;

    public ResumeSection() {}

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public int getResumeId() { return resumeId; }
    public void setResumeId(int resumeId) { this.resumeId = resumeId; }

    public String getSectionType() { return sectionType; }
    public void setSectionType(String sectionType) { this.sectionType = sectionType; }

    public int getSectionOrder() { return sectionOrder; }
    public void setSectionOrder(int sectionOrder) { this.sectionOrder = sectionOrder; }

    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
}
