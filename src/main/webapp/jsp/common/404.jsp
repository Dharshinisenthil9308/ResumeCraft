<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>404 - Page Not Found | ResumeCraft</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/jsp/common/header.jsp" %>
    <div class="error-page">
        <h1>404</h1>
        <p>The page you're looking for doesn't exist.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go Home</a>
    </div>
    <%@ include file="/jsp/common/footer.jsp" %>
</body>
</html>
