package de.cfp1.ai;

import de.cfp1.client.net.NetworkEvent;
import de.cfp1.client.net.NetworkEventType;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.client.utils.AiType;
import de.cfp1.ai.gui.GUI;

/**
 * @author robert.kratz
 */

public class Ai {

  public static NetworkHandler networkHandler = new NetworkHandler(new NetworkEvent() {
    @Override
    public void onEvent(NetworkEventType type, Object... args) {
      System.out.println("Network event: " + type);
    }
  });

  private static String baseUrl;

  /**
   * Main method
   *
   * @param args The arguments
   * @author robert.kratz
   */
  public static void main(String[] args) {
    System.out.println("AI Client Bot");

    try {
      baseUrl = args.length > 1 ? args[1] + "/api" : "http://localhost:8888/api";

      networkHandler.setBaseUrl(baseUrl);

      GUI.launch(GUI.class, args);
    } catch (Exception e) {
      System.out.println("Usage: java -jar ai.jar [base_url]");
      e.printStackTrace();
      return;
    }
  }

  /**
   * Get the AI type by the given arguments
   *
   * @param args The arguments
   * @return The AI type
   * @author robert.kratz
   */
  private static AiType getAiTypeByArgs(String args) {
    return switch (args) {
      case "--easy" -> AiType.EASY;
      case "--medium" -> AiType.MEDIUM;
      case "--hard" -> AiType.HARD;
      default -> AiType.EASY;
    };
  }
}