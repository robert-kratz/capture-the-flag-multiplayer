package de.cfp1.ai.utils;

import de.cfp1.server.game.map.Directions;
import de.cfp1.server.game.map.PieceDescription;
import de.cfp1.server.game.state.Piece;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("The methods only work with a running server. Therefore, the method bodies of the methods to be tested are copied into the tests and not all methods can be tested with JUnit Tests.")
class AiHelperTest {

  @Test
  void calcDistance() {
    int[] position1 = {0, 0}; // Position des ersten Objekts
    int[] position2 = {1, 1}; // Position des zweiten Objekts

    //Body of the method calcDistance
    double dx = position1[0] - position2[0]; // Differenz in der x-Achse
    double dy = position1[1] - position2[1]; // Differenz in der y-Achse
    int result = (int) Math.ceil(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));

    assertEquals(2, result);
  }

  @Test
  void hasHigherAtk() {
    Piece pawn1 = new Piece();
    Piece pawn2 = new Piece();

    PieceDescription desc1 = new PieceDescription();
    PieceDescription desc2 = new PieceDescription();

    pawn1.setDescription(desc1);
    pawn2.setDescription(desc2);
    pawn1.getDescription().setAttackPower(5);
    pawn2.getDescription().setAttackPower(3);

    //Body of the method hasHigherAtk
    int playerAtk = pawn1.getDescription().getAttackPower();
    int enemyAtk = pawn2.getDescription().getAttackPower();
    boolean result = playerAtk >= enemyAtk;

    assertTrue(result);
  }

  @Test
  void getPieceMoves() {
    Piece pawn = new Piece();
    PieceDescription desc = new PieceDescription();
    desc.setType("Pawn");
    pawn.setDescription(desc);
    pawn.setPosition(new int[]{3, 3});

    //Body of the method getPieceMoves
    int[][] result;
    int[] positon = pawn.getPosition();
    switch (pawn.getDescription().getType()) {
      case "King":
        //Moves
        result = new int[][]{{positon[0] + 1, positon[1] + 1}, {positon[0] + 1, positon[1]},
            {positon[0] + 1, positon[1] - 1},
            {positon[0], positon[1] + 1}, {positon[0], positon[1] - 1},
            {positon[0] - 1, positon[1] + 1}, {positon[0] - 1, positon[1]},
            {positon[0] - 1, positon[1] - 1}};
        break;
      case "Queen":
        result = new int[][]{{positon[0] + 2, positon[1] + 2}, {positon[0] + 2, positon[1]},
            {positon[0] + 2, positon[1] - 2},
            {positon[0], positon[1] + 2}, {positon[0], positon[1] - 2},
            {positon[0] - 2, positon[1] + 2}, {positon[0] - 2, positon[1]},
            {positon[0] - 2, positon[1] - 2}, {positon[0] + 1, positon[1] + 1},
            {positon[0] + 1, positon[1]}, {positon[0] + 1, positon[1] - 1},
            {positon[0], positon[1] + 1}, {positon[0], positon[1] - 1},
            {positon[0] - 1, positon[1] + 1}, {positon[0] - 1, positon[1]},
            {positon[0] - 1, positon[1] - 1}};
        break;
      case "Rook":
        result = new int[][]{{positon[0] + 2, positon[1]}, {positon[0], positon[1] - 2},
            {positon[0], positon[1] + 2},
            {positon[0] - 2, positon[1]}, {positon[0] + 1, positon[1]},
            {positon[0], positon[1] - 1}, {positon[0], positon[1] + 1},
            {positon[0] - 1, positon[1]}};
        break;
      case "Bishop":
        result = new int[][]{{positon[0] + 2, positon[1] + 2}, {positon[0] + 2, positon[1] - 2},
            {positon[0] - 2, positon[1] + 2}, {positon[0] - 2, positon[1] - 2},
            {positon[0] + 1, positon[1] + 1}, {positon[0] + 1, positon[1] - 1},
            {positon[0] - 1, positon[1] + 1}, {positon[0] - 1, positon[1] - 1}};
      case "Knight":
        result = new int[][]{{positon[0] + 2, positon[1] + 1}, {positon[0] + 2, positon[1] - 1},
            {positon[0] - 2, positon[1] + 1}, {positon[0] - 2, positon[1] - 1},
            {positon[0] + 1, positon[1] + 2},
            {positon[0] + 1, positon[1] - 2}, {positon[0] - 1, positon[1] + 2},
            {positon[0] - 1, positon[1] - 2}};
        break;
      case "Pawn":
        result = new int[][]{{positon[0] + 1, positon[1]}, {positon[0], positon[1] - 1},
            {positon[0], positon[1] + 1},
            {positon[0] - 1, positon[1]}};
        break;
      default:
        Directions defaultDirections = pawn.getDescription().getMovement().getDirections();
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
        result = movesArray;
    }
    assertArrayEquals(new int[][]{{4, 3}, {3, 2}, {3, 4}, {2, 3}}, result);
  }

  @Test
  void getType() {
    Piece pawn = new Piece();
    PieceDescription desc = new PieceDescription();
    desc.setType("Pawn");
    pawn.setDescription(desc);
    assertEquals("Pawn", pawn.getDescription().getType());
  }


  @Test
  void getAllBlocks() {
    String[][] initGrid = new String[][]{{"", "", "b", "", ""}, {"", "b", "", "", ""},
        {"", "", "", "b", ""}, {"b", "", "", "", ""}, {"", "", "", "", "b"}};

    //Body of the method getAllBlocks
    ArrayList<int[]> blocks = new ArrayList<>();
    String[][] grid = initGrid;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if ("b".equals(grid[i][j])) {
          blocks.add(new int[]{i, j});
        }
      }
    }

    ArrayList<int[]> expected = new ArrayList<>();
    expected.add(new int[]{0, 2});
    expected.add(new int[]{1, 1});
    expected.add(new int[]{2, 3});
    expected.add(new int[]{3, 0});
    expected.add(new int[]{4, 4});

    for (int i = 0; i < blocks.size(); i++) {
      assertArrayEquals(expected.get(i), blocks.get(i));
    }
  }

  @Test
  void isPieceOnTile() {
    String[][] initGrid = new String[][]{{"p:1_1", "", "b", "", ""}, {"", "b", "", "", ""},
        {"", "", "", "b", ""}, {"b", "", "", "", ""}, {"", "", "", "", "b"}};

    int[] tile = {0, 0};

    //Body of the method isPieceOnTile
    String[][] grid = initGrid;
    char c = grid[tile[0]][tile[1]].charAt(0);
    boolean result = "p".equals(String.valueOf(c));

    assertTrue(result);
  }

  @Test
  void isDestOutOfBounds() {
    String[][] grid = new String[5][5];

    int dest[] = {6, 6};
    int dest2[] = {0, 0};

    //Body of the method isDestOutOfBounds
    boolean result =
        dest[0] < 0 || dest[0] >= grid.length || dest[1] < 0 || dest[1] >= grid[0].length;
    boolean result2 =
        dest2[0] < 0 || dest2[0] >= grid.length || dest2[1] < 0 || dest2[1] >= grid[0].length;

    assertTrue(result);
    assertFalse(result2);
  }


  @Test
  void getClosestFlag() {
    Piece pawn = new Piece();
    pawn.setPosition(new int[]{3, 3});
    int[][] moves = new int[][]{{4, 3}, {3, 2}, {3, 4}, {2, 3}};
    ArrayList<int[]> bases = new ArrayList<>();
    bases.add(new int[]{3, 4});
    bases.add(new int[]{0, 0});

    //Body of the method getClosestFlag
    int[] closestFlag = null;
    int minDistance = Integer.MAX_VALUE;
    for (int[] move : moves) {
      for (int[] base : bases) {
        double dx = move[0] - base[0]; // Differenz in der x-Achse
        double dy = move[1] - base[1]; // Differenz in der y-Achse
        int distance = (int) Math.ceil(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
        if (closestFlag == null || distance < minDistance) {
          minDistance = distance;
          closestFlag = base;
        }
      }
    }

    int expected[] = {3, 4};
    assertArrayEquals(expected, closestFlag);
  }

  @Test
  void getWorstPiece() {
    ArrayList<EvaluatedPiece> evalPieces = new ArrayList<>();
    Piece pawn1 = new Piece();
    Piece pawn2 = new Piece();
    Piece pawn3 = new Piece();

    EvaluatedPiece ePiece1 = new EvaluatedPiece(pawn1);
    EvaluatedPiece ePiece2 = new EvaluatedPiece(pawn2);
    EvaluatedPiece ePiece3 = new EvaluatedPiece(pawn3);

    ePiece1.setValue(5);
    ePiece2.setValue(3);
    ePiece3.setValue(7);

    evalPieces.add(ePiece1);
    evalPieces.add(ePiece2);
    evalPieces.add(ePiece3);

    //Body of the method getWorstPiece
    EvaluatedPiece worstPiece = evalPieces.get(0);
    for (EvaluatedPiece ePiece : evalPieces) {
      if (ePiece.getValue() < worstPiece.getValue()) {
        worstPiece = ePiece;
      }
    }

    EvaluatedPiece expected = ePiece2;
    assertEquals(expected, worstPiece);
  }

}