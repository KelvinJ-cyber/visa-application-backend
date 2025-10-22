# Visa Application Site

Short project overview

- Spring Boot (Java 21) backend providing user/admin authentication, visa application submission, document uploads, and admin dashboards.
- Uses JWT for stateless authentication, PostgreSQL for persistence, and SMTP for email (verification / OTP).

Quick start (local)

1. Build:
   mvn -DskipTests package
2. Run (example):
   java -jar target/*.jar --server.port=8080
3. Open Postman or curl to test endpoints (see API section).

Deploying to Render (no Docker)

1. Ensure repository contains `render.yaml` (included). Render will read this and create a Java Web Service.
2. Push branch `main` to your remote repo.
3. In Render UI: New -> Web Service -> connect repo and choose branch. Confirm build/start commands:
   - Build: `mvn -DskipTests package`
   - Start: `java -jar target/*.jar --server.port=$PORT`
4. Add environment variables in Render (or update `render.yaml`): see Required environment variables below.
5. Add a managed Postgres on Render or an external DB and set SPRING_DATASOURCE_URL accordingly.
6. Check logs and verify endpoints after deploy.

Required environment variables

- SPRING_DATASOURCE_URL (e.g. jdbc:postgresql://<host>:5432/<db>)
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- SECURITY_JWT_SECRET_KEY (base64 string used by JWT signing)
- SECURITY_JWT_EXPIRATION_TIME (milliseconds, default 86400000)
- SPRING_MAIL_HOST (e.g. smtp.gmail.com)
- SPRING_MAIL_PORT (e.g. 587)
- SPRING_MAIL_USERNAME
- SPRING_MAIL_PASSWORD
- SPRING_PROFILES_ACTIVE (optional)

Notes about JWT secret

The project expects a Base64-encoded secret (the JwtServices decodes the key using Base64). Generate a secure random 256+ bit secret and set it in SECURITY_JWT_SECRET_KEY.

API Endpoints (summary)

Authentication - User (public)
- POST /api/auth/user/register
  - Body: { "email": "...", "password": "...", "firstName": "...", ... }
  - Response: 201 Created -> created user
- POST /api/auth/user/verify
  - Body: { "email": "...", "code": "..." }
  - Response: 200 OK on success
- POST /api/auth/user/resend-verification
  - Body: { "email": "..." }
- POST /api/auth/user/login
  - Body: { "email": "...", "password": "..." }
  - Response: 200 -> { "token": "<jwt>", ... }
- POST /api/auth/user/request-reset-otp
  - Body: { "email": "..." }
- POST /api/auth/user/reset-password
  - Body: { "email": "...", "otp": "...", "newPassword": "..." }

Authentication - Admin (public)
- POST /api/auth/admin/login
  - Body: { "email": "...", "password": "..." }
  - Response: 200 -> { "token": "<jwt>" }

User-protected endpoints (require Authorization: Bearer <token> with ROLE_USER)
- POST /api/user/applications
  - Create a visa application. Body: application DTO.
  - Response: 201 -> created application
- GET /api/user/applications
  - List user's applications. Response: 200 -> [ ... ]
- GET /api/user/applications/{id}
  - Get application by id (must belong to current user)
- POST /api/user/applications/{id}/submit
  - Submit application for processing
- Document uploads and other user endpoints exist under /api/user/**

Admin-protected endpoints (require Authorization: Bearer <token> with ROLE_ADMIN)
- GET /api/admin/applications
  - List all applications or filter by status
- GET /api/admin/applications/{id}
  - View application details
- POST /api/admin/applications/{id}/status
  - Update application status (approve/reject)
- POST /api/admin/notifications
  - Send notifications to users

Authentication notes

- All protected routes require Authorization header with a valid JWT: `Authorization: Bearer <token>`.
- JWT is validated by JwtAuthFilter and JwtServices (signature + expiration + subject match).
- Ensure the frontend sends Authorization header and CORS allows it (CORS config in SecurityConfig permits Authorization header for configured origins).

Example requests

1) Login (user)

curl -X POST https://<your-host>/api/auth/user/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"secret"}'

Response (example):
{ "token": "eyJ...", "userId": 123, "role": "ROLE_USER" }

2) Call protected endpoint

curl -X GET https://<your-host>/api/user/applications \
  -H "Authorization: Bearer eyJ..."

If you get 401/403
- Confirm the token returned by login is being sent in Authorization header exactly as `Bearer <token>`.
- Ensure FRONTEND origin in CORS config matches your frontend or add it temporarily.
- Check backend logs for `JWT exception` messages from JwtServices.

Testing with Postman

- Create a request to the login endpoint; store the returned token in an environment variable (e.g., `{{jwt}}`).
- For protected requests add header `Authorization: Bearer {{jwt}}`.
