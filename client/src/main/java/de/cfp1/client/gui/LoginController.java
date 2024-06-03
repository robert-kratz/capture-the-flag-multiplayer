package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.server.entities.User;
import de.cfp1.server.exception.InvalidCredentialsException;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.exception.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The role of the class SceneController is to control the behavior of the scenes under certain
 * events and conditions. It is used to switch between the different scenes of the game and to apply
 * certain visual effects. The class implements the Stageable interface, which is used to set the
 * stage for each individual scene. In the fxml files, the methods of this class are called when
 * certain buttons are clicked.
 *
 * @author virgil.baclanov, juan.steppacher, benjamin.sander
 */
public class LoginController implements Stageable, Initializable {

  private Stage stage;
  private final SceneController sceneController = new SceneController();

  @FXML
  TextField userLogInField;
  @FXML
  PasswordField passwordLogInField;
  @FXML
  Button logInButton;
  @FXML
  Label passExceptionLabel, userExceptionLabel;

  /**
   * This method is called when the user clicks the "Log In" button.
   *
   * @throws Exception if login fails
   * @author juan.steppacher
   */
  @FXML
  private void logInAsUser() throws Exception {
    try {
      this.sceneController.playAudio("selectMenuSound.wav");

      Client.networkHandler.login(userLogInField.getText(), passwordLogInField.getText());

      stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());

      this.sceneController.playAudio("mainThemeSong.wav");
    } catch (InvalidCredentialsException e) {
      clearAllErrorMessages();
      passExceptionLabel.setText("Invalid credentials");
      passExceptionLabel.setVisible(true);
    } catch (ServerTimeoutException e) {
      clearAllErrorMessages();
      userExceptionLabel.setText("Server timeout");
      userExceptionLabel.setVisible(true);
    } catch (Exception e) {
      clearAllErrorMessages();
      userExceptionLabel.setText("Server error");
      userExceptionLabel.setVisible(true);
    }
  }


  /**
   * This method is called when the user clicks the "Sign Up" button. It switches to the signup
   * frame.
   *
   * @author virgil.baclanov
   */
  @FXML
  public void navigateToSignUp() {
    try {
      stage.setScene(GUI.getScenes().get(SceneName.SIGNUP).getScene());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks the "Sign Up" button. It switches to the signup
   * frame.
   *
   * @author benjamin.sander
   */
  @FXML
  private void signUpAsGuest() {
    try {
      this.sceneController.playAudio("selectMenuSound.wav");
      if (Client.networkHandler.signUpAsGuest()) {
        MainMenuController mainMenuController = new MainMenuController();
        User user = Client.networkHandler.getUserFromServer();
        mainMenuController.updateUserProfileText(user);
        stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene(user));

        this.sceneController.playAudio("mainThemeSong.wav");
      }
    } catch (ServerTimeoutException e) {
      clearAllErrorMessages();
      userExceptionLabel.setText("Server timeout");
      userExceptionLabel.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
      clearAllErrorMessages();
      userExceptionLabel.setText("Server error");
    }
  }

  /**
   * This method is called when the user clicks the "Sign Up" button.
   *
   * @author robert.kratz
   */
  private void clearAllErrorMessages() {
    userExceptionLabel.setVisible(false);
    passExceptionLabel.setVisible(false);

    userExceptionLabel.setText("");
    passExceptionLabel.setText("");
  }

  /**
   * Required for the close button
   *
   * @param stage primary stage to set
   * @author virgil.baclanov
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
}
