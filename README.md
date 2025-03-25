# restaurant

# Restaurant Review API

## Overview
The **Restaurant Review API** is a Spring Boot-based application that allows users to manage restaurants and customer reviews. It includes features such as restaurant listing, review submission, and basic analytics.

## Features
- **Restaurant Management**: Create and retrieve restaurant details.
- **Review Management**: Submit and fetch restaurant reviews.
- **Analytics**:
    - Calculate the average rating per restaurant.
    - Retrieve the top 3 restaurants by cuisine type.
- **Security**: Basic authentication with User and Admin roles.
- **Pagination & Validation**: Supports paginated endpoints and request validation.

## **Tech Stack**
- **Spring Boot 3.2.2, **Java 17**
- **Spring Data JPA**, **H2 Database**
- **Spring Security** (Basic Authentication)
- **Swagger** (API Documentation)
- **JUnit (Testing)**

## **Setup & Run**
### **Clone the Repository**
```sh
git clone <your-repo-url>
cd restaurant-review-api
```
### **Build the Application**

```sh
 mvn clean install
```

### **Run the Application**
Using Maven:
```sh
mvn spring-boot:run
```

### **Access API Documentation**
After running the application, visit:
```
http://localhost:8080/swagger-ui/index.html
```

## **API Endpoints**
### **User Authentication**
- `POST /api/auth/register`- User register 
- `POST /api/auth/login`- Login (Role base)

### **Restaurant Endpoints**
- `POST /api/restaurants` - Add a new restaurant.
- `GET /api/restaurants` - List all restaurants (supports pagination).
- `GET /api/restaurants/top3`- List top3 restaurants
### **Review Endpoints**
- `POST /api/reviews` - Submit a review for a restaurant.
- `GET /api/reviews/average/{restaurantId}` - Get reviews for a specific restaurant.

## **Assumptions**
- The application uses an in-memory **H2 database** for simplicity.
- Authentication is implemented using **Basic Auth**.
- API security is enforced on sensitive endpoints.

## **Contributing**
Feel free to raise issues or contribute by submitting a pull request.