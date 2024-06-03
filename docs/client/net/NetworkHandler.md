## Class: NetworkHandler

**Package:** `de.cfp1.client.net`

**Imports:**

- Google's Gson library
- Various `de.cfp1.server` exceptions and entities
- `de.cfp1.client.game.GameHelper` and related classes

### Description

`NetworkHandler` is responsible for managing all network-related tasks in the application, including authentication, user management, and communication with the server. It provides functionality for logging in, signing up, refreshing tokens, and handling network events.

### Constructors

#### NetworkHandler(NetworkEvent eventListener)

Constructs a `NetworkHandler` with a specified event listener.

- **Parameters:**
  - `eventListener`: The event listener for network-related events.

### Methods

#### public boolean refreshTokens()

Attempts to refresh the access and refresh tokens using the server's token refresh endpoint.

- **Returns:**
  - `true` if successful, `false` otherwise.

#### public void setAccessToken(String accessToken, String refreshToken)

Sets the current access and refresh tokens.

- **Parameters:**
  - `accessToken`: The new access token.
  - `refreshToken`: The new refresh token.

#### public boolean login(String username, String password)

Logs in a user with the specified credentials.

- **Parameters:**
  - `username`: The user's username.
  - `password`: The user's password.
- **Returns:**
  - `true` if login is successful, `false` otherwise.
- **Throws:**
  - `InvalidCredentialsException`, `ServerTimeoutException`

#### public User getUserFromServer()

Retrieves the currently logged-in user's details from the server.

- **Returns:**
  - The `User` object representing the user's details.
- **Throws:**
  - `UserNotFoundException`, `ServerTimeoutException`

#### public boolean signup(String username, String email, String password)

Signs up a new user with the specified details.

- **Parameters:**
  - `username`: Desired username.
  - `email`: User's email address.
  - `password`: User's password.
- **Returns:**
  - `true` if signup is successful, `false` otherwise.
- **Throws:**
  - `UsernameTakenException`, `EmailTakenExceptions`, `PasswordInsecureException`

#### public boolean signUpAsGuest()

Creates a new guest account.

- **Returns:**
  - `true` if the operation is successful, `false` otherwise.
- **Throws:**
  - `ServerTimeoutException`

#### public boolean deleteUserProfile()

Deletes the currently logged-in user's profile from the server.

- **Returns:**
  - `true` if the deletion is successful, `false` otherwise.
- **Throws:**
  - `ServerTimeoutException`, `UserNotFoundException`

#### public boolean updateUsername(String newUsername)

Updates the username of the currently logged-in user.

- **Parameters:**
  - `newUsername`: The new username to set.
- **Returns:**
  - `true` if the update is successful, `false` otherwise.
- **Throws:**
  - `UsernameTakenException`, `ServerTimeoutException`, `UserSessionExpiredException`, `UserNotFoundException`

#### public void logout()

Logs out the currently logged-in user, clearing all session tokens.

### Member Variables

- `private static String BASE_URL`: Base URL of the server API.
- `private String accessToken, refreshToken`: Authentication tokens.
- `private final NetworkEvent eventListener`: Listener for network events.
- `private final MapHandler mapHandler`: Handler for map-related tasks.
- `private final StatisticsHandler statisticsHandler`: Handler for gathering statistics.
- `private GameHelper gameHelper`: Helper for game-related functionalities.
- `private User user`: The user associated with the current session.
- `private boolean isGuest`: Flag indicating if the current user is a guest.

### Example Usage

```java
NetworkEvent networkEvent = new MyNetworkEventImpl();
NetworkHandler networkHandler = new NetworkHandler(networkEvent);
networkHandler.login("username", "password");
```

robert.kratz May 2. 2024
