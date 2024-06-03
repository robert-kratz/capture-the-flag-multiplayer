# WaitingLobbyController Documentation

## WaitingLobbyController.java

This class handles the GUI logic for managing the waiting lobby in the game. It includes methods for:

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded. It initializes the players in the lobby and the join game code text. It comes from the Initializable interface.

### Game Event Handling

`onWaiting()`: This method is called when the client is idling in the waiting lobby. It updates the number of players in the lobby.

`onGameStart()`: This method is called when the game starts. It navigates to the game board.

`onGameEnded()`: This method is called when the game ends. It is not used in the waiting lobby.

`onGameError(String message)`: This method is called when there is an error in the game. It is not used in the waiting lobby.

`onMyTurn(GameState gameState)`: This method is called when it's the user's turn. It starts the game.

`onOpponentTurn(GameState gameState)`: This method is called when it's the opponent's turn. It starts the game.

`onGameDelete()`: This method is called when the game is deleted. It is not used in the waiting lobby.

### Lobby Management

`leaveLobby()`: This method is called when the leave lobby button is clicked. It interrupts the game thread and navigates back to the main menu.

`joinLobby(String sessionId)`: This method is called to join the lobby of the waiting lobby context.

### Navigation

`navigateToGameBoard()`: This method navigates to the game board based on the board theme.

## Usage in Game Flow

The WaitingLobbyController is used when the user is in the waiting lobby of the game. It handles the logic for updating the lobby status, joining the lobby, leaving the lobby, and navigating to the game board when the game starts.

## Attributes

`sessionId`: The session ID of the game.

`user`: The user who is in the waiting lobby.

`gameHandler`: The game handler for managing the game events.

`playersInLobby`: The number of players currently in the lobby.

`maxPlayers`: The maximum number of players allowed in the lobby.

`sceneController`: The scene controller for managing the scene transitions.

`stage`: The stage where the waiting lobby scene is displayed.

`playersInLobbyLabel`: The label for displaying the number of players in the lobby.

`joinGameCodeText`: The text for displaying the join game code.

`leaveLobbyButton`: The button for leaving the lobby.

robert.kratz May 2. 2024
