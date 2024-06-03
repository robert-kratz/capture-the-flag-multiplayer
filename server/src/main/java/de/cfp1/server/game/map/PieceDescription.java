package de.cfp1.server.game.map;

import de.cfp1.server.game.state.Piece;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

/**
 * This class describes a {@link Piece} in a game.
 */
public class PieceDescription {

  @Schema(
      description = "unique piece type (e.g., 'Pawn' or 'Rook')"
  )
  public String type;
  @Schema(
      description = "attack power of piece (the higher, the more power it has)"
  )
  @Min(1)
  public int attackPower;
  @Schema(
      description = "the number of pieces of this type a team has"
  )
  @Min(1)
  public int count;
  @Schema(
      description = "possible movements of a piece"
  )
  public Movement movement;

  public PieceDescription() {
    this.type = "";
    this.attackPower = 0;
    this.count = 0;
    this.movement = new Movement();
  }

  public PieceDescription(String type, int attackPower, int count, Movement movement) {
    this.type = type;
    this.attackPower = attackPower;
    this.count = count;
    this.movement = movement;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getAttackPower() {
    return attackPower;
  }

  public void setAttackPower(int attackPower) {
    this.attackPower = attackPower;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public Movement getMovement() {
    return movement;
  }

  public void setMovement(Movement movement) {
    this.movement = movement;
  }
}
