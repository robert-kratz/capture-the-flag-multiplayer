# MapRouter Documentation

This document provides an overview of the `MapRouter` class in the project. The class is implemented in Java using the Spring Boot framework.

## MapRouter.java

This class handles HTTP requests related to map management. It includes endpoints for:

- Getting all maps (`/api/map/maps`)
- Getting all public maps (`/api/map/public/maps`)
- Creating a map (`/api/map/create`)
- Updating a map (`/api/map/update/{id}`)
- Deleting a map (`/api/map/delete/{id}`)
- Searching a map by id (`/api/map/search/{id}`)

## Protocols

The service uses JSON Web Tokens (JWT) for authentication. When a user makes a request, they must include an access token in the Authorization header of the request. The token is used to authenticate the user and authorize the operation.

## Error Handling

The service returns appropriate HTTP status codes and error messages for various error conditions, such as invalid or expired tokens, map not found, and internal server error.

## Detailed Endpoint Descriptions

### GET `/api/map/maps`

This endpoint is used to get all maps. The response will be an array of `Map` objects.

### GET `/api/map/public/maps`

This endpoint is used to get all public maps. The response will be an array of `Map` objects.

### POST `/api/map/create`

This endpoint is used to create a new map. The response will be a `Map` object containing the details of the created map.

### PUT `/api/map/update/{id}`

This endpoint is used to update a map. The `{id}` in the URL should be replaced with the actual map ID. The request body should include the updated map details. The response will be a `Map` object containing the updated map details.

### DELETE `/api/map/delete/{id}`

This endpoint is used to delete a map. The `{id}` in the URL should be replaced with the actual map ID. The response will be a boolean indicating the success of the operation.

### GET `/api/map/search/{id}`

This endpoint is used to get a map by its id. The `{id}` in the URL should be replaced with the actual map ID. The response will be a `Map` object. If the map is not found, it returns a 404 status code.

robert.kratz May 2. 2024
