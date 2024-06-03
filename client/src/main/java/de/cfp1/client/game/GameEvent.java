package de.cfp1.client.game;

import de.cfp1.server.game.state.GameState;

/**
 * @author robert.kratz
 */

public interface GameEvent {

  /**
   * This method is called when the client is idling in waiting lobby
   *
   * @author robert.kratz
   */
  void onWaiting();

  /**
   * Called when the game is ended
   *
   * @author robert.kratz
   */
  void onGameEnded();

  /**
   * Called when the game is started
   *
   * @author robert.kratz
   */
  void onGameStart();

  /**
   * Called when the game is errored
   *
   * @param message the error message
   * @author robert.kratz
   */
  void onGameError(String message);

  /**
   * Called when the game is updated
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  void onMyTurn(GameState gameState);

  /**
   * Called when the opponent's turn is updated
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  void onOpponentTurn(GameState gameState);

  /**
   * Called when the game is deleted
   *
   * @author robert.kratz
   */
  void onGameDelete();

}
