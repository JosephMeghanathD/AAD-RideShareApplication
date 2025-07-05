# 🚗 AAD RideShare Application 

[![React](https://img.shields.io/badge/React-18.2.0-%2361DAFB?logo=react)](https://reactjs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-%236DB33F?logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16.0-%23316192?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-24.0.7-%232496ED?logo=docker)](https://www.docker.com/)
[![JWT](https://img.shields.io/badge/JWT-Auth-%23000000?logo=jsonwebtokens)](https://jwt.io/)

**Connect. Share. Travel Sustainably.**  
A privacy-first ride-sharing platform for students, professionals, and travelers to split costs, reduce emissions, and build community.  

👉 **Live Demo**: [https://jdride.netlify.app/](https://jdride.netlify.app/)  
🔗 **GitHub Repository**: [https://github.com/JoeHitHard/AAD-RideShareApplication](https://github.com/JoeHitHard/AAD-RideShareApplication)

---

## ✨ Features That Shine

### 🔒 Privacy-First Design
- **Masked Communication**: Chat securely without sharing personal contact details.
- **User Profiles**: Manage your ride history and preferences privately.

### 🚘 Ride Management
- **Post Rides**: Share trip details (source, destination, seats, fare).
- **Search Rides**: Filter by location, date, or availability.
- **Edit/Delete**: Full control over your posted rides.

### 💬 Real-Time Chat
- Coordinate rides seamlessly with built-in messaging.

### 🔐 Secure Authentication
- JWT-based login/signup with encrypted credentials.

### 🌱 Eco-Friendly Impact
- Reduce carbon footprints by sharing rides!

---

## 🛠️ Tech Stack

| **Frontend**       | **Backend**              | **Database**   | **Tools**          |
|---------------------|--------------------------|----------------|--------------------|
| ReactJS             | Spring Boot              | PostgreSQL     | Docker             |
| Material-UI         | Spring Data JPA          |                | Gradle             |
| Axios (HTTP Client) | JWT Authentication       |                | React Toastify     |
| React Router DOM    | RESTful APIs             |                | Postman (Testing)  |

---

## 🚀 Getting Started

### Prerequisites
- Docker 🐳
- Java 21 ☕
- Node.js (for React) 📦

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/JoeHitHard/AAD-RideShareApplication.git
   cd AAD-RideShareApplication

2. **Deploy the Backend with One Command**:
   ```bash
   chmod +x build.sh  # Make the script executable (only once)
   ./build.sh         # Builds services, starts Docker containers
   ```
   This script:
   - Compiles all backend services using Gradle.
   - Tears down old Docker containers.
   - Rebuilds and starts fresh containers for Auth, Ride, and Chat services.

3. **Run the Frontend**:
   ```bash
   cd ride-share-ui
   npm install    # Install dependencies
   npm run start  # Launch React app
   ```

4. **Access the App**:
   - Frontend: `http://localhost:3000`
   - Backend Services: Running on ports `8080-8082`

---

## 📸 Sneak Peek *(Screenshots Coming Soon!)*

| **Homepage** | **Ride Posting** | **Chat Interface** |
|--------------|-------------------|--------------------|
| Browse rides | Share your trip   | Coordinate safely  |

---

## 📚 Resources & Learning
- [React Documentation](https://reactjs.org/)
- [Spring Boot Guides](https://spring.io/projects/spring-boot)
- [JWT Best Practices](https://jwt.io/introduction)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)

---

🌟 **Key Improvements**:
- **One-Click Deployment**: The `build.sh` script automates complex backend setup.
- **Modern Tech Stack**: Combines industry-standard tools like React, Spring Boot, and Docker.
- **Scalable Architecture**: Microservices design ensures easy maintenance and expansion.
- **Focus on Security**: JWT and encrypted communication prioritize user safety.
- **Real-World Impact**: Solves transportation challenges sustainably.

---

**Let’s drive the future of shared mobility together!** 🚀  
*Star the repo if you love it! ⭐*
