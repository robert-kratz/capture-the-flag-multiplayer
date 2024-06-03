package de.cfp1.server.game.state;

import de.cfp1.server.game.map.PieceDescription;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class represents a piece.
 */
public class Piece {

  @Schema(
      description = "unique piece identifier"
  )
  public String id;
  @Schema(
      description = "team owning the piece"
  )
  public String teamId;
  @Schema(
      description = "the description of the piece (including its attack power etc.)"
  )
  public PieceDescription description;
  @Schema(
      description = "current position of the piece"
  )
  public int[] position;

  public Piece() {
    this.id = "";
    this.teamId = "";
    this.description = null;
    this.position = new int[2];
  }

  public Piece(String id, int[] position) {
    this.id = id;
    this.position = position;
    this.teamId = "";
    this.description = null;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int[] getPosition() {
    return position;
  }

  public void setPosition(int[] position) {
    this.position = position;
  }

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public PieceDescription getDescription() {
    return description;
  }

  public void setDescription(PieceDescription description) {
    this.description = description;
  }
}
