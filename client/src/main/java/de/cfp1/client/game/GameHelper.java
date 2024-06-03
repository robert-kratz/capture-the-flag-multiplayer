package de.cfp1.client.game;


import com.google.gson.Gson;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.client.net.RequestBuilder;
import de.cfp1.server.entities.User;
import de.cfp1.server.exception.RequestErrorException;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.exceptions.*;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Team;
import de.cfp1.server.web.data.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.kratz
 */

public class GameHelper {

  private final String BASE_URL = NetworkHandler.getBaseUrl();

  /**
   * Create a game session
   *
   * @param mapTemplate the map template
   * @return the game session response
   * @throws ServerTimeoutException if the server is not reachable
   * @author robert.kratz
   */
  public GameSessionResponse createGameSession(MapTemplate mapTemplate)
      throws ServerTimeoutException {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(BASE_URL + "/gamesession");
      requestBuilder.method("POST");

      requestBuilder.payload("{\"template\": " + new Gson().toJson(mapTemplate) + "}");
      requestBuilder.contentTypeJson();

      String response = requestBuilder.send();

      return new Gson().fromJson(response, GameSessionResponse.class);
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Join a game session
   *
   * @param gameSessionId the game session id
   * @return the join game response
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @author robert.kratz
   */
  public JoinGameResponse joinGameSession(String gameSessionId, String userId)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + gameSessionId + "/join");
      requestBuilder.method("POST");

      JoinGameRequest joinGameRequest = new JoinGameRequest();
      joinGameRequest.setTeamId(userId);

      requestBuilder.payload(new Gson().toJson(joinGameRequest));
      requestBuilder.contentTypeJson();

      String response = requestBuilder.send();

      return new Gson().fromJson(response, JoinGameResponse.class);
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      if (e.getResponseCode() == 429) {
        e.printStackTrace();
        throw new NoMoreTeamSlots();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  public JoinGameResponse joinGameByCode(String joinCode, String userId)
      throws ServerTimeoutException, GameSessionNotFound, NoMoreTeamSlots {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + joinCode + "/joincode");
      requestBuilder.method("POST");

      JoinGameRequest joinGameRequest = new JoinGameRequest();
      joinGameRequest.setTeamId(userId);

      requestBuilder.payload(new Gson().toJson(joinGameRequest));
      requestBuilder.contentTypeJson();

      String response = requestBuilder.send();

      return new Gson().fromJson(response, JoinGameResponse.class);
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      if (e.getResponseCode() == 429) {
        throw new NoMoreTeamSlots();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get the join code from a game session
   *
   * @param gameSessionId the game session id
   * @return the join code of the game session
   * @author robert.kratz
   */
  public String getJoinCodeFromGameSession(String gameSessionId)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + gameSessionId + "/joincode");
      requestBuilder.method("GET");

      String response = requestBuilder.send();

      return new Gson().fromJson(response, String.class);
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get the game session by id
   *
   * @param gameSessionId the game session id
   * @return the game session
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @author robert.kratz
   */
  public GameSessionResponse getGameSession(String gameSessionId)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + gameSessionId);
      requestBuilder.method("GET");

      String response = requestBuilder.send();

      return new Gson().fromJson(response, GameSessionResponse.class);
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get the game state of a game session
   *
   * @param gameSessionId the game session id
   * @return the game state
   * @throws ServerTimeoutException if the server is not reachable
   * @author robert.kratz
   */
  public GameState getGameState(String gameSessionId)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + gameSessionId + "/state");
      requestBuilder.method("GET");

      String response = requestBuilder.send();

      return new Gson().fromJson(response, GameState.class);
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Make a move in the game session
   *
   * @param gameSessionId the game session id
   * @param moveRequest   the move request
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @throws ForbiddenMove          if the move is forbidden
   * @throws GameOver               if the game is over
   * @author robert.kratz
   */
  public void makeMove(String gameSessionId, MoveRequest moveRequest)
      throws ServerTimeoutException, GameSessionNotFound, ForbiddenMove, GameOver {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + gameSessionId + "/move");
      requestBuilder.method("POST");

      requestBuilder.payload(new Gson().toJson(moveRequest));
      requestBuilder.contentTypeJson();

      requestBuilder.send();
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      if (e.getResponseCode() == 403) {
        throw new ForbiddenMove();
      }
      if (e.getResponseCode() == 409) {
        throw new InvalidMove();
      }
      if (e.getResponseCode() == 410) {
        throw new GameOver();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Give up the game session
   *
   * @param gameSessionId the game session id
   * @param giveupRequest the giveup request
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @author robert.kratz
   */
  public void giveUp(String gameSessionId, GiveupRequest giveupRequest)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + gameSessionId + "/giveup");
      requestBuilder.method("POST");
      requestBuilder.payload(new Gson().toJson(giveupRequest));
      requestBuilder.contentTypeJson();

      requestBuilder.send();
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      if (e.getResponseCode() == 403) {
        throw new ForbiddenMove();
      }
      if (e.getResponseCode() == 410) {
        throw new GameOver();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Deletes a game session
   *
   * @param gameSessionId the game session id
   * @throws GameSessionNotFound    if the game session is not found
   * @throws ServerTimeoutException if the server is not reachable
   * @author robert.kratz
   */
  public void deleteGameSession(String gameSessionId)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + gameSessionId);
      requestBuilder.method("DELETE");

      requestBuilder.send();
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get the user by id
   *
   * @param joinCode the join code
   * @return the GameSessionResponse
   * @throws ServerTimeoutException if the server is not reachable
   * @throws GameSessionNotFound    if the game session is not found
   * @author robert.kratz
   */
  public GameSessionResponse getGameStateByJoinCode(String joinCode)
      throws ServerTimeoutException, GameSessionNotFound {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + joinCode + "/state/joincode");
      requestBuilder.method("GET");

      String response = requestBuilder.send();

      return new Gson().fromJson(response, GameSessionResponse.class);
    } catch (RequestErrorException e) {
      if (e.getResponseCode() == 404) {
        throw new GameSessionNotFound();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get the game board for a game session
   *
   * @param sessionId the session id
   * @return the GetBoardResponse
   * @throws ServerTimeoutException if the server is not reachable
   * @author robert.kratz
   */
  public GetBoardThemeResponse getBoardTheme(String sessionId) throws ServerTimeoutException {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + sessionId + "/theme");
      requestBuilder.method("GET");

      requestBuilder.send();

      return new Gson().fromJson(requestBuilder.send(), GetBoardThemeResponse.class);
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Set the board theme for a game session
   *
   * @param sessionId  the session id
   * @param boardTheme the board theme
   * @return the GetBoardThemeResponse
   * @throws ServerTimeoutException if the server is not reachable
   * @author robert.kratz
   */
  public GetBoardThemeResponse setBoardTheme(String sessionId, BoardTheme boardTheme)
      throws ServerTimeoutException {
    try {
      RequestBuilder requestBuilder = new RequestBuilder(
          BASE_URL + "/gamesession/" + sessionId + "/theme");
      requestBuilder.method("POST");

      SetBoardThemeRequest setBoardThemeRequest = new SetBoardThemeRequest();
      setBoardThemeRequest.setBoardTheme(boardTheme);
      setBoardThemeRequest.setSessionId(sessionId);

      requestBuilder.payload(new Gson().toJson(setBoardThemeRequest));
      requestBuilder.contentTypeJson();

      requestBuilder.send();

      return new Gson().fromJson(requestBuilder.send(), GetBoardThemeResponse.class);
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }
}
