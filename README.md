
# Java/SpringBootTest

Introduction

This is a Java Spring Boot application designed to meet the specified requirements. 
It includes API endpoints for user management and product record handling.
The application uses Spring Security and JPA for user authentication and data storage.

## Table of Contents:
Endpoints:


1.1 /user/add (POST)

1.2 /user/authenticate (POST)

1.3 /products/add (POST)

1.4 /products/all (GET)

Environment Configuration

Testing

Database Configuration

        
## Endpoints
        
1.1. /user/add (POST)
Request:

{
  "username": "any username",
  "password": "any password"
}

Response:
HTTP Status: 201 Created

1.2. /user/authenticate (POST)
Request:

{
  "username": "any username",
  "password": "any password"
}

Response:
HTTP Status: 200 OK
JWT Access Token

1.3. /products/add (POST)
Request:

{
  "table": "products",
  "records": [
    {
      "entryDate": "03-01-2023",
      "itemCode": "11111",
      "itemName": "Test Inventory 1",
      "itemQuantity": "20",
      "status": "Paid"
    },
    {
      "entryDate": "03-01-2023",
      "itemCode": "11111",
      "itemName": "Test Inventory 2",
      "itemQuantity": "20",
      "status": "Paid"
    }
  ]
}

Response:
HTTP Status: 201 Created

1.4. /products/all (GET)

Response:
HTTP Status: 200 OK
JSON Array of stored records in the "products" table

## Environment Configuration
Configure the database credentials in the application.properties file.

## Testing
The application is tested using Postman. Execute end-to-end integration tests for all APIs to ensure their proper functionality.

## Database Configuration
The application dynamically creates tables and fields/columns based on the provided JSON payloads. Ensure that the database connection is properly configured in application.properties.
