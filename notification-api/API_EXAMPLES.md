# API Request & Response Examples

Complete guide for testing the Notification Preference & Delivery API with sample requests and responses.

## Base URL
```
http://localhost:8080/api
```

---

## 1. USER MANAGEMENT ENDPOINTS

### 1.1 Create User

**Request**
```http
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890"
}
```

**cURL Command**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890"
  }'
```

**Response (201 Created)**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "createdAt": "2024-01-15T10:30:25.123456",
    "updatedAt": "2024-01-15T10:30:25.123456"
  },
  "timestamp": "2024-01-15T10:30:25.456789"
}
```

---

### 1.2 Create Multiple Users

**Request 1**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "+9876543210"
  }'
```

**Response**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "+9876543210",
    "createdAt": "2024-01-15T10:31:00.000000",
    "updatedAt": "2024-01-15T10:31:00.000000"
  },
  "timestamp": "2024-01-15T10:31:00.123456"
}
```

**Request 2**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Johnson",
    "email": "bob.johnson@example.com",
    "phone": "+5555555555"
  }'
```

---

### 1.3 Get User by ID

**Request**
```http
GET /api/users/1
```

**cURL Command**
```bash
curl http://localhost:8080/api/users/1
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "createdAt": "2024-01-15T10:30:25.123456",
    "updatedAt": "2024-01-15T10:30:25.123456"
  },
  "timestamp": "2024-01-15T10:32:00.123456"
}
```

---

### 1.4 Get All Users

**Request**
```http
GET /api/users
```

**cURL Command**
```bash
curl http://localhost:8080/api/users
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "phone": "+1234567890",
      "createdAt": "2024-01-15T10:30:25.123456",
      "updatedAt": "2024-01-15T10:30:25.123456"
    },
    {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane.smith@example.com",
      "phone": "+9876543210",
      "createdAt": "2024-01-15T10:31:00.000000",
      "updatedAt": "2024-01-15T10:31:00.000000"
    },
    {
      "id": 3,
      "name": "Bob Johnson",
      "email": "bob.johnson@example.com",
      "phone": "+5555555555",
      "createdAt": "2024-01-15T10:31:30.000000",
      "updatedAt": "2024-01-15T10:31:30.000000"
    }
  ],
  "timestamp": "2024-01-15T10:33:00.123456"
}
```

---

### 1.5 Update User

**Request**
```http
PUT /api/users/1
Content-Type: application/json

{
  "name": "John Smith",
  "email": "john.smith@example.com",
  "phone": "+1111111111"
}
```

**cURL Command**
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john.smith@example.com",
    "phone": "+1111111111"
  }'
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 1,
    "name": "John Smith",
    "email": "john.smith@example.com",
    "phone": "+1111111111",
    "createdAt": "2024-01-15T10:30:25.123456",
    "updatedAt": "2024-01-15T10:34:00.654321"
  },
  "timestamp": "2024-01-15T10:34:00.987654"
}
```

---

### 1.6 Delete User

**Request**
```http
DELETE /api/users/1
```

**cURL Command**
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "User deleted successfully",
  "data": null,
  "timestamp": "2024-01-15T10:35:00.123456"
}
```

---

### 1.7 Error: User Not Found

**Request**
```http
GET /api/users/999
```

**Response (404 Not Found)**
```json
{
  "success": false,
  "message": "User not found with ID: 999",
  "data": null,
  "timestamp": "2024-01-15T10:36:00.123456"
}
```

---

### 1.8 Error: Duplicate Email

**Request**
```http
POST /api/users
Content-Type: application/json

{
  "name": "Another User",
  "email": "john.doe@example.com",
  "phone": "+2222222222"
}
```

**Response (400 Bad Request)**
```json
{
  "success": false,
  "message": "Invalid request: Email already exists: john.doe@example.com",
  "data": null,
  "timestamp": "2024-01-15T10:37:00.123456"
}
```

---

## 2. NOTIFICATION PREFERENCES ENDPOINTS

### 2.1 Get All User Preferences

**Request**
```http
GET /api/users/2/preferences
```

**cURL Command**
```bash
curl http://localhost:8080/api/users/2/preferences
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Preferences retrieved successfully",
  "data": [
    {
      "id": 4,
      "userId": 2,
      "channel": "EMAIL",
      "enabled": false,
      "createdAt": "2024-01-15T10:31:00.000000",
      "updatedAt": "2024-01-15T10:31:00.000000"
    },
    {
      "id": 5,
      "userId": 2,
      "channel": "SMS",
      "enabled": false,
      "createdAt": "2024-01-15T10:31:00.000000",
      "updatedAt": "2024-01-15T10:31:00.000000"
    },
    {
      "id": 6,
      "userId": 2,
      "channel": "PUSH",
      "enabled": false,
      "createdAt": "2024-01-15T10:31:00.000000",
      "updatedAt": "2024-01-15T10:31:00.000000"
    }
  ],
  "timestamp": "2024-01-15T10:38:00.123456"
}
```

---

### 2.2 Get Specific Channel Preference

**Request**
```http
GET /api/users/2/preferences/email
```

**cURL Command**
```bash
curl http://localhost:8080/api/users/2/preferences/email
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Preference retrieved successfully",
  "data": {
    "id": 4,
    "userId": 2,
    "channel": "EMAIL",
    "enabled": false,
    "createdAt": "2024-01-15T10:31:00.000000",
    "updatedAt": "2024-01-15T10:31:00.000000"
  },
  "timestamp": "2024-01-15T10:39:00.123456"
}
```

---

### 2.3 Enable Email Channel

**Request**
```http
POST /api/users/2/preferences/email/enable
```

**cURL Command**
```bash
curl -X POST http://localhost:8080/api/users/2/preferences/email/enable
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Channel enabled successfully",
  "data": {
    "id": 4,
    "userId": 2,
    "channel": "EMAIL",
    "enabled": true,
    "createdAt": "2024-01-15T10:31:00.000000",
    "updatedAt": "2024-01-15T10:40:00.000000"
  },
  "timestamp": "2024-01-15T10:40:00.123456"
}
```

---

### 2.4 Enable SMS Channel

**Request**
```http
POST /api/users/2/preferences/sms/enable
```

**cURL Command**
```bash
curl -X POST http://localhost:8080/api/users/2/preferences/sms/enable
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Channel enabled successfully",
  "data": {
    "id": 5,
    "userId": 2,
    "channel": "SMS",
    "enabled": true,
    "createdAt": "2024-01-15T10:31:00.000000",
    "updatedAt": "2024-01-15T10:40:30.000000"
  },
  "timestamp": "2024-01-15T10:40:30.123456"
}
```

---

### 2.5 Enable Push Notification Channel

**Request**
```http
POST /api/users/2/preferences/push/enable
```

**cURL Command**
```bash
curl -X POST http://localhost:8080/api/users/2/preferences/push/enable
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Channel enabled successfully",
  "data": {
    "id": 6,
    "userId": 2,
    "channel": "PUSH",
    "enabled": true,
    "createdAt": "2024-01-15T10:31:00.000000",
    "updatedAt": "2024-01-15T10:41:00.000000"
  },
  "timestamp": "2024-01-15T10:41:00.123456"
}
```

---

### 2.6 Disable Email Channel

**Request**
```http
POST /api/users/2/preferences/email/disable
```

**cURL Command**
```bash
curl -X POST http://localhost:8080/api/users/2/preferences/email/disable
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Channel disabled successfully",
  "data": {
    "id": 4,
    "userId": 2,
    "channel": "EMAIL",
    "enabled": false,
    "createdAt": "2024-01-15T10:31:00.000000",
    "updatedAt": "2024-01-15T10:42:00.000000"
  },
  "timestamp": "2024-01-15T10:42:00.123456"
}
```

---

### 2.7 Update Preference (PUT)

**Request**
```http
PUT /api/users/2/preferences/sms
Content-Type: application/json

{
  "enabled": false
}
```

**cURL Command**
```bash
curl -X PUT http://localhost:8080/api/users/2/preferences/sms \
  -H "Content-Type: application/json" \
  -d '{
    "enabled": false
  }'
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Preference updated successfully",
  "data": {
    "id": 5,
    "userId": 2,
    "channel": "SMS",
    "enabled": false,
    "createdAt": "2024-01-15T10:31:00.000000",
    "updatedAt": "2024-01-15T10:43:00.000000"
  },
  "timestamp": "2024-01-15T10:43:00.123456"
}
```

---

### 2.8 Error: User Not Found

**Request**
```http
GET /api/users/999/preferences
```

**Response (404 Not Found)**
```json
{
  "success": false,
  "message": "User not found with ID: 999",
  "data": null,
  "timestamp": "2024-01-15T10:44:00.123456"
}
```

---

## 3. NOTIFICATION DELIVERY ENDPOINTS

### 3.1 Send Notification via Email (Success)

**Request**
```http
POST /api/notifications/send
Content-Type: application/json

{
  "userId": 2,
  "channel": "EMAIL",
  "message": "Your account has been successfully verified!"
}
```

**cURL Command**
```bash
curl -X POST http://localhost:8080/api/notifications/send \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "channel": "EMAIL",
    "message": "Your account has been successfully verified!"
  }'
```

**Response (201 Created) - Success Case**
```json
{
  "success": true,
  "message": "Notification processed",
  "data": {
    "id": 1,
    "userId": 2,
    "channel": "EMAIL",
    "message": "Your account has been successfully verified!",
    "status": "SENT",
    "sentAt": "2024-01-15T10:45:00.000000",
    "failureReason": null,
    "createdAt": "2024-01-15T10:45:00.123456"
  },
  "timestamp": "2024-01-15T10:45:00.987654"
}
```

---

### 3.2 Send SMS Notification

**Request**
```http
POST /api/notifications/send
Content-Type: application/json

{
  "userId": 2,
  "channel": "SMS",
  "message": "Your verification code is: 123456"
}
```

**cURL Command**
```bash
curl -X POST http://localhost:8080/api/notifications/send \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "channel": "SMS",
    "message": "Your verification code is: 123456"
  }'
```

**Response (201 Created)**
```json
{
  "success": true,
  "message": "Notification processed",
  "data": {
    "id": 2,
    "userId": 2,
    "channel": "SMS",
    "message": "Your verification code is: 123456",
    "status": "SENT",
    "sentAt": "2024-01-15T10:46:00.000000",
    "failureReason": null,
    "createdAt": "2024-01-15T10:46:00.123456"
  },
  "timestamp": "2024-01-15T10:46:00.987654"
}
```

---

### 3.3 Send Push Notification

**Request**
```http
POST /api/notifications/send
Content-Type: application/json

{
  "userId": 2,
  "channel": "PUSH",
  "message": "You have a new message from John Doe"
}
```

**cURL Command**
```bash
curl -X POST http://localhost:8080/api/notifications/send \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "channel": "PUSH",
    "message": "You have a new message from John Doe"
  }'
```

**Response (201 Created)**
```json
{
  "success": true,
  "message": "Notification processed",
  "data": {
    "id": 3,
    "userId": 2,
    "channel": "PUSH",
    "message": "You have a new message from John Doe",
    "status": "SENT",
    "sentAt": "2024-01-15T10:47:00.000000",
    "failureReason": null,
    "createdAt": "2024-01-15T10:47:00.123456"
  },
  "timestamp": "2024-01-15T10:47:00.987654"
}
```

---

### 3.4 Error: Channel Disabled

**Request**
```http
POST /api/notifications/send
Content-Type: application/json

{
  "userId": 2,
  "channel": "EMAIL",
  "message": "This should fail"
}
```

(Assuming EMAIL channel was disabled via `POST /api/users/2/preferences/email/disable`)

**Response (400 Bad Request)**
```json
{
  "success": false,
  "message": "Notification channel EMAIL is disabled for user: 2",
  "data": null,
  "timestamp": "2024-01-15T10:48:00.123456"
}
```

---

### 3.5 Error: User Not Found

**Request**
```http
POST /api/notifications/send
Content-Type: application/json

{
  "userId": 999,
  "channel": "EMAIL",
  "message": "Test message"
}
```

**Response (404 Not Found)**
```json
{
  "success": false,
  "message": "User not found with ID: 999",
  "data": null,
  "timestamp": "2024-01-15T10:49:00.123456"
}
```

---

### 3.6 Simulated Failure Example

**Response (201 Created) - Failure Case**
```json
{
  "success": true,
  "message": "Notification processed",
  "data": {
    "id": 4,
    "userId": 2,
    "channel": "EMAIL",
    "message": "Test email message",
    "status": "FAILED",
    "sentAt": "2024-01-15T10:50:00.000000",
    "failureReason": "Email service temporarily unavailable",
    "createdAt": "2024-01-15T10:50:00.123456"
  },
  "timestamp": "2024-01-15T10:50:00.987654"
}
```

---

## 4. NOTIFICATION HISTORY ENDPOINTS

### 4.1 Get All User Notifications

**Request**
```http
GET /api/notifications/user/2
```

**cURL Command**
```bash
curl http://localhost:8080/api/notifications/user/2
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Notifications retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 2,
      "channel": "EMAIL",
      "message": "Your account has been successfully verified!",
      "status": "SENT",
      "sentAt": "2024-01-15T10:45:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:45:00.123456"
    },
    {
      "id": 2,
      "userId": 2,
      "channel": "SMS",
      "message": "Your verification code is: 123456",
      "status": "SENT",
      "sentAt": "2024-01-15T10:46:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:46:00.123456"
    },
    {
      "id": 3,
      "userId": 2,
      "channel": "PUSH",
      "message": "You have a new message from John Doe",
      "status": "SENT",
      "sentAt": "2024-01-15T10:47:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:47:00.123456"
    }
  ],
  "timestamp": "2024-01-15T10:51:00.123456"
}
```

---

### 4.2 Get User Notifications by Status (SENT)

**Request**
```http
GET /api/notifications/user/2/status/sent
```

**cURL Command**
```bash
curl http://localhost:8080/api/notifications/user/2/status/sent
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Notifications retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 2,
      "channel": "EMAIL",
      "message": "Your account has been successfully verified!",
      "status": "SENT",
      "sentAt": "2024-01-15T10:45:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:45:00.123456"
    },
    {
      "id": 2,
      "userId": 2,
      "channel": "SMS",
      "message": "Your verification code is: 123456",
      "status": "SENT",
      "sentAt": "2024-01-15T10:46:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:46:00.123456"
    }
  ],
  "timestamp": "2024-01-15T10:52:00.123456"
}
```

---

### 4.3 Get User Notifications by Status (FAILED)

**Request**
```http
GET /api/notifications/user/2/status/failed
```

**cURL Command**
```bash
curl http://localhost:8080/api/notifications/user/2/status/failed
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Notifications retrieved successfully",
  "data": [],
  "timestamp": "2024-01-15T10:53:00.123456"
}
```

---

### 4.4 Get User Notifications by Channel (EMAIL)

**Request**
```http
GET /api/notifications/user/2/channel/email
```

**cURL Command**
```bash
curl http://localhost:8080/api/notifications/user/2/channel/email
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Notifications retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 2,
      "channel": "EMAIL",
      "message": "Your account has been successfully verified!",
      "status": "SENT",
      "sentAt": "2024-01-15T10:45:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:45:00.123456"
    }
  ],
  "timestamp": "2024-01-15T10:54:00.123456"
}
```

---

### 4.5 Get System-wide Notifications by Status

**Request**
```http
GET /api/notifications/status/sent
```

**cURL Command**
```bash
curl http://localhost:8080/api/notifications/status/sent
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Notifications retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 2,
      "channel": "EMAIL",
      "message": "Your account has been successfully verified!",
      "status": "SENT",
      "sentAt": "2024-01-15T10:45:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:45:00.123456"
    },
    {
      "id": 2,
      "userId": 2,
      "channel": "SMS",
      "message": "Your verification code is: 123456",
      "status": "SENT",
      "sentAt": "2024-01-15T10:46:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:46:00.123456"
    },
    {
      "id": 3,
      "userId": 2,
      "channel": "PUSH",
      "message": "You have a new message from John Doe",
      "status": "SENT",
      "sentAt": "2024-01-15T10:47:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:47:00.123456"
    }
  ],
  "timestamp": "2024-01-15T10:55:00.123456"
}
```

---

### 4.6 Get System-wide Notifications by Channel

**Request**
```http
GET /api/notifications/channel/email
```

**cURL Command**
```bash
curl http://localhost:8080/api/notifications/channel/email
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "Notifications retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 2,
      "channel": "EMAIL",
      "message": "Your account has been successfully verified!",
      "status": "SENT",
      "sentAt": "2024-01-15T10:45:00.000000",
      "failureReason": null,
      "createdAt": "2024-01-15T10:45:00.123456"
    }
  ],
  "timestamp": "2024-01-15T10:56:00.123456"
}
```

---

## Testing Workflow

### Complete User Journey

```bash
# 1. Create a new user
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Wonder",
    "email": "alice.wonder@example.com",
    "phone": "+1357924680"
  }')

echo "User Created: $USER_RESPONSE"

# 2. Extract user ID (requires jq for JSON parsing)
USER_ID=$(echo $USER_RESPONSE | jq '.data.id')

# 3. Get all preferences
curl http://localhost:8080/api/users/$USER_ID/preferences

# 4. Enable email channel
curl -X POST http://localhost:8080/api/users/$USER_ID/preferences/email/enable

# 5. Enable SMS channel
curl -X POST http://localhost:8080/api/users/$USER_ID/preferences/sms/enable

# 6. Send email notification
curl -X POST http://localhost:8080/api/notifications/send \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": $USER_ID,
    \"channel\": \"EMAIL\",
    \"message\": \"Welcome to our platform!\"
  }"

# 7. Send SMS notification
curl -X POST http://localhost:8080/api/notifications/send \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": $USER_ID,
    \"channel\": \"SMS\",
    \"message\": \"Thanks for signing up!\"
  }"

# 8. Get all notifications
curl http://localhost:8080/api/notifications/user/$USER_ID

# 9. Get only SENT notifications
curl http://localhost:8080/api/notifications/user/$USER_ID/status/sent

# 10. Get only email notifications
curl http://localhost:8080/api/notifications/user/$USER_ID/channel/email
```

---

## Response Status Codes Reference

| Code | Meaning | Scenario |
|------|---------|----------|
| 200 OK | Success | GET, PUT operations completed successfully |
| 201 Created | Resource Created | POST operation created new resource |
| 400 Bad Request | Invalid Input | Invalid enum, disabled channel, duplicate email |
| 404 Not Found | Not Found | User/preference doesn't exist |
| 500 Server Error | Server Error | Unexpected backend error |

---

**Note**: Adjust `localhost:8080` with your server's actual address when deployed.