## StatisticsHandler Class Documentation

The `StatisticsHandler` class within the `de.cfp1.client.net` package serves as a comprehensive tool for managing statistics-related data in a client-server architecture. This class is tailored to handle operations such as retrieving the leaderboard and individual player statistics, interfacing through the `NetworkHandler` to communicate with a server.

### Constructor

- **StatisticsHandler(NetworkHandler networkHandler)**: Constructs an instance of the `StatisticsHandler` using the provided `NetworkHandler`. This relationship allows the `StatisticsHandler` to perform network requests effectively.

### Methods

#### Leaderboard and Statistics Retrieval

- **getLeaderboard()**: Fetches the leaderboard, specifically targeting the top 100 players ranked by their Elo rating. The method issues a GET request to the server and parses the response into a list of `LeaderboardResponse` objects. If the server does not respond in time, a `ServerTimeoutException` is thrown.

- **getStats(String userid)**: Retrieves the statistics for a specific user identified by `userid`. This method sends a GET request and returns a `StatsResponse` object containing detailed statistics. If the user does not exist or has no statistics, a `UserNotFoundException` is thrown. Server-related issues result in a `ServerTimeoutException`.

#### Utility Method

- **userHasStats(User user)**: Determines if a user has associated statistics. This static method checks the user's guest status; typically, guest users do not have persisting statistics. The method returns `true` if the user is not a guest (indicating they have statistics) and `false` otherwise.

### Exception Handling

The class manages several exceptions to ensure robust error handling:

- **ServerTimeoutException**: Thrown when the server fails to respond within a specified timeframe, indicating potential network issues or server unavailability.
- **UserNotFoundException**: Thrown if no statistics are found for a given user, which may be due to the user not existing in the database or lacking associated statistical data.

### Event Handling

The `StatisticsHandler` employs the `NetworkHandler`'s event system to broadcast significant events such as `FETCH_LEADERBOARD` and `FETCH_USER_STATS`. This feature facilitates real-time updates within the client application, ensuring that user interfaces reflect the most current data.

### Usage Example

Here's a practical example of how to use the `StatisticsHandler` to retrieve and display the leaderboard:

```java
NetworkHandler networkHandler = new NetworkHandler();
StatisticsHandler statsHandler = new StatisticsHandler(networkHandler);

try {
    ArrayList<LeaderboardResponse> leaderboard = statsHandler.getLeaderboard();
    leaderboard.forEach(entry -> System.out.println(entry.getUsername() + ": " + entry.getElo()));
} catch (ServerTimeoutException e) {
    System.err.println("Error retrieving leaderboard: " + e.getMessage());
}
```

robert.kratz May 2. 2024
