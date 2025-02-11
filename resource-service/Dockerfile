FROM eclipse-temurin:21-alpine as build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]