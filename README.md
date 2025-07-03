# SAS.DHop.Site.BE

## Project Description
SAS.DHop.Site.BE is a Spring Boot backend application for a dance-related platform that connects dancers, choreographers, and users. The system manages dance types, performances, bookings, and subscriptions.

## Technologies Used

### Core Framework & Languages
- Java 17
- Spring Boot
- Spring Security with JWT Authentication
- Spring Data JPA
- MongoDB (for OTP functionality)

### Database
- MySQL Database (JPA/Hibernate)
- MongoDB (NoSQL database for OTP)

### Security
- JWT (JSON Web Token) for authentication
- BCrypt password encoding
- Role-based authorization (ADMIN, USER, DANCER, CHOREOGRAPHY)

### Documentation
- Swagger/OpenAPI for API documentation

### Build & Deployment
- Maven for dependency management and build
- Docker support

## Project Setup

### Prerequisites
- JDK 17
- Maven
- Docker (optional)
- MongoDB
- SQL Database (MySQL/PostgreSQL)

### Configuration

1. Clone the repository:
```bash
git clone https://github.com/HauHH1910/SAS.DHop.Site.BE.git
cd SAS.DHop.Site.BE
```

2. Configure application properties:
Create `application.properties` or `application.yml` in `src/main/resources` with the following configurations:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/your_mongodb_database

# JWT Configuration
jwt.secret=your_jwt_secret
jwt.expiration=86400000

# Server Configuration
server.port=8080
```

### Running the Application

#### Using Maven
```bash
mvn clean install
mvn spring:run
```

#### Using Docker
```bash
# Build the Docker image
docker build -t sas-dhop-site-be .

# Run the container
docker run -p 8080:8080 sas-dhop-site-be
```

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui/
```

## Key Features

1. User Management
   - User registration and authentication
   - Role-based access control (Admin, User, Dancer, Choreographer)
   - JWT-based authentication

2. Dance Management
   - Dance types management
   - Choreography management
   - Dancer profiles

3. Booking System
   - Performance bookings
   - Scheduling system
   - Status tracking

4. Subscription Management
   - Different subscription plans (Free, Standard, Premium)
   - Subscription status tracking
   - Payment integration

5. Area Management
   - Location-based services
   - Geographic organization

6. Chat/Messaging
   - Real-time communication
   - WebSocket integration

## Security

The application implements several security measures:
- JWT-based authentication
- Protected endpoints with role-based access
- Password encryption using BCrypt
- CORS configuration
- Stateless session management

## Public Endpoints

The following endpoints are publicly accessible:
- `/auth/**` - Authentication endpoints
- `/users` - User registration
- `/dance-type` - Public dance type information
- `/ws/**` - WebSocket endpoints
- `/area/getAllArea/` - Public area information
- `/swagger-ui/**` - API documentation
- `/v3/api-docs*/**` - OpenAPI documentation

## Initial Setup

The application includes an initialization configuration that sets up:
- Default admin user
- Basic dance types
- Subscription plans
- Initial roles and statuses

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
