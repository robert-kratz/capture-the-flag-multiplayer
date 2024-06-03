package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.server.entities.Map;
import de.cfp1.server.exception.EmailTakenExceptions;
import de.cfp1.server.exception.PasswordInsecureException;
import de.cfp1.server.exception.UsernameTakenException;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author robert.kratz, gabriel.victor.arthur.himmelein, benjamin.sander
 */

public class SignUpController implements Stageable, Initializable {

  private Stage stage;
  private final SceneController sceneController = new SceneController();

  @FXML
  PasswordField passwordSignUpField, passwordReconfirmSignUpField, password_field_popUp;
  @FXML
  TextField userSignUpField, emailSignUpField;
  @FXML
  Label usernameExceptionLabel, passExceptionLabel, reconfirmPassExceptionLabel;

  /**
   * Initializes the signup screen
   *
   * @param location  location
   * @param resources resources
   * @author gabriel.victor.arthur.himmelein, robert.kratz
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      System.out.println("SignUpController initialized");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Required for the signup button
   *
   * @throws Exception if sign up fails
   * @author benjamin.sander, gabriel.victor.arthur.himmelein
   */
  @FXML
  private void signUp() throws Exception {
    try {
      this.sceneController.playAudio("selectMenuSound.wav");
      //clear all when pass not same
      if (passwordSignUpField.getText().equals(passwordReconfirmSignUpField.getText())) {
        if (Client.networkHandler.signup(userSignUpField.getText(),
            "test" + userSignUpField.getText() + "-" + System.currentTimeMillis() + "@example.com",
            passwordSignUpField.getText())) {
          stage.setScene(GUI.getScenes().get(SceneName.LOGIN).getScene());
        }
      } else {
        clearAllErrorMessages();
        reconfirmPassExceptionLabel.setText("Passwords do not match");
        reconfirmPassExceptionLabel.setVisible(true);
        System.out.println("Passwörter stimmen nicht überein");
      }
    } catch (PasswordInsecureException e) {
      clearAllErrorMessages();
      passExceptionLabel.setText(
          "Password too weak, must contain at least 8 characters, a-z, A-Z, 0-9 and special characters");
      passExceptionLabel.setVisible(true);
      System.out.println("Passwort zu unsicher");
    } catch (EmailTakenExceptions e) {
      clearAllErrorMessages();
      //TODO this will never be thrown because all emails are test@example.com
    } catch (UsernameTakenException e) {
      //todo add error message gui
      passExceptionLabel.setText("Username already taken");
      passExceptionLabel.setVisible(true);
      System.out.println("Username bereits vergeben");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Required for the cancel sign up button
   *
   * @author benjamin.sander
   */
  @FXML
  private void cancelSignUp() throws Exception {
    stage.setScene(GUI.getScenes().get(SceneName.LOGIN).getScene());
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  /**
   * Required for clearing all error messages
   *
   * @author robert.kratz
   */
  private void clearAllErrorMessages() {
    this.usernameExceptionLabel.setVisible(false);
    this.passExceptionLabel.setVisible(false);
    this.reconfirmPassExceptionLabel.setVisible(false);

    this.usernameExceptionLabel.setText("");
    this.passExceptionLabel.setText("");
    this.reconfirmPassExceptionLabel.setText("");
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
}
