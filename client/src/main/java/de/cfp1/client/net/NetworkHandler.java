package de.cfp1.client.net;

import com.google.gson.Gson;
import de.cfp1.client.game.GameHandler;
import de.cfp1.client.game.GameHelper;
import de.cfp1.client.net.data.TokenRefreshEvent;
import de.cfp1.server.auth.TokenPair;
import de.cfp1.server.entities.User;
import de.cfp1.server.exception.*;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.web.data.GameSessionResponse;

/**
 * @author robert.kratz
 */

public class NetworkHandler {

  private static String BASE_URL = "http://localhost:8888/api";

  private String accessToken, refreshToken;
  private final NetworkEvent eventListener;
  private final MapHandler mapHandler;
  private final StatisticsHandler statisticsHandler;

  private GameHelper gameHelper = new GameHelper();

  public boolean authTokenValid = true;
  private User user;
  private boolean isGuest = false;

  /**
   * Constructor
   *
   * @param eventListener The event listener for the network handler
   * @author robert.kratz
   */
  public NetworkHandler(NetworkEvent eventListener) {
    this.eventListener = eventListener;
    this.mapHandler = new MapHandler(this);
    this.statisticsHandler = new StatisticsHandler(this);
  }

  /**
   * Refresh the access token
   *
   * @return True if the refresh was successful, false otherwise
   * @author robert.kratz
   */
  public boolean refreshTokens() {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/token/refresh");
      request.method("PUT");
      request.payload("{\"token\":\"" + refreshToken + "\"}");
      request.contentTypeJson();

      String tokenJson = request.send();

      TokenPair tokenPair = new Gson().fromJson(tokenJson, TokenPair.class);

      setAccessToken(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
      eventListener.onEvent(NetworkEventType.TOKEN_REFRESHED,
          new TokenRefreshEvent(accessToken, refreshToken));
      this.authTokenValid = true;
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Set the access and refresh tokens
   *
   * @param accessToken  The access token
   * @param refreshToken The refresh token
   * @author robert.kratz
   */
  public void setAccessToken(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  /**
   * Login to the server with username and password
   *
   * @param username The username
   * @param password The password
   * @return True if the login was successful, false otherwise
   * @author robert.kratz
   */
  public boolean login(String username, String password)
      throws InvalidCredentialsException, ServerTimeoutException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/login");
      request.method("POST");
      request.payload("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
      request.contentTypeJson();

      String tokenJson = request.send();

      TokenPair tokenPair = new Gson().fromJson(tokenJson, TokenPair.class);

      setAccessToken(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
      this.authTokenValid = true;
      eventListener.onEvent(NetworkEventType.LOGIN);

      getUserFromServer();

      return true;
    } catch (RequestErrorException e) {
        if (e.getMessage().equals("Invalid credentials")) {
            throw new InvalidCredentialsException();
        }
        if (e.getResponseCode() == 500) {
            throw new ServerTimeoutException();
        }
        if (e.getResponseCode() == 400) {
            throw new InvalidCredentialsException();
        }
      return false;
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get the user object from the server
   *
   * @return User object from the server or null if the request fails
   * @author robert.kratz
   */
  public User getUserFromServer() throws UserNotFoundException, ServerTimeoutException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/user");
      request.method("GET");
      request.useAuth(accessToken);
      String userJson = request.send();
      user = new Gson().fromJson(userJson, User.class);

      this.isGuest = user.isGuest();

      return user;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.refreshTokens();

          if (!authTokenValid) {
              throw new UserSessionExpiredException();
          }
        this.authTokenValid = false;
        return this.getUserFromServer();
      }
      if (e.getMessage().equals("User not found")) {
        throw new UserNotFoundException();
      }
      if (e.getMessage().equals("Failed to get user data")) {
        throw new UserSessionExpiredException();
      }
        if (e.getResponseCode() == 500) {
            throw new ServerTimeoutException(e.getMessage());
        }
      return null;
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Update the user object on the server
   *
   * @param username The new username
   * @param email    The new email
   * @param password The new password
   * @return True if the update was successful, false otherwise
   * @throws UsernameTakenException    If the username is already taken
   * @throws EmailTakenExceptions      If the email is already taken
   * @throws PasswordInsecureException If the password is insecure
   * @author robert.kratz
   */
  public boolean signup(String username, String email, String password)
      throws UsernameTakenException, EmailTakenExceptions, PasswordInsecureException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/signup");
      request.method("POST");
      request.payload(
          "{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\""
              + password + "\"}");
      request.contentTypeJson();
      String tokenJson = request.send();
      TokenPair tokenPair = new Gson().fromJson(tokenJson, TokenPair.class);
      setAccessToken(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
      this.authTokenValid = true;
      eventListener.onEvent(NetworkEventType.SIGNUP);
      return true;
    } catch (RequestErrorException e) {
      switch (e.getMessage()) {
        case "Username already exist" -> throw new UsernameTakenException();
        case "Email already exist" -> throw new EmailTakenExceptions();
        case "Password insecure" -> throw new PasswordInsecureException();
      }
        if (e.getResponseCode() == 500) {
            throw new ServerTimeoutException(e.getMessage());
        }
      return false;
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Sign up as a guest, it will create a new guest account and override the current access and
   * refresh tokens
   *
   * @author robert.kratz
   */
  public boolean signUpAsGuest() throws ServerTimeoutException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/signup/guest");
      request.method("POST");
      request.contentTypeJson();

      String tokenJson = request.send();

      TokenPair tokenPair = new Gson().fromJson(tokenJson, TokenPair.class);
      setAccessToken(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
      this.authTokenValid = true;
      this.isGuest = true;

      eventListener.onEvent(NetworkEventType.SIGNUP_AS_GUEST);

      return true;
    } catch (RequestErrorException e) {
        if (e.getResponseCode() == 500) {
            throw new ServerTimeoutException(e.getMessage());
        }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Upgrade the guest account to a user account
   *
   * @param username The username
   * @param email    The email
   * @param password The password
   * @throws UsernameTakenException       If the username is already taken
   * @throws EmailTakenExceptions         If the email is already taken
   * @throws PasswordInsecureException    If the password is insecure
   * @throws UserNotGuestException        If the user is not a guest
   * @throws GuestSessionExpiredException If the guest session has expired
   * @author robert.kratz
   */
  public boolean upgradeToUser(String username, String email, String password)
      throws UsernameTakenException, EmailTakenExceptions, PasswordInsecureException, UserNotGuestException, GuestSessionExpiredException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/signup/guest/transform");
      request.method("POST");
      request.contentTypeJson();
      request.useAuth(accessToken); //TODO: Check if this is correct
      request.payload(
          "{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\""
              + password + "\"}");
      String tokenJson = request.send();
      this.authTokenValid = true;
      this.isGuest = false;

      eventListener.onEvent(NetworkEventType.GUEST_UPGRADE_ACCOUNT, username, email, password);
      return true;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Invalid token")) {
        this.refreshTokens();

          if (!authTokenValid) {
              throw new GuestSessionExpiredException();
          }
        this.authTokenValid = false;
        this.upgradeToUser(username, email, password);
      }
      if (e.getMessage().equals("Username already exists")) {
        throw new UsernameTakenException();
      }
      if (e.getMessage().equals("Email already exists")) {
        throw new EmailTakenExceptions();
      }
      if (e.getMessage().equals("Password insecure")) {
        throw new PasswordInsecureException();
      }
      if (e.getMessage().equals("User is not a guest")) {
        throw new UserNotGuestException();
      }
        if (e.getResponseCode() == 500) {
            throw new ServerTimeoutException(e.getMessage());
        }
      return false;
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Delete the user profile from the server
   *
   * @return True if the deletion was successful, false otherwise
   * @author robert.kratz
   */
  public boolean deleteUserProfile() throws ServerTimeoutException, UserNotFoundException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/delete");
      request.method("DELETE");
      request.useAuth(accessToken);
      String response = request.send();

        if (!response.equals("true")) {
            return false;
        }

      setAccessToken(null, null); //remove tokens
      this.user = null;
      this.isGuest = false;

      eventListener.onEvent(NetworkEventType.DELETE_USER);

      return true;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.refreshTokens();

          if (!authTokenValid) {
              return false;
          }
        this.authTokenValid = false;
        return this.deleteUserProfile();
      }
        if (e.getMessage().equals("User not found")) {
            throw new UserNotFoundException();
        }
        if (e.getResponseCode() == 500) {
            throw new ServerTimeoutException(e.getMessage());
        }
      return false;
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Update the username of the user
   *
   * @param newUsername The new username (unique)
   * @return True if the update was successful, false otherwise
   * @throws UsernameTakenException      If the username is already taken
   * @throws ServerTimeoutException      If the server times out
   * @throws UserSessionExpiredException If the user session has expired
   * @author robert.kratz
   */
  public boolean updateUsername(String newUsername)
      throws UsernameTakenException, ServerTimeoutException, UserSessionExpiredException, UserNotFoundException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/user");
      request.method("PUT");
      request.contentTypeJson();
      System.out.println(accessToken);
      System.out.println("{\"username\":\"" + newUsername + "\"}");
      request.useAuth(accessToken);
      request.payload("{\"username\":\"" + newUsername + "\"}");
      String response = request.send();

      eventListener.onEvent(NetworkEventType.USER_UPDATE, newUsername);

      //this.user = new Gson().fromJson(response, User.class);
      this.authTokenValid = true;
      return true;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.refreshTokens();

          if (!authTokenValid) {
              throw new UserSessionExpiredException();
          }
        this.authTokenValid = false;
        return this.updateUsername(newUsername);
      }
        if (e.getMessage().equals("User not found")) {
            throw new UserNotFoundException();
        }
        if (e.getMessage().equals("Username already exists")) {
            throw new UsernameTakenException();
        }
        if (e.getResponseCode() == 500) {
            throw new ServerTimeoutException(e.getMessage());
        }
      return false;
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Check if the token is valid
   *
   * @param tokenType The type of token to check
   * @return True if the token is valid, false otherwise
   * @author robert.kratz
   */
  public boolean isTokenValid(TokenType tokenType) {
    if (tokenType == TokenType.ACCESS) {
        if (accessToken == null) {
            return false;
        }
      return validateToken(accessToken);
    } else {
        if (refreshToken == null) {
            return false;
        }
      return validateToken(refreshToken);
    }
  }

  /**
   * Get the user object by the id
   *
   * @param id The id of the user
   * @return The user object or null if the user is not found
   * @throws UserNotFoundException  If the user is not found
   * @throws ServerTimeoutException If the server times out
   * @author robert.kratz
   */
  public User getUserById(String id) throws UserNotFoundException, ServerTimeoutException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/user/" + id);
      request.method("GET");
      request.useAuth(accessToken);
      String userJson = request.send();
      return new Gson().fromJson(userJson, User.class);
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.refreshTokens();

          if (!authTokenValid) {
              throw new UserSessionExpiredException();
          }
        this.authTokenValid = false;
        return this.getUserById(id);
      }
      if (e.getMessage().equals("User not found")) {
        throw new UserNotFoundException();
      }
        if (e.getResponseCode() == 500) {
            throw new ServerTimeoutException(e.getMessage());
        }
      return null;
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Validate the token
   *
   * @param token The token to validate
   * @return True if the token is valid, false otherwise
   * @author robert.kratz
   */
  private boolean validateToken(String token) {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/auth/token/validate");
      request.method("POST");
      request.useAuth(token);
      String response = request.send();
      return response.equals("true");
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Get the game session by the id
   *
   * @param mapTemplate The map template of the game
   * @return The game handler object
   * @throws ServerTimeoutException If the server times out
   * @author robert.kratz
   */
  public GameHandler createNewGameSession(MapTemplate mapTemplate) throws ServerTimeoutException {
    GameHandler gameHandler = new GameHandler(this);
    GameSessionResponse gameSessionResponse = gameHandler.createNewGame(mapTemplate);

    gameHandler.setSessionId(gameSessionResponse.getId());

    return gameHandler;
  }

  /**
   * Logout the user, it will clear the access and refresh tokens
   *
   * @author robert.kratz
   */
  public void logout() {
    this.accessToken = null;
    this.refreshToken = null;
    this.user = null;
    this.authTokenValid = true;
    eventListener.onEvent(NetworkEventType.LOGOUT);
  }

  public enum TokenType {
    ACCESS, REFRESH
  }

  /**
   * Getter Method for the AccessToken
   *
   * @author robert.kratz
   */
  // Getters, Setters, and other methods
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Getter Method for the BaseURL
   *
   * @author robert.kratz
   */
  public static String getBaseUrl() {
    return BASE_URL;
  }

  /**
   * Getter Method for the RefreshToken
   *
   * @author robert.kratz
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Getter Method for the MapHandler
   *
   * @return The MapHandler object
   * @author robert.kratz
   */
  public MapHandler getMapHandler() {
    return mapHandler;
  }

  /**
   * Getter Method for the GameHelper
   *
   * @return The GameHelper object
   * @return The GameHelper object
   * @author robert.kratz
   */
  public GameHelper getGameHelper() {
    return gameHelper;
  }

  /**
   * Getter Method for the User
   *
   * @return The User object
   * @author robert.kratz
   */
  public User getUser() {
    return user;
  }

  /**
   * Getter Method for the isGuest boolean
   *
   * @return True if the user is a guest, false otherwise
   * @author robert.kratz
   */
  public NetworkEvent getEventListener() {
    return eventListener;
  }

  /**
   * Getter Method for the StatisticsHandler
   *
   * @return The StatisticsHandler object
   * @author robert.kratz
   */
  public StatisticsHandler getStatisticsHandler() {
    return statisticsHandler;
  }

  /**
   * Setter Method for the BaseURL
   *
   * @param baseUrl The base URL to set
   * @author robert.kratz
   */
  public void setBaseUrl(String baseUrl) {
    BASE_URL = baseUrl;
  }
}
