package de.cfp1.ai.utils;

import de.cfp1.client.game.GameHandler;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;

import java.util.ArrayList;

public class AiEvaluator {

  AiHelper aiHelper;


  /**
   * Constructor for AiEvaluator
   *
   * @param gameHandler
   * @author Joel Bakirel
   */
  public AiEvaluator(GameHandler gameHandler) {
    aiHelper = new AiHelper(gameHandler);
  }

  /**
   * Evaluates the Threat value of a piece
   *
   * @param p Piece to evaluate
   * @return Threat value of piece
   * @author Joel Bakirel
   */
  public int calcThreats(Piece p) {
    int threats = 0;
    Team myTeam = aiHelper.getMyTeam();
    ArrayList<Team> enemyTeams = aiHelper.getEnemyTeams();
    ArrayList<Piece> opponentPieces = aiHelper.getAllEnemyPieces();
    //Calc distance between each piece and opponent
    //If piece is in range of opponent, add 1 to threats
    for (Piece piece : opponentPieces) {
      int[] position = piece.getPosition();
      int[] pPosition = p.getPosition();
      if (aiHelper.calcDistance(position, pPosition) <= 3) {
        if (!aiHelper.hasHigherAtk(p, piece)) {
          threats++;
        }
      }
    }
    return threats / 2;
  }

  /**
   * Evaluates the Defensive value of a piece
   *
   * @param p Piece to evaluate
   * @return Defensive value of piece
   * @author Joel Bakirel
   */
  public int calcDefensiveValue(Piece p) {
    int[] base = aiHelper.getMyTeam().getBase();
    int[] position = p.getPosition();
    int distance = aiHelper.calcDistance(base, position);
    if (distance == 0) {
      return 2;
    } else if (distance == 1) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * Evaluates the Mobility value of a piece
   *
   * @param p Piece to evaluate
   * @return Mobility value of piece
   * @author Joel Bakirel
   */
  public int calcMobility(Piece p) {
    try {
      int[][] moves = aiHelper.getPieceMoves(p);
      int mobility = moves.length;
      for (int[] move : moves) {
        if (aiHelper.isDestOutOfBounds(move)) {
          mobility--;
        }
        if (aiHelper.isPieceOnTile(move)) {
          Piece piece = aiHelper.getPieceByTile(move);
          if (piece.getTeamId().equals(p.getTeamId())) {
            mobility--;
          } else {
            if (aiHelper.hasHigherAtk(p, piece)) {
              mobility--;
            }
          }

        }
        if (aiHelper.isFlagOnTile(p, move)) {
          mobility--;
        }
      }
      return mobility;
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Evaluates the Distance to Flag value of a piece
   *
   * @param p Piece to evaluate
   * @return Distance to Flag value of piece
   * @author Joel Bakirel
   */
  public int calcDisFlag(Piece p) {
    int distance = aiHelper.calculateDistanceBetweenPieceAndFlag(p);
    int maxValue = 10;
    return (int) (maxValue / (distance + 1.0));
  }
}
