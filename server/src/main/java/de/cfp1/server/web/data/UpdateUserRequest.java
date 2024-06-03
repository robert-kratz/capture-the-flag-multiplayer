package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author robertkratz
 */
public class UpdateUserRequest {

  @Schema(description = "The new username", example = "user")
  public String username;

  /**
   * Get the username of the user
   *
   * @return username
   * @author robert.kratz
   */
  public String getUsername() {
    return this.username;
  }
}
