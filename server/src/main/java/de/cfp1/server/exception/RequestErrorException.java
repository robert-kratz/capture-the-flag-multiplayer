package de.cfp1.server.exception;

/**
 * @author robertkratz
 */

public class RequestErrorException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final int responseCode;
  private final String message;

  /**
   * Constructor for RequestErrorException
   *
   * @param message      the message of the exception
   * @param responseCode the response code of the request
   */
  public RequestErrorException(String message, int responseCode) {
    super(message + " (Response Code: " + responseCode + ")");
    this.responseCode = responseCode;
    this.message = message;

  }

  /**
   * Getter for the response code
   *
   * @return the response code
   * @author robert.kratz
   */
  public int getResponseCode() {
    return responseCode;
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
