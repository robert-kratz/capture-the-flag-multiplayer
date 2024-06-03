package de.cfp1.server.game;

import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Move;
import de.cfp1.server.game.state.Team;
import de.cfp1.server.game.exceptions.GameOver;
import de.cfp1.server.game.exceptions.InvalidMove;
import de.cfp1.server.game.exceptions.NoMoreTeamSlots;

import java.util.Date;

/**
 * The interface that your game engine has to implement.
 * <p>
 * The game engine is supposed to implement logic (rules) that cover all fields in
 * {@link MapTemplate} (e.g., time limitations etc.).
 * <p>
 * Important: Modifications to this interface and its protocol are not allowed.
 */
public interface Game {

  /**
   * Initializes a new game based on given {@link MapTemplate} configuration.
   *
   * @param template {@link MapTemplate}
   * @return GameState
   */
  GameState create(MapTemplate template);

  /**
   * Get current state of the game
   *
   * @return GameState
   */
  GameState getCurrentGameState();

  /**
   * Updates a game and its state based on team join request (add team).
   *
   * <ul>
   *     <li>adds team if a slot is free (array element is null)</li>
   *     <li>if all team slots are finally assigned, implicitly starts the game by picking a starting team at random</li>
   * </ul>
   *
   * @param teamId Team ID
   * @return Team
   * @throws NoMoreTeamSlots No more team slots available
   */
  Team joinGame(String teamId);

  /**
   * @return number of remaining team slots
   */
  int getRemainingTeamSlots();

  /**
   * Make a move
   *
   * @param move {@link Move}
   * @throws InvalidMove Requested move is invalid
   * @throws GameOver    Game is over
   */
  void makeMove(Move move);

  /**
   * @return -1 if no total game time limit set, 0 if over, > 0 if seconds remain
   */
  int getRemainingGameTimeInSeconds();

  /**
   * @return -1 if no move time limit set, 0 if over, > 0 if seconds remain
   */
  int getRemainingMoveTimeInSeconds();

  /**
   * A team has to option to give up a game (i.e., losing the game as a result).
   * <p>
   * Assume that a team can only give up if it is its move (turn).
   *
   * @param teamId Team ID
   */
  void giveUp(String teamId);

  /**
   * Checks whether a move is valid based on the current game state.
   *
   * @param move {@link Move}
   * @return true if move is valid based on current game state, false otherwise
   */
  boolean isValidMove(Move move);

  /**
   * Checks whether the game is started based on the current {@link GameState}.
   *
   * <ul>
   *     <li>{@link Game#isGameOver()} == false</li>
   *     <li>{@link Game#getCurrentGameState()} != null</li>
   * </ul>
   *
   * @return
   */
  boolean isStarted();

  /**
   * Checks whether the game is over based on the current {@link GameState}.
   *
   * @return true if game is over, false if game is still running.
   */
  boolean isGameOver();

  /**
   * Get winner(s) (if any)
   *
   * @return {@link Team#getId()} if there is a winner
   */
  String[] getWinner();

  /**
   * @return Start {@link Date} of game
   */
  Date getStartedDate();

  /**
   * @return End date of game
   */
  Date getEndDate();

  /**
   * Turn time limit in seconds (<= 0 if none)
   *
   * @return
   */
  int getTurnTimeLimit();

}
