## Class: GameHandler

**Package:** `de.cfp1.client.game`

**Imports:**

- `de.cfp1.client.net.NetworkHandler`
- Various `de.cfp1.server` exceptions and helpers
- Java utility classes like `ArrayList` and `Date`

### Description

`GameHandler` is a core class responsible for managing game state interactions between the client and the server. It encapsulates game logic, network communications, and session state management. Implements `Runnable` to handle game loop execution in a separate thread.

### Constructors

#### GameHandler(NetworkHandler networkHandler)

Initializes a new instance of the GameHandler class using an existing network handler.

- **Parameters:**
  - `networkHandler` - the network handler for managing network operations.

#### GameHandler(NetworkHandler networkHandler, String sessionId)

Initializes a new instance of the GameHandler class using an existing network handler and session ID.

- **Parameters:**
  - `networkHandler` - the network handler for managing network operations.
  - `sessionId` - the unique identifier of the game session.

### Methods

#### public void run()

Executes the main game loop, managing game state updates, event firing, and exception handling.

#### public CurrentGameState getCurrentGameState()

Returns the current state of the game session (e.g., LOBBY, RUNNING, FINISHED).

#### public GameSessionResponse createNewGame(MapTemplate mapTemplate)

Creates a new game session with a specified map template.

- **Parameters:**
  - `mapTemplate` - the template for the game map.
- **Throws:**
  - `ServerTimeoutException` - if the server fails to respond in time.

#### public void makeMove(Move move)

Processes a player's move within the current game session.

- **Parameters:**
  - `move` - the move to be made.
- **Throws:**
  - `ServerTimeoutException`, `GameSessionNotFound`, `ForbiddenMove`, `GameOver` - depending on game rules and server response.

#### public void joinGameByCode(String joinCode, String userId)

Joins a game session using a join code.

- **Parameters:**
  - `joinCode` - the join code of the game session.
  - `userId` - the user's identifier.
- **Throws:**
  - `ServerTimeoutException`, `GameSessionNotFound` - if unable to join the session.

#### public void giveUp()

Signals a give up, effectively resigning from the current game session.

- **Throws:**
  - `ServerTimeoutException`, `GameSessionNotFound` - if the operation fails.

#### public boolean isClientTurn()

Checks if it's the client's turn to make a move in the current game state.

#### public void setBoardTheme(BoardTheme boardTheme)

Sets the board theme for the current game session.

- **Parameters:**
  - `boardTheme` - the desired board theme.

#### public BoardTheme getBoardTheme()

Retrieves the current board theme of the game session.

#### public int getTeamIndex(String teamId)

Finds the index of the team with the specified ID.

- **Parameters:**
  - `teamId` - the team's identifier.

### Member Variables

- `private final NetworkHandler networkHandler` - Handles network communication.
- `private GameEvent gameEvent` - Manages game events.
- `private GameBoardHelper gameBoardHelper` - Utility class for board operations.
- `private GameState gameState, lastGameState` - Stores the current and previous game states.
- `private GameSessionResponse gameSessionResponse, lastGameSessionResponse` - Stores the current and previous game session responses.
- `private String teamSecret, teamColor, teamId, joinCode, sessionId` - Authentication and session identifiers.
- `private boolean isInLobby` - Indicates whether the client is in the lobby.
- `private boolean moveEventAlreadyFired, gameStartedEventAlreadyFired, gameEndedEventAlreadyFired` - Flags for tracking event firing status.
- `private String[] originalTeamIds` - Original IDs of the teams in the session.
- `public Thread gameThread` - Thread that runs the game loop.

### Example Usage

```java
NetworkHandler networkHandler = new NetworkHandler();
GameHandler gameHandler = new GameHandler(networkHandler);
gameHandler.createNewGame(new MapTemplate());
```

robert.kratz May 2. 2024
