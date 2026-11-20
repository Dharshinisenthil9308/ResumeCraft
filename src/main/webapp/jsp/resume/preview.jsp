<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resume Preview | ResumeCraft</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/resume-templates.css">
</head>
<body>
    <%@ include file="/jsp/common/header.jsp" %>

    <div class="preview-container">
        <div class="preview-actions">
            <a href="${pageContext.request.contextPath}/resume/download?resumeId=${resume.resumeId}" class="btn btn-primary">📥 Download Resume</a>
            <a href="${pageContext.request.contextPath}/resume/build?resumeId=${resume.resumeId}" class="btn btn-outline">✏️ Edit Resume</a>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline">← Back to Dashboard</a>
        </div>

        <!-- Resume preview area -->
        <div class="resume-paper">

            <%-- ===== HEADER ===== --%>
            <div class="resume-header">
                <h1>${user.fullName}</h1>
                <p class="contact-info">
                    ${user.email}
                    <c:if test="${not empty user.phone}"> | ${user.phone}</c:if>
                    <c:if test="${not empty user.address}"> | ${user.address}</c:if>
                </p>
                <p class="links">
                    <c:if test="${not empty user.linkedinUrl}"><a href="${user.linkedinUrl}">LinkedIn</a></c:if>
                    <c:if test="${not empty user.githubUrl}"> | <a href="${user.githubUrl}">GitHub</a></c:if>
                    <c:if test="${not empty user.portfolioUrl}"> | <a href="${user.portfolioUrl}">Portfolio</a></c:if>
                </p>
            </div>

            <%-- ===== SUMMARY ===== --%>
            <c:if test="${not empty user.summary}">
                <div class="resume-section">
                    <h2>Professional Summary</h2>
                    <hr>
                    <p>${user.summary}</p>
                </div>
            </c:if>

            <%-- ===== EDUCATION ===== --%>
            <c:if test="${not empty educationList}">
                <div class="resume-section">
                    <h2>Education</h2>
                    <hr>
                    <c:forEach var="edu" items="${educationList}">
                        <div class="entry">
                            <div class="entry-header">
                                <strong>${edu.degree}<c:if test="${not empty edu.fieldOfStudy}"> in ${edu.fieldOfStudy}</c:if></strong>
                                <span class="date">${edu.startDate} - ${not empty edu.endDate ? edu.endDate : 'Present'}</span>
                            </div>
                            <p>${edu.institution} <c:if test="${not empty edu.grade}">| Grade: ${edu.grade}</c:if></p>
                            <c:if test="${not empty edu.description}"><p>${edu.description}</p></c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <%-- ===== EXPERIENCE ===== --%>
            <c:if test="${not empty experienceList}">
                <div class="resume-section">
                    <h2>Experience</h2>
                    <hr>
                    <c:forEach var="exp" items="${experienceList}">
                        <div class="entry">
                            <div class="entry-header">
                                <strong>${exp.jobTitle}</strong>
                                <span class="date">${exp.startDate} - ${exp.current ? 'Present' : exp.endDate}</span>
                            </div>
                            <p>${exp.companyName} <c:if test="${not empty exp.location}">| ${exp.location}</c:if></p>
                            <c:if test="${not empty exp.description}"><p>${exp.description}</p></c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <%-- ===== SKILLS ===== --%>
            <c:if test="${not empty skillList}">
                <div class="resume-section">
                    <h2>Skills</h2>
                    <hr>
                    <div class="skills-list">
                        <c:forEach var="skill" items="${skillList}">
                            <span class="skill-tag">${skill.skillName} (${skill.proficiency})</span>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <%-- ===== PROJECTS ===== --%>
            <c:if test="${not empty projectList}">
                <div class="resume-section">
                    <h2>Projects</h2>
                    <hr>
                    <c:forEach var="proj" items="${projectList}">
                        <div class="entry">
                            <strong>${proj.projectName}</strong>
                            <c:if test="${not empty proj.technologies}"><span class="tech"> | ${proj.technologies}</span></c:if>
                            <c:if test="${not empty proj.description}"><p>${proj.description}</p></c:if>
                            <c:if test="${not empty proj.projectUrl}"><p><a href="${proj.projectUrl}">${proj.projectUrl}</a></p></c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <%-- ===== CERTIFICATIONS ===== --%>
            <c:if test="${not empty certificationList}">
                <div class="resume-section">
                    <h2>Certifications</h2>
                    <hr>
                    <c:forEach var="cert" items="${certificationList}">
                        <div class="entry">
                            <strong>${cert.certName}</strong> — ${cert.issuingOrg}
                            <c:if test="${not empty cert.issueDate}"> | Issued: ${cert.issueDate}</c:if>
                            <c:if test="${not empty cert.credentialUrl}">
                                <br><a href="${cert.credentialUrl}">View Credential</a>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

        </div>
    </div>

    <%@ include file="/jsp/common/footer.jsp" %>
</body>
</html>
