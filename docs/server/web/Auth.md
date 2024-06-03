# AuthRouter Documentation

This document provides an overview of the `AuthRouter` class in the project. The class is implemented in Java using the Spring Boot framework.

## AuthRouter.java

This class handles HTTP requests related to authentication and user management. It includes endpoints for:

- User registration (`/api/auth/register`)
- User login (`/api/auth/login`)
- Refreshing access tokens (`/api/auth/refresh`)

## Protocols

The service uses JSON Web Tokens (JWT) for authentication. When a user makes a request, they must include an access token in the Authorization header of the request. The token is used to authenticate the user and authorize the operation.

## Error Handling

The service returns appropriate HTTP status codes and error messages for various error conditions, such as invalid or expired tokens, user not found, and internal server error.

## Detailed Endpoint Descriptions

### POST `/api/auth/register`

This endpoint is used to register a new user. The request body should include `username`, `password`, and `email`. The response will be a `UserResponse` object containing the user's details.

### POST `/api/auth/login`

This endpoint is used to authenticate a user. The request body should include `username` and `password`. The response will be a `LoginResponse` object containing the user's details and access tokens.

### POST `/api/auth/refresh`

This endpoint is used to refresh the access token. The request should include the refresh token in the Authorization header. The response will be a `TokenResponse` object containing the new access token.

robert.kratz May 2. 2024
