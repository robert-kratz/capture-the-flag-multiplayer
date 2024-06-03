package de.cfp1.server.web.data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class is used to represent the response after adding a team successfully to a game session.
 */
public class JoinGameResponse {

  @Schema(
      description = "join the game with this session identifier"
  )
  public String gameSessionId;

  @Schema(
      description = "a secret generated for the team to only allow certain teams to make a move when it's their turn (basic anti-cheat)"
  )
  public String teamSecret;

  @Schema(
      description = "unique team identifier"
  )
  public String teamId;

  @Schema(
      description = "the unique color set for the team"
  )
  public String teamColor;

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getTeamColor() {
    return teamColor;
  }

  public void setTeamColor(String teamColor) {
    this.teamColor = teamColor;
  }

  public String getGameSessionId() {
    return gameSessionId;
  }

  public void setGameSessionId(String gameSessionId) {
    this.gameSessionId = gameSessionId;
  }

  public String getTeamSecret() {
    return teamSecret;
  }

  public void setTeamSecret(String teamSecret) {
    this.teamSecret = teamSecret;
  }
}
