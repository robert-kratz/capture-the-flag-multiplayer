package de.cfp1.client.gui;

/**
 * This class is used to store information about a player that is displayed in the game.
 *
 * @author robert.kratz
 */

public class GamePlayerLabelInfo {

  private final String playerName;
  private final String id;
  private final int teamIndex, labelIndex;
  private final String color;

  /**
   * Constructor
   *
   * @param playerName name of the player
   * @param id         id of the player
   * @param teamIndex  index of the team
   * @param color      color of the player
   */
  public GamePlayerLabelInfo(String playerName, String id, int teamIndex, int labelIndex,
      String color) {
    this.playerName = playerName;
    this.id = id;
    this.teamIndex = teamIndex;
    this.labelIndex = labelIndex;
    this.color = color;
  }

  /**
   * Getter for the player name
   *
   * @return name of the player
   * @author robert.kratz
   */
  public String getPlayerName() {
    return playerName;
  }

  /**
   * Getter for the player id
   *
   * @return id of the player
   * @author robert.kratz
   */
  public String getId() {
    return id;
  }

  /**
   * Getter for the label index
   *
   * @return index of the player
   * @author robert.kratz
   */
  public int getLabelIndex() {
    return labelIndex;
  }

  /**
   * Getter for the team index
   *
   * @return index of the team
   */
  public int getTeamIndex() {
    return teamIndex;
  }

  /**
   * Getter for the player color
   *
   * @return color of the player
   * @author robert.kratz
   */
  public String getColor() {
    return color;
  }

  /**
   * Returns a string representation of the object
   *
   * @return string representation of the object
   * @author robert.kratz
   */
  @Override
  public String toString() {
    return "GamePlayerLabelInfo{" +
        "playerName='" + playerName + '\'' +
        ", id='" + id + '\'' +
        ", teamIndex=" + teamIndex +
        ", labelIndex=" + labelIndex +
        ", color='" + color + '\'' +
        '}';
  }
}
