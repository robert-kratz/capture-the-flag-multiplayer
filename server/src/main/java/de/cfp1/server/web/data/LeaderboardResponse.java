package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * @author robert.kratz
 */

public class LeaderboardResponse {

  @Schema(
      description = "The userid of the user",
      example = "1231"
  )
  public String userId;

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public String username;

  @Schema(
      description = "stats of the user",
      example = ".."
  )
  public StatsResponse stats;

  /**
   * Constructor
   *
   * @param userId   The id of the user
   * @param username The username of the user
   * @param stats    The stats of the user
   * @author robert.kratz
   */
  public LeaderboardResponse(String userId, String username, StatsResponse stats) {
    this.userId = userId;
    this.username = username;
    this.stats = stats;
  }

  /**
   * Default constructor
   *
   * @author robert.kratz
   */
  public LeaderboardResponse() {
  }

  /**
   * Setter for the stats of the user
   *
   * @param stats the stats of the user
   * @author robert.kratz
   */
  public void setStats(StatsResponse stats) {
    this.stats = stats;
  }

  /**
   * Setter for the userid of the user
   *
   * @param userId the userid of the user
   * @author robert.kratz
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * Setter for the username of the user
   *
   * @param username the username of the user
   * @author robert.kratz
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getter for the stats of the user
   *
   * @return the stats of the user
   * @author robert.kratz
   */
  public StatsResponse getStats() {
    return stats;
  }

  /**
   * Getter for the userid of the user
   *
   * @return the userid of the user
   * @author robert.kratz
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Getter for the username of the user
   *
   * @return the username of the user
   * @author robert.kratz
   */
  public String getUsername() {
    return username;
  }
}
