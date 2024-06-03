package de.cfp1.ai.utils;

import de.cfp1.server.game.state.Piece;

public class EvaluatedPiece {

  private Piece piece;
  private int disFlag;
  private int mobility;
  private int defensiveValue;
  private int threats;
  private int value;

  /**
   * Constructor
   *
   * @param piece piece to evaluate
   * @author Joel Bakirel
   */
  public EvaluatedPiece(Piece piece) {
    this.piece = piece;
    disFlag = 0;
    mobility = 0;
    value = 0;
    threats = 0;
    defensiveValue = 0;
  }

  /**
   * Constructor
   *
   * @param piece          piece to evaluate
   * @param disFlag        distance to flag
   * @param defensiveValue defense value
   * @param mobility       mobility value
   * @author Joel Bakirel
   */
  public EvaluatedPiece(Piece piece, int disFlag, int defensiveValue, int mobility, int threats) {
    this.piece = piece;
    this.disFlag = disFlag;
    this.mobility = mobility;
    this.defensiveValue = defensiveValue;
    this.threats = threats;
    setValue();
  }

  /**
   * Get the threats
   *
   * @return threats
   * @author Joel Bakirel
   */
  public int getThreats() {
    return threats;
  }

  /**
   * Get the defensive value
   *
   * @return defensive value
   * @author Joel Bakirel
   */
  public int getDefensiveValue() {
    return defensiveValue;
  }

  /**
   * Get the piece
   *
   * @return piece
   * @author Joel Bakirel
   */
  public Piece getPiece() {
    return piece;
  }

  /**
   * Get the value
   *
   * @return value
   * @author Joel Bakirel
   */
  public int getValue() {
    return value;
  }

  /**
   * Get the distance to flag
   *
   * @return distance to flag
   * @author Joel Bakirel
   */
  public int getDisFlag() {
    return disFlag;
  }

  /**
   * Get the mobility
   *
   * @return mobility
   * @author Joel Bakirel
   */
  public int getMobility() {
    return mobility;
  }

  /**
   * Set the threats
   *
   * @param threats threats
   * @author Joel Bakirel
   */
  public void setThreats(int threats) {
    this.threats = -threats;
    setValue();
  }

  /**
   * Set the distance to flag
   *
   * @param disFlag distance to flag
   * @author Joel Bakirel
   */
  public void setDisFlag(int disFlag) {
    this.disFlag = disFlag;
    setValue();
  }

  /**
   * Set the mobility
   *
   * @param mobility mobility
   * @return mobility
   * @author Joel Bakirel
   */
  public void setMobility(int mobility) {
    this.mobility = mobility;
    setValue();
  }

  /**
   * Set the defensive value
   *
   * @param defensiveValue
   * @author Joel Bakirel
   */
  public void setDefensiveValue(int defensiveValue) {
    this.defensiveValue = defensiveValue;
    setValue();
  }

  /**
   * Set the value
   *
   * @author Joel Bakirel
   */
  private void setValue() {
    value = disFlag + mobility + defensiveValue + threats;
  }

  /**
   * Set the value
   *
   * @param value value to set
   */
  public void setValue(int value) {
    this.value = value;
  }

  /**
   * Set the piece
   *
   * @param piece
   * @author Joel Bakirel
   */
  public void setPiece(Piece piece) {
    this.piece = piece;
    setValue();
  }
}
