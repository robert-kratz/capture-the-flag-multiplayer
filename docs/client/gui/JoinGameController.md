# JoinGameController Documentation

## JoinGameController.java

This class handles the GUI logic for joining a game. It includes methods for:

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded. It comes from the Initializable interface.

### Game Joining

`joinGame()`: This method validates a user-entered password against the correct password to join the game. If the passwords match, it closes the dialog and switches the GUI to the game board scene; otherwise, it displays an error message.

### Navigation

`navigateToGameBoard()`: This method navigates to the game board scene.

`navigateToWaitingLobby(GameHandler gameHandler)`: This method navigates to the waiting lobby scene.

`navigateToPlayMenu()`: This method closes the pop-up window when cancel is clicked.

`setStage(Stage stage)`: This method is required for the close button. It sets the primary stage.

## Attributes

`stage`: The primary stage of the application.

`gameHandler`: The game handler of the application.

`sceneController`: The scene controller of the application.

`joinCodeEnterTextField`: The text field for entering the join code.

`joinCodeExceptionLabel`: The label for displaying exceptions related to the join code.

## Usage in Game Flow

The `JoinGameController` is used when a player wants to join a game. It handles the validation of the join code and navigates to the appropriate scene based on the validation result. It also provides methods to navigate to the game board, waiting lobby, and play menu.

robert.kratz May 2. 2024
