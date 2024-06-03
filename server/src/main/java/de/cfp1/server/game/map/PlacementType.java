package de.cfp1.server.game.map;

/**
 * This enum represents strategies for piece placements.
 */
public enum PlacementType {

  /**
   * Symmetrical placement: To ensure a fair and balanced game, the initial placement of pieces
   * should be symmetrical. This means that each team should have the same number and type of
   * pieces, placed in the same configuration on the grid. For example, if one team has a piece on a
   * certain square, the other team should have a piece on the corresponding square on the opposite
   * side of the grid.
   */
  symmetrical,

  /**
   * Spaced out placement: To allow for movement and strategy, the initial placement of pieces
   * should be spaced out. This means that pieces should not be placed too close to each other, as
   * this can limit their mobility and make them vulnerable to attack. Instead, pieces should be
   * placed in strategic positions that allow for maximum flexibility and maneuverability.
   */
  spaced_out,

  /**
   * Defensive placement: To protect the team's base and flag, the initial placement of pieces
   * should include some defensive positions. This means placing some pieces in strategic locations
   * around the team's base and flag, to prevent enemy pieces from getting too close.
   */
  defensive
}
