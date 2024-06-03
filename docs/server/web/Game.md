# Game Session Controller Documentation

This document provides an overview of the `GameSessionController` class in the project. The class is implemented in Java using the Spring Boot framework.

## GameSessionController.java

This class handles HTTP requests related to game session management. It includes endpoints for:

- Creating a new game session (`/api/gamesession`)
- Retrieving a game session and its status (`/api/gamesession/{sessionId}`)
- A new team joining the game session (`/api/gamesession/{sessionId}/join`)
- Retrieving the current game state for a specific game session (`/api/gamesession/{sessionId}/state`)
- Making a move request for a specific game session (`/api/gamesession/{sessionId}/move`)
- Making a request to give up the game for a specific game session (`/api/gamesession/{sessionId}/giveup`)
- Deleting a specific game session (`/api/gamesession/{sessionId}`)
- Getting the join code for a game session (`/api/gamesession/{gameCode}/joincode`)
- Setting the board theme for a game session (`/api/gamesession/{sessionId}/theme`)
- Getting the current board theme for a game session (`/api/gamesession/{sessionId}/theme`)
- Getting the join code for a game session (`/api/gamesession/{sessionId}/joincode`)
- Getting the current game state by join code (`/api/gamesession/{code}/state/joincode`)

## Protocols

The service uses JSON Web Tokens (JWT) for authentication. When a user makes a request, they must include an access token in the Authorization header of the request. The token is used to authenticate the user and authorize the operation.

## Error Handling

The service returns appropriate HTTP status codes and error messages for various error conditions, such as invalid or expired tokens, game session not found, no more team slots available, move is forbidden for given team (anti-cheat), invalid move, game is over, and unknown error occurred.

## Detailed Endpoint Descriptions

### POST `/api/gamesession`

This endpoint is used to create a new game session. The request body should contain a `GameSessionRequest` payload that specifies the number of players and the grid size. The response will be a unique session ID and an initial game state.

### GET `/api/gamesession/{sessionId}`

This endpoint is used to retrieve the current session for a specific game session. The `{sessionId}` in the URL should be replaced with the actual session ID. The response will be a `GameSessionResponse` object.

### POST `/api/gamesession/{sessionId}/join`

This endpoint is used for a new team to join a game session. The request body should contain a `JoinGameRequest` payload that specifies the team to join (i.e., team id). The `{sessionId}` in the URL should be replaced with the actual session ID.

### GET `/api/gamesession/{sessionId}/state`

This endpoint is used to retrieve the current game state for a specific game session. The `{sessionId}` in the URL should be replaced with the actual session ID. The response will be a `GameState` object.

### POST `/api/gamesession/{sessionId}/move`

This endpoint is used to make a move request for a specific game session. The request body should contain a `MoveRequest` payload that specifies the piece ID and the new position. The `{sessionId}` in the URL should be replaced with the actual session ID.

### POST `/api/gamesession/{sessionId}/giveup`

This endpoint is used for a team to give up the game for a specific game session. The `{sessionId}` in the URL should be replaced with the actual session ID.

### DELETE `/api/gamesession/{sessionId}`

This endpoint is used to delete a specific game session. The `{sessionId}` in the URL should be replaced with the actual session ID.

### POST `/api/gamesession/{gameCode}/joincode`

This endpoint is used to get the join code for a game session. The `{gameCode}` in the URL should be replaced with the actual game code. The response will be a `JoinGameResponse` object.

### POST `/api/gamesession/{sessionId}/theme`

This endpoint is used to set the board theme for a game session. The `{sessionId}` in the URL should be replaced with the actual session ID. The request body should contain a `SetBoardThemeRequest` payload that specifies the board theme.

### GET `/api/gamesession/{sessionId}/theme`

This endpoint is used to get the current board theme for a game session. The `{sessionId}` in the URL should be replaced with the actual session ID. The response will be a `GetBoardThemeResponse` object.

### GET `/api/gamesession/{sessionId}/joincode`

This endpoint is used to get the join code for a game session. The `{sessionId}` in the URL should be replaced with the actual session ID.

### GET `/api/gamesession/{code}/state/joincode`

This endpoint is used to get the current game state by join code. The `{code}` in the URL should be replaced with the actual join code. The response will be a `GameSessionResponse` object.

robert.kratz May 2. 2024
