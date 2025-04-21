# ----------- Stage 1: Build WAR using Maven -----------
FROM maven:3.9.3-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy your Maven project files
COPY . .

# Build the WAR file
RUN mvn clean package -DskipTests


# ----------- Stage 2: Deploy to Tomcat -----------
FROM tomcat:10.1-jdk17-corretto

# Clean default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the generated WAR to Tomcat’s webapps directory
COPY --from=builder /app/target/ATM_Application_New-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
