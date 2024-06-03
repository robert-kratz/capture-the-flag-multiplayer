# EditMapController Documentation

This document provides an overview of the `EditMapController` class in the project. The class is implemented in Java using the JavaFX framework.

## EditMapController.java

This class handles the GUI logic for editing a game map. It includes methods for:

- Initializing the scene (`initialize(URL location, ResourceBundle resources)`)
- Populating map details (`populateMapDetails()`)
- Displaying map preview (`displayMapPreview()`)
- Getting the Map Template from the current field configuration (`getMapTemplateFromFields()`)
- Updating the map preview with the current map configuration (`updateMapPreview(GameBoardHelper gameBoardHelper)`)
- Getting the updated team object from the current map configuration (`getUpdatedTeam(Map map)`)
- Getting the updated game state grid with placement types from the current map configuration (`getUpdatedGameStateGrid(Map map, Team[] teams)`)
- Saving the map (`saveMap()`)
- Choosing if the map is public (`chooseMapPublic() throws Exception`)
- Navigating to the map editor (`navigateToMapEditor() throws Exception`)

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded and initializes the map details and the map preview. It comes from the Initializable interface.

### Map Details

`populateMapDetails()`: If there is a map, import the current map details into the fields.

### Map Preview

`displayMapPreview()`: Method to display the map preview based on the current map configuration, visually.

`updateMapPreview(GameBoardHelper gameBoardHelper)`: Updates the map preview with the current map configuration.

### Map Template

`getMapTemplateFromFields()`: Gets the Map Template from the current field configuration.

### Team and GameState

`getUpdatedTeam(Map map)`: Method to get the updated team object from the current map configuration.

`getUpdatedGameStateGrid(Map map, Team[] teams)`: Method to get the updated game state grid with placement types from the current map configuration.

### Map Saving and Publicity

`saveMap()`: Saves the current map configuration.

`chooseMapPublic() throws Exception`: Chooses if the map is public or not.

### Navigation

`navigateToMapEditor() throws Exception`: Navigates to the map editor scene.

## Usage in Game Flow

The `EditMapController` is pivotal throughout the game setup phase:

- It initializes the map editing scene.
- It populates the map details into the fields.
- It displays the map preview based on the current map configuration.
- It gets the Map Template from the current field configuration.
- It updates the map preview with the current map configuration.
- It gets the updated team object from the current map configuration.
- It gets the updated game state grid with placement types from the current map configuration.
- It saves the current map configuration.
- It chooses if the map is public or not.
- It navigates to the map editor scene.

robert.kratz May 2. 2024
