# Note API

A secure, temporary note-sharing backend service built with Spring Boot. Create encrypted notes with expiration times and share them via unique URL codes.

## 📋 Features

- **Temporary Notes**: Create notes with customizable expiration times (1-730 hours)
- **Secure Storage**: AES encryption for note content
- **HTML Sanitization**: Protection against XSS attacks using JSoup
- **Rate Limiting**: Request throttling using Bucket4j
- **Auto Cleanup**: Scheduled task to automatically delete expired notes
- **RESTful API**: Clean REST endpoints for note management
- **PostgreSQL Database**: Persistent storage with JPA/Hibernate

## 🛠️ Technology Stack

- **Java 25**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Bucket4j** (Rate Limiting)
- **JSoup** (HTML Sanitization)
- **Maven**

## 📦 Prerequisites

- Java 25 or higher
- PostgreSQL 12+ (running on port 5432)
- Maven 3.6+

## 🚀 Getting Started

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE notesdb;
```

### 2. Configure Database

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/notesdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

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

Key configuration properties in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/notesdb
spring.jpa.hibernate.ddl-auto=update

# Connection Pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
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
│   │   ├── configuration/     # Rate limiting & CORS config
│   │   ├── controller/        # REST controllers
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── entity/            # JPA entities
│   │   ├── exception/         # Custom exceptions
│   │   ├── mapper/            # Entity-DTO mappers
│   │   ├── repository/        # JPA repositories
│   │   ├── service/           # Business logic
│   │   └── util/              # Utility classes (AES, HTML sanitization)
│   └── resources/
│       └── application.properties
└── test/
```

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

## 🔗 Related Projects

This is the backend API. A frontend application can be built to consume these endpoints.

---

**Note**: Remember to change the default AES encryption key in `AESUtil.java` for production use and store it securely using environment variables or a secrets management system.
