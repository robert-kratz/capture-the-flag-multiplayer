package de.cfp1.server.exception;

/**
 * @author robert.kratz
 */

public class UserNotFoundException extends Exception {

  /**
   * @param message the message
   * @author robert.kratz
   */
  public UserNotFoundException(String message) {
    super(message);
  }

  /**
   * @author robert.kratz
   */
  public UserNotFoundException() {
    super();
  }
}
