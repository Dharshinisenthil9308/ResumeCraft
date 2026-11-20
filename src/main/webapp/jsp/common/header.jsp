<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Navigation Header (included in all pages) -->
<nav class="navbar">
    <div class="nav-brand">
        <a href="${pageContext.request.contextPath}/">🎯 ResumeCraft</a>
    </div>
    <div class="nav-links">
        <% if (session.getAttribute("user") != null) { %>
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/profile">Profile</a>
            <a href="${pageContext.request.contextPath}/resume/build">Build Resume</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        <% } else { %>
            <a href="${pageContext.request.contextPath}/login">Login</a>
            <a href="${pageContext.request.contextPath}/register">Register</a>
        <% } %>
    </div>
</nav>
