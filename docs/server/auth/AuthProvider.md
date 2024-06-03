## AuthProvider Class Documentation

The `AuthProvider` class is a core component of the server-side authentication system, designed to handle JWT-based authentication. This class provides methods for generating, validating, and managing access and refresh tokens, crucial for securing REST API endpoints.

### Overview

The `AuthProvider` utilizes JWTs to facilitate secure communication and ensure that only authorized users can access sensitive operations within the application. It deals with token creation, validation, user authentication, and token expiration checks.

### Key Methods

#### Token Management

- **generateTokens(String userid)**: Creates both an access token and a refresh token for a specified user. The access token typically expires in 24 hours, while the refresh token lasts for 7 days. This method is critical for session management and re-authentication processes.

- **refreshAccessToken(String refreshToken)**: Validates the refresh token and generates a new access token if the refresh token is still valid. This mechanism supports seamless user experiences by extending session validity without user intervention.

#### Token Validation and Extraction

- **validateTokenAndGetUserId(String token)**: Extracts and returns the user ID from a given JWT if it is valid, facilitating user identification in subsequent operations.

- **validateTokenAndGetUser(String token)**: Further extends the functionality of token validation by retrieving the associated `User` object from the database, ensuring the user exists and the token is linked to a valid user.

- **isTokenExpired(String token)**: Checks whether a given token has expired, a fundamental check to prevent expired tokens from being used for authentication.

- **isJwtToken(String token)**: Determines if a given string is a valid JWT by verifying its structure and signature, which helps to ensure that only tokens generated by the server are processed.

#### Utility Methods

- **extractToken(String header)**: Extracts the JWT from the authorization header of HTTP requests, which is essential for processing secured endpoints.

- **isHeaderValid(String header)**: Ensures that the authorization header of incoming requests is correctly formatted and contains the token.

- **getTokenType(String token)**: Identifies whether a token is an access or a refresh token, aiding in processes that require differentiation, such as token refreshing.

- **getExpirationDate(String token)**: Retrieves the expiration date of a token, useful for logging, debugging, or displaying token expiration information to the user.

#### Security and Password Management

- **isPasswordInsecure(String password)**: Checks if a given password meets the defined security criteria, including length and character diversity, to prevent the use of weak passwords.

### Usage Scenario

In a typical use case within a SpringBoot application, the `AuthProvider` is instantiated and utilized in authentication endpoints to handle login requests, secure API access, and manage user sessions through tokens. For instance, when a user logs in, the system generates tokens via `generateTokens` and sends these to the client. Subsequent requests from the client include these tokens in the authorization header, which are then validated using methods like `validateTokenAndGetUser` to ensure ongoing session security.

### Security Considerations

The `AuthProvider` class relies on a strong, unique signing key for JWT operations, which should be securely managed and kept confidential to prevent unauthorized token creation. Additionally, handling tokens and user authentication processes securely is critical to prevent security breaches such as token theft or replay attacks.

robert.kratz May 2, 2024