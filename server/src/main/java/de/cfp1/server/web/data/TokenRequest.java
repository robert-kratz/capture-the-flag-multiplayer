package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author robert.kratz
 */

public class TokenRequest {

  @Schema(description = "The token to be refreshed", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSJ9.3z2zehz82hfzhz72rg3z27hud2h3uzgre")
  public String token;

  /**
   * Get the token
   *
   * @return token
   * @author robert.kratz
   */
  public String getToken() {
    return token;
  }
}
