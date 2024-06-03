## GUI Class Documentation

### Overview

The `GUI` class serves as the main driver for the graphical user interface of the client-side application. It manages the lifecycle of various stages and scenes, facilitating transitions between different parts of the application such as the login screen, main menu, map editor, and game views.

### Dependencies

- **JavaFX**: Utilized for rendering the GUI components.
- **FXMLLoader**: For loading the FXML files that define the layout of the GUI.
- **Scene**: Manages the various scenes.
- **Stage**: The top-level JavaFX container.

### Key Attributes

- `scenes`: A static map linking scene names to their respective `FXMLInfo` objects, which contain information necessary to load and display the scene.
- Various FXML file paths: Constants that store paths to FXML files for different GUI components.

### Key Methods

#### `start(Stage stage)`

Initializes the main application window with the login scene as the starting point.

- **Parameters**: `Stage stage` - the primary stage for this application, onto which the scenes are set.
- **Throws**: `Exception` if the initial scene file cannot be loaded.

#### `getScenes()`

Provides access to the map of scenes managed by the GUI.

- **Returns**: `Map<SceneName, FXMLInfo>` - a map linking scene names to their FXML configurations.

#### `updateScenes(SceneName name, FXMLInfo fxmlInfo)`

Updates the scene information in the scenes map, allowing for dynamic changes to scene configurations.

- **Parameters**:
  - `SceneName name`: The name of the scene to update.
  - `FXMLInfo fxmlInfo`: The new FXML information for the scene.

### Usage Example

To start the application, ensure that the main method is correctly set up as follows:

```java
public static void main(String[] args) {
    GUI.launch(GUI.class, args);
}
```

This method will invoke the `start` method, setting up the primary stage with the login scene initially displayed.

### Exception Handling

- **IOExceptions** related to FXML loading are caught and handled, typically resulting in an application exit or error message display.
- **MediaExceptions** may occur if media content fails to load, in which case they should be caught and handled appropriately to ensure the application remains stable.

### Styling and Resources

- System properties are set for macOS compatibility, ensuring that the application adheres to macOS design guidelines when run on Apple computers.
- A macOS dock icon is set via system properties to align with macOS aesthetics.

robert.kratz May 2. 2024
