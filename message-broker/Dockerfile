FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY ../pom.xml /app/
COPY ../message-broker/ /app/message-broker/

RUN mvn -f /app/message-broker/pom.xml dependency:go-offline

WORKDIR /app/message-broker
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

EXPOSE 8099
COPY --from=build /app/message-broker/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]