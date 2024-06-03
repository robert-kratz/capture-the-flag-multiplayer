package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.client.game.GameHandler;
import de.cfp1.server.entities.Map;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.exception.UserSessionExpiredException;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.state.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;

/**
 * @author virgil.baclanov, robert.kratz
 */

public class CreateGameController implements Stageable, Initializable {

  private Stage stage;
  SceneController sceneController = new SceneController();

  @FXML
  Text selectedMapNameText, selectedMapSizeText, selectedMapPlayersText, selectedMapPlacementText, selectedMapGameTimeText, selectedMapTurnTimeText, selectedMapCreatedByText, selectedMapCreatedOnText, selectedMapLastModifiedText;
  @FXML
  ToggleGroup themeGroup;
  @FXML
  RadioButton classicThemeToggle, zombieThemeToggle, wildWestThemeToggle;
  @FXML
  ToggleButton filterPublicMaps, filterPrivateMaps;
  @FXML
  ListView<Map> mapList;
  @FXML
  GridPane boardPreview;
  @FXML
  Button startGameButton;

  Map selectedMap = null;
  BoardTheme currentBoardTheme = BoardTheme.CLASSIC;
  boolean canStartGame = false;

  /**
   * Initialize the controller
   *
   * @param location  URL
   * @param resources ResourceBundle
   * @author virgil.baclanov
   */
  @Override
  public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
    try {
      showMaps();

      if (!canStartGame) {
        startGameButton.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.1)");
      }

      startGameButton.setOnMouseEntered(event -> {
        if (canStartGame) {
          startGameButton.setStyle(
              "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0))");
        }
      });

      startGameButton.setOnMouseExited(event -> {
        if (canStartGame) {
          startGameButton.setStyle(
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
            this.canStartGame = false;
            startGameButton.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.1)");
          } else {
            this.canStartGame = true;
            startGameButton.setStyle("-fx-text-fill: #ffffff");

            String username = "Unknown User";

            try {
              username = Client.networkHandler.getUserById(selectedMap.getUserId()).getUsername();
            } catch (Exception e) {
              System.out.println("User not found, using default name");
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

            boardPreview.getChildren().clear();
            boardPreview.add(new GameBoard(selectedMap.getGameState(), currentBoardTheme, false), 0,
                0);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      themeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
        this.sceneController.playAudio("selectMenuSound.wav");

        if (classicThemeToggle.isSelected()) {
          currentBoardTheme = BoardTheme.CLASSIC;
          boardPreview.getChildren().clear();
          boardPreview.add(new GameBoard(selectedMap.getGameState(), BoardTheme.CLASSIC, false), 0,
              0);
        } else if (zombieThemeToggle.isSelected()) {
          currentBoardTheme = BoardTheme.ZOMBIE;
          boardPreview.getChildren().clear();
          boardPreview.add(new GameBoard(selectedMap.getGameState(), BoardTheme.ZOMBIE, false), 0,
              0);
        } else if (wildWestThemeToggle.isSelected()) {
          currentBoardTheme = BoardTheme.WEST;
          boardPreview.getChildren().clear();
          boardPreview.add(new GameBoard(selectedMap.getGameState(), BoardTheme.WEST, false), 0, 0);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Show maps based on the selected filter
   *
   * @throws Exception if no maps are found
   * @author virgil.baclanov
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
      maps = Client.networkHandler.getMapHandler().getUserMaps();
      filterPrivateMaps.setStyle(
          "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0));");
      filterPublicMaps.setStyle(
          "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");
    }
    mapList.getItems().clear();

    try {
      mapList.getItems().addAll(maps);
    } catch (NullPointerException e) {
      System.out.println("No maps found");
    }
  }

  /**
   * Start the game with the selected map and theme
   *
   * @author virgil.baclanov, robert.kratz
   */
  @FXML
  public void startGame() {
    if (canStartGame) {
      try {
        GameHandler gameHandler = Client.networkHandler.createNewGameSession(
            this.selectedMap.getMapTemplate());

        gameHandler.setBoardTheme(currentBoardTheme);

        stage.setScene(GUI.getScenes().get(SceneName.WAITING_GAME).getScene(gameHandler));

        this.sceneController.playAudio("selectMenuSound.wav");

        System.out.println("Game started with id: " + gameHandler.getSessionId());
      } catch (UserSessionExpiredException e) {
        e.printStackTrace();
        System.out.println("User session expired");
      } catch (ServerTimeoutException e) {
        e.printStackTrace();
        System.out.println("Server timeout");
      } catch (NullPointerException e) {
        System.out.println("Game cannot be started, map not found.");
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Navigate to the map editor
   *
   * @author virgil.baclanov
   */
  @FXML
  private void navigateToMapEditor() {
    stage.setScene(GUI.getScenes().get(SceneName.MAP_EDITOR).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * Navigate to the play menu
   *
   * @author virgil.baclanov
   */
  @FXML
  private void navigateToPlayMenu() {
    stage.setScene(GUI.getScenes().get(SceneName.PLAY_MENU).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * Navigate back to the main menu
   *
   * @param event action event
   * @author virgil.baclanov
   */
  @FXML
  private void handleOnActionBackButton(ActionEvent event) {
    stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());
  }

  /**
   * Close application
   *
   * @author virgil.baclanov
   */
  @FXML
  private void handleOnActionClose(ActionEvent event) {
    stage.close();
  }

  /**
   * Required for the close button
   *
   * @param stage primary stage to set
   * @author virgil.baclanov
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
