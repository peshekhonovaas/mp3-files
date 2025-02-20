FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY ../pom.xml /app/
COPY ../song-service/ /app/song-service/

RUN mvn -f /app/song-service/pom.xml dependency:go-offline

WORKDIR /app/song-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/song-service/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]