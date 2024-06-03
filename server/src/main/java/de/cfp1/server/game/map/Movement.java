package de.cfp1.server.game.map;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class represents a possible movement.
 */
public class Movement {

  @Schema(
      description = "directions a piece can move. if set, shape must NOT be set"
  )
  public Directions directions;
  @Schema(
      description = "shapes a piece can move (e.g., L. if set, directions must NOT be set"
  )
  public Shape shape;

  public Movement() {
    this.directions = new Directions();
    this.shape = new Shape();
  }

  public Movement(Directions directions, Shape shape) {
    this.directions = directions;
    this.shape = shape;
  }


  public Movement(Directions directions) {
    this.directions = directions;
  }

  public Movement(Shape shape) {
    this.shape = shape;
  }

  public Directions getDirections() {
    return directions;
  }

  public void setDirections(Directions directions) {
    this.directions = directions;
  }

  public Shape getShape() {
    return shape;
  }

  public void setShape(Shape shape) {
    this.shape = shape;
  }
}
