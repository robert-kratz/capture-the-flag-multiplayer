package de.cfp1.client.game;

import de.cfp1.client.gui.SceneController;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.CurrentGameState;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.exceptions.ForbiddenMove;
import de.cfp1.server.game.exceptions.GameOver;
import de.cfp1.server.game.exceptions.GameSessionNotFound;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Move;
import de.cfp1.server.game.state.Team;
import de.cfp1.server.web.data.GameSessionResponse;
import de.cfp1.server.web.data.GiveupRequest;
import de.cfp1.server.web.data.JoinGameResponse;
import de.cfp1.server.web.data.MoveRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author robert.kratz
 */

public class GameHandler implements Runnable {

  private final NetworkHandler networkHandler;
  private GameEvent gameEvent;
  private GameBoardHelper gameBoardHelper = new GameBoardHelper();
  private GameSessionResponse gameSessionResponse, lastGameSessionResponse;

  private SceneController sceneController = new SceneController();

  private GameState gameState, lastGameState;

  private String teamSecret, teamColor, teamId, joinCode, sessionId;

  private boolean isInLobby = false;
  private boolean moveEventAlreadyFired = false, gameStartedEventAlreadyFired = false, gameEndedEventAlreadyFired = false;

  private String[] originalTeamIds;

  public Thread gameThread = new Thread(this);
  private int clientTeamId;

  /**
   * Constructor for existing user account without gui
   *
   * @param networkHandler the network handler
   * @author robert.kratz, virgil.baclanov
   */
  public GameHandler(NetworkHandler networkHandler) {
    this.networkHandler = networkHandler;
    this.gameEvent = null;

      if (this.networkHandler.getUser() == null) {
          this.networkHandler.signUpAsGuest(); //in case the user is not logged in
      }
  }

  /**
   * Constructor for existing user account without gui
   *
   * @param networkHandler the network handler
   * @param sessionId      the session id of the game
   * @author robert.kratz
   */
  public GameHandler(NetworkHandler networkHandler, String sessionId) {
    this.networkHandler = networkHandler;

    this.sessionId = sessionId;

      if (this.networkHandler.getUser() == null) {
          this.networkHandler.signUpAsGuest(); //in case the user is not logged in
      }

    this.gameState = this.networkHandler.getGameHelper().getGameState(this.getSessionId());
    this.gameSessionResponse = this.networkHandler.getGameHelper()
        .getGameSession(this.getSessionId());

    try {
      this.joinCode = this.networkHandler.getGameHelper()
          .getJoinCodeFromGameSession(this.sessionId);
    } catch (Exception e) {
      this.joinCode = "unknown";
    }
  }

  /**
   * Runs this operation.
   *
   * @author robert.kratz, virgil.baclanov
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(1000);

        this.lastGameState = this.gameState;
        this.lastGameSessionResponse = this.gameSessionResponse;

        this.gameState = this.networkHandler.getGameHelper().getGameState(this.getSessionId());
        this.gameSessionResponse = this.networkHandler.getGameHelper()
            .getGameSession(this.getSessionId());

        this.printCurrentGameBoard();

        //print all teams
        for (int i = 0; i < this.gameState.getTeams().length; i++) {
          if (this.gameState.getTeams()[i] != null) {
            System.out.println("Team " + i + ": " + this.gameState.getTeams()[i].getId());
          } else {
            System.out.println("Team " + i + ": null");
          }
        }

        int clientIndex = this.getTeamIndex(this.teamId);

        if (clientIndex < 0) {
          this.gameThread.interrupt();
            if (this.gameEvent != null) {
                this.gameEvent.onGameEnded();
            }
          continue;
        } else {
          this.clientTeamId = clientIndex;
        }

        if (!isUserAlive()) {
          this.gameThread.interrupt();
            if (this.gameEvent != null) {
                this.gameEvent.onGameEnded();
            }
          continue;
        }

        //TODO: fire events
        if (this.lastGameSessionResponse.getGameStarted() == null
            && this.gameSessionResponse.getGameStarted() != null && !gameStartedEventAlreadyFired) {
            if (this.gameEvent != null) {
                this.gameEvent.onGameStart();
            }
          gameStartedEventAlreadyFired = true;
          continue;
        }

        System.out.println("Current Game State: " + this.getCurrentGameState().toString());

        switch (this.getCurrentGameState()) {
          case LOBBY -> {
              if (this.gameEvent != null) {
                  this.gameEvent.onWaiting(); // fires x times during the lobby to display online players
              }

          }
          case RUNNING -> {
            // Check if the game has ended
            /**if(this.gameSessionResponse.getGameEnded().after(new Date())) {
             if(this.gameEvent != null) this.gameEvent.onGameEnded();
             System.out.println("Game Ended due to rime running thread");
             continue;
             }**/
            if (originalTeamIds == null) {
              originalTeamIds = new String[this.gameState.getTeams().length];
              for (int i = 0; i < this.gameState.getTeams().length; i++) {
                if (this.gameState.getTeams()[i] != null) {
                  originalTeamIds[i] = this.gameState.getTeams()[i].getId();
                }
              }
            }

            if (isClientTurn()) {
                if (this.gameEvent != null) {
                    this.gameEvent.onMyTurn(this.gameState);
                }
              moveEventAlreadyFired = true;
              System.out.println("My Turn");
            } else {
                if (this.gameEvent != null) {
                    this.gameEvent.onOpponentTurn(this.gameState);
                }
              moveEventAlreadyFired = false;
              System.out.println("Opponent Turn");
            }
          }
          case FINISHED -> {
            if (this.gameEvent != null && !gameEndedEventAlreadyFired) {
              this.gameThread.interrupt();
              this.gameEvent.onGameEnded();
              gameEndedEventAlreadyFired = true;
            }
          }
        }

      } catch (ServerTimeoutException | InterruptedException | GameSessionNotFound e) {
        e.printStackTrace();
        System.out.println("Server Timeout");
        this.gameThread.interrupt();
      }
    }
  }

  /**
   * Get the current game state of the game session
   *
   * @return currentGameState the current game state
   * @author robert.kratz
   */
  public CurrentGameState getCurrentGameState() {
    if (this.gameSessionResponse.getGameStarted() == null) {
      return CurrentGameState.LOBBY;
    } else if (this.gameSessionResponse.isGameOver()) {
      return CurrentGameState.FINISHED;
    } else {
      return CurrentGameState.RUNNING;
    }
  }

  /**
   * Create a new game session
   *
   * @param mapTemplate The map template for the game session
   * @return The game session object or null if the game session is not found
   * @throws ServerTimeoutException If the server times out
   * @author robert.kratz
   */
  public GameSessionResponse createNewGame(MapTemplate mapTemplate) throws ServerTimeoutException {
    this.networkHandler.getGameHelper().createGameSession(mapTemplate);

    GameSessionResponse response = this.networkHandler.getGameHelper()
        .createGameSession(mapTemplate);
    this.sessionId = response.getId();

    return response;
  }

  /**
   * Start the game
   *
   * @param move the move to start the game with
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @throws ForbiddenMove          if the move is forbidden
   * @throws GameOver               if the game is over
   * @author robert.kratz
   */
  public void makeMove(Move move)
      throws ServerTimeoutException, GameSessionNotFound, ForbiddenMove, GameOver {
    MoveRequest moveRequest = new MoveRequest();
    moveRequest.setNewPosition(move.getNewPosition());
    moveRequest.setPieceId(move.getPieceId());
    moveRequest.setTeamId(this.teamId);
    System.out.println("Team Secret: " + this.teamSecret);
    moveRequest.setTeamSecret(this.teamSecret);
    this.networkHandler.getGameHelper().makeMove(this.getSessionId(), moveRequest);

    try {
      this.sceneController.playAudio("makeMoveSound.wav");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the game session by the join code
   *
   * @param joinCode The join code of the game session
   * @throws ServerTimeoutException If the server times out
   * @throws GameSessionNotFound    If the game session is not found
   * @author robert.kratz, virgil.baclanov
   */
  public void joinGameByCode(String joinCode, String userId)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      this.isInLobby = true;
      JoinGameResponse joinGameResponse = this.networkHandler.getGameHelper()
          .joinGameByCode(joinCode, userId);

      this.teamSecret = joinGameResponse.getTeamSecret();
      this.teamColor = joinGameResponse.getTeamColor();
      this.teamId = joinGameResponse.getTeamId();
      this.sessionId = joinGameResponse.getGameSessionId();

      //this.gameTimeResponse = this.networkHandler.getGameHelper().getGameTime(this.getSessionId());
      this.gameState = this.networkHandler.getGameHelper().getGameState(this.getSessionId());
      this.gameSessionResponse = this.networkHandler.getGameHelper()
          .getGameSession(this.getSessionId());

      //this.currentGameState = this.gameTimeResponse.getCurrentGameState();

      this.joinCode = joinCode;

      this.startGame();
    } catch (Exception e) {
      this.isInLobby = false;
      throw e;
    }
  }

  /**
   * Get the game session by the session id
   *
   * @param sessionId The join code of the game session
   * @throws ServerTimeoutException If the server times out
   * @throws GameSessionNotFound    If the game session is not found
   * @author robert.kratz
   */
  public void joinGameBySessionId(String sessionId, String userId)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      this.isInLobby = true;
      JoinGameResponse joinGameResponse = this.networkHandler.getGameHelper()
          .joinGameSession(sessionId, userId);

      this.teamSecret = joinGameResponse.getTeamSecret();
      this.teamColor = joinGameResponse.getTeamColor();
      this.teamId = joinGameResponse.getTeamId();
      this.sessionId = joinGameResponse.getGameSessionId();

      this.gameState = this.networkHandler.getGameHelper().getGameState(this.getSessionId());
      this.gameSessionResponse = this.networkHandler.getGameHelper()
          .getGameSession(this.getSessionId());

      try {
        this.joinCode = this.networkHandler.getGameHelper().getJoinCodeFromGameSession(sessionId);
      } catch (Exception e) {
        this.joinCode = "unknown";
      }

      this.startGame();
    } catch (Exception e) {
      this.isInLobby = false;
      throw e;
    }
  }

  /**
   * Give up the game
   *
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @author robert.kratz
   */
  public void giveUp() throws ServerTimeoutException, GameSessionNotFound {
    try {
      this.stopGameThread();

      GiveupRequest giveupRequest = new GiveupRequest();
      giveupRequest.setTeamId(this.teamId);
      giveupRequest.setTeamSecret(this.teamSecret);
      this.networkHandler.getGameHelper().giveUp(this.getSessionId(), giveupRequest);

        if (this.gameEvent != null) {
            this.gameEvent.onGameDelete();
        }
    } catch (Exception e) {
      if (!isInLobby) {
        this.isInLobby = false;
      }
      throw e;
    }
  }

  /**
   * Get the game session by the session id
   *
   * @return the game session
   * @author robert.kratz
   */
  public boolean isUserAlive() {
    return this.gameState.getTeams()[this.clientTeamId] != null;
  }

  /**
   * Set the board theme of the game session
   *
   * @param boardTheme the board theme
   * @author robert.kratz
   */
  public void setBoardTheme(BoardTheme boardTheme) {
    this.networkHandler.getGameHelper().setBoardTheme(this.getSessionId(), boardTheme);
  }

  /**
   * Get the game session by the session id
   *
   * @return the board theme
   * @author robert.kratz
   */
  public BoardTheme getBoardTheme() {
    try {
      return this.networkHandler.getGameHelper().getBoardTheme(this.getSessionId()).getBoardTheme();
    } catch (Exception e) {
      return BoardTheme.CLASSIC;
    }
  }

  /**
   * Get the game session by the session id
   *
   * @return if it is my turn
   * @author robert.kratz
   */
  public boolean isClientTurn() {
    return this.gameState.getCurrentTeam() == this.clientTeamId;
  }

  /**
   * Get the user by id
   *
   * @return the user
   * @author robert.kratz
   */
  public int getOnlineTeams() {
    List<String> winners = new ArrayList<>();
    for (Team team : this.gameState.getTeams()) {
      if (team != null) {
        winners.add(team.getId());
      }
    }
    return winners.size();
  }

  /**
   * Delete the game session
   *
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @author robert.kratz
   */
  public void deleteGameSession() throws ServerTimeoutException, GameSessionNotFound {
    this.networkHandler.getGameHelper().deleteGameSession(this.getSessionId());
  }

  /**
   * Get the current game session response
   *
   * @return the game session response
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @author robert.kratz
   */
  public GameSessionResponse getCurrentGameSessionResponse()
      throws ServerTimeoutException, GameSessionNotFound {
    return this.gameSessionResponse;
  }

  /**
   * Get the winner of the game session at the end of the game
   *
   * @return the team ids of the winning teams
   * @author robert.kratz
   */
  public String[] getWinnerTeamIds() {
    List<String> winners = new ArrayList<>();

    String[] originalTeamIds = this.getOriginalTeamIds();

    for (int i = 0; i < this.gameState.getTeams().length; i++) {
      if (this.gameState.getTeams()[i] != null) {
        winners.add(this.gameState.getTeams()[i].getId());
      }
    }

    String[] winnerTeamIds = new String[winners.size()];

    return winners.toArray(winnerTeamIds);
  }

  /**
   * Get the winner of the game session at the end of the game
   *
   * @return the team ids of the losing teams
   * @author robert.kratz
   */
  public String[] getLoserTeamIds() {
    List<String> losers = new ArrayList<>();

    String[] originalTeamIds = this.getOriginalTeamIds();

    for (int i = 0; i < this.gameState.getTeams().length; i++) {
      if (this.gameState.getTeams()[i] == null) {
        losers.add(originalTeamIds[i]);
      }
    }

    String[] loserTeamIds = new String[losers.size()];

    return losers.toArray(loserTeamIds);
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   */
  public String[] getOriginalTeamIds() {
    return originalTeamIds;
  }

  /**
   * Get the current game state of the game session
   *
   * @author robert.kratz
   */
  public void startGame() {
    this.gameThread.start();
  }

  /**
   * Get the current game state of the game session
   *
   * @author robert.kratz
   */
  public String getSessionId() {
    return sessionId;
  }

  /**
   * Get the current game state of the game session
   *
   * @author robert.kratz
   */
  public void stopGameThread() {
    this.gameThread.interrupt();
  }

  /**
   * Get the current join code of the game session
   *
   * @return the join code of the game session
   * @author robert.kratz
   */
  public String getCurrentJoinCode() {
    return this.joinCode;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public String getTeamId() {
    return teamId;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public Team getMyTeam() {
    return this.gameState.getTeams()[this.clientTeamId];
  }

  /**
   * Get the user by id
   *
   * @param teamId the team id
   * @return the user
   * @author robert.kratz
   */
  public int getTeamIndex(String teamId) {
    for (int i = 0; i < this.gameState.getTeams().length; i++) {
      if (this.gameState.getTeams()[i] != null && this.gameState.getTeams()[i].getId()
          .equals(teamId)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Print the current game board of the game session
   *
   * @author robert.kratz
   */
  private void printCurrentGameBoard() {
    String[][] gameBoard = new String[this.gameState.getGrid().length][this.gameState.getGrid()[0].length];

    for (Team team : this.gameState.getTeams()) {
      if (team != null) {
        for (int i = 0; i < team.getPieces().length; i++) {
          if (team.getPieces()[i] != null) {
            int x = team.getPieces()[i].getPosition()[0];
            int y = team.getPieces()[i].getPosition()[1];

            if (x >= 0 && x < gameBoard.length && y >= 0 && y < gameBoard[x].length) {
              gameBoard[x][y] = team.getPieces()[i].getId();
            }
          }
        }
      }
    }

    for (int i = 0; i < gameBoard.length; i++) {
      for (int j = 0; j < gameBoard[i].length; j++) {
        if (gameBoard[i][j] == null) {
          System.out.print(" ");
        } else {
          System.out.print(gameBoard[i][j] + " ");
        }
      }
      System.out.println();
    }
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public NetworkHandler getNetworkHandler() {
    return networkHandler;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public GameEvent getGameEvent() {
    return gameEvent;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public GameSessionResponse getGameSessionResponse() {
    return gameSessionResponse;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public Thread getGameThread() {
    return gameThread;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public String getTeamColor() {
    return teamColor;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public String getTeamSecret() {
    return teamSecret;
  }

  /**
   * Set the current game state of the game session
   *
   * @param gameEvent the game event
   * @author robert.kratz
   */
  public void setGameEvent(GameEvent gameEvent) {
    this.gameEvent = gameEvent;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public GameState getGameState() {
    return gameState;
  }

  /**
   * Get the current game state of the game session
   *
   * @param sessionId the session id
   * @author robert.kratz
   */
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public GameBoardHelper getGameBoardHelper() {
    return gameBoardHelper;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public GameSessionResponse getLastGameSessionResponse() {
    return lastGameSessionResponse;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public GameState getLastGameState() {
    return lastGameState;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public int getClientTeamId() {
    return clientTeamId;
  }

  /**
   * Get the current game state of the game session
   *
   * @return the game state of the game session
   * @author robert.kratz
   */
  public String getJoinCode() {
    return joinCode;
  }
}
