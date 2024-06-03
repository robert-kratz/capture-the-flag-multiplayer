package de.cfp1.server.game;

/**
 * @author robert.kratz
 */

public class GameProvider {

  /**
   * Create a new game engine
   *
   * @return a new game engine
   * @author robert.kratz
   */
  public static Game createNewGameEngin() {
    return new CtfGame();
  }
}
