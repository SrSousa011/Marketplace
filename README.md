# Marketplace Spring Boot Project

The Marketplace API is a RESTful web service designed to manage products in a marketplace. It provides endpoints for creating, updating, retrieving, and deleting products. Additionally, it includes robust exception handling for common scenarios such as not found entities and deleted users.


## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Introduction

The Marketplace Spring Boot Project is designed to showcase the implementation of a basic marketplace application using Spring Boot and HATEOAS. It includes CRUD operations for managing products.

## Features

- List all products
- Get details of a specific product
- Add a new product
- Update an existing product
- Delete a product

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed on your machine:

- Java Development Kit (JDK) 17
- Database Postgres
- Maven
- Your preferred IDE (e.g., IntelliJ, Eclipse)

### Installation

1. Clone the repository:

   ```bash
    git clone https://github.com/SrSousa011/marketplace-api.git
2. Open the project in your IDE.
3. Build the project:

   ```bash
   mvn clean install   
   

4. Run the application:   

   ```bash
   java -jar target/marketplace-api-1.0.jar



### Swagger Documentation
Explore the API using Swagger documentation. Visit 

    
    http://localhost:8080/swagger-ui/index.html
    


### API Endpoints

**Update Product** <br/>
• Endpoint: _PUT /products/{id}_ <br/>
• Description: Update details of a product with the specified ID. <br/>
• Request Body:

        {
        "name": "New Product Name",
        "price": 19.99
        }

**GET All Products**<br/>
• Endpoint: _GET /products_<br/>
• Description: Retrieve details of a specific product by its ID.<br/>


**Get One Product**<br/>
• Endpoint: _GET /product/{id}_<br/>
• Description: Retrieve details of a specific product by its ID.<br/>


**Save Product**<br/>
• Endpoint: _POST /product/{id}_<br/>
• Description: Update details of a product with the specified ID.**<br/>

**DELETE Product** <br/>
• Endpoint: _DELETE /product/{id}_<br/>
• Description: Delete a product with the specified ID.<br/>


### Exception Handling
The Marketplace API includes robust exception handling for various scenarios. Notable exceptions include:<br/>

• NotFoundException: Thrown when an entity is not found.<br/>
• DeletedUserException: Thrown when attempting to delete a non-existent user.<br/>
Customized error responses are provided for each exception type.<br/>

### Built With
• [Spring Boot](https://spring.io/projects/spring-boot) - Framework for building Java-based enterprise applications.<br/>
• [Swagger](https://swagger.io) - API documentation tool.<br/>
• [HATEOAS](https://spring.io/projects/spring-hateoas) - Hypermedia as the engine of application state.<br/>

### Contributing
If you'd like to contribute to the project, please follow these steps: <br/>

1. Fork the repository.
2. Create a new branch: **git checkout -b feature/your-feature**. <br/>
3. Commit your changes: **git commit -m 'Add some feature**. <br/>
4. Push to the branch: **git push origin feature/your-feature**. <br/>
5. Submit a pull request. <br/><br/><br/>

### License
This project is licensed under the MIT License - see the LICENSE file for details.