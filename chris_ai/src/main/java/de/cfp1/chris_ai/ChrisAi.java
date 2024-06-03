package de.cfp1.chris_ai;

import de.cfp1.chris_ai.ai.ChrisAiHandler;
import de.cfp1.client.game.GameHandler;
import de.cfp1.client.net.NetworkEvent;
import de.cfp1.client.net.NetworkEventType;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.server.entities.User;

public class ChrisAi {

  public static String sessionId;

  public static NetworkHandler networkHandler = new NetworkHandler(new NetworkEvent() {
    @Override
    public void onEvent(NetworkEventType type, Object... args) {
      System.out.println("Network event: " + type);
    }
  });

  private static String baseUrl;

  /**
   * Main method
   * <p>
   * Start the AI Chris Client Bot java -jar ai.jar [base_url] [session_id]
   *
   * @param args The arguments
   * @author robert.kratz
   */
  public static void main(String[] args) {
    System.out.println("Chris AI Bot");

    try {
      baseUrl = args.length > 2 ? args[1] + "/api" : "http://localhost:8888/api";

      System.out.println("Base URL: " + args.length);

      if (args.length > 1) {
        sessionId = args[2];
      }

      if (sessionId != null) {
        System.out.println("Session ID: " + sessionId);
      } else {
        System.out.println("No session ID provided");
        System.out.println("Usage: java -jar ai.jar [base_url] [session_id]");
        System.exit(1);
      }

      networkHandler.setBaseUrl(baseUrl);

      System.out.println("Using base URL: " + baseUrl);

      networkHandler.signUpAsGuest();

      User user = networkHandler.getUserFromServer();

      System.out.println("Signed up as guest " + user.getUsername());

      joinGame();

    } catch (Exception e) {
      System.out.println("Usage: java -jar ai.jar [base_url]");
      e.printStackTrace();
      return;
    }
  }

  /**
   * Join the game
   *
   * @throws Exception if the game cannot be joined
   * @author robert.kratz
   */
  private static void joinGame() {
    try {
      GameHandler gameHandler = new GameHandler(networkHandler);

      if (sessionId.length() == 6) {
        System.out.println("Attempting to join game by code");
        gameHandler.joinGameByCode(sessionId, networkHandler.getUser().getId());
      } else {
        System.out.println("Attempting to join game by session ID");
        gameHandler.joinGameBySessionId(sessionId, networkHandler.getUser().getId());
      }

      System.out.println("Joined game " + gameHandler.getSessionId());

      ChrisAiHandler chrisAiHandler = new ChrisAiHandler();

      chrisAiHandler.setGameHandler(gameHandler);
      gameHandler.setGameEvent(chrisAiHandler);

    } catch (Exception e) {
      System.out.println("Failed to join game, exiting...");
      e.printStackTrace();
      System.exit(1);
    }
  }
}