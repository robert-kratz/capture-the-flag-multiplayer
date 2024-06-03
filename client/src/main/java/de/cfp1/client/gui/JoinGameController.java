package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.client.game.GameHandler;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.game.exceptions.GameSessionNotFound;
import de.cfp1.server.game.exceptions.NoMoreTeamSlots;
import de.cfp1.server.web.data.GameSessionResponse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * @author juan.steppacher, robert.kratz, benjamin.sander
 */

public class JoinGameController implements Stageable, Initializable {

  private Stage stage;

  private GameHandler gameHandler;

  private final SceneController sceneController = new SceneController();

  @FXML
  TextField joinCodeEnterTextField;

  @FXML
  Label joinCodeExceptionLabel;

  @Override
  public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
  }

  /**
   * This method initializes the join game controller.
   *
   * @author robert.kratz, benjamin.sander
   */
  @FXML
  private void joinGame() throws Exception {
    String inputCode = joinCodeEnterTextField.getText();
    try {
      GameSessionResponse response = Client.networkHandler.getGameHelper()
          .getGameStateByJoinCode(inputCode); //test if the join code is correct

      GameHandler gameHandler = new GameHandler(Client.networkHandler,
          response.getId()); //create a new game handler with the game id

      clearErrorLabel();
      navigateToWaitingLobby(gameHandler);

    } catch (ServerTimeoutException e) {
      e.printStackTrace();
      joinCodeExceptionLabel.setText("Server timeout. Please try again later.");
      joinCodeExceptionLabel.setVisible(true);
    } catch (GameSessionNotFound e) {
      //e.printStackTrace();
      joinCodeExceptionLabel.setText("Game session not found. Please try again.");
      joinCodeExceptionLabel.setVisible(true);
    } catch (NoMoreTeamSlots e) {
      e.printStackTrace();
      joinCodeExceptionLabel.setText("No more team slots available.");
      joinCodeExceptionLabel.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
      joinCodeExceptionLabel.setText("An error occurred. Please try again.");
      joinCodeExceptionLabel.setVisible(true);
    }
  }

  /**
   * This method clears the error label when the join code is correct.
   *
   * @author juan.steppacher, robert.kratz
   */
  private void clearErrorLabel() {
    joinCodeExceptionLabel.setText("");
    joinCodeExceptionLabel.setVisible(false);
  }

  /**
   * This method navigates to the game board scene.
   *
   * @author robert.kratz, juan.steppacher
   */
  @FXML
  private void navigateToGameBoard() {
    stage.setScene(GUI.getScenes().get(SceneName.BOARDGAME_CLASSIC).getScene());
  }

  /**
   * This method navigates to the waiting lobby scene.
   *
   * @param gameHandler the game handler to navigate to the waiting lobby with
   * @author robert.kratz, benjamin.sander
   */
  private void navigateToWaitingLobby(GameHandler gameHandler) {
    stage.setScene(GUI.getScenes().get(SceneName.WAITING_GAME).getScene(gameHandler));
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * This method closes the popUp window when cancel is clicked.
   *
   * @author juan.steppacher, benjamin.sander
   */
  @FXML
  private void navigateToPlayMenu() {
    stage.setScene(GUI.getScenes().get(SceneName.PLAY_MENU).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }


  /**
   * Required for the close button
   *
   * @param stage primary stage to set
   * @author juan.steppacher
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
