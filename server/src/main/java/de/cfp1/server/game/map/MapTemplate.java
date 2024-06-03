package de.cfp1.server.game.map;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class represents a game specific configuration.
 */
public class MapTemplate {

  @Schema(
      description = "grid size. format [rows, columns]",
      example = "[10, 10]"
  )
  public int[] gridSize;
  @Schema(
      description = "number of teams (i.e., players)",
      example = "2"
  )
  public int teams;
  @Schema(
      description = "number of flags located in each team's base",
      example = "1"
  )
  public int flags;
  @Schema(
      description = "list of pieces configured in a game"
  )
  public PieceDescription[] pieces;
  @Schema(
      description = "number of blocks placed randomly (squares occupied by a block cannot be occupied by a piece)"
  )
  public int blocks;
  @Schema(
      description = "placement strategy for pieces and blocks",
      example = "symmetrical, spaced_out or defensive (see Enum 'PlacementType' for more details)"
  )
  public PlacementType placement;
  @Schema(
      description = "total game time limit in seconds - after time limit, a winner is determined  (-1 if none)"
  )
  public int totalTimeLimitInSeconds;
  @Schema(
      description = "time limit for moves in seconds - after time limit, a move request by the current team is disregarded (-1 if none)"
  )
  public int moveTimeLimitInSeconds;

  public PieceDescription[] getPieces() {
    return pieces;
  }

  public void setPieces(PieceDescription[] pieces) {
    this.pieces = pieces;
  }

  public PlacementType getPlacement() {
    return placement;
  }

  public void setPlacement(PlacementType placement) {
    this.placement = placement;
  }

  public int[] getGridSize() {
    return gridSize;
  }

  public void setGridSize(int[] gridSize) {
    this.gridSize = gridSize;
  }

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public int getBlocks() {
    return blocks;
  }

  public void setBlocks(int blocks) {
    this.blocks = blocks;
  }

  public int getTeams() {
    return teams;
  }

  public void setTeams(int teams) {
    this.teams = teams;
  }

  public int getTotalTimeLimitInSeconds() {
    return totalTimeLimitInSeconds;
  }

  public void setTotalTimeLimitInSeconds(int totalTimeLimitInSeconds) {
    this.totalTimeLimitInSeconds = totalTimeLimitInSeconds;
  }

  public int getMoveTimeLimitInSeconds() {
    return moveTimeLimitInSeconds;
  }

  public void setMoveTimeLimitInSeconds(int moveTimeLimitInSeconds) {
    this.moveTimeLimitInSeconds = moveTimeLimitInSeconds;
  }

  /**
   * Returns the object as a JSON string.
   *
   * @return JSON string
   * @author robert.kratz
   */
  public String getAsJson() {
    return new Gson().toJson(this);
  }
}
