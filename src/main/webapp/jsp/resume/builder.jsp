<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Build Resume | ResumeCraft</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/jsp/common/header.jsp" %>

    <div class="builder-container">
        <h2>Build Your Resume</h2>

        <form action="${pageContext.request.contextPath}/resume/build" method="POST">
            <input type="hidden" name="action" value="create">
            <!-- Resume Title -->
            <div class="form-group">
                <label for="resumeTitle">Resume Title</label>
                <input type="text" id="resumeTitle" name="resumeTitle" required placeholder="e.g. Software Engineer Resume">
            </div>

            <!-- Template Selection -->
            <div class="form-group">
                <label>Choose a Template</label>
                <div class="template-grid">
                    <label class="template-option">
                        <input type="radio" name="templateName" value="classic" checked>
                        <div class="template-preview">📄 Classic</div>
                    </label>
                    <label class="template-option">
                        <input type="radio" name="templateName" value="modern">
                        <div class="template-preview">🎨 Modern</div>
                    </label>
                    <label class="template-option">
                        <input type="radio" name="templateName" value="minimal">
                        <div class="template-preview">✨ Minimal</div>
                    </label>
                </div>
            </div>

            <!-- Section Selection -->
            <div class="form-group">
                <label>Include Sections</label>
                <div class="section-checklist">
                    <label><input type="checkbox" name="sections" value="summary" checked> Professional Summary</label>
                    <label><input type="checkbox" name="sections" value="education" checked> Education</label>
                    <label><input type="checkbox" name="sections" value="experience" checked> Experience</label>
                    <label><input type="checkbox" name="sections" value="skills" checked> Skills</label>
                    <label><input type="checkbox" name="sections" value="projects"> Projects</label>
                    <label><input type="checkbox" name="sections" value="certifications"> Certifications</label>
                </div>
            </div>

            <button type="submit" class="btn btn-primary btn-block">Generate Resume</button>
        </form>
    </div>

    <%@ include file="/jsp/common/footer.jsp" %>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
