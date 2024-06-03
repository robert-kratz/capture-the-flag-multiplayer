package de.cfp1.client.gui;

import de.cfp1.server.entities.Map;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for the settings view. It is responsible for displaying the settings
 * (meme).
 *
 * @author benjamin.sander, juan.steppacher
 */

public class SettingsController implements Stageable, Initializable {

  private Stage stage;

  @FXML
  MediaView mediaView;
  @FXML
  MediaPlayer mediaPlayer;

  /**
   * This method prepares the video to be played in the settings scene.
   *
   * @author benjamin.sander
   */
  public void prepareVideo() {
    try {
      String videoFile = getClass().getResource("/videos/lecker_bierchen_meme.mp4")
          .toExternalForm();

      this.mediaPlayer = new MediaPlayer(new Media(videoFile));
      this.mediaView.setMediaPlayer(mediaPlayer);

      this.mediaPlayer.setOnReady(() -> {
        mediaView.fitWidthProperty().bind(this.stage.widthProperty());
        mediaView.fitHeightProperty().bind(this.stage.heightProperty());
      });
      this.mediaView.setPreserveRatio(true);
      this.mediaView.setSmooth(true);

      this.mediaPlayer.play();
      // play on a loop when remaining in the settings scene
      this.mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(javafx.util.Duration.ZERO));
      //close video when exiting the scene
      this.mediaView.getMediaPlayer().setOnEndOfMedia(() -> {
        this.mediaView.getMediaPlayer().stop();
      });

    } catch (MediaException e) {
      System.out.println(e);
    }
  }

  /**
   * This method navigates to game settings.
   *
   * @author benjamin.sander
   */
  @FXML
  private void navigateToMainMenu() throws Exception {
    if (this.mediaPlayer != null) {
      this.mediaPlayer.stop();
    }
    stage.setScene(GUI.getScenes().get(SceneName.MAIN_MENU).getScene());
  }

  /**
   * Required for the close button
   *
   * @param stage primary stage to set
   * @author benjamin.sander
   */
  @Override
  public void setStage(Stage stage) {
    System.out.println("SettingsController setStage");
    prepareVideo();
    this.stage = stage;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
}
