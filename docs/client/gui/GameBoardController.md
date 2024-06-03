# GameBoardController Documentation

## GameBoardController.java

This class handles the GUI logic for the game board controller. It includes methods for:

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded. It comes from the Initializable interface.

`showGameBoard()`: This method is the entry point for displaying the game board from the game handler.

### Game State Management

`onGameEnded()`: This method is called when the game ends.

`onGameStart()`: This method is called when the game starts.

`onGameError(String message)`: This method is called when there is an error in the game.

`onMyTurn(GameState gameState)`: This method is called when it's the user's turn.

`onOpponentTurn(GameState gameState)`: This method is called when it's the opponent's turn.

`onGameDelete()`: This method is called when the game is deleted.

### Navigation

`navigateToMainMenu()`: This method is used to navigate back to the main menu from the game board.

### Game Board Update

`updateUI()`: This method is used to update the game board UI.

`displayUsersTurnBadge()`: This method is used to display the user's turn badge.

`setPlayerDisplayNames()`: This method is used to update the player info on the game board.

## Usage in Game Flow

The `GameBoardController` is used during a game session. It manages the game state, updates the game board UI, and handles navigation. It also provides methods to handle different game events such as game start, game end and game error.

robert.kratz May 2. 2024
