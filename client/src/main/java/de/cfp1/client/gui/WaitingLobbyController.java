package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.client.game.GameEvent;
import de.cfp1.client.game.GameHandler;
import de.cfp1.client.game.GameHelper;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.server.entities.User;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.exception.UserNotFoundException;
import de.cfp1.server.game.CtfGame;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.GameTeamHelper;
import de.cfp1.server.game.exceptions.GameSessionNotFound;
import de.cfp1.server.game.exceptions.NoMoreTeamSlots;
import de.cfp1.server.game.map.*;
import de.cfp1.server.game.state.*;
import de.cfp1.server.web.data.GiveupRequest;
import de.cfp1.server.web.data.JoinGameResponse;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.HashMap;

/**
 * @author robert.kratz, virgil.baclanov
 */

public class WaitingLobbyController implements Stageable, Initializable, GameEvent {

  private String sessionId;
  private User user;

  private GameHandler gameHandler;

  private int playersInLobby = 0, maxPlayers = 0;

  private SceneController sceneController = new SceneController();

  private Stage stage;
  @FXML
  Label playersInLobbyLabel;

  @FXML
  Text joinGameCodeText;

  @FXML
  Button leaveLobbyButton;

  @FXML
  ImageView waiting_icon;

  /**
   * This method is called when the controller is initialized
   *
   * @param location
   * @param resources
   * @author robert.kratz, virgil.baclanov
   */
  @Override
  public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
    System.out.println("WaitingLobbyController initialized");

    playersInLobbyLabel.setText("Players in lobby: " + playersInLobby + "/" + maxPlayers);

    joinGameCodeText.setOnMouseClicked(event -> {
        if (joinGameCodeText.getText().isEmpty()) {
            return;
        }

      Clipboard clipboard = Clipboard.getSystemClipboard();
      ClipboardContent content = new ClipboardContent();
      content.putString(joinGameCodeText.getText());
      clipboard.setContent(content);
    });
  }

  /**
   * This method is called when the client is idling in waiting lobby
   *
   * @author robert.kratz, virgil.baclanov
   */
  @Override
  public void onWaiting() {
    this.playersInLobby = this.gameHandler.getOnlineTeams();
    this.maxPlayers = this.gameHandler.getGameState().getTeams().length;

    Platform.runLater(() -> {
      playersInLobbyLabel.setText("Players in lobby: " + playersInLobby + "/" + maxPlayers);
      System.out.println("Players in lobby: " + playersInLobby + "/" + maxPlayers);

      RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), waiting_icon);
      rotateTransition.setByAngle(360);
      rotateTransition.setCycleCount(Animation.INDEFINITE);
      rotateTransition.setInterpolator(Interpolator.LINEAR);
      rotateTransition.play();
    });
  }

  /**
   * Called when the game is started
   *
   * @author robert.kratz, virgil.baclanov
   */
  @Override
  public void onGameStart() {
    Platform.runLater(() -> {
      try {
        navigateToGameBoard(); // Assuming navigateToGameBoard needs to be on JavaFX thread
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * This method is called when the leave lobby button is clicked
   *
   * @author robert.kratz
   */
  public synchronized void leaveLobby() {
    Platform.runLater(() -> {
      stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());
      try {
        this.gameHandler.gameThread.interrupt();
        this.gameHandler.giveUp();
      } catch (ServerTimeoutException e) {
        e.printStackTrace();
      }
      this.sceneController.playAudio("selectMenuSound.wav");
    });
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

  /**
   * This method is called to join the lobby of the waiting lobby context. this method must be
   * called!!!
   *
   * @author robert.kratz
   */
  public void joinLobby(String sessionId) {
    try {
      System.out.println("Joining lobby");

      System.out.println("Session ID: " + sessionId);

      this.sessionId = this.gameHandler.getSessionId();
      this.user = this.gameHandler.getNetworkHandler().getUserFromServer();

      //creates a new game handler
      this.gameHandler.setGameEvent(this); //sets the game event

      this.gameHandler.joinGameBySessionId(sessionId, user.getId()); //joins the game

      joinGameCodeText.setText(this.gameHandler.getCurrentJoinCode());

      this.maxPlayers = this.gameHandler.getGameState().getTeams().length;

    } catch (NoMoreTeamSlots e) {
      e.printStackTrace();
      System.out.println("Game already started");
      leaveLobby();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called to join the lobby of the waiting lobby context. this method must be
   * called!
   *
   * @throws Exception if the game handler is not set
   * @author virgil.baclanov
   */
  private void navigateToGameBoard() throws Exception {
    switch (this.gameHandler.getBoardTheme()) {
      case CLASSIC:
        stage.setScene(GUI.getScenes().get(SceneName.BOARDGAME_CLASSIC).getScene(this.gameHandler));
        break;
      case WEST:
        stage.setScene(GUI.getScenes().get(SceneName.BOARDGAME_WEST).getScene(this.gameHandler));
        break;
      case ZOMBIE:
        stage.setScene(GUI.getScenes().get(SceneName.BOARDGAME_ZOMBIE).getScene(this.gameHandler));
        break;
    }
  }

  /**
   * This method is called when the game is ended
   *
   * @param sessionId the session id
   * @author robert.kratz, virgil.baclanov
   */
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * This method is called when the user is set
   *
   * @param user the user
   * @author robert.kratz, virgil.baclanov
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Called when the game is ended DO NOT USE NOT NESSARY IN WAITING LOBBY
   *
   * @author robert.kratz
   */
  @Override
  public void onGameEnded() {
  }

  /**
   * Called when the game is errored DO NOT USE NOT NESSARY IN WAITING LOBBY
   *
   * @param message the error message
   * @author robert.kratz
   */
  @Override
  public void onGameError(String message) {
  }

  /**
   * Called when the game is updated DO NOT USE NOT NESSARY IN WAITING LOBBY
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  @Override
  public void onMyTurn(GameState gameState) {
    System.out.println("onMyTurn");
    this.onGameStart();
  }

  /**
   * Called when the opponent's turn is updated DO NOT USE NOT NESSARY IN WAITING LOBBY
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  @Override
  public void onOpponentTurn(GameState gameState) {
    System.out.println("onOpponentTurn");
    this.onGameStart();
  }

  /**
   * Called when the game is deleted DO NOT USE NOT NESSARY IN WAITING LOBBY
   *
   * @author robert.kratz
   */
  @Override
  public void onGameDelete() {
  }

  /**
   * Called when the game is updated
   *
   * @param gameHandler the game handler
   * @author robert.kratz
   */
  public void setGameHandler(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
  }
}
