package de.cfp1.client;

import de.cfp1.client.gui.GUI;
import de.cfp1.client.net.NetworkEvent;
import de.cfp1.client.net.NetworkEventType;
import de.cfp1.client.net.NetworkHandler;
import de.cfp1.client.utils.ProcessManager;
import java.util.Objects;

/**
 * @author robert.kratz
 */

public class Client {

  public static NetworkHandler networkHandler;
  public static ProcessManager processManager;

  /**
   * Main method start the client with: java -jar client.jar http://localhost:8888
   *
   * @param args command line arguments
   * @author robert.kratz
   */
  public static void main(String[] args) {
    System.out.println("Client started");

    processManager = new ProcessManager();

    networkHandler = new NetworkHandler(new NetworkEvent() {
      @Override
      public void onEvent(NetworkEventType type, Object... args) {
        if (Objects.requireNonNull(type) == NetworkEventType.TOKEN_REFRESHED) {
          System.out.println("Token refreshed: " + args[0] + " " + args[1]);
        }
      }
    });

    //todo: if --url is set, use that url instead of the default one
    if (args.length > 0) {
      System.out.println("Starting client with url: " + args[1]);
      networkHandler.setBaseUrl(args[1] + "/api");
    } else {
      networkHandler.setBaseUrl("http://localhost:8888/api");
    }

    GUI.launch(GUI.class, args);
  }
}