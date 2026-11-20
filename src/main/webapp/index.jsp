<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ResumeCraft - Smart Resume Builder</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/jsp/common/header.jsp" %>

    <!-- Hero Section -->
    <section class="hero">
        <h1>Build Your Perfect Resume</h1>
        <p>ResumeCraft helps college students create professional resumes in minutes.</p>
        <div class="hero-actions">
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Get Started Free</a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Login</a>
        </div>
    </section>

    <!-- Features Section -->
    <section class="features">
        <h2>Why ResumeCraft?</h2>
        <div class="feature-grid">
            <div class="feature-card">
                <h3>📝 Easy Builder</h3>
                <p>Fill in your details and we format everything beautifully.</p>
            </div>
            <div class="feature-card">
                <h3>🎨 Multiple Templates</h3>
                <p>Choose from professional templates designed for students.</p>
            </div>
            <div class="feature-card">
                <h3>📄 PDF Download</h3>
                <p>Download your resume as a polished PDF, ready to submit.</p>
            </div>
            <div class="feature-card">
                <h3>🔒 Secure & Private</h3>
                <p>Your data is safe. Access your resumes anytime, anywhere.</p>
            </div>
        </div>
    </section>

    <%@ include file="/jsp/common/footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
