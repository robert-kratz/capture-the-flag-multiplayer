package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class represents the request to give up a game.
 */
public class GiveupRequest {

  @Schema(
      description = "unique identifier of the team"
  )
  public String teamId;

  @Schema(
      description = "the team secret which is checked if team is allowed to give up"
  )
  public String teamSecret;

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getTeamSecret() {
    return teamSecret;
  }

  public void setTeamSecret(String teamSecret) {
    this.teamSecret = teamSecret;
  }

  public GiveupRequest() {
  }

  /**
   * Constructor for the GiveupRequest
   *
   * @param teamId     the unique identifier of the team
   * @param teamSecret the team secret which is checked if team is allowed to give up
   * @author robert.kratz
   */
  public GiveupRequest(String teamId, String teamSecret) {
    this.teamId = teamId;
    this.teamSecret = teamSecret;
  }
}

