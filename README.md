# Organ Donation Registry & Smart Matching System

A production-level full-stack web application for organ donation management with intelligent matching, real-time updates, and analytics.

## Tech Stack

- **Backend**: Spring Boot 3.2.5
- **ORM**: Hibernate / Spring Data JPA
- **Frontend**: JSP + Bootstrap 5
- **Security**: Spring Security + JWT
- **Database**: MySQL 8.0
- **Real-time**: WebSocket

## Features

### Core Modules

1. **User Management**
   - Roles: Donor, Recipient, Admin, Hospital
   - Secure registration/login with JWT
   - Profile management

2. **Donor Registry**
   - Blood group, organs to donate
   - Location (city, state)
   - Medical history
   - Living vs deceased donor option
   - Emergency availability toggle

3. **Recipient Registry**
   - Required organ, urgency level
   - Medical compatibility details
   - Waiting list with priority scoring

4. **Smart Matching System**
   - AI-based match scoring algorithm
   - Blood group compatibility
   - Organ type matching
   - Location proximity
   - Urgency priority
   - Quick match for emergencies

5. **Admin Dashboard**
   - View all donors/recipients
   - Approve/reject registrations
   - Monitor matches
   - Analytics with charts

6. **Hospital Module**
   - Verify transplant cases
   - Update transplant status

7. **Notification System**
   - Real-time notifications
   - Match alerts

8. **Analytics & Visualization**
   - Organ demand charts
   - Urgency distribution
   - Successful transplants tracking

## Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE organ_donation_db;
```

2. Update `application.properties` with your MySQL credentials.

## Running the Application

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Steps

```bash
# Navigate to project directory
cd organ-donation-system

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start at: **http://localhost:8080**

## Default Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@organdonor.com | admin123 |
| Hospital | hospital@cityhospital.com | hospital123 |
| Donor | donor@example.com | donor123 |
| Recipient | recipient@example.com | recipient123 |

## Project Structure

```
organ-donation-system/
├── src/main/java/com/organdonation/
│   ├── OrganDonationApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── DataInitializer.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── AdminController.java
│   │   ├── DonorController.java
│   │   ├── RecipientController.java
│   │   └── HospitalController.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Donor.java
│   │   ├── Recipient.java
│   │   ├── Hospital.java
│   │   ├── OrganMatch.java
│   │   └── Notification.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── DonorRepository.java
│   │   ├── RecipientRepository.java
│   │   ├── HospitalRepository.java
│   │   ├── MatchRepository.java
│   │   └── NotificationRepository.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── MatchingService.java
│   │   └── NotificationService.java
│   ├── security/
│   │   ├── CustomUserDetailsService.java
│   │   └── JwtAuthenticationFilter.java
│   └── util/
│       └── JwtUtil.java
├── src/main/resources/
│   └── application.properties
├── src/main/webapp/WEB-INF/jsp/
│   ├── auth/
│   │   ├── login.jsp
│   │   └── register.jsp
│   ├── admin/
│   │   └── dashboard.jsp
│   ├── donor/
│   │   └── dashboard.jsp
│   └── recipient/
│       └── dashboard.jsp
└── pom.xml
```

## API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /login` - Login user
- `GET /register` - Registration page
- `POST /register` - Register new user
- `GET /logout` - Logout user

### Admin
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/donors` - List all donors
- `GET /admin/recipients` - List all recipients
- `GET /admin/hospitals` - List all hospitals
- `GET /admin/matches` - List all matches
- `POST /admin/verify-user/{id}` - Verify user

### Donor
- `GET /donor/dashboard` - Donor dashboard
- `GET /donor/register` - Donor registration form
- `POST /donor/register` - Register donor details
- `GET /donor/profile` - Donor profile
- `POST /donor/toggle-emergency` - Toggle emergency availability

### Recipient
- `GET /recipient/dashboard` - Recipient dashboard
- `GET /recipient/register` - Recipient registration form
- `POST /recipient/register` - Register recipient details
- `GET /recipient/find-match` - Find matching donors
- `POST /recipient/quick-match` - Emergency quick match
- `POST /recipient/create-match/{donorId}` - Create match

### Hospital
- `GET /hospital/dashboard` - Hospital dashboard
- `GET /hospital/matches` - List hospital matches
- `POST /hospital/approve-match/{id}` - Approve match
- `POST /hospital/complete-match/{id}` - Complete transplant

## Smart Matching Algorithm

The matching algorithm scores potential matches (0-100) based on:

1. **Blood Group Compatibility (30 points)**
   - Exact match: +5 bonus points

2. **Organ Match (25 points)**

3. **Urgency Priority (20 points)**
   - Critical: 20 points
   - High: 15 points
   - Medium: 10 points
   - Low: 5 points

4. **Location Proximity (15 points)**
   - < 50 km: 15 points
   - < 100 km: 12 points
   - < 200 km: 8 points
   - < 500 km: 5 points

5. **Waiting Time Bonus (10 points)**
   - > 365 days: 10 points
   - > 180 days: 7 points
   - > 90 days: 5 points
   - > 30 days: 3 points

## License

MIT License
