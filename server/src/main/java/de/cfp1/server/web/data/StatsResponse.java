package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author robert.kratz
 */

public class StatsResponse {

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public String userId;

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public int wins;

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public int losses;

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public int draws;

  @Schema(
      description = "The username of the user",
      example = "user"
  )
  public int elo;

  /**
   * Constructor
   *
   * @param userId The id of the user
   * @param wins   The wins of the user
   * @param losses The losses of the user
   * @param draws  The draws of the user
   * @param elo    The elo of the user
   * @author robert.kratz
   */
  public StatsResponse(String userId, int wins, int losses, int draws, int elo) {
    this.userId = userId;
    this.wins = wins;
    this.losses = losses;
    this.draws = draws;
    this.elo = elo;
  }

  /**
   * Constructor
   *
   * @author robert.kratz
   */
  public StatsResponse() {
  }

  /**
   * Set the draws of the user
   *
   * @param draws The draws of the user
   * @author robert.kratz
   */
  public void setDraws(int draws) {
    this.draws = draws;
  }

  /**
   * Set the elo of the user
   *
   * @param elo The elo of the user
   * @author robert.kratz
   */
  public void setElo(int elo) {
    this.elo = elo;
  }

  /**
   * Set the losses of the user
   *
   * @param losses The losses of the user
   * @author robert.kratz
   */
  public void setLosses(int losses) {
    this.losses = losses;
  }

  /**
   * Set the userId of the user
   *
   * @param userId The id of the user
   * @author robert.kratz
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * Set the wins of the user
   *
   * @param wins The wins of the user
   * @author robert.kratz
   */
  public void setWins(int wins) {
    this.wins = wins;
  }

  /**
   * Get the userId of the user
   *
   * @return userId
   * @author robert.kratz
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Get the draws of the user
   *
   * @return draws
   * @author robert.kratz
   */
  public int getDraws() {
    return draws;
  }

  /**
   * Get the elo of the user
   *
   * @return elo
   * @author robert.kratz
   */
  public int getElo() {
    return elo;
  }

  /**
   * Get the losses of the user
   *
   * @return losses
   * @author robert.kratz
   */
  public int getLosses() {
    return losses;
  }

  /**
   * Get the wins of the user
   *
   * @return wins
   * @author robert.kratz
   */
  public int getWins() {
    return wins;
  }
}
