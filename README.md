# Visa Application Site

A Java Spring Boot application for managing visa applications, with separate modules for users and administrators.

---

## Technologies Used

- Java 21+
- Spring Boot
- Maven
- Spring Security
- RESTful APIs

---

## Setup Instructions

1. Clone the repository.
2. Run `mvn clean install` to build the project.
3. Start the application with `mvn spring-boot:run`.
4. The backend runs on `http://localhost:8080`.

---

## API Endpoints

### User APIs

#### Authentication
- `POST /api/auth/user/register`  
  Register a new user.  
  **Body:** `{ "email": "...", "password": "...", ... }`

- `POST /api/auth/user/verify`  
  Verify user email/code.  
  **Body:** `{ "email": "...", "code": "..." }`

#### Dashboard
- `GET /api/user/home`  
  Get user dashboard info (requires authentication).

#### Visa Applications
- `POST /api/user/applications/{userId}`  
  Create a new visa application for a user.  
  **Body:** `{ ...application data... }`

- `GET /api/user/applications`  
  Get all applications for the authenticated user.

- `GET /api/user/applications/{id}`  
  Get a specific application by ID.

#### Documents
- `POST /api/user/documents/upload/{applicationId}`  
  Upload a document for a visa application.  
  **Form Data:** `file`

- `GET /api/user/documents/download/{documentId}`  
  Download a document by ID.

---

### Admin APIs

#### Authentication
- `POST /api/auth/admin/login`  
  Admin login.  
  **Body:** `{ "username": "...", "password": "..." }`

#### Dashboard
- `GET /api/admin/home`  
  Get admin dashboard statistics.

#### Applications
- `GET /api/admin/applications`  
  Get all visa applications (optionally filter by status).

- `PUT /api/admin/applications/{id}/status`  
  Update the status of a visa application.  
  **Body:** `{ "status": "APPROVED" | "REJECTED" | ... }`

#### Notifications
- `POST /api/admin/notification/email/{userId}`  
  Send an email notification to a user.  
  **Body:** `{ "subject": "...", "message": "..." }`

---

## Error Handling

- Standard HTTP status codes are used.
- Error responses include a message describing the issue.

---

## License

MIT License

