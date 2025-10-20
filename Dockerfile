# Build version: 2025-10-20-v2
FROM eclipse-temurin:25-jdk-jammy as builder
WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY ./src ./src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:25-jre-jammy
WORKDIR /opt/app
EXPOSE 8080

COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]