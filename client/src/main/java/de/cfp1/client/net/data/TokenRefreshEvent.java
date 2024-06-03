package de.cfp1.client.net.data;

/**
 * @author robertkratz
 */

public class TokenRefreshEvent {

  private final String accessToken;
  private final String refreshToken;

  /**
   * Event for token refresh
   *
   * @param accessToken  The new access token
   * @param refreshToken The new refresh token
   * @author robert.kratz
   */
  public TokenRefreshEvent(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  /**
   * Get the new access token
   *
   * @return The new access token
   * @author robert.kratz
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Get the new refresh token
   *
   * @return The new refresh token
   * @author robert.kratz
   */
  public String getRefreshToken() {
    return refreshToken;
  }
}
