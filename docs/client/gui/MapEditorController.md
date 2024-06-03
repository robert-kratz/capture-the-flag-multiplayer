# MapEditorController Documentation

## MapEditorController.java

This class handles the GUI logic for the map editor in the game. It includes methods for initializing the map editor, editing and deleting maps, switching between public and private maps, and navigating to other scenes.

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded. It initializes the map editor, sets up the map details and the map preview, and sets up listeners for various GUI elements. It comes from the Initializable interface.

### Map Editing and Deletion

`editMap()`: This method is called when the user chooses to edit a selected map. It changes the scene to the map editing scene.

`deleteMap()`: This method is called when the user chooses to delete a selected map. It deletes the map from the server and updates the map list.

### Map Display

`showMaps()`: This method is called to refresh the list of maps displayed in the map editor. It fetches the maps from the server based on the selected filter (public or private) and displays them in the GUI.

### Navigation

`navigateToAddMap()`: This method is called when the user chooses to create a new map. It changes the scene to the map editing scene.

`navigateToMainMenu()`: This method is called when the user chooses to return to the main menu. It changes the scene to the main menu scene.

## Attributes

`stage`: The primary stage of the application.

`sceneController`: An instance of SceneController used to play audio effects.

`filterPublicMaps`, `filterPrivateMaps`: ToggleButtons used to switch between displaying public and private maps.

`mapList`: ListView used to display the list of maps.

`selectedMapNameText`, `selectedMapSizeText`, `selectedMapPlayersText`, `selectedMapPlacementText`, `selectedMapGameTimeText`, `selectedMapTurnTimeText`, `selectedMapCreatedByText`, `selectedMapCreatedOnText`, `selectedMapLastModifiedText`: Text elements used to display the details of the selected map.

`boardPreview`: GridPane used to display a preview of the selected map.

`editMapButton`, `deleteMapButton`: Buttons used to edit and delete the selected map.

`selectedMap`: The map currently selected in the map list.

`canEditMap`: A boolean indicating whether the selected map can be edited.

## Usage in Game Flow

The MapEditorController is used when the user navigates to the map editor scene. It allows the user to view, edit, and delete maps, as well as create new maps. It also allows the user to switch between viewing public and private maps.

robert.kratz May 2. 2024
