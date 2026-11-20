<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Classic Resume Template — clean, traditional layout --%>
<div class="resume-template template-classic">

    <!-- Header -->
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

    <!-- Summary -->
    <c:if test="${not empty user.summary}">
        <div class="resume-section">
            <h2>Professional Summary</h2>
            <hr>
            <p>${user.summary}</p>
        </div>
    </c:if>

    <!-- Education -->
    <c:if test="${not empty educationList}">
        <div class="resume-section">
            <h2>Education</h2>
            <hr>
            <c:forEach var="edu" items="${educationList}">
                <div class="entry">
                    <div class="entry-header">
                        <strong>${edu.degree} in ${edu.fieldOfStudy}</strong>
                        <span class="date">${edu.startDate} - ${not empty edu.endDate ? edu.endDate : 'Present'}</span>
                    </div>
                    <p>${edu.institution} <c:if test="${not empty edu.grade}">| Grade: ${edu.grade}</c:if></p>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <!-- Experience -->
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

    <!-- Skills -->
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

    <!-- Projects -->
    <c:if test="${not empty projectList}">
        <div class="resume-section">
            <h2>Projects</h2>
            <hr>
            <c:forEach var="proj" items="${projectList}">
                <div class="entry">
                    <strong>${proj.projectName}</strong>
                    <c:if test="${not empty proj.technologies}"><span class="tech"> | ${proj.technologies}</span></c:if>
                    <c:if test="${not empty proj.description}"><p>${proj.description}</p></c:if>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>
