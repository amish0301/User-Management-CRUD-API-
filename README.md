# User Management API

A robust Spring Boot REST API for managing user data with comprehensive CRUD operations, validation, and error handling.

## What It Does

User Management API is a production-ready REST service that provides complete user lifecycle management capabilities. It allows you to create, retrieve, update, and delete user records with built-in validation, duplicate detection, and search functionality.

## Key Features

- ✅ **Complete CRUD Operations** - Create, read, update, and delete users
- ✅ **Data Validation** - Email format, age range (18-100), and name length validation
- ✅ **Duplicate Prevention** - Automatic detection of duplicate email addresses
- ✅ **Search Functionality** - Search users by name with keyword matching
- ✅ **Error Handling** - Comprehensive global exception handling with detailed error responses
- ✅ **In-Memory Database** - H2 database for quick setup and testing
- ✅ **Web UI** - Thymeleaf templates for basic user interface
- ✅ **RESTful API Design** - Standard HTTP methods and status codes

## Technical Stack

- **Java 17**
- **Spring Boot 4.0.0**
- **Spring Data JPA** - ORM and data access
- **H2 Database** - In-memory relational database
- **Validation** - Jakarta validation constraints
- **Thymeleaf** - Server-side template engine

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd user-management-api
   ```

2. **Build the project**
   ```bash
   ./mvnw clean package
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The API will start on `http://localhost:8000`

### Quick Start Example

#### 1. Create a User
```bash
curl -X POST http://localhost:8000/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "age": 30
  }'
```

#### 2. Get All Users
```bash
curl http://localhost:8000/api/v1/users
```

#### 3. Get User by ID
```bash
curl http://localhost:8000/api/v1/users/1
```

#### 4. Update a User
```bash
curl -X PUT http://localhost:8000/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "age": 28
  }'
```

#### 5. Delete a User
```bash
curl -X DELETE http://localhost:8000/api/v1/users/1
```

#### 6. Search Users by Name
```bash
curl "http://localhost:8000/api/v1/users/search?keyword=john"
```

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users` | Get all users |
| GET | `/api/v1/users/{id}` | Get user by ID |
| POST | `/api/v1/users` | Create new user |
| PUT | `/api/v1/users/{id}` | Update user |
| DELETE | `/api/v1/users/{id}` | Delete user |
| GET | `/api/v1/users/search?keyword=...` | Search users by name |

### User Data Model

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "age": 30,
  "createdAt": "2025-12-06T10:30:00"
}
```

**Validation Rules:**
- **name**: Required, 2-50 characters
- **email**: Required, valid email format, unique
- **age**: Required, between 18-100 years old
- **createdAt**: Auto-generated timestamp

### Database Access

The application uses H2 in-memory database for easy development and testing. Access the H2 console at:

```
http://localhost:8000/h2-console
```

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: (leave blank)

### Configuration

Key application properties can be configured in `src/main/resources/application.properties`:

```properties
server.port=8000                              # API port
spring.h2.console.enabled=true                # H2 console access
spring.jpa.show-sql=true                      # Show SQL queries
spring.jpa.hibernate.ddl-auto=update          # Auto schema update
```

## Project Structure

```
src/main/java/com/example/usermanagement/
├── controller/          # REST endpoints
├── service/            # Business logic
├── repository/         # Database access
├── model/              # Entity classes
├── exception/          # Exception handling
└── dto/                # Response objects
```

## Error Handling

The API returns standardized error responses:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "User with ID 1 not found",
  "path": "/api/v1/users/1"
}
```

**Common Error Codes:**
- `400` - Bad Request (validation failed)
- `404` - User not found
- `409` - Duplicate email address
- `500` - Internal server error

## Support & Documentation

- **Spring Boot Documentation**: [https://docs.spring.io/spring-boot/](https://docs.spring.io/spring-boot/)
- **Spring Data JPA**: [https://docs.spring.io/spring-data/jpa/docs/current/reference/html/](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- **Jakarta Validation**: [https://jakarta.ee/specifications/validation/](https://jakarta.ee/specifications/validation/)

For issues or questions, please open an issue in the repository.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

Maintained by [amish0301](https://github.com/amish0301)
