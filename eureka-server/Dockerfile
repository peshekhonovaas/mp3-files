FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY mp3-files/pom.xml /app/
COPY mp3-files/eureka-server/ /app/eureka-server/

RUN mvn -f /app/eureka-server/pom.xml dependency:go-offline

WORKDIR /app/eureka-server
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/eureka-server/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]