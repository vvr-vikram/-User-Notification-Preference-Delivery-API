# User Notification Preference & Delivery API

A production-ready Spring Boot REST API for managing user notification preferences and simulating notification delivery across multiple channels (EMAIL, SMS, PUSH).

## Features

✅ **User Management**
- Create users with email and phone verification
- Retrieve user information by ID or list all users
- Update and delete user records

✅ **Notification Preference Management**
- Enable/disable notification channels per user
- Default preferences: all channels disabled
- Fetch preferences by user or specific channel
- Update preference status

✅ **Notification Delivery**
- Send notifications with validation of user existence and channel status
- Simulate delivery to EMAIL, SMS, and PUSH channels
- Prevent sending to disabled channels
- Record all delivery attempts

✅ **Notification History**
- Track all notification delivery attempts
- Filter by user, status (SENT/FAILED), or channel
- Store failure reasons for debugging

## Tech Stack

- **Framework**: Spring Boot 2.7.4
- **Language**: Java 17
- **ORM**: Spring Data JPA / Hibernate
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Additional**: Lombok, SLF4J

## Project Structure

```
notification-api/
├── src/main/java/com/notification/
│   ├── NotificationApplication.java         # Main entry point
│   ├── controller/                          # REST Controllers
│   │   ├── UserController.java
│   │   ├── NotificationPreferenceController.java
│   │   └── NotificationController.java
│   ├── service/                             # Business logic
│   │   ├── UserService.java
│   │   ├── NotificationPreferenceService.java
│   │   └── NotificationService.java
│   ├── repository/                          # Data access
│   │   ├── UserRepository.java
│   │   ├── NotificationPreferenceRepository.java
│   │   └── NotificationHistoryRepository.java
│   ├── entity/                              # JPA Entities
│   │   ├── User.java
│   │   ├── NotificationPreference.java
│   │   └── NotificationHistory.java
│   ├── dto/                                 # Data Transfer Objects
│   │   ├── UserDTO.java
│   │   ├── NotificationPreferenceDTO.java
│   │   ├── SendNotificationDTO.java
│   │   ├── NotificationHistoryDTO.java
│   │   └── ApiResponse.java
│   ├── mapper/                              # Entity-DTO Mapping
│   │   └── EntityMapper.java
│   ├── exception/                           # Exception Handling
│   │   ├── ResourceNotFoundException.java
│   │   ├── NotificationChannelDisabledException.java
│   │   └── GlobalExceptionHandler.java
│   └── enums/                               # Enumerations
│       ├── NotificationChannel.java
│       └── NotificationStatus.java
├── src/main/resources/
│   └── application.properties               # Configuration
└── pom.xml                                  # Maven configuration
```

## Setup & Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build the Project

```bash
# Navigate to project directory
cd notification-api

# Build the project
mvn clean install

# Run tests (if any)
mvn test
```

### Run the Application

```bash
# Using Maven
mvn spring-boot:run

# Or run the JAR directly after building
java -jar target/notification-preference-api-1.0.0.jar
```

The application will start on `http://localhost:8080` with base URL `/api`

### H2 Database Console

Access H2 Console at: `http://localhost:8080/api/h2-console`
- JDBC URL: `jdbc:h2:mem:notificationdb`
- Username: `root`
- Password: 1254

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────────┐
│         REST Controllers Layer           │
│  (Handle HTTP requests/responses)       │
├─────────────────────────────────────────┤
│         Service Layer                    │
│  (Business logic & validation)           │
├─────────────────────────────────────────┤
│         Repository Layer                 │
│  (Data access & persistence)             │
├─────────────────────────────────────────┤
│         Entity & Database                │
│  (JPA entities & H2 database)            │
└─────────────────────────────────────────┘
```

### Entity Relationships

```
User (1) ─────────── (M) NotificationPreference
  │                       └─ channel: ENUM (EMAIL/SMS/PUSH)
  │                       └─ enabled: Boolean
  │
  └─────────── (M) NotificationHistory
                    └─ channel: ENUM
                    └─ message: String
                    └─ status: ENUM (SENT/FAILED)
```

## Business Rules

1. **Default Preferences**: When a user is created, all notification channels are disabled by default
2. **Channel Validation**: Notifications can only be sent to enabled channels
3. **Audit Trail**: Every notification attempt is recorded in history, regardless of success/failure
4. **Status Tracking**: Notifications are marked as SENT or FAILED with optional failure reasons
5. **Unique Constraints**: Email and phone numbers must be unique across users
6. **User Validation**: All preference and notification operations require valid user IDs

## API Documentation

### User Management

#### Create User
```http
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890"
}
```

**Response (201 Created)**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

#### Get User by ID
```http
GET /api/users/1
```

#### List All Users
```http
GET /api/users
```

#### Update User
```http
PUT /api/users/1
Content-Type: application/json

{
  "name": "John Smith",
  "email": "john.smith@example.com",
  "phone": "+9876543210"
}
```

#### Delete User
```http
DELETE /api/users/1
```

---

### Notification Preferences

#### Get All User Preferences
```http
GET /api/users/1/preferences
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Preferences retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "channel": "EMAIL",
      "enabled": false,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    },
    {
      "id": 2,
      "userId": 1,
      "channel": "SMS",
      "enabled": false,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    },
    {
      "id": 3,
      "userId": 1,
      "channel": "PUSH",
      "enabled": false,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "timestamp": "2024-01-15T10:30:00"
}
```

#### Get Specific Channel Preference
```http
GET /api/users/1/preferences/email
```

#### Enable Notification Channel
```http
POST /api/users/1/preferences/email/enable
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Channel enabled successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "channel": "EMAIL",
    "enabled": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:35:00"
  },
  "timestamp": "2024-01-15T10:35:00"
}
```

#### Disable Notification Channel
```http
POST /api/users/1/preferences/email/disable
```

#### Update Channel Preference
```http
PUT /api/users/1/preferences/sms
Content-Type: application/json

{
  "enabled": true
}
```

---

### Notification Delivery

#### Send Notification
```http
POST /api/notifications/send
Content-Type: application/json

{
  "userId": 1,
  "channel": "EMAIL",
  "message": "Your account has been verified successfully!"
}
```

**Response (201 Created)**
```json
{
  "success": true,
  "message": "Notification processed",
  "data": {
    "id": 1,
    "userId": 1,
    "channel": "EMAIL",
    "message": "Your account has been verified successfully!",
    "status": "SENT",
    "sentAt": "2024-01-15T10:40:00",
    "failureReason": null,
    "createdAt": "2024-01-15T10:40:00"
  },
  "timestamp": "2024-01-15T10:40:00"
}
```

#### Error Response: Channel Disabled
```json
{
  "success": false,
  "message": "Notification channel EMAIL is disabled for user: 1",
  "data": null,
  "timestamp": "2024-01-15T10:41:00"
}
```

---

### Notification History

#### Get All User Notifications
```http
GET /api/notifications/user/1
```

#### Get User Notifications by Status
```http
GET /api/notifications/user/1/status/sent
GET /api/notifications/user/1/status/failed
```

#### Get User Notifications by Channel
```http
GET /api/notifications/user/1/channel/email
GET /api/notifications/user/1/channel/sms
GET /api/notifications/user/1/channel/push
```

#### Get All Notifications by Status (System-wide)
```http
GET /api/notifications/status/sent
GET /api/notifications/status/failed
```

#### Get All Notifications by Channel (System-wide)
```http
GET /api/notifications/channel/email
```

---

## Error Handling

The API provides consistent error responses with appropriate HTTP status codes:

| Status Code | Scenario |
|-------------|----------|
| 200 OK | Successful GET/PUT requests |
| 201 Created | Successful POST requests |
| 400 Bad Request | Invalid request data, disabled channel, invalid enum values |
| 404 Not Found | Resource (user, preference) not found |
| 500 Internal Server Error | Unexpected server errors |

**Error Response Format**
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-01-15T10:45:00"
}
```

## Testing the API

### Using cURL

```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "phone": "+1111111111"
  }'

# Enable email notification
curl -X POST http://localhost:8080/api/users/1/preferences/email/enable

# Send notification
curl -X POST http://localhost:8080/api/notifications/send \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "channel": "EMAIL",
    "message": "Welcome to our service!"
  }'

# Get user notifications
curl http://localhost:8080/api/notifications/user/1
```

### Using Postman

Import the collection from `api-requests.postman_collection.json` or manually create requests for all endpoints.

## Advanced Features

### Notification Simulation

The system simulates delivery failures with realistic failure rates:
- **EMAIL**: 10% failure rate
- **SMS**: 5% failure rate
- **PUSH**: 15% failure rate

In a production system, these would be replaced with actual integrations to:
- SendGrid/AWS SES for email
- Twilio/AWS SNS for SMS
- Firebase Cloud Messaging for push notifications

### Logging

Application logs are configured with different levels:
- **INFO**: General application flow
- **DEBUG**: Detailed notification processing steps

View logs in the console when running `mvn spring-boot:run`

## Extending the System

### Adding New Notification Channels

1. Add new enum value to `NotificationChannel`
2. Add simulation method in `NotificationService`
3. Existing preference initialization automatically includes new channels

### Storing Notification Records

The system stores all attempts in `notification_history` table with indexes on:
- `user_id` (for filtering by user)
- `status` (for filtering by delivery status)
- `channel` (for filtering by channel)

### Database Queries

Example custom queries (can be added to repository):

```java
// Find failed notifications in last hour
List<NotificationHistory> findFailedNotificationsInLastHour(LocalDateTime since);

// Find user statistics
Object[] getUserNotificationStats(Long userId);
```

## Production Deployment

### Before Deployment

1. Replace H2 with production database (PostgreSQL, MySQL)
2. Integrate with real notification services
3. Add authentication/authorization
4. Implement rate limiting
5. Add API versioning (/api/v1/users)
6. Enable HTTPS/TLS
7. Configure proper logging and monitoring
8. Add request validation and sanitization
9. Implement caching where appropriate

### Configuration Changes

Update `application.properties`:
```properties
# Use PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/notification_db
spring.datasource.username=notification_user
spring.datasource.password=secure_password
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Performance Considerations

- **Indexing**: Automatic indexes on frequently queried fields
- **Pagination**: Can be added to list endpoints for large datasets
- **Connection Pooling**: Configured automatically by Spring Boot
- **Transaction Management**: Proper transaction boundaries for data consistency

## Security Considerations

- Input validation at controller level
- SQL injection prevention via JPA/Hibernate
- CSRF protection can be added
- Role-based access control (RBAC) can be implemented
- Request rate limiting recommended

## Troubleshooting

### Application won't start
- Ensure Java 17+ is installed
- Check port 8080 is not in use
- Verify Maven is installed: `mvn -v`

### H2 Console not accessible
- Ensure `spring.h2.console.enabled=true` in properties
- Use correct URL: `http://localhost:8080/api/h2-console`

### Notifications keep failing
- Check user exists
- Verify channel is enabled
- Check notification message is not empty
- Review application logs

## Future Enhancements

1. **Batch Operations**: Send notifications to multiple users at once
2. **Scheduled Notifications**: Schedule notifications for future delivery
3. **Notification Templates**: Predefined templates for common notifications
4. **Analytics Dashboard**: Real-time delivery statistics
5. **Webhooks**: Callback endpoints for notification status updates
6. **Multi-language Support**: Localization for different regions
7. **User Preferences UI**: Web interface for preference management
8. **Notification Priority Levels**: Urgent vs. regular notifications

## License

This project is licensed under the MIT License.

## Support & Contribution

For issues, feature requests, or contributions, please create an issue or pull request.

---

**Last Updated**: January 2024
**Version**: 1.0.0
**Status**: Production Ready
