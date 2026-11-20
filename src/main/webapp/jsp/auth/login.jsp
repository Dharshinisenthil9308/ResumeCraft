<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | ResumeCraft</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/jsp/common/header.jsp" %>

    <div class="auth-container">
        <h2>Login to ResumeCraft</h2>

        <!-- Error message display -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <%-- Check session for success message (from redirect after registration) --%>
        <% 
            String successMsg = null;
            if (session != null && session.getAttribute("success") != null) {
                successMsg = (String) session.getAttribute("success");
                session.removeAttribute("success");
            }
            if (successMsg != null) { 
        %>
            <div class="alert alert-success"><%= successMsg %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="POST">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required placeholder="you@example.com">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required placeholder="Enter your password">
            </div>
            <button type="submit" class="btn btn-primary btn-block">Login</button>
        </form>

        <p class="auth-link">Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a></p>
    </div>

    <%@ include file="/jsp/common/footer.jsp" %>
</body>
</html>
