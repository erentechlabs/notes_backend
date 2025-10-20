# Build aşaması
FROM eclipse-temurin:25-jdk-jammy as builder

# Çalışma dizini
WORKDIR /opt/app

# .mvn, mvnw, ve pom.xml dosyalarını kopyalayın
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# mvnw'ye çalıştırma izni verin
RUN chmod +x mvnw

# Bağımlılıkları offline olarak indirin
RUN ./mvnw dependency:go-offline

# Kaynak kodlarını kopyalayın
COPY ./src ./src

# Uygulamayı derleyin, testleri atlayın
RUN ./mvnw clean package -DskipTests

# Runtime aşaması
FROM eclipse-temurin:25-jre-jammy

# Çalışma dizini
WORKDIR /opt/app

# Container dışına expose edilen port
EXPOSE 8080

# Derlenen .jar dosyasını copy edin
COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar

# Uygulamayı çalıştırma komutu
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
