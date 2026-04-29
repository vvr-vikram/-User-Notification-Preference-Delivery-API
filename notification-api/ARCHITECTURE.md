# Architecture & Design Documentation

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                    HTTP Clients                                  │
│          (Postman, cURL, Browser, IDE Tools)                    │
└──────────────────────────┬──────────────────────────────────────┘
                           │
        ┌──────────────────┴──────────────────┐
        │  Spring Boot Application (Port 8080) │
        └──────────────────┬──────────────────┘
                           │
        ┌──────────────────┴──────────────────────────────────────┐
        │                REST Controllers                          │
        │  ┌─────────────────────────────────────────────────┐  │
        │  │ • UserController                               │  │
        │  │ • NotificationPreferenceController             │  │
        │  │ • NotificationController                       │  │
        │  │ + GlobalExceptionHandler                       │  │
        │  └─────────────────────────────────────────────────┘  │
        └──────────────────┬──────────────────────────────────────┘
                           │
        ┌──────────────────┴──────────────────────────────────────┐
        │              Service Layer                              │
        │  ┌─────────────────────────────────────────────────┐  │
        │  │ • UserService                                  │  │
        │  │ • NotificationPreferenceService                │  │
        │  │ • NotificationService                          │  │
        │  │   - Simulates delivery                         │  │
        │  │   - Records history                            │  │
        │  │   - Validates channels                         │  │
        │  └─────────────────────────────────────────────────┘  │
        └──────────────────┬──────────────────────────────────────┘
                           │
        ┌──────────────────┴──────────────────────────────────────┐
        │            Repository Layer (JPA)                       │
        │  ┌─────────────────────────────────────────────────┐  │
        │  │ • UserRepository                               │  │
        │  │ • NotificationPreferenceRepository             │  │
        │  │ • NotificationHistoryRepository                │  │
        │  └─────────────────────────────────────────────────┘  │
        └──────────────────┬──────────────────────────────────────┘
                           │
        ┌──────────────────┴──────────────────────────────────────┐
        │              Entity Layer (JPA)                         │
        │  ┌─────────────────────────────────────────────────┐  │
        │  │ • User Entity                                   │  │
        │  │ • NotificationPreference Entity                 │  │
        │  │ • NotificationHistory Entity                    │  │
        │  └─────────────────────────────────────────────────┘  │
        └──────────────────┬──────────────────────────────────────┘
                           │
        ┌──────────────────┴──────────────────────────────────────┐
        │           H2 In-Memory Database                         │
        │  ┌─────────────────────────────────────────────────┐  │
        │  │ • users table                                  │  │
        │  │ • notification_preferences table               │  │
        │  │ • notification_history table                   │  │
        │  └─────────────────────────────────────────────────┘  │
        └─────────────────────────────────────────────────────────┘
```

---

## Entity Relationship Diagram (ERD)

```
┌────────────────────────┐
│       User             │
├────────────────────────┤
│ id (PK)                │
│ name                   │
│ email (UNIQUE)         │
│ phone (UNIQUE)         │
│ created_at             │
│ updated_at             │
└────────┬───────────────┘
         │ 1
         │
         │ M
         ├─────────────────────────────────┬─────────────────────────────┐
         │                                 │                             │
         │                                 │                             │
         ▼                                 ▼                             ▼
┌─────────────────────────────┐   ┌──────────────────────────┐
│ NotificationPreference       │   │ NotificationHistory      │
├─────────────────────────────┤   ├──────────────────────────┤
│ id (PK)                      │   │ id (PK)                  │
│ user_id (FK)                 │   │ user_id (FK) [INDEXED]   │
│ channel (ENUM) ──┐           │   │ channel (ENUM) [INDEXED] │
│ enabled (BOOL)   │           │   │ message                  │
│ created_at       │           │   │ status (ENUM) [INDEXED]  │
│ updated_at       │           │   │ sent_at                  │
│ UNIQUE(user_id,  │           │   │ failure_reason           │
│         channel) │           │   │ created_at               │
└─────────────────────────────┘   └──────────────────────────┘
                                    
Enumerations:
├─ NotificationChannel.java
│  └─ EMAIL, SMS, PUSH
└─ NotificationStatus.java
   └─ SENT, FAILED, PENDING
```

---

## Request/Response Flow Diagram

### Example 1: Create User and Enable Notifications

```
Client (Postman/cURL)
   │
   ├─ POST /api/users ────────────────────────────────────────────┐
   │                                                               │
   │                                                               ▼
   │                                         UserController
   │                                              │
   │                                              ├─ Validate input
   │                                              │
   │                                              ▼
   │                                         UserService
   │                                              │
   │                                    ┌─────────┼─────────┐
   │                                    │         │         │
   │                                    ▼         │         ▼
   │                              Check unique   │    Create entity
   │                              (email/phone)  │
   │                                    │         │
   │                                    ▼         │
   │                         UserRepository.save()
   │                                    │         │
   │                                    ▼         │
   │                          H2 Database (Insert)
   │                                    │◄────────┘
   │                                    │
   │                                    ▼
   │                    Preference init service
   │                    (disabled=true for all)
   │                                    │
   │                                    ▼
   │                    NotificationPreferenceService
   │                                    │
   │                                    └─► Save 3 preferences
   │                                         (EMAIL, SMS, PUSH)
   │◄────────────────────────────────────────────────────────────┘
   │
   └─ 201 Created ◄─ UserDTO + preferences initialized
```

### Example 2: Send Notification

```
Client
   │
   ├─ POST /api/notifications/send ───────────────────────┐
   │  {userId: 1, channel: EMAIL, message: "..."}         │
   │                                                       ▼
   │                              NotificationController
   │                                       │
   │                                       ▼
   │                             NotificationService
   │                                       │
   │                    ┌──────────────────┼──────────────────┐
   │                    │                  │                  │
   │                    ▼                  ▼                  ▼
   │         Verify user exists   Check channel   Simulate delivery
   │                    │              enabled         │
   │                 (Found)              │            │
   │                    │           (enabled=true)     │
   │                    │                  │            ▼
   │                    │                  │    simulateEmailDelivery()
   │                    │                  │      [90% success rate]
   │                    │                  │            │
   │                    └──────────────────┴────────────┴─┐
   │                                                      │
   │                                    ┌────────────────┘
   │                                    │
   │                                    ▼
   │                         Create NotificationHistory
   │                         (status = SENT or FAILED)
   │                                    │
   │                                    ▼
   │                      NotificationHistoryRepository
   │                                    │
   │                                    ▼
   │                           Save to H2 Database
   │◄───────────────────────────────────────────────────┐
   │                                                     │
   └─ 201 Created ◄─ NotificationHistoryDTO (with status)
```

---

## Data Flow: Complete User Journey

```
STEP 1: CREATE USER
┌─────────────────┐
│ User Registration│
└────────┬────────┘
         │
         ▼
    ┌─────────────────────────────────────────┐
    │ POST /api/users                         │
    │ {name, email, phone}                    │
    └────────┬────────────────────────────────┘
             │
             ▼
        UserController
             │
             ├─► Validate email/phone unique
             │
             ▼
        UserService.createUser()
             │
             ├─► Save User entity
             ├─► Initialize Default Preferences
             │   (EMAIL=disabled, SMS=disabled, PUSH=disabled)
             │
             ▼
        Response: 201 Created with User + Preferences


STEP 2: ENABLE NOTIFICATION CHANNELS
┌──────────────────────────────┐
│ User enables email channel   │
└────────┬─────────────────────┘
         │
         ▼
    ┌────────────────────────────────────────────┐
    │ POST /api/users/{id}/preferences/email/enable
    └────────┬─────────────────────────────────────┘
             │
             ▼
        NotificationPreferenceController
             │
             ├─► Extract user ID and channel
             │
             ▼
        NotificationPreferenceService.enableChannel()
             │
             ├─► Find or create preference
             ├─► Set enabled = true
             ├─► Save to database
             │
             ▼
        Response: 200 OK with Updated Preference


STEP 3: SEND NOTIFICATION
┌──────────────────────────────┐
│ Application sends notification
└────────┬─────────────────────┘
         │
         ▼
    ┌──────────────────────────────────────┐
    │ POST /api/notifications/send         │
    │ {userId, channel: EMAIL, message}    │
    └────────┬────────────────────────────┘
             │
             ▼
        NotificationController
             │
             ▼
        NotificationService.sendNotification()
             │
             ├─► UserService.getUserEntityById(userId)
             │   [Validate user exists]
             │
             ├─► PreferenceService.isChannelEnabled()
             │   [Check: EMAIL enabled? YES]
             │
             ├─► simulateEmailDelivery()
             │   [Random: 10% fail, 90% success]
             │   [Result: SENT]
             │
             ├─► Create NotificationHistory
             │   status = SENT
             │   failureReason = null
             │
             ├─► Save to history repository
             │
             ▼
        Response: 201 Created with Notification Details


STEP 4: QUERY NOTIFICATION HISTORY
┌──────────────────────────┐
│ User checks notifications
└────────┬─────────────────┘
         │
         ▼
    ┌────────────────────────────────────────────┐
    │ GET /api/notifications/user/{id}           │
    │ GET /api/notifications/user/{id}/status/{status}
    │ GET /api/notifications/user/{id}/channel/{channel}
    └────────┬─────────────────────────────────────┘
             │
             ▼
        NotificationController
             │
             ▼
        NotificationService.getUserNotifications*()
             │
             ├─► Verify user exists
             ├─► Query NotificationHistoryRepository
             │   [With optional filters]
             │
             ▼
        Response: 200 OK with List<NotificationHistoryDTO>
```

---

## Class Relationships & Dependencies

```
UserController
    ├─ UserService
    │   ├─ UserRepository
    │   └─ EntityMapper
    └─ NotificationPreferenceService

NotificationPreferenceController
    └─ NotificationPreferenceService
        ├─ NotificationPreferenceRepository
        ├─ UserService
        └─ EntityMapper

NotificationController
    └─ NotificationService
        ├─ NotificationHistoryRepository
        ├─ NotificationPreferenceService
        ├─ UserService
        └─ EntityMapper

EntityMapper
    └─ [Converts between Entities and DTOs]

GlobalExceptionHandler
    └─ [Catches and formats all exceptions]
```

---

## Key Processing Flow: Send Notification

```
INPUT: SendNotificationDTO
{
  "userId": 1,
  "channel": "EMAIL",
  "message": "Welcome!"
}
        │
        ▼
┌──────────────────────────────────────────────┐
│ NotificationService.sendNotification()        │
└──────────────────────────────────────────────┘
        │
        ├─────────────┬──────────────────┬──────────────────┐
        │             │                  │                  │
        ▼             ▼                  ▼                  ▼
   Verify User  Check Channel    Simulate Delivery   Track Status
        │             │                  │                  │
        ├─► User?     ├─► Enabled?       ├─► Success?       ├─► SENT
        │   (404)     │   (400)          │   status = SENT   │   status = FAILED
        │             │                  │   [90-95% rate]   │
        ▼             ▼                  ▼                   ▼
    EXISTS          TRUE            FAILED              CREATE RECORD
                                    status = FAILED
                                    failureReason = msg
        │             │                  │                  │
        └─────────────┴──────────────────┴──────────────────┘
                              │
                              ▼
                  NotificationHistory entity
                  ├─ id: auto-generated
                  ├─ userId: 1
                  ├─ channel: EMAIL
                  ├─ message: "Welcome!"
                  ├─ status: SENT | FAILED
                  ├─ failureReason: null | "..."
                  ├─ sentAt: NOW
                  └─ createdAt: NOW
                              │
                              ▼
                    Save to Database
                    (Audit Trail)
                              │
                              ▼
OUTPUT: NotificationHistoryDTO
{
  "id": 1,
  "userId": 1,
  "channel": "EMAIL",
  "message": "Welcome!",
  "status": "SENT",
  "sentAt": "2024-01-15T10:40:00",
  "failureReason": null,
  "createdAt": "2024-01-15T10:40:00"
}
```

---

## Transaction Boundaries & Isolation

```
@Transactional Methods (Write Operations)
┌───────────────────────────────────────────────┐
│ UserService.createUser()                      │
│ ├─ Save User (DB Write)                       │
│ └─ Initialize Preferences (3 DB Writes)       │
│ => All 4 operations in single transaction     │
└───────────────────────────────────────────────┘

┌───────────────────────────────────────────────┐
│ NotificationService.sendNotification()        │
│ ├─ Verify User (DB Read)                      │
│ ├─ Check Preference (DB Read)                 │
│ ├─ Simulate Delivery (No DB)                  │
│ └─ Save History (DB Write)                    │
│ => Read operations outside transaction scope  │
│    History write in separate transaction      │
└───────────────────────────────────────────────┘

@Transactional(readOnly = true) Methods
┌───────────────────────────────────────────────┐
│ UserService.getUserById()                     │
│ NotificationService.getUserNotifications()    │
│ => Optimized for read-only access             │
│    No write locks, better performance         │
└───────────────────────────────────────────────┘
```

---

## Error Handling Flow

```
CLIENT REQUEST
        │
        ▼
┌───────────────────────────────┐
│ Controller                    │
│ ├─ Validate path variables    │
│ └─ Call Service               │
└───────────────────────────────┘
        │
        ├─────────────────────┬────────────────────┬──────────────┐
        │                     │                    │              │
        ▼                     ▼                    ▼              ▼
   SUCCESS             VALIDATION ERROR      RESOURCE ERROR   RUNTIME ERROR
        │                     │                    │              │
        ▼                     ▼                    ▼              ▼
   RETURN 200/201    IllegalArgumentException  ResourceNotFound  General Exception
        │                     │                    │              │
        ├─────────────────────┼────────────────────┼──────────────┤
        │                     │                    │              │
        └─────────────────────┴────────────────────┴──────────────┘
                              │
                              ▼
                    GlobalExceptionHandler
                              │
        ┌─────────────────────┼────────────────────┬──────────────┐
        │                     │                    │              │
        ▼                     ▼                    ▼              ▼
   400 Bad Request        400 Bad Request      404 Not Found   500 Server Error
        │                     │                    │              │
        └─────────────────────┼────────────────────┴──────────────┘
                              │
                              ▼
                    ApiResponse<Object>
                    {
                      success: false,
                      message: "Error description",
                      data: null,
                      timestamp: "..."
                    }
```

---

## Database Connection Pool & Lifecycle

```
Application Startup
        │
        ▼
┌─────────────────────────┐
│ Spring Boot             │
│ ├─ Load properties      │
│ ├─ Initialize DataSource│
│ └─ Create H2 In-memory DB
└─────────────────────────┘
        │
        ▼
┌─────────────────────────┐
│ Hibernate JPA           │
│ ├─ Parse Entities       │
│ ├─ Generate DDL         │
│ └─ Create Tables        │
└─────────────────────────┘
        │
        ▼
┌─────────────────────────┐
│ Application Ready       │
│ Listening on :8080/api  │
└─────────────────────────┘
        │
        ├─ GET/POST/PUT/DELETE requests
        │       │
        │       ▼
        │   Repository methods
        │       │
        │       ├─ Hibernate creates SQL
        │       ├─ Executes on H2
        │       ├─ Caches results (if needed)
        │       └─ Returns to service
        │
        └─ Application Shutdown
                │
                ▼
           Close connections
                │
                ▼
           Destroy in-memory DB
```

---

## Concurrency & Thread Safety

```
Multi-request Scenario:

Request 1 (User A)          Request 2 (User B)
    │                           │
    ├─ GET /users/1             ├─ GET /users/2
    │  └─► UserService          │  └─► UserService
    │      └─► Repository       │      └─► Repository
    │          └─► Query        │          └─► Query (Different User)
    │              (NO LOCK)    │              (NO LOCK)
    │              │            │              │
    ▼              ▼            ▼              ▼
 Response 1     Response 2  [Concurrent - Safe due to isolation]

Request 3 (Notification)
    │
    ├─ POST /notifications/send
    │  └─► NotificationService
    │      ├─ Read (preference check)      [Isolated Read]
    │      ├─ Simulate delivery
    │      └─ Write (history record)       [Transaction]

Spring Transactional annotation ensures:
├─ Session per request
├─ Proper transaction handling
├─ Thread-local contexts
└─ Connection management
```

---

## Notification Delivery Simulation

```
NotificationService.simulateNotificationDelivery()
        │
        ├─────────────┬──────────────┬──────────────┐
        │             │              │              │
        ▼             ▼              ▼              ▼
     EMAIL           SMS            PUSH          Others
        │             │              │              │
        ▼             ▼              ▼              ▼
   Math.random()  Math.random()  Math.random()  IllegalArgumentException
   < 0.10?        < 0.05?        < 0.15?        │
   │              │              │              │
   ├─ YES: Fail   ├─ YES: Fail   ├─ YES: Fail   └─ Throw
   │              │              │
   └─ NO: Success └─ NO: Success └─ NO: Success
        │              │              │
        ▼              ▼              ▼
    [Recorded as SENT status with null failureReason]
```

---

## API Contract (Request → Response)

### Pattern 1: Create Operation
```
Request:  POST /api/resource
Body:     { "field1": "value", "field2": "value" }
Response: 201 Created
Body:     {
             "success": true,
             "message": "Resource created successfully",
             "data": { "id": 1, ... },
             "timestamp": "..."
           }
```

### Pattern 2: Read Operation (List)
```
Request:  GET /api/resources
Response: 200 OK
Body:     {
             "success": true,
             "message": "Resources retrieved successfully",
             "data": [ {...}, {...} ],
             "timestamp": "..."
           }
```

### Pattern 3: Read Operation (Single)
```
Request:  GET /api/resources/{id}
Response: 200 OK | 404 Not Found
Body:     {
             "success": true/false,
             "message": "...",
             "data": {...} | null,
             "timestamp": "..."
           }
```

### Pattern 4: Update Operation
```
Request:  PUT /api/resources/{id}
Body:     { "field1": "newValue" }
Response: 200 OK
Body:     {
             "success": true,
             "message": "Resource updated successfully",
             "data": { "id": 1, ... },
             "timestamp": "..."
           }
```

### Pattern 5: Delete Operation
```
Request:  DELETE /api/resources/{id}
Response: 200 OK
Body:     {
             "success": true,
             "message": "Resource deleted successfully",
             "data": null,
             "timestamp": "..."
           }
```

### Pattern 6: Error Operation
```
Request:  GET /api/resources/999
Response: 404 Not Found
Body:     {
             "success": false,
             "message": "Resource not found",
             "data": null,
             "timestamp": "..."
           }
```

---

## Deployment Architecture (Future)

```
Production Environment:

┌─────────────────────────────────────────────┐
│ Load Balancer (nginx/AWS ALB)               │
│ ├─ Handle HTTPS/TLS                         │
│ └─ Route to multiple instances              │
└────────────┬────────────────────────────────┘
             │
    ┌────────┼────────┐
    │        │        │
    ▼        ▼        ▼
┌─────┐  ┌─────┐  ┌─────┐
│ App │  │ App │  │ App │ (Instances 1-N)
│ Pod │  │ Pod │  │ Pod │ (Kubernetes/Docker)
└────┬┘  └────┬┘  └────┬┘
     │        │        │
     └────────┼────────┘
              │
              ▼
    ┌─────────────────────────┐
    │ Connection Pool         │
    │ (HikariCP - 10 conns)   │
    └──────────┬──────────────┘
               │
               ▼
    ┌─────────────────────────┐
    │ PostgreSQL Database     │
    │ ├─ Persistence          │
    │ ├─ Replication          │
    │ └─ Backups              │
    └─────────────────────────┘
               │
               ▼
    ┌─────────────────────────┐
    │ Redis Cache (Optional)  │
    │ ├─ Session cache        │
    │ ├─ Preference cache     │
    │ └─ Reduce DB load       │
    └─────────────────────────┘
               │
               ▼
    ┌─────────────────────────┐
    │ Third-party Services    │
    │ ├─ SendGrid (Email)     │
    │ ├─ Twilio (SMS)         │
    │ └─ Firebase (Push)      │
    └─────────────────────────┘
```

---

**This architecture provides:**
- ✅ Clear separation of concerns
- ✅ Easy to test each layer independently
- ✅ Scalable design
- ✅ Database agnostic
- ✅ Easy to extend with new features