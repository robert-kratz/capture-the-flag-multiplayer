package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.client.game.GameEvent;
import de.cfp1.client.game.GameHandler;
import de.cfp1.server.entities.User;
import de.cfp1.server.exception.UserNotFoundException;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.CtfGame;
import de.cfp1.server.game.CurrentGameState;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;
import java.util.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author robert.kratz virgil.baclanov
 */

public class EndGameController implements Stageable, Initializable {

  private Stage stage;
  private GameHandler gameHandler;
  private SceneController sceneController = new SceneController();

  @FXML
  private Text endResultText, endEloText, endWinners1Text, endWinners2Text, endWinners3Text, endWinners4Text;

  /**
   * Initializes the end game screen
   *
   * @param location  the location
   * @param resources the resources
   * @author robert.kratz, virgil.baclanov
   */
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(() -> {
      //get current user
      String currentUserId = Client.networkHandler.getUser().getId();

      String[] winners = this.gameHandler.getGameSessionResponse().getWinner();

      //todo check against requirements AGAIN if logic correctly implemented
      //loser screen displayed if no other of the below-mentioned conditions met
      for (String winner : winners) {
        if (Objects.equals(winner, currentUserId)) {
          if (winners.length > 1) {
            endResultText.setText("Draw!");
            endEloText.setText("+11 ELO");

            endResultText.setStyle("-fx-fill: white;");
            endEloText.setStyle("-fx-fill: white;");
          } else {
            endResultText.setText("Victory!");
            endEloText.setText("+30 ELO");

            endResultText.setStyle("-fx-fill: yellow;");
            endEloText.setStyle("-fx-fill: yellow;");
          }
        }
      }

      //todo set winners
      //alle 4 texts am anfang visible
    });
  }

  /**
   * Entry point for displaying the winners of the game
   *
   * @author robert.kratz
   */
  public void displayWinners() {
    Platform.runLater(() -> {
      String[] winners = this.gameHandler.getWinnerTeamIds();
      Text[] labels = {endWinners1Text, endWinners2Text, endWinners3Text, endWinners4Text};

      for (Text label : labels) {
        label.setVisible(false);
      }

      boolean isLooser = true;

      for (int i = 0; i < winners.length; i++) {
        labels[i].setVisible(true);
        try {
          User user = Client.networkHandler.getUserById(winners[i]);

          if (user.getId().equals(Client.networkHandler.getUser().getId())) {
            isLooser = false;
          }

          boolean isCurrentUser = user.getId().equals(Client.networkHandler.getUser().getId());

          if (isCurrentUser) {
            this.sceneController.playAudio("matchWon.wav");
          }

          labels[i].setText(user.getUsername() + (isCurrentUser ? " (You)" : ""));
        } catch (Exception e) {
          labels[i].setText("Unknown User");
        }
      }

      if (isLooser) {
        this.sceneController.playAudio("matchLost.wav");
      }
    });
  }

  /**
   * Navigates to the main menu
   *
   * @author virgil.baclanov
   */
  @FXML
  private void navigateToMainMenu() {
    try {
      this.stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());
      this.sceneController.playAudio("selectMenuSound.wav");
      this.gameHandler.giveUp();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Required for the close button
   *
   * @param stage The graphics user interface to be displayed
   * @author virgil.baclanov
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Sets the game handler
   *
   * @param gameHandler The game handler
   * @author virgil.baclanov
   */
  public void setGameHandler(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
  }

  /**
   * Gets the game handler
   *
   * @return The game handler
   * @author virgil.baclanov
   */
  public GameHandler getGameHandler() {
    return gameHandler;
  }
}
