package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author robert.kratz
 */

public class LoginRequest {

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public String username;

  @Schema(
      description = "The password of the user",
      example = "P4ssword_!1234"
  )
  public String password;

  /**
   * Getter for the password
   *
   * @return the password
   * @author robert.kratz
   */
  public String getPassword() {
    return password;
  }

  /**
   * Getter for the username
   *
   * @return the username
   * @author robert.kratz
   */
  public String getUsername() {
    return username;
  }
}
