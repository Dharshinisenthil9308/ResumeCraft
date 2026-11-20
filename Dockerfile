# ============================================================
# ResumeCraft — Multi-stage Dockerfile for Railway Deployment
# Stage 1: Build with Maven + JDK 17
# Stage 2: Deploy on Apache Tomcat 10.1 (Jakarta EE 9/10 / Servlet 5.0+)
# ============================================================

# ---------- STAGE 1: BUILD ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache Maven dependencies first (layer caching optimisation)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the WAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- STAGE 2: RUNTIME ----------
FROM tomcat:10.1-jdk17-temurin

# Remove default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR from the build stage as ROOT.war for root context
COPY --from=build /app/target/ResumeCraft.war /usr/local/tomcat/webapps/ROOT.war

# Expose default port (Railway auto-detects this)
EXPOSE 8080

# Configure Tomcat to listen on Railway's PORT environment variable (defaulting to 8080) and start
CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT:-8080}\\\"/g\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]
