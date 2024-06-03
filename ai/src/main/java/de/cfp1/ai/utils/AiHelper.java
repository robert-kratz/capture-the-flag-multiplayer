package de.cfp1.ai.utils;

import de.cfp1.client.game.GameHandler;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.map.Directions;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;

import java.util.*;

public class AiHelper {

  GameHandler gameHandler;
  GameState gameState;
  GameBoardHelper gameBoardHelper = new GameBoardHelper();

  /**
   * Constructor for AiHelper
   *
   * @param gameHandler
   * @author Joel Bakirel
   */
  public AiHelper(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
    this.gameState = gameHandler.getGameState();
    this.gameBoardHelper = new GameBoardHelper();
  }

  /**
   * Setter for GameHandler
   *
   * @param gameHandler
   * @author Joel Bakirel
   */
  public void setGameHandler(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
    this.gameState = gameHandler.getGameState();
  }

  /**
   * Setter for GameState
   *
   * @param gameState
   * @author Joel Bakirel
   */

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  /**
   * Getter for myTeam
   *
   * @return Team myTeam
   * @author Joel Bakirel
   */
  public Team getMyTeam() {
    return gameHandler.getMyTeam();
  }

  /**
   * Getter for enemyTeams
   *
   * @return ArrayList<Team> enemyTeams
   * @author Joel Bakirel
   */
  public ArrayList<Team> getEnemyTeams() {
    Team[] allTeams = getAllTeams();
    int myTeamId = gameHandler.getClientTeamId();
    ArrayList<Team> enemyTeams = new ArrayList<>();
    for (int i = 0; i < allTeams.length; i++) {
      if (i != myTeamId) {
        if (allTeams[i] != null) {
          enemyTeams.add(allTeams[i]);
        }
      }
    }
    return enemyTeams;
  }

  /**
   * Getter for allTeams
   *
   * @return Team[] allTeams
   * @author Joel Bakirel
   */
  public Team[] getAllTeams() {
    return gameState.getTeams();
  }

  /**
   * Getter for allEnemyPieces
   *
   * @return ArrayList<Piece> allEnemyPieces
   * @author Joel Bakirel
   */
  public ArrayList<Piece> getAllEnemyPieces() {
    ArrayList<Piece> allEnemyPieces = new ArrayList<>();
    ArrayList<Team> enemyTeams = getEnemyTeams();
    for (Team team : enemyTeams) {
      Collections.addAll(allEnemyPieces, team.getPieces());
    }
    return allEnemyPieces;
  }

  /**
   * Calculates the distance between two positions
   *
   * @param position1
   * @param position2
   * @return Distance between two positions
   * @author Joel Bakirel
   */
  public int calcDistance(int[] position1, int[] position2) {
    double dx = position1[0] - position2[0]; // Differenz in der x-Achse
    double dy = position1[1] - position2[1]; // Differenz in der y-Achse
    return (int) Math.ceil(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
  }

  /**
   * Checks if a piece has a higher attack power than another piece
   *
   * @param pawn1
   * @param pawn2
   * @return True if pawn1 has a higher attack power than pawn2, false otherwise
   * @author Joel Bakirel
   */
  public boolean hasHigherAtk(Piece pawn1, Piece pawn2) {
    int playerAtk = pawn1.getDescription().getAttackPower();
    int enemyAtk = pawn2.getDescription().getAttackPower();
    return playerAtk >= enemyAtk;
  }


  public int[][] getPieceMoves(Piece p) {
    int[] positon = p.getPosition();
    switch (getType(p)) {
      case "King":
        //Moves
        return new int[][]{{positon[0] + 1, positon[1] + 1}, {positon[0] + 1, positon[1]},
            {positon[0] + 1, positon[1] - 1},
            {positon[0], positon[1] + 1}, {positon[0], positon[1] - 1},
            {positon[0] - 1, positon[1] + 1}, {positon[0] - 1, positon[1]},
            {positon[0] - 1, positon[1] - 1}};
      case "Queen":
        return new int[][]{{positon[0] + 2, positon[1] + 2}, {positon[0] + 2, positon[1]},
            {positon[0] + 2, positon[1] - 2},
            {positon[0], positon[1] + 2}, {positon[0], positon[1] - 2},
            {positon[0] - 2, positon[1] + 2}, {positon[0] - 2, positon[1]},
            {positon[0] - 2, positon[1] - 2}, {positon[0] + 1, positon[1] + 1},
            {positon[0] + 1, positon[1]}, {positon[0] + 1, positon[1] - 1},
            {positon[0], positon[1] + 1}, {positon[0], positon[1] - 1},
            {positon[0] - 1, positon[1] + 1}, {positon[0] - 1, positon[1]},
            {positon[0] - 1, positon[1] - 1}};
      case "Rook":
        return new int[][]{{positon[0] + 2, positon[1]}, {positon[0], positon[1] - 2},
            {positon[0], positon[1] + 2},
            {positon[0] - 2, positon[1]}, {positon[0] + 1, positon[1]},
            {positon[0], positon[1] - 1}, {positon[0], positon[1] + 1},
            {positon[0] - 1, positon[1]}};
      case "Bishop":
        return new int[][]{{positon[0] + 2, positon[1] + 2}, {positon[0] + 2, positon[1] - 2},
            {positon[0] - 2, positon[1] + 2}, {positon[0] - 2, positon[1] - 2},
            {positon[0] + 1, positon[1] + 1}, {positon[0] + 1, positon[1] - 1},
            {positon[0] - 1, positon[1] + 1}, {positon[0] - 1, positon[1] - 1}};
      case "Knight":
        return new int[][]{{positon[0] + 2, positon[1] + 1}, {positon[0] + 2, positon[1] - 1},
            {positon[0] - 2, positon[1] + 1}, {positon[0] - 2, positon[1] - 1},
            {positon[0] + 1, positon[1] + 2},
            {positon[0] + 1, positon[1] - 2}, {positon[0] - 1, positon[1] + 2},
            {positon[0] - 1, positon[1] - 2}};
      case "Pawn":
        return new int[][]{{positon[0] + 1, positon[1]}, {positon[0], positon[1] - 1},
            {positon[0], positon[1] + 1},
            {positon[0] - 1, positon[1]}};
      default:
        Directions defaultDirections = p.getDescription().getMovement().getDirections();
        ArrayList<int[]> moves = new ArrayList<>();
        int left = defaultDirections.getLeft();
        int right = defaultDirections.getRight();
        int up = defaultDirections.getUp();
        int down = defaultDirections.getDown();
        int downLeft = defaultDirections.getDownLeft();
        int downRight = defaultDirections.getDownRight();
        int upLeft = defaultDirections.getUpLeft();
        int upRight = defaultDirections.getUpRight();
        //Fill array with all possible moves therefore iterate over all directions
        for (int i = 1; i < right; i++) {
          moves.add(new int[]{positon[0], positon[1] + i});
        }
        for (int i = 1; i < left; i++) {
          moves.add(new int[]{positon[0], positon[1] - i});
        }
        for (int i = 1; i < up; i++) {
          moves.add(new int[]{positon[0] - i, positon[1]});
        }
        for (int i = 1; i < down; i++) {
          moves.add(new int[]{positon[0] + i, positon[1]});
        }
        for (int i = 1; i < upRight; i++) {
          moves.add(new int[]{positon[0] - i, positon[1] + i});
        }
        for (int i = 1; i < upLeft; i++) {
          moves.add(new int[]{positon[0] - i, positon[1] - i});
        }
        for (int i = 1; i < downRight; i++) {
          moves.add(new int[]{positon[0] + i, positon[1] + i});
        }
        for (int i = 1; i < downLeft; i++) {
          moves.add(new int[]{positon[0] + i, positon[1] - i});
        }
        int[][] movesArray = new int[moves.size()][];
        movesArray = moves.toArray(movesArray);
        return movesArray;
    }
  }


  /**
   * Getter for type
   *
   * @param p
   * @return String type
   * @author Joel Bakirel
   */
  public String getType(Piece p) {
    return p.getDescription().getType();
  }

  /**
   * Getter for grid
   *
   * @return String[][] grid
   * @author Joel Bakirel
   */
  public String[][] getGrid() {
    return gameState.getGrid();
  }

  /**
   * Getter for allBlocks
   *
   * @return ArrayList<int [ ]> blocks
   * @author Joel Bakirel
   */
  public ArrayList<int[]> getAllBlocks() {
    ArrayList<int[]> blocks = new ArrayList<>();
    String[][] grid = getGrid();
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if ("b".equals(grid[i][j])) {
          blocks.add(new int[]{i, j});
        }
      }
    }
    return blocks;
  }

  /**
   * Checks if a piece is on a tile
   *
   * @param tile
   * @return True if a piece is on the tile, false otherwise
   * @author Joel Bakirel
   */
  public boolean isPieceOnTile(int[] tile) {
    String[][] grid = getGrid();
    Piece p = getPieceById(grid[tile[0]][tile[1]]);
    return p != null;
  }

  /**
   * Get piece on tile
   *
   * @param tile
   * @return Piece on tile
   * @author Joel Bakirel
   */
  public Piece getPieceByTile(int[] tile) {
    String[][] grid = getGrid();
    return getPieceById(grid[tile[0]][tile[1]]);
  }

  /**
   * Checks if a destination is out of bounds
   *
   * @param dest
   * @return True if destination is out of bounds, false otherwise
   * @author Joel Bakirel
   */
  public boolean isDestOutOfBounds(int[] dest) {
    String[][] grid = getGrid();
    return dest[0] < 0 || dest[0] >= grid.length || dest[1] < 0 || dest[1] >= grid[0].length;
  }


  public ArrayList<int[]> getAllBases() {
    ArrayList<int[]> bases = new ArrayList<>();
    Team[] teams = getAllTeams();
    for (Team team : teams) {
      if (!team.getId().equals(gameHandler.getMyTeam().getId())) {
        bases.add(team.getBase());
      }
    }
    return bases;
  }

  /**
   * Calculates the distance between a piece and the flag
   *
   * @param p
   * @return Distance between piece and flag
   */
  public int calculateDistanceBetweenPieceAndFlag(Piece p) {
    int[][] moves = getPieceMoves(p);
    int[] closestFlag = getClosestFlag(p);
    Piece flag = new Piece();
    flag.setPosition(closestFlag);
    int distance;
    int x = p.getPosition()[0];
    int y = p.getPosition()[1];
    int destX = closestFlag[0];
    int destY = closestFlag[1];
    String[][] grid = getGrid();
    switch (getType(p)) {
      case "Queen":
        if (!gameBoardHelper.isObstacleBetweenVerticalHorizontal(x, y, destX, destY, grid)
            && !gameBoardHelper.isObstacleBetweenDiagonal(x, y, destX, destY, grid)) {
          distance = calcDistance(p.getPosition(), flag.getPosition());
          distance -= 2;
        } else {
          distance = calcDistance(p.getPosition(), flag.getPosition()) + 2;
        }
        break;
      case "Rook":
        if (!gameBoardHelper.isObstacleBetweenVerticalHorizontal(x, y, destX, destY, grid)) {
          distance = calcDistance(p.getPosition(), flag.getPosition());
          distance -= 2;
        } else {
          distance = calcDistance(p.getPosition(), flag.getPosition()) + 2;
        }
        break;
      case "Knight":
        int[][] movesKnight = getPieceMoves(p);
        for (int[] move : movesKnight) {
          if (move[0] == closestFlag[0] && move[1] == closestFlag[1]) {
            distance = 0;
            break;
          }
          break;
        }
      case "Bishop":
        if (!gameBoardHelper.isObstacleBetweenDiagonal(x, y, destX, destY, grid)) {
          distance = calcDistance(p.getPosition(), flag.getPosition());
          distance -= 2;
        } else {
          distance = calcDistance(p.getPosition(), flag.getPosition()) + 2;
        }
        break;
      case "King":
        if (!gameBoardHelper.isObstacleBetweenVerticalHorizontal(x, y, destX, destY, grid)
            && !gameBoardHelper.isObstacleBetweenDiagonal(x, y, destX, destY, grid)) {
          distance = calcDistance(p.getPosition(), flag.getPosition());
          distance -= 1;
        } else {
          distance = calcDistance(p.getPosition(), flag.getPosition()) + 1;
        }
        break;
      case "Pawn":
        if (!gameBoardHelper.isObstacleBetweenVerticalHorizontal(x, y, destX, destY, grid)) {
          distance = calcDistance(p.getPosition(), flag.getPosition());
          distance -= 1;
        } else {
          distance = calcDistance(p.getPosition(), flag.getPosition()) + 1;
        }
        break;
      default:
        distance = 10;
        break;
    }
    return distance;
  }

  /**
   * Getter for closestFlag
   *
   * @param p
   * @return int[] closestFlag
   * @author Joel Bakirel
   */
  public int[] getClosestFlag(Piece p) {
    int[][] moves = getPieceMoves(p);
    ArrayList<int[]> bases = getAllBases();
    int[] closestFlag = null;
    int minDistance = Integer.MAX_VALUE;

    for (int[] move : moves) {
      for (int[] base : bases) {
        int distance = calcDistance(move, base);
        if (closestFlag == null || distance < minDistance) {
          minDistance = distance;
          closestFlag = base;
        }
      }
    }
    return closestFlag;
  }


  /**
   * Getter for pieceById
   *
   * @param id
   * @return Piece by id, null if no piece with the id exists
   */
  public Piece getPieceById(String id) {
    Team[] teams = getAllTeams();
    for (Team team : teams) {
      Piece[] pieces = team.getPieces();
      for (Piece piece : pieces) {
        if (piece.getId().equals(id)) {
          return piece;
        }
      }
    }
    return null;
  }

  /**
   * Get the worst piece from a list of evaluated pieces
   *
   * @param evalPieces
   * @return EvaluatedPiece
   * @author Joel Bakirel
   */
  public EvaluatedPiece getWorstPiece(ArrayList<EvaluatedPiece> evalPieces) {
    EvaluatedPiece worstPiece = evalPieces.get(0);
    for (EvaluatedPiece ePiece : evalPieces) {
      if (ePiece.getValue() < worstPiece.getValue()) {
        worstPiece = ePiece;
      }
    }
    return worstPiece;
  }

  /**
   * Checks if a flag is between a piece and its destination
   *
   * @param piece, dest
   * @return True if a flag is on tile
   * @return False if a flag is not on tile
   */
  public boolean isFlagOnTile(Piece piece, int[] tile) {
    int[] flag = getClosestFlag(piece);
    return Arrays.equals(flag, tile);
  }

  /**
   * Filter out illegal moves that would result own flag being captured
   *
   * @param piece
   * @return ArrayList<int [ ]> legalMoves
   * @author Joel Bakirel
   */
  public ArrayList<int[]> filterIllegalFlagMoves(Piece piece) {
    ArrayList<int[]> legalMoves = new ArrayList<>();
    int[][] moves = getPieceMoves(piece);
    int x = piece.getPosition()[0];
    int y = piece.getPosition()[1];
    int destX;
    int destY;
    String[][] grid = getGrid();
    for (int[] move : moves) {
      try {
        destX = move[0];
        destY = move[1];
        switch (getType(piece)) {
          case "King":
          case "Queen":
            if (!gameBoardHelper.isObstacleBetweenVerticalHorizontal(x, y, destX, destY, grid)) {
              legalMoves.add(move);
            }
            if (!gameBoardHelper.isObstacleBetweenDiagonal(x, y, destX, destY, grid)) {
              legalMoves.add(move);
            }
            break;
          case "Rook":
          case "Pawn":
            if (!gameBoardHelper.isObstacleBetweenVerticalHorizontal(x, y, destX, destY, grid)) {
              legalMoves.add(move);
            }
            break;
          case "Bishop":
            if (!gameBoardHelper.isObstacleBetweenDiagonal(x, y, destX, destY, grid)) {
              legalMoves.add(move);
            }
            break;
          case "Knight":
            if (!isFlagOnTile(piece, move) || !isBlockOnTile(move)) {
              legalMoves.add(move);
            }
            break;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        continue;
      }
    }
    return legalMoves;
  }

  /**
   * Get all valid moves from the current position of the piece
   *
   * @param piece
   * @return ArrayList<int [ ] [ ]>  all valid Moves
   * @author Joel Bakirel
   */
  public ArrayList<int[]> getValidMoves(Piece piece) {
    ArrayList<int[]> moves = filterIllegalFlagMoves(piece);
    ArrayList<int[]> validMoves = new ArrayList<>();
    int x = piece.getPosition()[0];
    int y = piece.getPosition()[1];
    int[] flag = getClosestFlag(piece);
    String[][] grid = getGrid();
    for (int[] move : moves) {
      // Only add the move if it does not go out of bounds
      if (!isDestOutOfBounds(move) && gameBoardHelper.canMoveTo(gameState.getTeams(), gameState, x,
          y, move[0], move[1])) {
        if (gameBoardHelper.isFlag(grid[move[0]][move[1]])) {
          validMoves.clear();
          validMoves.add(move);
          return validMoves;
        }
        validMoves.add(move);
      }
    }
    return validMoves;
  }

  /**
   * Check if a piece can capture a flag
   *
   * @param move
   * @return True if a piece can capture a flag, false otherwise
   */
  public boolean canCaptureFlag(int[] move) {
    String[][] grid = getGrid();
    return gameBoardHelper.isFlag(grid[move[0]][move[1]]);
  }

  public boolean canCaptureEnemy(Piece p, int[] move) {
    String[][] grid = getGrid();
    if (gameBoardHelper.isPiece(grid[move[0]][move[1]])) {
      Piece enemy = getPieceByTile(move);
      if (!p.getTeamId().equals(enemy.getTeamId())) {
        return gameBoardHelper.canTakePiece(gameState, p.getId(), enemy.getId());
      }
    }
    return false;
  }

  /**
   * Check if a block is on a tile
   *
   * @param tile
   * @return True if a block is on the tile, false otherwise
   * @author Joel Bakirel
   */
  public boolean isBlockOnTile(int[] tile) {
    ArrayList<int[]> allBlock = getAllBlocks();
    for (int[] block : allBlock) {
      if (Arrays.equals(block, tile)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Filter out dead pieces from the list of evaluated pieces
   *
   * @param evalPieces
   * @return ArrayList<EvaluatedPiece> evalPieces without dead pieces
   * @author Joel Bakirel
   */
  public ArrayList<EvaluatedPiece> filterDeadPieces(ArrayList<EvaluatedPiece> evalPieces) {
    ArrayList<EvaluatedPiece> alivePieces = new ArrayList<>();
    int[] dead = {-1, -1};
    for (EvaluatedPiece ePiece : evalPieces) {
      int[] pos = ePiece.getPiece().getPosition();
      if (!Arrays.equals(pos, dead)) {
        alivePieces.add(ePiece);
      }
    }
    return alivePieces;
  }
}
