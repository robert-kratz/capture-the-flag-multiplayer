package de.cfp1.ai.gui;

import de.cfp1.ai.Ai;
import de.cfp1.client.game.GameEvent;
import de.cfp1.client.game.GameHandler;
import de.cfp1.client.gui.Stageable;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.game.exceptions.GameSessionNotFound;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.web.data.GameSessionResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author robertkratz, virgil.baclanov
 */

public class JoinAIController implements GameEvent, Initializable, Stageable {

  Stage stage;
  @FXML
  Label usernameLabel, aiModeLabel, errorLabel;
  @FXML
  Button closeButton, joinButton;
  @FXML
  TextField joinCodeInputTextField;

  private NetworkHandler networkHandler = Ai.networkHandler;

  private GameHandler gameHandler = new GameHandler(this.networkHandler);

  /**
   * This method is called when the client wants to navigate to the join screen
   *
   * @param url            the url
   * @param resourceBundle the resource bundle
   * @author robert.kratz
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Platform.runLater(() -> {
      try {
        this.networkHandler.signUpAsGuest(); // sign up as guest

        this.usernameLabel.setText("Username: " + networkHandler.getUserFromServer().getUsername());
      } catch (Exception e) {
        this.errorLabel.setText("Error: " + e.getMessage());
        this.usernameLabel.setText("Username: unknown");
      }
    });
  }

  /**
   * This method is called to join a game by a game code
   *
   * @author robert.kratz
   */
  @FXML
  public void joinGame() {
    try {
      GameSessionResponse gameSessionResponse = null;

      if (joinCodeInputTextField.getText().isEmpty()) {
        errorLabel.setVisible(true);
        errorLabel.setText("Please enter a game code");
        return;
      }

      if (joinCodeInputTextField.getText().length() > 6) {
        gameSessionResponse = Ai.networkHandler.getGameHelper()
            .getGameSession(joinCodeInputTextField.getText());
      } else {
        gameSessionResponse = Ai.networkHandler.getGameHelper()
            .getGameStateByJoinCode(joinCodeInputTextField.getText());
      }

      if (gameSessionResponse == null) {
        errorLabel.setVisible(true);
        errorLabel.setText("Game not found");
        return;
      }

      String userId = this.networkHandler.getUser() != null ? this.networkHandler.getUser().getId()
          : "unknown_player";

      this.gameHandler.joinGameBySessionId(gameSessionResponse.getId(), userId);
      navigateToGameScreen();
    } catch (GameSessionNotFound e) {
      errorLabel.setVisible(true);
      errorLabel.setText("Game not found");
    } catch (ServerTimeoutException e) {
      errorLabel.setVisible(true);
      errorLabel.setText("Server timeout");
    } catch (Exception e) {
      errorLabel.setVisible(true);
      errorLabel.setText("Error: " + e.getMessage());
    }
  }

  /**
   * This method is called to navigate to the game screen
   *
   * @author robert.kratz, virgil.baclanov
   */
  public void navigateToGameScreen() {
    try {
      URL url = getClass().getResource("/game_frame.fxml");
      FXMLLoader loader = new FXMLLoader(url);
      Scene scene = new Scene(loader.load());
      this.stage.setScene(scene);

      GameAIController controller = loader.getController();
      controller.setGameHandler(this.gameHandler);
      if (controller != null) {
        controller.setStage(this.stage);
      }
    } catch (Exception e) {
      errorLabel.setVisible(true);
      errorLabel.setText("Error: " + e.getMessage());
      e.printStackTrace();
      return;
    }
  }

  /**
   * This method is called to close the AI client
   *
   * @author robert.kratz
   */
  @FXML
  public void closeAI() {
    //backend

    this.stage.close();
    System.exit(1);
  }

  /**
   * This method is called when the client is idling in waiting lobby
   *
   * @author robert.kratz
   */
  @Override
  public void onWaiting() {
  }

  /**
   * Called when the game is ended
   *
   * @author robert.kratz
   */
  @Override
  public void onGameEnded() {
  }

  /**
   * Called when the game is started
   *
   * @author robert.kratz
   */
  @Override
  public void onGameStart() {
  }

  /**
   * Called when the game is errored
   *
   * @param message the error message
   * @author robert.kratz
   */
  @Override
  public void onGameError(String message) {
  }

  /**
   * Called when the game is updated
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  @Override
  public void onMyTurn(GameState gameState) {
    try {
      //String[][] currentGrid = this.gameHandler.getGameState().getGrid();

      //this.gameHandler.makeMove(new Move());
    } catch (Exception e) {
      errorLabel.setText("Error: " + e.getMessage());
      return;
    }
  }

  /**
   * Called when the opponent's turn is updated
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  @Override
  public void onOpponentTurn(GameState gameState) {
  }

  /**
   * Called when the game is deleted
   *
   * @author robert.kratz
   */
  @Override
  public void onGameDelete() {
  }

  /**
   * This method is called to set the stage of the AI client
   *
   * @param stage The graphics user interface to be displayed
   * @author robert.kratz
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * This method is called to get the stage of the AI client
   *
   * @return The stage
   * @author robert.kratz
   */
  public Stage getStage() {
    return this.stage;
  }

}
