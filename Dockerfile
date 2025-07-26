# Use a builder image to compile the app
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller image for running the app
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Set environment variable to ensure UTF-8 encoding
ENV LANG C.UTF-8

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
