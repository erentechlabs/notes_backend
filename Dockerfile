
FROM eclipse-temurin:25-jdk-jammy as builder
WORKDIR /opt/app

RUN chmod +x mvnw

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:25-jre-jammy
WORKDIR /opt/app
EXPOSE 8080

RUN chmod +x /opt/app/note.jar

COPY --from=builder /opt/app/target/note-0.0.1-SNAPSHOT.jar /opt/app/note.jar

ENTRYPOINT ["java", "-jar", "/opt/app/note.jar"]
