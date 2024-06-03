package de.cfp1.server.exception;

/**
 * @author robert.kratz
 */

public class TokenInvalidException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final int responseCode;

  /**
   * Constructor for TokenInvalidException
   *
   * @param responseCode The response code of the request
   */
  public TokenInvalidException(int responseCode) {
    super("Token is invalid. Response code: " + responseCode);
    this.responseCode = responseCode;
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
}
