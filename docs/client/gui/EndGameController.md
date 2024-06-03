# EndGameController Documentation

## EndGameController.java

This class handles the GUI logic for the end game screen. It includes methods for:

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded and initializes the end game screen. It comes from the Initializable interface.

### Displaying Winners

`displayWinners()`: This method is the entry point for displaying the winners of the game.

### Navigation

`navigateToMainMenu()`: This method is used to navigate back to the main menu from the end game screen.

`setStage(Stage stage)`: This method is used to set the primary stage of the application.

`setGameHandler(GameHandler gameHandler)`: This method is used to set the game handler.

`getGameHandler()`: This method is used to get the game handler.

## Usage in Game Flow

The `EndGameController` is used at the end of a game session. It displays the result of the game, the ELO changes, and the winners. It also provides an option to navigate back to the main menu.

robert.kratz May 2. 2024
