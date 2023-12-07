# Marketplace Spring Boot Project

Explore the Marketplace Spring Boot Project, an illustrative project of a sophisticated marketplace application crafted with the power of Spring Boot. Immerse yourself in the intricacies of modern web development as we showcase comprehensive features, seamless CRUD operations, and the elegance of HATEOAS integration. Whether you're a developer seeking inspiration or an enthusiast eager to understand the dynamic


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
    


### Usage
Once the application is running, you can access the API en
dpoints to interact with the marketplace.

• API Endpoints
• GET /products: Get a list of all products.
• GET /product/{id}: Get details of a specific product by ID.
• POST /products: Add a new product.
• PUT /products/{id}: Update an existing product by ID.
• DELETE /product/{id}: Delete a product by ID.

### Contributing
If you'd like to contribute to the project, please follow these steps:

1. Fork the repository.
2. Create a new branch: **git checkout -b feature/your-feature**.
3. Commit your changes: **git commit -m 'Add some feature**.
4. Push to the branch: **git push origin feature/your-feature**.
5. Submit a pull request.

### License
This project is licensed under the MIT License - see the LICENSE file for details.