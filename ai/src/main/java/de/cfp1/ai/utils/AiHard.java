package de.cfp1.ai.utils;

import de.cfp1.client.game.GameHandler;
import de.cfp1.server.game.state.Move;
import de.cfp1.server.game.state.Piece;

import java.util.ArrayList;

public class AiHard {

  AiHelper aiHelper;
  AiEvaluator aiEvaluator;
  private ArrayList<EvaluatedPiece> evalPieces = new ArrayList<>();

  /**
   * Constructor
   *
   * @param gameHandler GameHandler
   * @author Joel Bakirel
   */
  public AiHard(GameHandler gameHandler) {
    aiHelper = new AiHelper(gameHandler);
    aiEvaluator = new AiEvaluator(gameHandler);

    Piece[] myPieces = aiHelper.getMyTeam().getPieces();
    for (Piece p : myPieces) {
      EvaluatedPiece ePiece = new EvaluatedPiece(p);
      evalPieces.add(ePiece);
    }
  }

  /**
   * Calculate the hard move
   *
   * @return Move
   * @author Joel Bakirel
   */
  public Move calcHardMove() {
    evalPieces = aiHelper.filterDeadPieces(evalPieces);
    Move move = new Move();
    //Evaluate all pieces
    evalPieces();
    for (EvaluatedPiece ePiece : evalPieces) {
      ArrayList<int[]> validMoves = aiHelper.getValidMoves(ePiece.getPiece());
      for (int[] movePos : validMoves) {
        if (aiHelper.canCaptureFlag(movePos)) {
          move.setPieceId(ePiece.getPiece().getId());
          move.setNewPosition(movePos);
          move.setTeamId(ePiece.getPiece().teamId);
          return move;
        }
        if (aiHelper.canCaptureEnemy(ePiece.getPiece(), movePos)) {
          move.setPieceId(ePiece.getPiece().getId());
          move.setNewPosition(movePos);
          move.setTeamId(ePiece.getPiece().teamId);
          return move;
        }
      }
    }
    //Get worst Piece
    EvaluatedPiece worstPiece = aiHelper.getWorstPiece(evalPieces);
    ArrayList<int[]> validMoves = aiHelper.getValidMoves(worstPiece.getPiece());
    int value = worstPiece.getValue();
    for (int[] movePos : validMoves) {
      worstPiece.getPiece().setPosition(movePos);
      worstPiece.setDisFlag(aiEvaluator.calcDisFlag(worstPiece.getPiece()));
      worstPiece.setMobility(aiEvaluator.calcMobility(worstPiece.getPiece()));
      worstPiece.setMobility(aiEvaluator.calcDefensiveValue(worstPiece.getPiece()));
      worstPiece.setMobility(aiEvaluator.calcThreats(worstPiece.getPiece()));
      int newValue = worstPiece.getValue();
      if (value <= newValue) {
        move.setPieceId(worstPiece.getPiece().getId());
        move.setNewPosition(movePos);
        move.setTeamId(worstPiece.getPiece().teamId);
        value = worstPiece.getValue();
      } else {
        move.setPieceId(worstPiece.getPiece().getId());
        move.setNewPosition(movePos);
        move.setTeamId(worstPiece.getPiece().teamId);
      }
    }
    return move;
  }

  /**
   * Evaluate all pieces and fill the evalPieces list
   *
   * @author Joel Bakirel
   */
  private void evalPieces() {
    //Eval Pieces
    for (EvaluatedPiece ePiece : evalPieces) {
      ePiece.setDisFlag(aiEvaluator.calcDisFlag(ePiece.getPiece()));
      ePiece.setMobility(aiEvaluator.calcMobility(ePiece.getPiece()));
      ePiece.setDefensiveValue(aiEvaluator.calcDefensiveValue(ePiece.getPiece()));
      ePiece.setThreats(aiEvaluator.calcThreats(ePiece.getPiece()));
    }
  }
}
