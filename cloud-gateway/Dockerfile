FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY ../pom.xml /app/
COPY ../cloud-gateway/ /app/cloud-gateway/

RUN mvn -f /app/cloud-gateway/pom.xml dependency:go-offline

WORKDIR /app/cloud-gateway
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/cloud-gateway/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]