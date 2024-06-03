package de.cfp1.client.gui;

import javafx.fxml.Initializable;
import javax.sound.sampled.*;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author virgil.baclanov
 */

public class SceneController implements Stageable, Initializable {

  private Stage stage;

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

  /**
   * Required for the close button
   *
   * @param audioName audioName
   * @author virgil.baclanov
   */
  public void playAudio(String audioName) {
    try {
      URL url = getClass().getResource("/soundFX/" + audioName);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(
          Objects.requireNonNull(url).openStream());
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Required for the close button
   *
   * @param audioName audioName
   * @author virgil.baclanov
   */
  public void stopAudio(String audioName) {
    try {
      URL url = getClass().getResource("/soundFX/" + audioName);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(
          Objects.requireNonNull(url).openStream());
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.stop();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Required for the close button
   *
   * @param location  location
   * @param resources resources
   * @author virgil.baclanov
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
}