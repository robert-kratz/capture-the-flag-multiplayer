package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.client.utils.AiType;
import de.cfp1.server.entities.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author virgil.baclanov, benjamin.sander, robert.kratz
 */

public class PlayMenuController implements Stageable, Initializable {

  private Stage stage;
  private final SceneController sceneController = new SceneController();

  @FXML
  Text usernameLabel;

  /**
   * Setter Method for the GUI
   *
   * @param stage The graphics user interface to be displayed
   * @author virgil.baclanov, benjamin.sander
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }

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
  public void updateUserProfileText(User user) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        int elo = 0;
        //usernameLabel.setText(user.getUsername());
        try {
          elo = Client.networkHandler.getStatisticsHandler().getStats(user.getId()).getElo();

          usernameLabel.setText(user.getUsername() + " (" + elo + " Elo)");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * This method navigates to the AI popup.
   *
   * @throws Exception the exception to be thrown when navigating to the AI popup fails
   * @author robert.kratz
   */
  @FXML
  private void navigateToAiPopUp() throws Exception {
    try {
      Client.processManager.startNewProcess();
      this.sceneController.playAudio("selectMenuSound.wav");
      System.out.println("AI Popup started");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method navigates to the join game frame.
   *
   * @author benjamin.sander
   */
  @FXML
  private void navigateToJoinGame() throws Exception {
    stage.setScene(GUI.getScenes().get(SceneName.JOIN_GAME).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * This method navigates to the create game frame.
   *
   * @author benjamin.sander
   */
  @FXML
  private void navigateToCreateGame() throws Exception {
    stage.setScene(GUI.getScenes().get(SceneName.CREATE_GAME).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * This method navigates to the main menu frame.
   *
   * @author benjamin.sander
   */
  @FXML
  private void navigateToMainMenu() throws Exception {
    stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * This method navigates to the settings frame.
   *
   * @author benjamin.sander
   */
  @FXML
  private void navigateToSettings() throws Exception {
    stage.setScene(GUI.getScenes().get(SceneName.SETTINGS).getScene());
    //no audio played here, relatively big file loading for the next scene
  }

  /**
   * This method switches back to the log in frame.
   *
   * @author benjamin.sander
   */
  @FXML
  private void logOut() throws Exception {
    stage.setScene(GUI.getScenes().get(SceneName.LOGIN).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

}
