# food-delivery-springboot
java assessment app - food-delivery-springboot

# Food Delivery Backend (Spring Boot)

This is a Spring Boot backend service for managing a food delivery platform. It handles restaurant onboarding, menu management, order processing, delayed order detection, and notification delivery.

---

## Features

-  Admin & Restaurant Management
-  Menu and Dish CRUD
-  Order Lifecycle (Placed → Cooking → Delivery)
-  Scheduled Jobs for Delayed Orders
-  Notification System (for delivery delays)
-  Global API Response Wrapper (with Generic DTOs)
-  Role-Based Authentication (Spring Security + JWT)
-  Lazy Loading Handling with `@Transactional`

---

## Tech Stack

- Java 17
- Spring Boot 3+
- Spring Data JPA (Hibernate)
- Spring Security (JWT-based)
- MySQL (or any JPA-compatible RDBMS)
- Lombok

---

## Project Structure
src/ main/ java/ com.assessment.food_delivery/
├── controller/ → REST controllers
├── entity/ → JPA Entities
├── dto/ → Request/Response DTOs
├── repository/ → Spring Data JPA Repositories
├── service/ → Business Logic
├── enums/ → Enums constants
├── config/ → Security
├── utils/ → Jwt Utils and Exception Handler
└── scheduler/ → Scheduled tasks (delayed order check)

## Api Response Message
{
"statusCode": 200,
"status": "SUCCESS",
"message": "success message.",
"data": {}
}


# Clone the repo
git clone https://github.com/bhavikpandit2001/food-delivery-springboot.git
cd food-delivery-backend

# Configure DB credentials in src/main/resources/application.yml

# Build the project
mvn clean install

# Run the app
mvn spring-boot:run
