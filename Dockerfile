# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-22 AS build
WORKDIR /app

# Cache deps first
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:22-jre
WORKDIR /app

# copy the fat jar produced by Spring Boot
COPY --from=build /app/target/*.jar app.jar

# Render will route to this port; your app should listen on 8080
EXPOSE 8080

# small memory footprint on free plan
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java","-jar","/app/app.jar"]
