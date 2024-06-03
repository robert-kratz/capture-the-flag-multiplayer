package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.server.entities.Map;
import de.cfp1.server.entities.User;
import de.cfp1.server.web.data.UserTransformationRequest;
import java.util.Properties;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author juan.steppacher, benjamin.sander
 */

public class MainMenuController implements Stageable, Initializable {

  private Stage stage;
  private final SceneController sceneController = new SceneController();

  @FXML
  Text usernameLabel;
  int elo;

  /**
   * This method initializes the main menu controller.
   *
   * @param location  the location of the URL
   * @param resources the resources to be used
   * @author benjamin.sander
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      updateUserProfileText(Client.networkHandler.getUser());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method updates the user profile text.
   *
   * @param user the user to update the profile text for
   * @author benjamin.sander
   */
  public synchronized void updateUserProfileText(User user) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          int elo = Client.networkHandler.getStatisticsHandler().getStats(user.getId()).getElo();
          usernameLabel.setText(user.getUsername() + " (" + elo + " Elo)");
        } catch (Exception e) {
          //e.printStackTrace();
        }
      }
    });
  }

  /**
   * This method sets the elo of the user.
   *
   * @param elo the elo of the user
   * @author benjamin.sander
   */
  public void setElo(int elo) {
    this.elo = elo;
  }

  /**
   * This method navigates to the play menu.
   *
   * @author juan.steppacher
   */
  @FXML
  private void navigateToPlayMenu() {
    stage.setScene(GUI.getScenes().get(SceneName.PLAY_MENU).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * This method navigates to the map editor.
   *
   * @author benjamin.sander
   */
  @FXML
  public void navigateToMapEditor() {
    stage.setScene(GUI.getScenes().get(SceneName.MAP_EDITOR).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * This method navigates to game settings.
   *
   * @author juan.steppacher
   */
  @FXML
  private void navigateToSettings() {
    stage.setScene(GUI.getScenes().get(SceneName.SETTINGS).getScene());
    //no audio effect here, relatively big file loading in the next scene
  }

  /**
   * This method navigates to the leaderboard.
   *
   * @author juan.steppacher
   */
  @FXML
  private void navigateToLeaderboard() {
    stage.setScene(GUI.getScenes().get(SceneName.LEADERBOARD).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * This method switches back to the log in frame.
   *
   * @author juan.steppacher
   */
  @FXML
  private void logOut() {
    stage.setScene(GUI.getScenes().get(SceneName.LOGIN).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * Required for the close button
   *
   * @param stage primary stage to set
   * @author benjamin.sander
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
