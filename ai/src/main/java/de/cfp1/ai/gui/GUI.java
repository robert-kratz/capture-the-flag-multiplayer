package de.cfp1.ai.gui;

import de.cfp1.client.gui.Stageable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author robert.kratz, virgil.baclanov
 */

public class GUI extends Application {

  /**
   * This method is called when the client wants to navigate to the join screen
   *
   * @param stage the stage
   * @throws Exception if the stage cannot be created
   * @author robert.kratz, virgil.baclanov
   */
  @Override
  public void start(Stage stage) throws Exception {
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CtF AI Client");
    System.setProperty("apple.awt.application.name", "CtF AI Client");
    System.setProperty("com.apple.macos.use-file-dialog-packages", "true");
    System.setProperty("com.apple.macos.useScreenMenuBar", "true");
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("apple.awt.application.appearance", "system");

    // macOS dock icon
    System.setProperty("com.apple.smallIcon", "/icons/main_logo_icon.png");

    URL url = getClass().getResource("/join_frame.fxml");
    FXMLLoader loader = new FXMLLoader(url);
    Scene scene = new Scene(loader.load());

    Stageable controller = loader.getController();
    controller.setStage(stage);

    stage.setScene(scene);
    stage.setMinWidth(800);
    stage.setMinHeight(600);
    stage.setTitle("AI Client");
    stage.show();
  }
}
