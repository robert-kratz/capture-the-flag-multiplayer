package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.server.entities.Map;
import de.cfp1.server.exception.UserNotFoundException;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author juan.steppacher, gabriel.victor.arthur.himmelein, virgil.baclanov
 */

public class MapEditorController implements Stageable, Initializable {

  private Stage stage;
  private final SceneController sceneController = new SceneController();

  @FXML
  ToggleButton filterPublicMaps, filterPrivateMaps;
  @FXML
  ListView<Map> mapList;
  @FXML
  Text selectedMapNameText, selectedMapSizeText, selectedMapPlayersText, selectedMapPlacementText, selectedMapGameTimeText, selectedMapTurnTimeText, selectedMapCreatedByText, selectedMapCreatedOnText, selectedMapLastModifiedText;
  @FXML
  GridPane boardPreview;
  @FXML
  Button editMapButton, deleteMapButton;

  private Map selectedMap = null;
  private boolean canEditMap = false;

  /**
   * Initializes the map editor.
   *
   * @param location  URL location
   * @param resources ResourceBundle resources
   * @author gabriel.victor.arthur.himmelein, juan.steppacher, virgil.baclanov
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      showMaps();

      if (!canEditMap) {
        editMapButton.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.1)");
        deleteMapButton.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.1)");
      }

      editMapButton.setOnMouseEntered(event -> {
        if (canEditMap) {
          editMapButton.setStyle(
              "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0))");
        }
      });
      editMapButton.setOnMouseExited(event -> {
        if (canEditMap) {
          editMapButton.setStyle(
              "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3))");
        }
      });
      deleteMapButton.setOnMouseEntered(event -> {
        if (canEditMap) {
          deleteMapButton.setStyle(
              "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0))");
        }
      });
      deleteMapButton.setOnMouseExited(event -> {
        if (canEditMap) {
          deleteMapButton.setStyle(
              "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3))");
        }
      });

      //listener so that elements in the listview are deselected when clicking outside the listview
      mapList.focusedProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue) {
          mapList.getFocusModel().focus(-1);
        }
      });

      //listener to display the details of the selected map
      mapList.getSelectionModel().selectedItemProperty().addListener((observableValue, map, t1) -> {
        try {
          this.selectedMap = mapList.getSelectionModel().getSelectedItem();
          this.sceneController.playAudio("selectMenuSound.wav");

          if (this.selectedMap == null) {
            this.canEditMap = false;
            editMapButton.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.1)");
            deleteMapButton.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.1)");
          } else {
            this.canEditMap = true;
            editMapButton.setStyle("-fx-text-fill: #ffffff");
            deleteMapButton.setStyle("-fx-text-fill: #ffffff");

            String username = "Unknown User";

            try {
              username = Client.networkHandler.getUserById(selectedMap.getUserId()).getUsername();
            } catch (UserNotFoundException e) {
              e.printStackTrace();
            }

            //add map details
            selectedMapNameText.setText("Name: " + selectedMap.getName());
            selectedMapCreatedByText.setText("Created by: " + username);
            selectedMapCreatedOnText.setText("Created on: " + selectedMap.getCreated());
            selectedMapLastModifiedText.setText("Last modified: " + selectedMap.getLastModified());
            selectedMapSizeText.setText(
                "Grid Size: " + selectedMap.getMapTemplate().getGridSize()[0] + "x"
                    + selectedMap.getMapTemplate().getGridSize()[1]);
            selectedMapPlayersText.setText(
                "Number of Players: " + selectedMap.getMapTemplate().getTeams());
            selectedMapGameTimeText.setText(
                "Game Time: " + selectedMap.getMapTemplate().getTotalTimeLimitInSeconds() / 60
                    + " minutes");
            selectedMapTurnTimeText.setText(
                "Turn Time: " + selectedMap.getMapTemplate().getMoveTimeLimitInSeconds()
                    + " seconds");
            selectedMapPlacementText.setText(
                "Placement type: " + selectedMap.getMapTemplate().getPlacement());

            //calculate board preview
            EditMapController editMapController = new EditMapController();
            GameBoardHelper gameBoardHelper = new GameBoardHelper();
            Team[] teams = editMapController.getUpdatedTeam(this.selectedMap);
            GameState gameState = new GameState(this.selectedMap.getMapTemplate().getGridSize()[0],
                this.selectedMap.getMapTemplate().getGridSize()[1],
                this.selectedMap.getMapTemplate().getTeams());
            String[][] updatedFilledGrid = editMapController.getUpdatedGameStateGrid(
                this.selectedMap, teams);
            updatedFilledGrid = gameBoardHelper.placeBlocks(updatedFilledGrid,
                this.selectedMap.getMapTemplate().getBlocks());
            gameState.setGrid(updatedFilledGrid);
            gameState.setTeams(teams);
            this.selectedMap.setGameState(gameState);

            //add board preview
            boardPreview.getChildren().clear();
            boardPreview.add(new GameBoard(selectedMap.getGameState(), BoardTheme.CLASSIC, false),
                0, 0);
          }
        } catch (NullPointerException e) {
          this.selectedMap = null;
          this.canEditMap = false;
          System.out.println(
              "No map or details found in the map editor and therefore not selected.");
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("Error when selecting the map in the map editor.");
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Go and edit selected map.
   *
   * @author juan.steppacher
   */
  @FXML
  private void editMap() {
    try {
      if (canEditMap) {
        this.sceneController.playAudio("selectMenuSound.wav");
        stage.setScene(GUI.getScenes().get(SceneName.EDIT_MAP).getScene(this.selectedMap));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Method to delete saved maps.
   *
   * @author juan.steppacher
   */
  @FXML
  private void deleteMap() throws Exception {
    try {
      if (this.selectedMap != null) {
        Client.networkHandler.getMapHandler().deleteMap(selectedMap.getId());
        showMaps();
        boardPreview.getChildren().clear();
        this.sceneController.playAudio("selectMenuSound.wav");
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method switches between the public & private maps It will fetch maps corresponding to the
   * selected toggle and display them in the GUI.
   *
   * @author juan.steppacher
   */
  @FXML
  public void showMaps() throws Exception {
    List<Map> maps = null;
    if (filterPublicMaps.isSelected()) {
      maps = Client.networkHandler.getMapHandler().getPublicMaps();
      filterPublicMaps.setStyle(
          "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0));");
      filterPrivateMaps.setStyle(
          "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");
    } else if (filterPrivateMaps.isSelected()) {
      System.out.println();
      maps = Client.networkHandler.getMapHandler().getUserMaps();
      filterPrivateMaps.setStyle(
          "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0));");
      filterPublicMaps.setStyle(
          "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");
    }
    mapList.getItems().clear();
    mapList.getItems().addAll(maps);

    canEditMap = false;
  }

  /**
   * This method switches to the edit map frame.
   *
   * @author juan.steppacher
   */
  @FXML
  private void navigateToAddMap() throws Exception {
    this.sceneController.playAudio("selectMenuSound.wav");
    stage.setScene(GUI.getScenes().get(SceneName.EDIT_MAP).getScene());
  }

  /**
   * This method switches back to the main menu frame.
   *
   * @author juan.steppacher
   */
  @FXML
  private void navigateToMainMenu() throws Exception {
    this.sceneController.playAudio("selectMenuSound.wav");
    stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());
  }

  /**
   * This method sets the selected map.
   *
   * @param map the map to set
   * @author juan.steppacher
   */
  public void setSelectedMap(Map map) {
    this.selectedMap = map;
  }

  /**
   * This method gets the selected map.
   *
   * @return the selected map
   * @author juan.steppacher
   */
  public Map getSelectedMap() {
    return this.selectedMap;
  }

  /**
   * Required for the close button
   *
   * @param stage primary stage to set
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}