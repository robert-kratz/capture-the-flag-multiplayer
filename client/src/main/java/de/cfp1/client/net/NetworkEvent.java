package de.cfp1.client.net;

/**
 * Interface for network events
 *
 * @author robert.kratz
 */

public interface NetworkEvent {

  /**
   * This method is called when a network event is received
   *
   * @param type Event type
   * @param args Event arguments
   * @author robert.kratz
   */
  void onEvent(NetworkEventType type, Object... args);
}
