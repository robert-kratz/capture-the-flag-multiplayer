# LeaderboardController Documentation

## LeaderboardController.java

This class handles the GUI logic for displaying the leaderboard. It includes methods for:

## Key Methods

### Initialization

`initialize(URL url, ResourceBundle resourceBundle)`: This method is called after the scene is loaded and initializes the leaderboard. It comes from the Initializable interface.

### Leaderboard Population

`populateLeaderboard()`: This method is responsible for populating the leaderboard. It works with the Client class and collects the data for each user through the specified methods.

### Navigation

`navigateToMainMenu()`: This method is used to navigate to the main menu scene when the button is clicked.

## Attributes

`stage`: The stage where the leaderboard is displayed.

`sceneController`: An instance of SceneController used for scene management.

`listView`: A ListView component used to display the leaderboard.

## Usage in Game Flow

The LeaderboardController is used in the game flow when the user navigates to the leaderboard scene. The leaderboard is populated with data fetched from the server, and the user can navigate back to the main menu.

robert.kratz May 2. 2024
