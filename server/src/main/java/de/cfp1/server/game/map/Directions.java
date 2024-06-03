package de.cfp1.server.game.map;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class represents possible piece movements in terms of squares to move in one or more
 * direction(s).
 */
public class Directions {

  @Schema(
      description = "move N squares left"
  )
  public int left = 0;
  @Schema(
      description = "move N squares right"
  )
  public int right = 0;
  @Schema(
      description = "move N squares up"
  )
  public int up = 0;
  @Schema(
      description = "move N squares down"
  )
  public int down = 0;
  @Schema(
      description = "move N squares up-left (diagonal)"
  )
  public int upLeft = 0;
  @Schema(
      description = "move N squares up-right (diagonal)"
  )
  public int upRight = 0;
  @Schema(
      description = "move N squares down-left (diagonal)"
  )
  public int downLeft = 0;
  @Schema(
      description = "move N squares down-right (diagonal)"
  )
  public int downRight = 0;

  public Directions() {
    this.down = 0;
    this.downLeft = 0;
    this.downRight = 0;
    this.left = 0;
    this.right = 0;
    this.up = 0;
    this.upLeft = 0;
    this.upRight = 0;
  }

  public Directions(int left, int up, int right, int down, int downLeft, int upLeft, int upRight,
      int downRight) {
    this.left = left;
    this.right = right;
    this.up = up;
    this.down = down;
    this.upLeft = upLeft;
    this.upRight = upRight;
    this.downLeft = downLeft;
    this.downRight = downRight;
  }

  public int getLeft() {
    return left;
  }

  public void setLeft(int left) {
    this.left = left;
  }

  public int getRight() {
    return right;
  }

  public void setRight(int right) {
    this.right = right;
  }

  public int getUp() {
    return up;
  }

  public void setUp(int up) {
    this.up = up;
  }

  public int getDown() {
    return down;
  }

  public void setDown(int down) {
    this.down = down;
  }

  public int getUpLeft() {
    return upLeft;
  }

  public void setUpLeft(int upLeft) {
    this.upLeft = upLeft;
  }

  public int getUpRight() {
    return upRight;
  }

  public void setUpRight(int upRight) {
    this.upRight = upRight;
  }

  public int getDownLeft() {
    return downLeft;
  }

  public void setDownLeft(int downLeft) {
    this.downLeft = downLeft;
  }

  public int getDownRight() {
    return downRight;
  }

  public void setDownRight(int downRight) {
    this.downRight = downRight;
  }
}
