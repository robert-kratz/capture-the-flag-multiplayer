## MapHandler Class Documentation

The `MapHandler` class in the `de.cfp1.client.net` package is designed to manage all operations related to map entities within a client-server architecture. This class encapsulates methods to handle map-related requests such as creating, fetching, updating, and deleting maps. It utilizes a `NetworkHandler` to communicate with the server, ensuring that map data is correctly synchronized between the client and server endpoints.

### Constructor

- **MapHandler(NetworkHandler networkHandler)**: Initializes a new instance of the `MapHandler` with the specified `NetworkHandler`. This setup allows the `MapHandler` to perform network operations by leveraging the existing network infrastructure.

### Methods

#### Map Management

- **createNewMap()**: Sends a request to the server to create a new map. It handles the server's response and updates the local map list accordingly. Exceptions like `UserSessionExpiredException` and `ServerTimeoutException` may be thrown if the session is invalid or the server fails to respond timely.

- **loadMap()**: Fetches all maps associated with the current user from the server. It updates the local user map list and handles potential session or server issues through exceptions.

- **getMap(String mapId)**: Retrieves a specific map by its ID. This method makes a GET request to the server and returns the map if found. It handles errors such as missing maps or invalid sessions.

- **updateMap(String mapId, String name, MapTemplate mapTemplate, boolean isPublic)**: Updates the details of an existing map. It sends modified map details to the server and refreshes the local cache of maps. Errors are managed via exceptions, similar to other methods.

- **deleteMap(String mapId)**: Removes a map by its ID from both the server and the local cache. This method handles exceptions related to session validity and server communication issues.

- **getPublicMaps()**: Retrieves all maps marked as public from the server. This method is intended for fetching maps that are available to all users, without needing authentication.

#### Utility Methods

- **getMapsFromDatabase()**: Similar to `loadMap()`, but explicitly named to imply direct fetching from the server's database. This method synchronizes the local cache with the server's data.

- **getUserMaps()**: Returns a list of all maps currently held in the user's local cache. This provides quick access to the user's maps without requiring a network request.

- **getNetworkHandler()**: Provides access to the underlying `NetworkHandler` used by this class. This allows other components of the client application to use the same network handler for different purposes, maintaining consistency in network operations.

### Exception Handling

This class uses custom exceptions to handle various error scenarios:

- **UserSessionExpiredException**: Thrown when the user session is no longer valid, requiring re-authentication.
- **ServerTimeoutException**: Thrown when the server does not respond within a set timeout period, indicating network or server-side issues.
- **MapNotFoundException**: Thrown when a specific map request fails because the map does not exist on the server.

### Event Handling

The `MapHandler` communicates with the server using predefined network events such as `MAP_CREATED`, `LOADED_MAPS`, `UPDATE_MAP`, and `DELETE_MAP`. These events help in managing the state of the application effectively by notifying other components about changes in map data.

### Usage Example

Here is how you might use the `MapHandler` to fetch and display user maps:

```java
NetworkHandler networkHandler = new NetworkHandler();
MapHandler mapHandler = new MapHandler(networkHandler);

try {
    ArrayList<Map> maps = mapHandler.getMapsFromDatabase();
    maps.forEach(map -> System.out.println(map.getName()));
} catch (UserSessionExpiredException | ServerTimeoutException e) {
    System.err.println("Error fetching maps: " + e.getMessage());
}
```

robert.kratz May 2, 2024
