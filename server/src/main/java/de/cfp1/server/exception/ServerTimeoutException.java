package de.cfp1.server.exception;

/**
 * @author robert.kratz
 */

public class ServerTimeoutException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String message = null;

  /**
   * Constructor for ServerTimeoutException
   *
   * @param message The message
   * @author robert.kratz
   */
  public ServerTimeoutException(String message) {
    super("Server timeout. Response code: " + message);
    this.message = "Server timeout. Response code: " + message;
  }

  /**
   * Constructor for ServerTimeoutException
   *
   * @author robert.kratz
   */
  public ServerTimeoutException() {
  }

  /**
   * Getter for the message
   *
   * @return the message
   * @author robert.kratz
   */
  @Override
  public String getMessage() {
    return message;
  }
}
