package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.server.web.data.LeaderboardResponse;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import de.cfp1.server.web.data.StatsResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

/**
 * Controller for the leaderboard scene
 *
 * @author juan.steppacher
 */

public class LeaderboardController implements Stageable, Initializable {

  private Stage stage;
  private final SceneController sceneController = new SceneController();

  /**
   * Cell factory defines the graphic style of the leaderboard.
   *
   * @author juan.steppacher
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    listView.setCellFactory(lv -> new ListCell<String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item);
          setStyle("-fx-font-family: 'Eras Light ITC'; -fx-font-size: 14; -fx-alignment: center;");
        }
      }
    });
    populateLeaderboard();
    listView.setStyle(
        "-fx-background-color: transparent; -fx-control-inner-background: transparent;");
  }

  @FXML
  private ListView<String> listView;

  /**
   * Method to populate the leaderboard, works with the Client class and collects the data for each
   * user through the specified methods.
   *
   * @author juan.steppacher
   */
  private void populateLeaderboard() {
    try {
      ObservableList<String> leaderboardData = FXCollections.observableArrayList();
      List<LeaderboardResponse> leaderboardEntries = Client.networkHandler.getStatisticsHandler()
          .getLeaderboard();

      int i = 1;
      for (LeaderboardResponse entry : leaderboardEntries) {
        StatsResponse stats = Client.networkHandler.getStatisticsHandler()
            .getStats(entry.getUserId());
        if (stats.getWins() > 0 || stats.getLosses() > 0 || stats.getDraws() > 0) {
          String displayText = "#" + i + " " + entry.getUsername() + " - ELO: " + stats.getElo();

          boolean isMe = entry.getUserId().equals(Client.networkHandler.getUser().getId());

          displayText += String.format(
              " - Wins: %d, Losses: %d, Draws: %d, Elo %d" + (isMe ? " (You)" : ""),
              stats.getWins(), stats.getLosses(), stats.getDraws(), stats.getElo());
          leaderboardData.add(displayText);

          i++;
        }
      }
      listView.setItems(leaderboardData);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to navigate to the main menu scene when the button is clicked.
   *
   * @author juan.steppacher
   */
  @FXML
  private void navigateToMainMenu() throws Exception {
    stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * Required for the close button
   *
   * @param stage The graphics user interface to be displayed
   * @author juan.steppacher
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}