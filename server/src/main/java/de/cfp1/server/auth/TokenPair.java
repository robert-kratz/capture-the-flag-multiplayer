package de.cfp1.server.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Class to represent a pair of tokens
 *
 * @author robert.kratz
 */

public class TokenPair {

  @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSJ9.3z2zehz82hfzhz72rg3z27hud2h3uzgre")
  public String accessToken;
  @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSJ9.3z2zehz82hfzhz72rg3z27hud2h3uzgre")
  public String refreshToken;

  /**
   * Constructor
   *
   * @param accessToken  the access token
   * @param refreshToken the refresh token
   * @author robert.kratz
   */
  public TokenPair(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  /**
   * Get the access token
   *
   * @return the access token
   * @author robert.kratz
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Get the refresh token
   *
   * @return the refresh token
   * @author robert.kratz
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Convert the token pair to a string
   *
   * @return the token pair as a string
   * @author robert.kratz
   */
  public String toString() {
    return "{\"accessToken\":\"" + accessToken + "\",\"refreshToken\":\"" + refreshToken + "\"}";
  }

  /**
   * Create a token pair from a string
   *
   * @param tokenPairString the token pair as a string
   * @return the token pair
   * @throws Exception if the string is not a valid token pair
   * @author robert.kratz
   */
  public static TokenPair fromString(String tokenPairString) throws Exception {
    //use gson or jackson to parse the string
    JsonObject jsonObject = JsonParser.parseString(tokenPairString).getAsJsonObject();

    String accessToken = jsonObject.get("accessToken").getAsString();
    String refreshToken = jsonObject.get("refreshToken").getAsString();

    return new TokenPair(accessToken, refreshToken);
  }
}
