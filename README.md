# Note API

A secure, temporary note-sharing backend service built with Spring Boot. Create encrypted notes with expiration times and share them via unique URL codes.

## 📋 Features

- **Temporary Notes**: Create notes with customizable expiration times (1-730 hours)
- **Secure Storage**: AES encryption for note content with configurable secret keys
- **HTML Sanitization**: Protection against XSS attacks using JSoup
- **Rate Limiting**: Request throttling using Bucket4j with configurable limits
- **Auto Cleanup**: Scheduled task to automatically delete expired notes
- **RESTful API**: Clean REST endpoints for note management with comprehensive validation
- **PostgreSQL Database**: Persistent storage with JPA/Hibernate and optimized connection pooling
- **Cloud-Ready**: Google Cloud Platform support with runtime configuration
- **Docker Support**: Multi-stage containerized deployment with optimized image size
- **Production Ready**: Comprehensive error handling, logging, and monitoring capabilities

## 🛠️ Technology Stack

- **Java 21** (Eclipse Temurin JRE)
- **Spring Boot 3.5.6**
- **Spring Data JPA** with Hibernate
- **PostgreSQL** with HikariCP connection pooling
- **Lombok** for code generation
- **Bucket4j 8.0.1** (Rate Limiting)
- **JSoup 1.21.2** (HTML Sanitization)
- **Google Cloud SQL Connector 1.26.1** (Cloud deployment support)
- **Maven** (Build automation)
- **Docker** (Multi-stage containerization)

## 📦 Prerequisites

- Java 21 or higher
- PostgreSQL 12+
- Maven 3.6+
- Docker (optional, for containerized deployment)

## 🚀 Getting Started

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE notesdb;
```

### 2. Configure Environment Variables

Set the required environment variables:

```bash
# Windows (PowerShell)
$env:NOTE_SECRET_KEY="your-16-character-secret-key-here"
$env:APP_BASE_URL="http://localhost:8080"
$env:POSTGRESQL_URL="jdbc:postgresql://localhost:5432/notesdb"
$env:POSTGRESQL_USER="your_username"
$env:POSTGRESQL_PASSWORD="your_password"

# Linux/macOS
export NOTE_SECRET_KEY="your-16-character-secret-key-here"
export APP_BASE_URL="http://localhost:8080"
export POSTGRESQL_URL="jdbc:postgresql://localhost:5432/notesdb"
export POSTGRESQL_USER="your_username"
export POSTGRESQL_PASSWORD="your_password"
```

**Important**: The application uses environment variables for sensitive configuration. Never commit credentials to version control.

### 3. Build the Project

```bash
./mvnw clean install
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 📡 API Endpoints

### Create Note

```http
POST /api/notes
Content-Type: application/json

{
  "content": "Your note content here",
  "durationInHours": 24
}
```

**Response:**
```json
{
  "urlCode": "aBcD1234",
  "expiresAt": "2025-10-19T00:18:00"
}
```

### Get Note

```http
GET /api/notes/{urlCode}
```

**Response:**
```json
{
  "urlCode": "aBcD1234",
  "content": "Your note content here",
  "expiresAt": "2025-10-19T00:18:00",
  "createdAt": "2025-10-18T00:18:00",
  "updatedAt": "2025-10-18T00:18:00"
}
```

### Update Note

```http
PUT /api/notes/{urlCode}
Content-Type: application/json

{
  "content": "Updated note content"
}
```

**Response:**
```json
{
  "urlCode": "aBcD1234",
  "content": "Updated note content",
  "expiresAt": "2025-10-19T00:18:00",
  "createdAt": "2025-10-18T00:18:00",
  "updatedAt": "2025-10-18T00:30:00"
}
```

## 🔒 Security Features

### Encryption
All note content is encrypted using AES encryption before storage and decrypted when retrieved.

### HTML Sanitization
User input is sanitized using JSoup to prevent XSS attacks and malicious HTML injection.

### Rate Limiting
API endpoints are protected with rate limiting to prevent abuse.

## ⚙️ Configuration

The application is configured via environment variables for security. Key settings in `application.properties`:

```properties
# Application Configuration
spring.application.name=note
server.port=8080

# Security Configuration
note.secret.key=${NOTE_SECRET_KEY}
app.base-url=${APP_BASE_URL}

# PostgreSQL Database Configuration
spring.datasource.url=${POSTGRESQL_URL}
spring.datasource.username=${POSTGRESQL_USER}
spring.datasource.password=${POSTGRESQL_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# Connection Pool Configuration (HikariCP)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000

# Logging Configuration
logging.level.com.example.notes=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Time Zone Configuration
spring.jackson.time-zone=UTC
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

## 🧹 Automatic Cleanup

The application includes a scheduled task that runs every 15 minutes to automatically delete expired notes, ensuring the database remains clean and efficient.

## 📊 Database Schema

**Notes Table:**
- `id` (Primary Key, Auto-increment)
- `url_code` (Unique, 8-character code)
- `content` (Encrypted text)
- `expires_at` (Expiration timestamp)
- `created_at` (Creation timestamp)
- `updated_at` (Last update timestamp)

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/note/
│   │   ├── configuration/     # Rate limiting & CORS configuration
│   │   │   ├── RateLimitFilter.java
│   │   │   └── WebConfig.java
│   │   ├── controller/        # REST API controllers
│   │   │   └── NoteController.java
│   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── CreateNoteRequest.java
│   │   │   ├── CreateNoteResponse.java
│   │   │   ├── NoteResponse.java
│   │   │   └── UpdateNoteRequest.java
│   │   ├── entity/            # JPA entities
│   │   │   └── Note.java
│   │   ├── exception/         # Custom exceptions & global handler
│   │   │   ├── entity/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── NoteExpiredException.java
│   │   │   └── NoteNotFoundException.java
│   │   ├── mapper/            # Entity-DTO mappers
│   │   │   └── NoteMapper.java
│   │   ├── repository/        # JPA repositories
│   │   │   └── NoteRepository.java
│   │   ├── service/           # Business logic layer
│   │   │   └── NoteService.java
│   │   ├── util/              # Utility classes
│   │   │   ├── AESUtil.java   # AES encryption/decryption
│   │   │   └── HtmlSanitizer.java # XSS protection
│   │   └── NoteApplication.java
│   └── resources/
│       ├── static/            # Static web resources
│       ├── templates/         # Template files
│       └── application.properties
└── test/                      # Test classes
```

## 🐳 Docker Deployment

The project includes a multi-stage Dockerfile for optimized containerized deployment using Eclipse Temurin JDK/JRE.

### Build Docker Image

```bash
docker build -t note-api .
```

### Run with Docker

```bash
docker run -p 8080:8080 \
  -e NOTE_SECRET_KEY="your-16-character-secret-key-here" \
  -e APP_BASE_URL="http://localhost:8080" \
  -e POSTGRESQL_URL="jdbc:postgresql://host.docker.internal:5432/notesdb" \
  -e POSTGRESQL_USER="your_username" \
  -e POSTGRESQL_PASSWORD="your_password" \
  note-api
```

### Docker Image Details

The Dockerfile uses a multi-stage build process:
- **Builder Stage**: Eclipse Temurin JDK 21 for compilation
- **Runtime Stage**: Eclipse Temurin JRE 21 for optimized runtime
- **Optimizations**: Dependency caching, offline dependency resolution, and minimal runtime image

### Docker Compose (Optional)

Create a `docker-compose.yml` for running the application with PostgreSQL:

```yaml
version: '3.8'
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: notesdb
      POSTGRES_USER: your_username
      POSTGRES_PASSWORD: your_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
  
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      NOTE_SECRET_KEY: "your-16-character-secret-key-here"
      APP_BASE_URL: "http://localhost:8080"
      POSTGRESQL_URL: "jdbc:postgresql://db:5432/notesdb"
      POSTGRESQL_USER: "your_username"
      POSTGRESQL_PASSWORD: "your_password"
    depends_on:
      - db

volumes:
  postgres_data:
```

Run with: `docker-compose up`

## 🧪 Testing

Run tests with:

```bash
./mvnw test
```

## 📝 Validation Rules

- **Content**: Cannot be blank
- **Duration**: Must be between 1 and 730 hours (1 hour to 30 days)

## ⚠️ Error Handling

The API provides descriptive error messages for common scenarios:
- `404 Not Found`: Note does not exist
- `410 Gone`: Note has expired
- `400 Bad Request`: Invalid input or validation errors

## ☁️ Cloud Deployment

### Google Cloud Platform

The application includes comprehensive Google Cloud Platform support:

- **Google Cloud SQL**: PostgreSQL connector with socket factory (v1.26.1)
- **Runtime Configuration**: Java 21 runtime specified in `project.toml`
- **Cloud SQL Connection**: Seamless integration with Cloud SQL PostgreSQL instances

For Google Cloud deployment, configure the `POSTGRESQL_URL` environment variable with the Cloud SQL connection string:

```bash
jdbc:postgresql:///<database>?cloudSqlInstance=<instance-connection-name>&socketFactory=com.google.cloud.sql.postgres.SocketFactory
```

### Google Cloud Run Deployment

The application is optimized for Google Cloud Run with:
- Multi-stage Docker builds for smaller images
- Java 21 runtime configuration
- Environment-based configuration
- Health check endpoints ready

## 🔗 Related Projects

This is the backend API. A frontend application can be built to consume these endpoints.

## 🔐 Security Best Practices

- **Encryption Key**: Always use a strong, randomly generated 16-character key for `NOTE_SECRET_KEY`
- **Environment Variables**: Never commit sensitive credentials to version control
- **Production**: Use a secrets management system (e.g., AWS Secrets Manager, Azure Key Vault, Google Secret Manager)
- **HTTPS**: Always use HTTPS in production environments
- **Database**: Ensure PostgreSQL is configured with strong authentication and network security

## 🚀 Recent Updates

- **Enhanced Docker Support**: Multi-stage builds with Eclipse Temurin for optimized container images
- **Improved Configuration**: Comprehensive application properties with timezone and logging configuration
- **Google Cloud Ready**: Full GCP integration with Cloud SQL connector and runtime configuration
- **Production Optimizations**: Enhanced connection pooling, logging, and error handling
- **Security Enhancements**: Improved AES encryption utilities and HTML sanitization

## 📈 Performance & Monitoring

- **Connection Pooling**: HikariCP with optimized pool settings (10 max, 5 min idle)
- **Database Optimization**: PostgreSQL dialect with proper timezone handling
- **Logging**: Structured logging with configurable levels for debugging and monitoring
- **Rate Limiting**: Bucket4j implementation for API protection
- **Auto-cleanup**: Scheduled tasks for expired note removal

---

**Built with Spring Boot 3.5.6 and Java 21 | Optimized for Cloud Deployment**
