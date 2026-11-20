<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | ResumeCraft</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/jsp/common/header.jsp" %>

    <div class="dashboard-container">
        <h2>Welcome, ${sessionScope.userFullName}!</h2>

        <!-- Quick Actions -->
        <div class="quick-actions">
            <a href="${pageContext.request.contextPath}/resume/build" class="btn btn-primary">+ Create New Resume</a>
            <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline">Edit Profile</a>
        </div>

        <!-- Saved Resumes -->
        <h3>Your Resumes</h3>
        <div class="resume-grid">
            <c:forEach var="resume" items="${resumes}">
                <div class="resume-card">
                    <h4>${resume.resumeTitle}</h4>
                    <p>Template: ${resume.templateName}</p>
                    <p>Last updated: ${resume.updatedAt}</p>
                    <div class="card-actions">
                        <a href="${pageContext.request.contextPath}/resume/preview?resumeId=${resume.resumeId}">Preview</a>
                        <a href="${pageContext.request.contextPath}/resume/download?resumeId=${resume.resumeId}">Download</a>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty resumes}">
                <p class="empty-state">No resumes yet. Create your first one!</p>
            </c:if>
        </div>

        <!-- Quick Stats -->
        <div class="stats-row">
            <a href="${pageContext.request.contextPath}/education" class="stat-card">📚 Education</a>
            <a href="${pageContext.request.contextPath}/experience" class="stat-card">💼 Experience</a>
            <a href="${pageContext.request.contextPath}/skills" class="stat-card">⚡ Skills</a>
            <a href="${pageContext.request.contextPath}/projects" class="stat-card">🚀 Projects</a>
        </div>
    </div>

    <%@ include file="/jsp/common/footer.jsp" %>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
