package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author robert.kratz
 */

public class UserTransformationRequest {

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public String username;
  @Schema(
      description = "The password of the user",
      example = "password"
  )
  public String password;

  @Schema(
      description = "The email of the user",
      example = "test@example.com")
  public String email;

  /**
   * Get the email of the user
   *
   * @return email
   * @author robert.kratz
   */
  public String getEmail() {
    return email;
  }

  /**
   * Get the password of the user
   *
   * @return password
   * @author robert.kratz
   */
  public String getPassword() {
    return password;
  }

  /**
   * Get the username of the user
   *
   * @return username
   * @author robert.kratz
   */
  public String getUsername() {
    return username;
  }
}
