# PlayMenuController Documentation

## PlayMenuController.java

This class handles the GUI logic for the play menu in the game. It includes methods for:

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded and initializes the user profile text. It comes from the Initializable interface.

### User Profile Update

`updateUserProfileText(User user)`: This method updates the user profile text with the username and Elo rating of the user.

### Navigation

`navigateToAiPopUp()`: This method navigates to the AI popup.

`navigateToJoinGame()`: This method navigates to the join game frame.

`navigateToCreateGame()`: This method navigates to the create game frame.

`navigateToMainMenu()`: This method navigates to the main menu frame.

`navigateToSettings()`: This method navigates to the settings frame.

`logOut()`: This method switches back to the log in frame.

## Usage in Game Flow

The `PlayMenuController` is used in the game flow when the user is in the play menu. It provides the user with options to navigate to different parts of the game such as joining a game, creating a game, configuring AI settings, and logging out. It also displays the user's profile information.

## Attributes

`stage`: The primary stage where the current scene is set.

`sceneController`: An instance of `SceneController` used to play audio.

`usernameLabel`: A `Text` object that displays the user's username and Elo rating.

robert.kratz May 2. 2024
