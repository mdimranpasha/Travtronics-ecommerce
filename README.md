Project Setup and Running Guide
Prerequisites
Before you begin, ensure you have the following software installed on your system:

Java 17 or higher
Maven (for building the project)
Spring Boot (part of the project dependencies, handled by Maven)
Postman (or any API testing tool for testing endpoints)
Database: This project uses an H2 database (or MySQL if you switch in the configuration).
IDE: Use any Java-supported IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
Project Structure
The project consists of a backend built with:

Spring Boot: For building RESTful APIs.
JPA/Hibernate: For ORM and database interaction.
H2/MySQL Database: Configurable database.
Setting Up the Project
Clone the Repository

Clone the project repository from GitHub:
git clone https://github.com/your-repo/your-project.git
cd your-project

Configure the Database

By default, the project uses the H2 database. If you want to switch to MySQL, update the configuration in the application.properties or application.yml file under src/main/resources/.
Example for H2 (already configured):
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

Test the API Endpoints

Use Postman or another API client to test the RESTful APIs. Here are some examples:

| **Method** | **Route**                                 | **Description**                              | **Role**       |
|------------|-------------------------------------------|----------------------------------------------|----------------|
| POST       | `/api/users/save`                         | Register new user/admin                      | Public         |
| POST       | `/api/users/login`                        | Login to generate JWT                        | Public         |
| POST       | `/api/admin/add-product`                  | Add a new product                            | Admin Only     |
| GET        | `/api/admin/products`                     | Get all products                             | Admin & User   |
| POST       | `/api/users/add-to-cart`                  | Add a product to the user's cart             | User Only      |
| GET        | `/api/users/cart/{userId}`                | View user's cart with product details        | User Only      |
| DELETE     | `/api/users/{userId}/{productId}`         | Remove a product from the user's cart        | User Only      |
| DELETE     | `/api/admin/delete-product/{productId}`   | Delete a product (Admin only)                | Admin Only     |
|POST        |`/api/users/place-order/{userId}`            |Place Order                                 | User Only      |


admin register data

{
    "userName": "admin_user",
    "email": "admin@example.com",
    "password": "adminPass123",
    "role": "ADMIN"
}

admin login data 

{
    "userName": "admin_user",
    
    "password": "adminPass123"
    
}

user register data 

{
    "userName": "john_doe",
    "email":"john@gmail.com",
    "password": "password123",
    "role":"USER"
}

user login data

{
    "userName": "john_doe",
    "password": "password123"
}




