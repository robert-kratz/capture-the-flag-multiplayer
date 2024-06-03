package de.cfp1.server.exception;

/**
 * @author robert.kratz
 */

public class InvalidTokenException extends Exception {

  /**
   * @param message the message
   * @author robert.kratz
   */
  public InvalidTokenException(String message) {
    super(message);
  }
}
