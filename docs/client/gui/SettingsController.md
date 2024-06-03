# SettingsController Documentation

## SettingsController.java

This class handles the GUI logic for the settings view of the game. It includes methods for:

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the settings scene is loaded. It comes from the Initializable interface.

`prepareVideo()`: This method prepares the video to be displayed in the settings view. It sets the video source, binds the video dimensions to the stage dimensions, and sets the video to loop while the settings scene is active.

### Navigation

`navigateToMainMenu()`: This method navigates back to the main menu frame. It stops the video playback before switching the scene.

### Stage Setting

`setStage(Stage stage)`: This method sets the primary stage for the settings view. It is required for the close button functionality. It also prepares the video to be displayed in the settings view.

## Usage in Game Flow

The `SettingsController` is used when the user navigates to the settings view. It prepares and plays a video, and provides a method to navigate back to the main menu. The video is stopped when the user navigates away from the settings view. The `setStage` method is used to set the primary stage for the settings view, which is required for the close button functionality.

robert.kratz May 2. 2024
