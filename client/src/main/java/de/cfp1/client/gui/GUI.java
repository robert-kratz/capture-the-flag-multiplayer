package de.cfp1.client.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * The GUI class is the main class for the client's graphical user interface.
 *
 * @author virgil.baclanov, juan.steppacher, benjamin.sander
 */

public class GUI extends Application {

  private final String LOG_IN_FXML = "logging_frame.fxml";
  private final String MAIN_MENU_FXML = "main_menu.fxml";
  private final String MAP_EDITOR_FXML = "map_editor.fxml";
  private final String SIGN_UP_FXML = "signup_frame.fxml";
  private final String PLAY_MENU_FXML = "play_menu.fxml";

  private final String SETTINGS_FXML = "settings.fxml";
  private final String EDIT_MAP_FXML = "edit_map.fxml";
  private final String CREATE_GAME_FXML = "create_game.fxml";
  private final String WAITING_GAME_FXML = "waiting_join_game.fxml";
  private final String JOIN_GAME_FXML = "join_game_popup.fxml";
  private final String LOADING_FXML = "loading_frame.fxml";
  private final String GAME_CLASSIC_FXML = "boardgame_classic.fxml";
  private final String GAME_ZOMBIE_FXML = "boardgame_zombie.fxml";
  private final String GAME_WEST_FXML = "boardgame_wildwest.fxml";
  private final String END_GAME_FXML = "end_game_screen.fxml";
  private final String LEADERBOARD_FXML = "leaderboard.fxml";

  private static Map<SceneName, FXMLInfo> scenes = new HashMap<>();

  /**
   * Abstract method from the Application class overridden to start the GUI
   *
   * @param stage The top-level JavaFX container that starts with the login frame.
   * @throws Exception If the login scene is not found, an exception is thrown
   * @author virgil.baclanov, juan.steppacher, benjamin.sander
   */
  @Override
  public void start(Stage stage) {

    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Capture the Flag");
    System.setProperty("apple.awt.application.name", "Capture the Flag");
    System.setProperty("com.apple.macos.use-file-dialog-packages", "true");
    System.setProperty("com.apple.macos.useScreenMenuBar", "true");
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("apple.awt.application.appearance", "system");

    // macOS dock icon
    System.setProperty("com.apple.smallIcon", "/icons/main_logo_icon.png");

    scenes.put(SceneName.LOGIN, new FXMLInfo(LOG_IN_FXML, SceneName.LOGIN, stage));
    scenes.put(SceneName.SIGNUP, new FXMLInfo(SIGN_UP_FXML, SceneName.SIGNUP, stage));
    scenes.put(SceneName.MAIN_MENU, new FXMLInfo(MAIN_MENU_FXML, SceneName.MAIN_MENU, stage));
    scenes.put(SceneName.PLAY_MENU, new FXMLInfo(PLAY_MENU_FXML, SceneName.PLAY_MENU, stage));
    scenes.put(SceneName.MAP_EDITOR, new FXMLInfo(MAP_EDITOR_FXML, SceneName.MAP_EDITOR, stage));
    scenes.put(SceneName.EDIT_MAP, new FXMLInfo(EDIT_MAP_FXML, SceneName.EDIT_MAP, stage));
    scenes.put(SceneName.SETTINGS, new FXMLInfo(SETTINGS_FXML, SceneName.SETTINGS, stage));
    scenes.put(SceneName.CREATE_GAME, new FXMLInfo(CREATE_GAME_FXML, SceneName.CREATE_GAME, stage));
    scenes.put(SceneName.WAITING_GAME,
        new FXMLInfo(WAITING_GAME_FXML, SceneName.WAITING_GAME, stage));
    scenes.put(SceneName.JOIN_GAME, new FXMLInfo(JOIN_GAME_FXML, SceneName.JOIN_GAME, stage));
    scenes.put(SceneName.LOADING, new FXMLInfo(LOADING_FXML, SceneName.LOADING, stage));
    scenes.put(SceneName.BOARDGAME_CLASSIC,
        new FXMLInfo(GAME_CLASSIC_FXML, SceneName.BOARDGAME_CLASSIC, stage));
    scenes.put(SceneName.BOARDGAME_ZOMBIE,
        new FXMLInfo(GAME_ZOMBIE_FXML, SceneName.BOARDGAME_ZOMBIE, stage));
    scenes.put(SceneName.BOARDGAME_WEST,
        new FXMLInfo(GAME_WEST_FXML, SceneName.BOARDGAME_WEST, stage));
    scenes.put(SceneName.END_GAME, new FXMLInfo(END_GAME_FXML, SceneName.END_GAME, stage));
    scenes.put(SceneName.LEADERBOARD, new FXMLInfo(LEADERBOARD_FXML, SceneName.LEADERBOARD, stage));

    // getScene() will load the FXML file the first time, the login scene is shown
    stage.setScene(scenes.get(SceneName.LOGIN).getScene());
    stage.setMinWidth(800);
    stage.setMinHeight(600);
    stage.setTitle("CAPTURE THE FLAG");
    stage.show();
  }

  /**
   * @return a Map of the {@link FXMLInfo} by {@link SceneName}
   * @author virgil.baclanov
   */
  public static Map<SceneName, FXMLInfo> getScenes() {
    return scenes;
  }

  /**
   * Update the scene Map with new FxmlInfo
   *
   * @param name     the {@link SceneName} that is the key to update
   * @param fxmlInfo the {@link FXMLInfo} that is the data to update
   * @author virgil.baclanov
   */
  public static void updateScenes(SceneName name, FXMLInfo fxmlInfo) {
    scenes.put(name, fxmlInfo);
  }
}