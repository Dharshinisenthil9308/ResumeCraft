package com.resumecraft.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing a certification entry.
 */
public class Certification {
    private int certificationId;
    private int userId;
    private String certName;
    private String issuingOrg;
    private Date issueDate;
    private Date expiryDate;
    private String credentialUrl;
    private Timestamp createdAt;

    public Certification() {}

    public int getCertificationId() { return certificationId; }
    public void setCertificationId(int certificationId) { this.certificationId = certificationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCertName() { return certName; }
    public void setCertName(String certName) { this.certName = certName; }

    public String getIssuingOrg() { return issuingOrg; }
    public void setIssuingOrg(String issuingOrg) { this.issuingOrg = issuingOrg; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public String getCredentialUrl() { return credentialUrl; }
    public void setCredentialUrl(String credentialUrl) { this.credentialUrl = credentialUrl; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
