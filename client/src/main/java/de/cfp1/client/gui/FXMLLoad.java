package de.cfp1.client.gui;

import de.cfp1.client.game.GameHandler;
import de.cfp1.server.entities.Map;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.CurrentGameState;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.entities.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author virgil.baclanov, robert.kratz, benjamin.sander
 */

public class FXMLLoad {

  /**
   * Builds the scene from FXMLInfo. Uses this class's ClassLoader to find the URL of the FXML
   * file.
   *
   * @param fxmlInfo the FXML file info to load the scene with
   * @return the built scene, or null if the URL was not found
   * @author virgil.baclanov
   */
  public Scene load(FXMLInfo fxmlInfo) {
    URL url = getClass().getResource("/fxml/" + fxmlInfo.getResourceName());

    if (url == null) {
      Platform.exit();
      return null;
    }

    FXMLLoader loader = new FXMLLoader(url);
    Scene scene;

    try {
      Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
      scene = new Scene(loader.load());
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
      return null;
    }

    fxmlInfo.setScene(scene);
    GUI.updateScenes(fxmlInfo.getSceneName(), fxmlInfo);

    Stageable controller = loader.getController();
    if (controller != null) {
      controller.setStage(fxmlInfo.getStage());
    }

    return scene;
  }

  /**
   * loader for the EditMapController
   *
   * @param fxmlInfo the FXML file info to load the scene with
   * @param map      the map to load the scene with
   * @return the built scene, or null if the URL was not found
   * @author virgil.baclanov
   */
  public Scene load(FXMLInfo fxmlInfo, Map map) {
    URL url = getClass().getResource("/fxml/" + fxmlInfo.getResourceName());

    if (url == null) {
      Platform.exit();
      return null;
    }

    FXMLLoader loader = new FXMLLoader(url);
    Scene scene;

    try {
      scene = new Scene(loader.load());
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
      return null;
    }

    fxmlInfo.setScene(scene);
    GUI.updateScenes(fxmlInfo.getSceneName(), fxmlInfo);

    Stageable controller = loader.getController();
    if (controller != null) {
      controller.setStage(fxmlInfo.getStage());
      if (controller instanceof EditMapController) {
        ((EditMapController) controller).setCurrentMap(map);
      } else if (controller instanceof MapEditorController) {
        ((MapEditorController) controller).setSelectedMap(map);
      }
    }

    return scene;
  }

  /**
   * loader for the MainMenuController
   *
   * @param fxmlInfo the FXML file info to load the scene with
   * @param user     the user to load the scene with
   * @return the built scene, or null if the URL was not found
   * @author benjamin.sander
   */
  public Scene load(FXMLInfo fxmlInfo, User user) {
    URL url = getClass().getResource("/fxml/" + fxmlInfo.getResourceName());

    if (url == null) {
      Platform.exit();
      return null;
    }

    FXMLLoader loader = new FXMLLoader(url);
    Scene scene;

    try {
      scene = new Scene(loader.load());
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
      return null;
    }

    fxmlInfo.setScene(scene);
    GUI.updateScenes(fxmlInfo.getSceneName(), fxmlInfo);

    Stageable controller = loader.getController();
    if (controller != null) {
      controller.setStage(fxmlInfo.getStage());
      if (controller instanceof MainMenuController) {
        ((MainMenuController) controller).updateUserProfileText(user);
      } else if (controller instanceof PlayMenuController) {
        ((PlayMenuController) controller).updateUserProfileText(user);
      }


    }
    return scene;
  }

  /**
   * loader for the GameBoardController
   *
   * @param fxmlInfo           the FXML file info to load the scene with
   * @param gameHandler        the game handler to load the scene with
   * @param allPiecesBothTeams the pieces of both teams to load the scene with
   * @param boardTheme         the board theme to load the scene with
   * @return the built scene, or null if the URL was not found
   * @author virgil.baclanov
   */
  public Scene load(FXMLInfo fxmlInfo, GameHandler gameHandler,
      HashMap<String, Piece> allPiecesBothTeams, BoardTheme boardTheme) {
    URL url = getClass().getResource("/fxml/" + fxmlInfo.getResourceName());

    if (url == null) {
      Platform.exit();
      return null;
    }

    FXMLLoader loader = new FXMLLoader(url);
    Scene scene;

    try {
      scene = new Scene(loader.load());
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
      return null;
    }

    fxmlInfo.setScene(scene);
    GUI.updateScenes(fxmlInfo.getSceneName(), fxmlInfo);

    Stageable controller = loader.getController();
    if (controller != null) {
      controller.setStage(fxmlInfo.getStage());
      if (controller instanceof GameBoardController) {
        ((GameBoardController) controller).setGameHandler(gameHandler);
        ((GameBoardController) controller).setBoardTheme(boardTheme);
      }
    }
    return scene;
  }

  /**
   * loader for the GameBoardController
   *
   * @param fxmlInfo    the FXML file info to load the scene with
   * @param gameHandler the game handler to load the scene with
   * @return the built scene, or null if the URL was not found
   * @author virgil.baclanov, robert.kratz
   */
  public Scene load(FXMLInfo fxmlInfo, GameHandler gameHandler) {
    URL url = getClass().getResource("/fxml/" + fxmlInfo.getResourceName());

    if (url == null) {
      Platform.exit();
      return null;
    }

    FXMLLoader loader = new FXMLLoader(url);
    Scene scene;

    try {
      scene = new Scene(loader.load());
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
      return null;
    }

    fxmlInfo.setScene(scene);
    GUI.updateScenes(fxmlInfo.getSceneName(), fxmlInfo);

    Stageable controller = loader.getController();
    if (controller != null) {
      controller.setStage(fxmlInfo.getStage());
      if (controller instanceof WaitingLobbyController) {
        ((WaitingLobbyController) controller).setGameHandler(gameHandler);

        System.out.println("Joining lobby with sessionId: " + gameHandler.getSessionId());

        ((WaitingLobbyController) controller).joinLobby(gameHandler.getSessionId());
      }
      if (controller instanceof GameBoardController) {
        ((GameBoardController) controller).setGameHandler(gameHandler);

        ((GameBoardController) controller).showGameBoard();
      }
      if (controller instanceof EndGameController) {
        ((EndGameController) controller).setGameHandler(gameHandler);

        ((EndGameController) controller).displayWinners();
      }
    }
    return scene;
  }

  /**
   * loader for the EndGameController
   *
   * @param fxmlInfo    the FXML file info to load the scene with
   * @param gameHandler the game handler to load the scene with
   * @return the built scene, or null if the URL was not found
   * @author virgil.baclanov
   */
  public Scene loadEndGame(FXMLInfo fxmlInfo, GameHandler gameHandler) {
    URL url = getClass().getResource("/fxml/" + fxmlInfo.getResourceName());

    if (url == null) {
      Platform.exit();
      return null;
    }

    FXMLLoader loader = new FXMLLoader(url);
    Scene scene;

    try {
      scene = new Scene(loader.load());
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
      return null;
    }

    fxmlInfo.setScene(scene);
    GUI.updateScenes(fxmlInfo.getSceneName(), fxmlInfo);

    Stageable controller = loader.getController();
    if (controller != null) {
      controller.setStage(fxmlInfo.getStage());
      if (gameHandler.getCurrentGameState().equals(CurrentGameState.FINISHED)) {
        if (controller instanceof EndGameController) {
          ((EndGameController) controller).setGameHandler(gameHandler);
        }
      }
    }
    return scene;
  }
}
