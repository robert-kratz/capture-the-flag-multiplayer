package de.cfp1.ai.gui;

import de.cfp1.ai.Ai;
import de.cfp1.ai.utils.AiEasy;
import de.cfp1.ai.utils.AiHard;
import de.cfp1.ai.utils.AiMedium;
import de.cfp1.client.game.GameEvent;
import de.cfp1.client.game.GameHandler;
import de.cfp1.client.gui.Stageable;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.client.utils.AiType;
import de.cfp1.server.game.state.GameState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.util.Duration;

/**
 * @author robert.kratz, virgil.baclanov
 */

public class GameAIController implements GameEvent, Initializable, Stageable {

  Stage stage;
  @FXML
  Label usernameJoinedLabel, joinCodeLabel, moveTimeAILabel, gameTimeAILabel, errorLabelJoined;
  @FXML
  ToggleButton easyDifficultyButton, mediumDifficultyButton, hardDifficultyButton;

  private AiType aiType = AiType.EASY;

  private final NetworkHandler networkHandler = Ai.networkHandler;

  private GameHandler gameHandler;

  private Timeline updateTimer;

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
        //backend

        this.easyDifficultyButton.setSelected(true);

        changeDifficulty();

        this.initUIUpdateTimer();

        String username =
            networkHandler.getUser() != null ? networkHandler.getUser().getUsername() : "unknown";

        this.usernameJoinedLabel.setText("Username: " + username);
        this.joinCodeLabel.setText("Join code: " + gameHandler.getJoinCode());

      } catch (Exception e) {
        errorLabelJoined.setVisible(true);
        errorLabelJoined.setText("Error: " + e.getMessage());
        return;
      }
    });
  }

  /**
   * Called when the game is updated
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  @Override
  public void onMyTurn(GameState gameState) {
    System.out.println("On my turn");
    switch (aiType) {
      case EASY:
        System.out.println("AI easy move");
        AiEasy aiEasy = new AiEasy(gameHandler);
        this.gameHandler.makeMove(aiEasy.calcEasyMove());
        break;
      case MEDIUM:
        System.out.println("AI medium move");
        AiMedium aiMedium = new AiMedium(gameHandler);
        this.gameHandler.makeMove(aiMedium.calcMediumMove());
        break;
      case HARD:
        System.out.println("AI hard move");
        AiHard aiHard = new AiHard(gameHandler);
        this.gameHandler.makeMove(aiHard.calcHardMove());
        break;
    }
  }


  /**
   * This method is called when the client wants to change the difficulty of the AI
   *
   * @author virgil.baclanov, robert.kratz
   */
  @FXML
  public void changeDifficulty() {
    try {
      if (easyDifficultyButton.isSelected()) {
        easyDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0));");
        mediumDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");
        hardDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");

        this.aiType = AiType.EASY;
      } else if (mediumDifficultyButton.isSelected()) {
        mediumDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0));");
        easyDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");
        hardDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");

        this.aiType = AiType.MEDIUM;
      } else if (hardDifficultyButton.isSelected()) {
        hardDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(237, 45, 49, 0.3), rgba(237, 45, 49, 1.0));");
        mediumDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");
        easyDifficultyButton.setStyle(
            "-fx-background-color: linear-gradient(rgba(255,255,255, 0.1), rgba(255,255,255,0.3));");

        this.aiType = AiType.HARD;
      }
    } catch (Exception e) {
      e.printStackTrace();
      errorLabelJoined.setVisible(true);
      errorLabelJoined.setText("Error: " + e.getMessage());
      return;
    }
  }

  /**
   * This method is called when the client wants to give up the game
   *
   * @author robert.kratz
   */
  @FXML
  public void giveUp() {
    try {
      this.gameHandler.giveUp();

      System.out.println("Game given up");

      navigateToJoinScreen();
    } catch (Exception e) {
      errorLabelJoined.setVisible(true);
      errorLabelJoined.setText("Error: " + e.getMessage());
      return;
    }
  }

  private void initUIUpdateTimer() {
    this.updateTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateUI()));
    this.updateTimer.setCycleCount(Timeline.INDEFINITE);
    this.updateTimer.play();
  }

  /**
   * Updates the game board UI
   *
   * @author virgil.baclanov
   */
  private void updateUI() {
    try {
      this.moveTimeAILabel.setText(formatTime(
          this.gameHandler.getCurrentGameSessionResponse().getRemainingMoveTimeInSeconds()) + " ");
      this.gameTimeAILabel.setText(formatTime(
          this.gameHandler.getCurrentGameSessionResponse().getRemainingGameTimeInSeconds()));
    } catch (Exception e) {
      this.errorLabelJoined.setText("Error updating game board");
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the client wants to navigate to the join screen
   *
   * @atuhor virgil.baclanov, robert.kratz
   */
  public void navigateToJoinScreen() {
    try {
      URL url = getClass().getResource("/join_frame.fxml");
      FXMLLoader loader = new FXMLLoader(url);
      Scene scene = new Scene(loader.load());
      this.stage.setScene(scene);

      JoinAIController controller = loader.getController();
      if (controller != null) {
        controller.setStage(this.stage);
      }
    } catch (Exception e) {
      errorLabelJoined.setVisible(true);
      errorLabelJoined.setText("Error: " + e.getMessage());
      e.printStackTrace();
      return;
    }
  }

  /**
   * Updates the player info on the game board
   *
   * @param seconds the time in seconds
   * @return the formatted time
   * @author virgil.baclanov
   */
  private String formatTime(int seconds) {
    if (seconds < 0) {
      return "--:--";
    }
    int minutes = seconds / 60;
    int secs = seconds % 60;
    return String.format("%02d:%02d", minutes, secs);
  }

  /**
   * This method is called when the client is idling in waiting lobby
   *
   * @author robert.kratz
   */
  @Override
  public void onWaiting() {
    System.out.println("Waiting for game to start");
  }

  /**
   * Called when the game is ended
   *
   * @author robert.kratz
   */
  @Override
  public void onGameEnded() {
    Platform.runLater(() -> {
      try {
        navigateToJoinScreen();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Called when the game is started
   *
   * @author robert.kratz
   */
  @Override
  public void onGameStart() {
    System.out.println("Game started");

  }

  /**
   * Called when the game is errored
   *
   * @param message the error message
   * @author robert.kratz
   */
  @Override
  public void onGameError(String message) {
    System.out.println("Game error: " + message);
  }

  /**
   * Called when the opponent's turn is updated
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  @Override
  public void onOpponentTurn(GameState gameState) {
    System.out.println("Opponent's turn");
  }

  /**
   * Called when the game is deleted
   *
   * @author robert.kratz
   */
  @Override
  public void onGameDelete() {
    System.out.println("Game deleted");
  }

  /**
   * Called when the game is updated
   *
   * @param stage The graphics user interface to be displayed
   * @author robert.kratz
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Get the stage
   *
   * @return the stage
   * @author robert.kratz
   */
  public Stage getStage() {
    return this.stage;
  }

  /**
   * Set the game handler
   *
   * @param gameHandler the game handler
   * @author robert.kratz
   */
  public void setGameHandler(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
    this.gameHandler.setGameEvent(this);
  }
}
