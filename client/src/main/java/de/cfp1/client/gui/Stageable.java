package de.cfp1.client.gui;

import javafx.stage.Stage;

/**
 * Interface for each scene's controller
 *
 * @author virgil.baclanov
 */

public interface Stageable {

  /**
   * Setter Method for the GUI
   *
   * @param stage The graphics user interface to be displayed
   * @author virgil.baclanov
   */
  void setStage(Stage stage);
}
