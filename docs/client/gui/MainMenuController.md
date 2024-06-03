# MainMenuController Documentation

## MainMenuController.java

This class handles the GUI logic for the main menu of the game. It includes methods for:

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded and initializes the user profile text. It comes from the Initializable interface.

### User Profile Update

`updateUserProfileText(User user)`: This method updates the user profile text in the GUI.

### Elo Setting

`setElo(int elo)`: This method sets the elo of the user.

### Navigation

`navigateToPlayMenu()`: This method navigates to the play menu.

`navigateToMapEditor()`: This method navigates to the map editor.

`navigateToSettings()`: This method navigates to the game settings.

`navigateToLeaderboard()`: This method navigates to the leaderboard.

`logOut()`: This method switches back to the log in frame.

## Usage in Game Flow

The MainMenuController is used after the user logs in. It displays the main menu of the game, where the user can navigate to different parts of the game such as the play menu, map editor, game settings, and leaderboard. The user can also log out from the main menu.

## Attributes

`stage`: The stage where the main menu is displayed.

`sceneController`: The controller for the scene.

`usernameLabel`: The label for the username.

`elo`: The elo of the user.

robert.kratz May 2. 2024
