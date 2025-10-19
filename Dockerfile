FROM eclipse-temurin:25-jdk-jammy as builder
WORKDIR /opt/app

# Copy Maven wrapper files first
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make mvnw executable AFTER copying it
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source and build
COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:25-jre-jammy
WORKDIR /opt/app
EXPOSE 8080

# Copy the jar from builder stage
COPY --from=builder /opt/app/target/note-0.0.1-SNAPSHOT.jar /opt/app/note.jar

ENTRYPOINT ["java", "-jar", "/opt/app/note.jar"]