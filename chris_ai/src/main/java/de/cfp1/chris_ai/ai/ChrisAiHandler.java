package de.cfp1.chris_ai.ai;

import de.cfp1.client.game.GameEvent;
import de.cfp1.client.game.GameHandler;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Move;
import de.cfp1.server.game.state.Team;
import de.cfp1.server.web.data.GameSessionResponse;

/**
 * @author robert.kratz
 */

public class ChrisAiHandler implements GameEvent {

  private GameHandler gameHandler;

  /**
   * Called when the game is updated
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  @Override
  public void onMyTurn(GameState gameState) {

    GameState currentGameState = this.gameHandler.getGameState();
    GameSessionResponse gameSessionResponse = this.gameHandler.getGameSessionResponse();

    int yourClientIndex = this.gameHandler.getClientTeamId();
    Team team = this.gameHandler.getMyTeam();

    //NOTE AN CHRIS: HIER KOMMT DEIN CODE REIN

    //this.gameHandler.makeMove(new Move()); <-- make move

  }

  /**
   * This method is called when the client is idling in waiting lobby
   *
   * @author robert.kratz
   */
  @Override
  public void onWaiting() {
    System.out.println("Game Event: Waiting");
  }

  /**
   * Called when the game is ended
   *
   * @author robert.kratz
   */
  @Override
  public void onGameEnded() {
    System.out.println("Game Event: Game Ended");
  }

  /**
   * Called when the game is errored
   *
   * @param message the error message
   * @author robert.kratz
   */
  @Override
  public void onGameError(String message) {
    System.out.println("Game Event: Game Error: " + message);
  }

  /**
   * Called when the opponent's turn is updated
   *
   * @param gameState the game state
   * @author robert.kratz
   */
  @Override
  public void onOpponentTurn(GameState gameState) {
    System.out.println("Game Event: Opponent Turn");
  }

  /**
   * Called when the game is deleted
   *
   * @author robert.kratz
   */
  @Override
  public void onGameDelete() {
    System.out.println("Game Event: Game Deleted");
    System.exit(0);
  }

  /**
   * Called when the game is started
   *
   * @author robert.kratz
   */
  @Override
  public void onGameStart() {
    System.out.println("Game Event: Game Started");
  }

  /**
   * Called when the game is updated
   *
   * @param gameHandler the game handler
   * @author robert.kratz
   */
  public void setGameHandler(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
  }
}
