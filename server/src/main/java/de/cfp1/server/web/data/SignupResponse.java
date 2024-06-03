package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author robert.kratz
 */

public class SignupResponse {

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public final String username;

  @Schema(
      description = "The email of the user",
      example = "your.name@example.com"
  )
  public final String email;

  @Schema(
      description = "The user is a guest",
      example = "false"
  )
  private final boolean guest;

  @Schema(
      description = "The user id",
      example = "e58ed763-928c-4155-bee9-fdbaaadc15f3\n"
  )
  private final String userId;

  /**
   * @param username The username
   * @param email    The email
   * @param guest    The user is a guest
   * @param userId   The user id
   * @author robert.kratz
   */
  public SignupResponse(String username, String email, boolean guest, String userId) {
    this.username = username;
    this.email = email;
    this.guest = guest;
    this.userId = userId;
  }

  /**
   * Get the user id
   *
   * @return The user id
   * @author robert.kratz
   */
  public String getUserId() {
    return userId;
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

  /**
   * Get if the user is a guest or not
   *
   * @return guest
   * @author robert.kratz
   */
  public boolean isGuest() {
    return guest;
  }

  /**
   * Get the email of the user
   *
   * @return email
   * @author robert.kratz
   */
  public String getEmail() {
    return email;
  }

}
