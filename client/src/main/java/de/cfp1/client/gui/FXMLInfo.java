package de.cfp1.client.gui;

import de.cfp1.client.game.GameHandler;
import de.cfp1.server.entities.Map;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.entities.User;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * @author virgil.baclanov, benjamin.sander
 */

public class FXMLInfo {

  private String resourceName;
  private SceneName sceneName;
  private Stage stage;
  private Scene scene;

  /**
   * Construct an FxmlInfo object
   *
   * @param resourceName the resource name for this FXML
   * @param sceneName    the {@link SceneName} for this FXML
   * @param stage        the primary stage that the scene will be set to
   * @author virgil.baclanov
   */
  public FXMLInfo(String resourceName, SceneName sceneName, Stage stage) {
    this.resourceName = resourceName;
    this.sceneName = sceneName;
    this.stage = stage;
  }

  /**
   * @return the resource name for this FXML file
   * @author virgil.baclanov
   */
  public String getResourceName() {
    return resourceName;
  }

  /**
   * Getter Method for the {@link SceneName} for this FXML
   *
   * @return the {@link SceneName} for this FXML
   * @author virgil.baclanov
   */
  public SceneName getSceneName() {
    return sceneName;
  }

  /**
   * Setter Method for the Scene
   *
   * @param scene the scene
   * @author virgil.baclanov
   */
  public void setScene(Scene scene) {
    this.scene = scene;
  }

  /**
   * Builds the scene iff {@code scene} is {@code null}. Then it returns the scene to the caller.
   *
   * @return the scene
   * @author virgil.baclanov
   */
  public Scene getScene() {
    // Everytime reload the controller
    return new FXMLLoad().load(this);
  }

  /**
   * Builds the scene iff {@code scene} is {@code null}. Then it returns the scene to the caller.
   *
   * @param gameHandler the game handler
   * @return the scene
   * @author virgil.baclanov
   */
  public Scene getScene(GameHandler gameHandler) {
    // Everytime reload the controller
    return new FXMLLoad().load(this, gameHandler);
  }

  /**
   * Builds the scene iff {@code scene} is {@code null}. Then it returns the scene to the caller.
   *
   * @param map the map
   * @return the scene
   * @author virgil.baclanov
   */
  public Scene getScene(Map map) {
    return new FXMLLoad().load(this, map);
  }

  /**
   * Builds the scene iff {@code scene} is {@code null}. Then it returns the scene to the caller.
   *
   * @param gameHandler        the game handler
   * @param allPiecesBothTeams the pieces of both teams
   * @param boardTheme         the board theme
   * @return the scene
   * @author virgil.baclanov
   */
  public Scene getScene(GameHandler gameHandler, HashMap<String, Piece> allPiecesBothTeams,
      BoardTheme boardTheme) {
    return new FXMLLoad().load(this, gameHandler, allPiecesBothTeams, boardTheme);
  }

  /**
   * Builds the scene iff {@code scene} is {@code null}.  Then it returns the scene to the caller.
   *
   * @return the scene
   * @author benjamin.sander
   */
  public Scene getScene(User user) {
    // Everytime reload the controller
    return new FXMLLoad().load(this, user);
  }

  /**
   * @return {@code true} if the scene has been built, otherwise {@code false}
   */
  public boolean hasScene() {
    return scene != null;
  }

  /**
   * Setter Method for the Stage
   *
   * @param stage The top-level JavaFX container
   * @author virgil.baclanov
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Getter Method for the Stage, which is the top-level JavaFX container.
   *
   * @author virgil.baclanov
   */
  public Stage getStage() {
    return this.stage;
  }
}
