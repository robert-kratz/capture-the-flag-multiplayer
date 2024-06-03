package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.client.game.GameEvent;
import de.cfp1.client.game.GameHandler;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.server.entities.Map;;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.exception.UserNotFoundException;
import de.cfp1.server.game.exceptions.GameSessionNotFound;
import de.cfp1.server.game.map.*;
import de.cfp1.server.game.state.*;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

import java.util.HashMap;

/**
 * @author juan.steppacher
 */

public class LoadingController implements Stageable, Initializable {

  private Stage stage;

  @FXML
  PasswordField password_field_popUp;
  @FXML
  Button okButtonPopUp, cancelButtonPopUp;

  @FXML
  ImageView waiting_icon;

  /**
   * This method initializes the loading controller.
   *
   * @param location  the location of the URL
   * @param resources the resources to be used
   * @author juan.steppacher
   */
  @Override
  public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
    startRotationAnimation(waiting_icon);
  }

  /**
   * This method creates a rotation animation for the waiting icon in the waiting join game frame.
   *
   * @author juan.steppacher
   */
  private void startRotationAnimation(ImageView imageView) {
    RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3.5), imageView);
    rotateTransition.setByAngle(360);
    rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
    rotateTransition.setInterpolator(Interpolator.LINEAR);
    rotateTransition.play();
  }

  /**
   * This method handles the action of the ok button in the pop up frame.
   *
   * @param event the event that triggered the action
   * @author juan.steppacher
   */
  @FXML
  private void handleOnActionBackButton(ActionEvent event) {
    stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());
  }

  /**
   * This method handles the action of the ok button in the pop up frame.
   *
   * @param event the event that triggered the action
   * @author juan.steppacher
   */
  @FXML
  private void handleOnActionClose(ActionEvent event) {
    stage.close();
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
