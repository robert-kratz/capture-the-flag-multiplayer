package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.client.game.GameEvent;
import de.cfp1.client.game.GameHandler;
import de.cfp1.server.exception.UserNotFoundException;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Team;
import java.util.HashMap;
import java.util.Map;
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
 * @author robert.kratz, virgil.baclanov
 */

public class GameBoardController implements Stageable, Initializable, GameEvent {

  private Stage stage;
  private GameBoard gameBoard;
  private GameHandler gameHandler;

  private BoardTheme boardTheme = BoardTheme.CLASSIC;

  private SceneController sceneController = new SceneController();

  private Timeline updateTimer;

  @FXML
  private Text userLabel1, userLabel2, userLabel3, userLabel4;

  private Map<String, GamePlayerLabelInfo> playerLabelInfoMap = new HashMap<>();

  @FXML
  private StackPane gameBoardContainer;
  @FXML
  private Label moveTimeLabel, gameTimeLabel;
  @FXML
  private Label gameBoardExceptionLabel;

  private boolean alreadyTriggeredMyTurnRepaint = false, alreadyTriggeredOpponentTurnRepaint = false, alreadyTriggeredGameEndRepaint = false;

  /**
   * Shows the game board entry point from the game handler
   *
   * @author robert.kratz
   */
  public void showGameBoard() {
    Platform.runLater(() -> {
      this.gameBoardContainer.getChildren().remove(gameBoard);
      this.boardTheme = this.gameHandler.getBoardTheme();
      this.gameBoard = new GameBoard(this.gameHandler, this.boardTheme, false);
      this.gameBoardContainer.getChildren().add(gameBoard);

      this.gameHandler.setGameEvent(this);

      Text[] userLabels = {userLabel1, userLabel2, userLabel3, userLabel4};

      for (Text label : userLabels) {
        label.setVisible(false);
      }

      this.initPlayerLabelInfo(); //update player names
      this.initUIUpdateTimer();
    });
  }

  /**
   * Called when the game is ended
   *
   * @author robert.kratz
   */
  @Override
  public void onGameEnded() {
    System.out.println("onGameEnded");

      if (this.alreadyTriggeredGameEndRepaint) {
          return;
      }

    Platform.runLater(() -> {
      this.gameBoardContainer.getChildren().remove(gameBoard);
      this.gameBoard = new GameBoard(this.gameHandler, this.boardTheme, false);
      this.gameBoardContainer.getChildren().add(gameBoard);
      this.alreadyTriggeredGameEndRepaint = true;
    });

    this.gameHandler.gameThread.interrupt(); //stop the game thread

    //String[] winners = this.gameHandler.getWinnerTeamIds(), losers = this.gameHandler.getLoserTeamIds();

    //navigate to end game screen
    Platform.runLater(() -> {
      stage.setScene(GUI.getScenes().get(SceneName.END_GAME).getScene(this.gameHandler));
    });
  }

  /**
   * Called when the game is started
   *
   * @author robert.kratz
   */
  @Override
  public void onGameStart() {
    initUIUpdateTimer();
  }

  /**
   * Called when the game is errored
   *
   * @param message the error message
   * @author robert.kratz
   */
  @Override
  public void onGameError(String message) {
    this.gameBoardExceptionLabel.setText(message);
    this.gameBoardExceptionLabel.setVisible(true);
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
      System.out.println("onMyTurn");

      boolean gameBoardHasUpdated = !Objects.equals(this.gameHandler.getGameBoardHelper()
              .gridToString(this.gameHandler.getLastGameState().getGrid()),
          this.gameHandler.getGameBoardHelper().gridToString(gameState.getGrid()));

      //play sound if remaining move time is less than 3 seconds
        if (this.gameHandler.getGameSessionResponse().getRemainingMoveTimeInSeconds() <= 3) {
            this.sceneController.playAudio("selectMenuSound.wav");
        }

      if (this.alreadyTriggeredMyTurnRepaint && !gameBoardHasUpdated) {
        return;
      }

      Platform.runLater(() -> {
        this.gameBoardContainer.getChildren().remove(gameBoard);
        this.gameBoard = new GameBoard(this.gameHandler, this.boardTheme, true);
        this.gameBoardContainer.getChildren().add(gameBoard);
        this.alreadyTriggeredMyTurnRepaint = true;

        this.gameBoardExceptionLabel.setVisible(true);
        this.gameBoardExceptionLabel.setText("Your Turn");
      });
    } catch (Exception e) {
      this.gameBoardExceptionLabel.setText("Error updating game board");
      e.printStackTrace();
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
    System.out.println("onOpponentTurn");
    this.alreadyTriggeredMyTurnRepaint = false;

    Platform.runLater(() -> {
      this.gameBoardContainer.getChildren().remove(gameBoard);
      this.gameBoard = new GameBoard(this.gameHandler, this.boardTheme, true);
      this.gameBoardContainer.getChildren().add(gameBoard);

      this.gameBoardExceptionLabel.setVisible(false);
    });
  }

  /**
   * Called when the game is updated
   *
   * @author virgil.baclanov
   */
  private void initUIUpdateTimer() {
    this.updateTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateUI()));
    this.updateTimer.setCycleCount(Timeline.INDEFINITE);
    this.updateTimer.play();
  }

  /**
   * Updates the UI on the game board
   *
   * @author robert.kratz, virgil.baclanov
   */
  private void updateUI() {
    try {
      this.updateUserLabels();

      this.moveTimeLabel.setText(formatTime(
          this.gameHandler.getCurrentGameSessionResponse().getRemainingMoveTimeInSeconds()) + " ");
      this.gameTimeLabel.setText(formatTime(
          this.gameHandler.getCurrentGameSessionResponse().getRemainingGameTimeInSeconds()));
    } catch (Exception e) {
      this.gameBoardExceptionLabel.setText("Error updating game board");
      e.printStackTrace();
    }
  }

  /**
   * Updates the user labels on the game board
   *
   * @author robert.kratz
   */
  public void updateUserLabels() {
    int currentTeam = this.getGameHandler().getGameState().getCurrentTeam();

    Team[] team = this.getGameHandler().getGameState().getTeams();

    //print team ids
    System.out.println("GameBoardController: Team IDs");
    for (int i = 0; i < this.gameHandler.getGameState().getTeams().length; i++) {
      if (this.gameHandler.getGameState().getTeams()[i] != null) {
        System.out.println(
            "Team " + i + ": " + this.gameHandler.getGameState().getTeams()[i].getId());
      } else {
        System.out.println("Team " + i + ": null");
      }
    }

    System.out.println("Print all label users:");
    for (GamePlayerLabelInfo playerLabelInfo1 : playerLabelInfoMap.values()) {
      System.out.println(playerLabelInfo1.toString());
    }

    //now use the playerLabelInfoMap to update the player labels. if it is players turn by currentTeam == teamIndex, set username label i from playerLabelInfoMap to username (your turn)
    for (GamePlayerLabelInfo playerLabelInfo : playerLabelInfoMap.values()) {
      Text label = switch (playerLabelInfo.getLabelIndex()) {
        case 0 -> userLabel1;
        case 1 -> userLabel2;
        case 2 -> userLabel3;
        case 3 -> userLabel4;
        default -> null;
      };

      Team labelTeam = this.gameHandler.getGameState().getTeams()[playerLabelInfo.getTeamIndex()];

      if (labelTeam == null) {
        label.setText(playerLabelInfo.getPlayerName() + " (Dead)");
        continue;
      }

      int remainingTeamFlagCount = labelTeam.getFlag();

      String remainingFlagsText =
          (remainingTeamFlagCount == 1) ? "" : remainingTeamFlagCount + " Flags";

      if (label != null) {
        if (currentTeam == playerLabelInfo.getTeamIndex()) {
          if (playerLabelInfo.getTeamIndex() == this.gameHandler.getClientTeamId()) {
            //players turn
            label.setText(playerLabelInfo.getPlayerName() + "\n(Your Turn)\n" + remainingFlagsText);
          } else {
            //opponents turn
            label.setText(
                playerLabelInfo.getPlayerName() + "\n(Opponent's Turn)\n" + remainingFlagsText);
          }
        } else {
          if (team[playerLabelInfo.getTeamIndex()] == null) {
            //dead
            label.setText(playerLabelInfo.getPlayerName() + " (Dead)");
          } else {
            if (playerLabelInfo.getTeamIndex() == this.gameHandler.getClientTeamId()) {
              //your team
              label.setText(playerLabelInfo.getPlayerName() + " (You)\n" + remainingFlagsText);
            } else {
              //opponents team
              label.setText(playerLabelInfo.getPlayerName() + "\n" + remainingFlagsText);
            }
          }
        }
      }
    }
  }

  /**
   * Initializes the player label info on the game board
   *
   * @author robert.kratz
   */
  private void initPlayerLabelInfo() {

    int onlineTeams = this.gameHandler.getOnlineTeams();

    //Player positions for 2, 3 and 4 players
    int[] twoPlayerIndices = {0, 3};
    int[] threePlayerIndices = {0, 2, 3};
    int[] fourPlayerIndices = {0, 2, 1, 3};

    Team[] team = this.getGameHandler().getGameState().getTeams();
    for (int i = 0; i < team.length; i++) {
      if (team[i] != null) {

        int labelIndex = switch (onlineTeams) {
          case 2 -> twoPlayerIndices[i];
          case 3 -> threePlayerIndices[i];
          case 4 -> fourPlayerIndices[i];
          default -> 0;
        };

        String teamId = team[i].getId();
        String color = team[i].getColor();
        String username = getUsernameById(team[i].getId());

        playerLabelInfoMap.put(team[i].getId(),
            new GamePlayerLabelInfo(username, teamId, i, labelIndex, color));
      }
    }

    this.updatePlayerLabelInfo();
  }

  /**
   * Updates the player label info on the game board
   *
   * @author robert.kratz
   */
  private void updatePlayerLabelInfo() {
    for (GamePlayerLabelInfo playerLabelInfo : playerLabelInfoMap.values()) {
      Text label = switch (playerLabelInfo.getLabelIndex()) {
        case 0 -> userLabel1;
        case 1 -> userLabel2;
        case 2 -> userLabel3;
        case 3 -> userLabel4;
        default -> null;
      };

      try {
        if (label != null) {
          label.setStyle("-fx-fill: " + playerLabelInfo.getColor() + ";");
          label.setText(playerLabelInfo.getPlayerName());
          label.setVisible(true);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Gets the username by id
   *
   * @param id the id
   * @return the username
   * @author robert.kratz
   */
  private String getUsernameById(String id) {
    try {
      return Client.networkHandler.getUserById(id).getUsername();
    } catch (UserNotFoundException e) {
      e.printStackTrace();
      return "Unknown User";
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
   * Sets the graphics user interface to be displayed
   *
   * @param stage The graphics user interface to be displayed
   * @author virgil.baclanov
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
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
      this.gameHandler.giveUp();
      this.updateTimer.stop();

      System.out.println("GiveUp");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the client is idling in waiting lobby NOTE: This method is not used
   * in the current implementation
   *
   * @author robert.kratz
   */
  @Override
  public void onWaiting() {
  }

  /**
   * Called when the game is deleted NOTE: This method is not used in the current implementation
   *
   * @author robert.kratz
   */
  @Override
  public void onGameDelete() {
  }

  /**
   * Initializes the game board controller
   *
   * @param location  the location
   * @param resources the resources
   * @author virgil.baclanov
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  /**
   * Gets the initial game state
   *
   * @return the initial game state
   * @author virgil.baclanov
   */
  public GameState getInitialGameState() {
    return this.gameHandler.getGameState();
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

  /**
   * Sets the board theme
   *
   * @param boardTheme The board theme
   * @author virgil.baclanov
   */
  public void setBoardTheme(BoardTheme boardTheme) {
    this.gameHandler.setBoardTheme(boardTheme);
  }
}
