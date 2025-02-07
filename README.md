# EcomProj

EcomProj is a Spring Boot-based e-commerce application. It provides a RESTful API for managing products, users, and roles, with JWT-based authentication and authorization.

## Features

- User registration and login, secure password handling with BCrypt encryption.
- Spring Security with Role-Based Access Control (RBAC): Manage user roles and permissions.
- JWT Authentication: Secure API endpoints with JWT tokens.
- RESTful endpoints following best practices.
- CSRF Protection: Protect against Cross-Site Request Forgery attacks.
- CORS Configuration: Allow cross-origin requests from specified origins.
- Product management (CRUD operations).
- Image upload for products - Multipart File Upload: Support for uploading product images.
- Integration with PostgreSQL.
- Database Initialization: Automatically initialize the database with default roles and users.
- Exception Handling: Implement global exception handling to provide meaningful and secure error responses, avoiding information leakage.
- API Documentation: Automatically generated API documentation with SpringDoc OpenAPI.

## Getting Started

### Prerequisites

- Java 21.06
- Maven 3.9.9
- PostgreSQL 17.2

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/kerpoz/ecom-proj.git
    cd ecom-proj
    ```

2. Create `env.properties` file next to `application.properties` and set up data to configure PostgreSQL, JWT and active profile:
    ```properties
    DATABASE_URL=jdbc:postgresql://localhost:5432/ecom_proj
    DATABASE_USER=your_username
    DATABASE_PASSWORD=your_password
    JWT_SECRET=your_base64_encoded_jwt_secret_key
    SPRING_PROFILES_ACTIVE=main_or_dev
    ```
   example:
   ```properties
    DATABASE_URL=jdbc:postgresql://localhost:5432/ecom_proj
    DATABASE_USER=username
    DATABASE_PASSWORD=password
    JWT_SECRET=eW91cl9iYXNlNjRfZW5jb2RlZF9qd3Rfc2VjcmV0X2tleQ
    SPRING_PROFILES_ACTIVE=dev
    ```

3. Build the project:
    ```sh
    mvn clean install
    ```

4. Run the application:
    ```sh
    mvn spring-boot:run
    ```

### Running Tests

To run the tests, use the following command:
```sh
mvn test
```

## REST API Endpoints

### Product Endpoints

- **Get All Products**
    - **URL:** `/api/products`
    - **Method:** `GET`
    - **Description:** Retrieves a list of all products.
    - **Response:** `200 OK` with a list of products.

- **Get Product by ID**
    - **URL:** `/api/product/{prodId}`
    - **Method:** `GET`
    - **Description:** Retrieves a product by its ID.
    - **Response:** `200 OK` with the product details.
    - **Error Response:** `404 Not Found` if the product is not found.

- **Add New Product**
    - **URL:** `/api/product`
    - **Method:** `POST`
    - **Description:** Adds a new product.
    - **Request Body:** `multipart/form-data` with product details and an optional image file.
    - **Response:** `201 Created` with the created product details.

- **Update Product**
    - **URL:** `/api/product/{prodId}`
    - **Method:** `PUT`
    - **Description:** Updates an existing product by its ID.
    - **Request Body:** `multipart/form-data` with updated product details and an optional image file.
    - **Response:** `200 OK` with the updated product details.
    - **Error Response:** `404 Not Found` if the product is not found.

- **Delete Product**
    - **URL:** `/api/product/{prodId}`
    - **Method:** `DELETE`
    - **Description:** Deletes a product by its ID.
    - **Response:** `200 OK` with a success message.
    - **Error Response:** `404 Not Found` if the product is not found.

- **Get Product Image**
    - **URL:** `/api/product/{prodId}/image`
    - **Method:** `GET`
    - **Description:** Retrieves the image of a product by its ID.
    - **Response:** `200 OK` with the image data.
    - **Error Response:** `404 Not Found` if the product is not found or `404 Not Found` if the image is not found.

- **Search Products**
    - **URL:** `/api/products/search`
    - **Method:** `GET`
    - **Description:** Searches for products by a keyword.
    - **Query Parameter:** `keyword` - The search keyword.
    - **Response:** `200 OK` with a list of matching products.

### User Endpoints

- **Register User**
    - **URL:** `/api/user/register`
    - **Method:** `POST`
    - **Description:** Registers a new user.
    - **Request Body:** JSON object with user details.
    - **Response:** `201 Created`

- **Get User by ID**
    - **URL:** `/api/user/{userId}`
    - **Method:** `GET`
    - **Description:** Retrieves a user by their ID.
    - **Response:** `200 OK` with the user details.
    - **Error Response:** `404 Not Found` if the user is not found.

- **Login User**
    - **URL:** `/api/user/login`
    - **Method:** `POST`
    - **Description:** Authenticates a user using their username or email and password.
    - **Request Body:** JSON object with `username` or `email` and `password`.
    - **Response:** `200 OK` with a JWT token.

### CSRF Token Endpoint

- **Get CSRF Token**
    - **URL:** `/api/csrf-token`
    - **Method:** `GET`
    - **Description:** Retrieves the CSRF token.
    - **Response:** `200 OK` with the CSRF token.

## API Documentation

- API documentation is available at /swagger-ui.html once the application is running
- OpenAPI JSON at /api-docs

## Components and Classes

### `config` Package

- **DataInitializer.java**: Initializes default roles and users in the database.
- **security**:
    - **JwtFilter.java**: Filters incoming requests to validate JWT tokens.
    - **SecurityConfig.java**: Configures Spring Security, including JWT authentication.
    - **UserPrincipal.java**: Implements `UserDetails` for Spring Security.
- **web**:
    - **CorsConfig.java**: Configures CORS settings for the application.

### `controller` Package

- **CsrfController.java**: Manages CSRF tokens.
- **ProductController.java**: Handles CRUD operations for products.
- **UserEntityController.java**: Manages user-related operations.

### `exception` Package

- **GlobalExceptionHandler.java**: Handles global exceptions and provides custom error responses.

### `model` Package

- **constants**:
    - **RoleConstants.java**: Defines constants for roles.
- **entity**:
    - **Product.java**: Represents a product entity.
    - **Role.java**: Represents a role entity.
    - **UserEntity.java**: Represents a user entity.
- **enums**:
    - **RoleType.java**: Enum for role types (e.g., ROLE_USER, ROLE_ADMIN).

### `repository` Package

- **ProductRepository.java**: Repository interface for `Product` entity.
- **RoleRepository.java**: Repository interface for `Role` entity.
- **UserEntityRepository.java**: Repository interface for `UserEntity` entity.

### `service` Package

- **CustomUserDetailsService.java**: Loads user-specific data for authentication.
- **JwtService.java**: Manages JWT token creation and validation.
- **ProductService.java**: Provides business logic for product management.
- **UserEntityService.java**: Provides business logic for user management.

### `EcomProjApplication.java`

- Main class to bootstrap the Spring Boot application.

## Technologies Used

- **Java 21**: Primary programming language.
- **Spring Boot**: Framework for building the application.
- **Spring Security**: Secures the application with JWT and role-based access control.
- **Spring Data JPA**: Interacts with the database using JPA.
- **PostgreSQL**: Relational database management system.
- **Maven**: Manages project dependencies and builds the application.
- **JUnit 5**: Framework for unit and integration tests.
- **Mockito**: Framework for mocking in unit tests.
- **MockMvc**: Facilitates testing of Spring MVC controllers by simulating HTTP requests and responses.
- **SpringDoc OpenAPI**: Generates API documentation.
- **Lombok**: Reduces boilerplate code.
- **SLF4J**: Logging facade for Java.
- **Jakarta Validation**: Validates user input and entity fields.

## Additional features
###  Enhanced Search Functionality

- The `ProductRepository` interface includes a custom query method `searchProducts` that allows searching for products based on a keyword. This method uses a **JPQL** query to search for products where the keyword matches any part of the product's name, brand, category, or description, ignoring case. This feature enhances the search functionality of the e-commerce application, making it easier for users to find products based on partial matches of various product attributes.

- The CustomUserDetailsService class provides an additional feature that allows users to log in using either their username or email.

## License

- This project is licensed under the MIT License.


## Author
**Konrad Cieślak**

[![GitHub](https://img.shields.io/badge/GitHub-%23181717.svg?style=for-the-badge&logo=github&logoColor=white)](https://github.com/kerpoz) &nbsp;
[![LinkedIn](https://img.shields.io/badge/LinkedIn-%230A66C2.svg?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/konrad-cieślak-283335193) &nbsp;
[![Google Play Developer](https://img.shields.io/badge/Google%20Play-%23234F34.svg?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/developer?id=kerpoz) &nbsp;
[![Email](https://img.shields.io/badge/Email-%23D14836.svg?style=for-the-badge&logo=gmail&logoColor=white)](mailto:konrad.cieslak.kerpoz@gmail.com)   
