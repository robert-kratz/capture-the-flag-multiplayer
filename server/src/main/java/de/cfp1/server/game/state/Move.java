package de.cfp1.server.game.state;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class represents a move.
 */
public class Move {

  @Schema(
      description = "the piece (by its ID) to move"
  )
  public String pieceId;
  @Schema(
      description = "new position of the piece on the board (grid)"
  )
  public int[] newPosition;

  @Schema(
      description = "unique identifier of the team that makes the move"
  )
  public String teamId;

  public Move() {
    this.pieceId = "";
    this.teamId = "";
    this.newPosition = new int[2];
  }

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getPieceId() {
    return pieceId;
  }

  public void setPieceId(String pieceId) {
    this.pieceId = pieceId;
  }

  public int[] getNewPosition() {
    return newPosition;
  }

  public void setNewPosition(int[] newPosition) {
    this.newPosition = newPosition;
  }
}
