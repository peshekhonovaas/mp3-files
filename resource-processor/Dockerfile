FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY ../pom.xml /app/
COPY ../resource-processor/ /app/resource-processor/

RUN mvn -f /app/resource-processor/pom.xml dependency:go-offline

WORKDIR /app/resource-processor
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/resource-processor/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]