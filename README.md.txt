# FCM Notification Service

## 🚀 Overview
This is a Spring Boot backend application that sends push notifications using Firebase Cloud Messaging (FCM).

It supports:
- Sending notifications to all users
- Sending notifications to selected users
- Sending notification to a single user
- Managing FCM tokens
- Logging notification delivery status

---

## 🛠 Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- Firebase Admin SDK
- MySQL (or any relational DB)
- Swagger UI (for API testing)

---

## 🔥 Features

### 👤 User Management
- Create user
- Get all users
- Get user by ID
- Store FCM token
- Enable/disable notifications

### 🔔 Notification System
- Send to ALL users (only enabled + valid token)
- Send to SELECTED users
- Send to ONE user
- Handles invalid/expired tokens automatically

### 📊 Logging
- Stores every notification result (success/failure)
- Tracks messageId and error messages

---

## 📡 API Endpoints

### User APIs
- `POST /users` → Create user
- `GET /users` → Get all users
- `GET /users/{id}` → Get user by ID
- `POST /users/{id}/token` → Save FCM token
- `PUT /users/{id}/notification` → Enable/disable notifications

---

### Notification APIs

#### Send to All Users