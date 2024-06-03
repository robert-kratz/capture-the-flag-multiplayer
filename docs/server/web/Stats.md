# StatsRouter Documentation

This document provides an overview of the `StatsRouter` class in the project. The class is implemented in Java using the Spring Boot framework.

## StatsRouter.java

This class handles HTTP requests related to statistics and leaderboard management. It includes endpoints for:

- Getting the leaderboard (`/api/stats/leaderboard`)
- Getting the stats of a user (`/api/stats/stats/{userId}`)

## Protocols

The service uses JSON Web Tokens (JWT) for authentication. When a user makes a request, they must include an access token in the Authorization header of the request. The token is used to authenticate the user and authorize the operation.

## Error Handling

The service returns appropriate HTTP status codes and error messages for various error conditions, such as invalid or expired tokens, user not found, and internal server error.

## Detailed Endpoint Descriptions

### GET `/api/stats/leaderboard`

This endpoint is used to get the leaderboard. It returns the top 100 players by elo. The response will be an array of `LeaderboardResponse` objects.

### GET `/api/stats/stats/{userId}`

This endpoint is used to get the stats of a user. The `{userId}` in the URL should be replaced with the actual user ID. The response will be a `StatsResponse` object. If the user is not found, it returns a 404 status code.

robert.kratz May 2. 2024
