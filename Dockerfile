# ============================================================
# ResumeCraft — Multi-stage Dockerfile
# Stage 1: Build with Maven + JDK 17
# Stage 2: Deploy on Tomcat 9
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
FROM tomcat:9.0-jdk17-temurin

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR from the build stage
COPY --from=build /app/target/ResumeCraft.war /usr/local/tomcat/webapps/ROOT.war

# Expose port (Railway auto-detects this)
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
