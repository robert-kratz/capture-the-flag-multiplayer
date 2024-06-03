package de.cfp1.server.game;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import de.cfp1.server.entities.Map;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.map.*;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author gabriel.victor.arthur.himmelein
 */

@Disabled(
    "Start the server and run test manually, "
)
public class GameBoardHelperTest {

  private GameBoardHelper gameBoardHelper;
  private GameState gameState;
  private String[][] grid;
  private Team[] teams;
  private MapTemplate mapTemplate;
  private Map currentMap;
  private Piece[] pieces;

  @BeforeEach
  public void setUp() {
    gameBoardHelper = new GameBoardHelper();
    gameState = new GameState();
    // String mapTemplateName = Parameters.getResourceByName(Parameters.DEFAULT_MAP_TEMPLATE);
    // mapTemplate = new Gson().fromJson(mapTemplateName, MapTemplate.class);

    mapTemplate = new MapTemplate();
    mapTemplate.setGridSize(new int[]{10, 10});  // Beispielwert für Gridgröße
    mapTemplate.setTeams(2);  // Beispielwert für Teams
    mapTemplate.setBlocks(4);  // Beispielwert für Blocks
    mapTemplate.setPlacement(PlacementType.symmetrical);  // Beispielwert für Placement

    Directions directions = new Directions(3, 3, 3, 3, 0, 0, 0, 0);
    PieceDescription pd1 = new PieceDescription("Rook", 3, 3, new Movement(directions, null));

    // Füge einige Stückbeschreibungen hinzu
    PieceDescription pd2 = new PieceDescription("Pawn", 3, 3, new Movement());

    PieceDescription[] pds = new PieceDescription[2];
    pds[0] = pd1;
    pds[1] = pd2;

    pieces = new Piece[2];

    pieces[0] = new Piece();
    pieces[0].setDescription(pd1);

    pieces[1] = new Piece();
    pieces[1].setDescription(pd2);

    mapTemplate.setPieces(pds);
  }

  private void initializeTeams(int numberOfTeams) {
    teams = new Team[numberOfTeams];

    for (int i = 0; i < numberOfTeams; i++) {
      teams[i] = new Team();
      teams[i].setId(String.valueOf(i + 1)); // Team IDs are 1-based
      teams[i].setPieces(pieces);
    }
    grid = new String[mapTemplate.getGridSize()[0]][mapTemplate.getGridSize()[1]];
    for (String[] row : grid) {
      Arrays.fill(row, "");
    }
    gameState.setTeams(teams);
  }

  /**
   * placePiecesAndBase Tests
   **/

  @Test
  public void testPlacePiecesAndBaseForTwoTeams() {
    mapTemplate.setTeams(2);
    initializeTeams(2);
    String[][] resultGrid = gameBoardHelper.placePiecesAndBase(gameState, grid, teams, mapTemplate);

    // Assertions to check if the bases are placed correctly
    assertEquals("b:1", resultGrid[2][4]); // Expected position of team 1's base
    assertEquals("b:2", resultGrid[7][5]); // Expected position of team 2's base
    assertNotNull(teams[0].getBase());
    assertNotNull(teams[1].getBase());
  }

  @Test
  public void testPlacePiecesAndBaseForThreeTeams() {
    mapTemplate.setTeams(3);
    initializeTeams(3);
    String[][] resultGrid = gameBoardHelper.placePiecesAndBase(gameState, grid, teams, mapTemplate);

    // Assertions to check if the bases are placed correctly
    assertEquals("b:1", resultGrid[2][2]);
    assertEquals("b:2", resultGrid[7][2]);
    assertEquals("b:3", resultGrid[7][7]);
  }

  @Test
  public void testPlacePiecesAndBaseForFourTeams() {
    mapTemplate.setTeams(4);
    initializeTeams(4);
    String[][] resultGrid = gameBoardHelper.placePiecesAndBase(gameState, grid, teams, mapTemplate);

    // Assertions to check if the bases are placed correctly
    assertEquals("b:1", resultGrid[2][2]);
    assertEquals("b:2", resultGrid[7][2]);
    assertEquals("b:3", resultGrid[2][7]);
    assertEquals("b:4", resultGrid[7][7]);
  }    /** canMoveTo() method tests **/

  /**
   * canMoveTo() method tests
   **/

  public void setSpecificGrid() {
    String[][] grid = new String[][]{
        {"", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", ""},
        {"", "", "b:1", "", "", "", "", "b:3", "", ""},
        {"", "", "p:1_1", "p:1_0", "", "", "p:3_0", "p:3_1", "", ""},
        {"", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", ""},
        {"", "", "p:2_1", "p:2_0", "", "", "p:4_0", "p:4_1", "", ""},
        {"", "", "b:2", "", "", "", "", "b:4", "", ""},
        {"", "", "", "", "", "", "", "", "", ""},
        {"", "", "", "", "", "", "", "", "", ""}
    };
    gameState.setGrid(grid);
  }

  // Test für unerlaubte Züge (z.B. diagonale Bewegung eines Rooks)
  @Test
  public void testInvalidRookMoveDiagonal() {
    initializeTeams(4);

    pieces[0].setTeamId("1");
    pieces[0].setId("p:1_0");
    pieces[0].setPosition(new int[]{3, 3});
    teams[0].setPieces(new Piece[]{pieces[0]});

    assertFalse(gameBoardHelper.canMoveTo(teams, gameState, 3, 3, 4, 4));
  }

  @Test
  public void testInvalidRookMoveIndexOutOfBounds() {
    initializeTeams(4);
    setSpecificGrid();

    pieces[0].setTeamId("1");
    pieces[0].setId("p:1_0");
    pieces[0].setPosition(new int[]{3, 3});
    pieces[0].getDescription().getMovement().getDirections().setDown(11);
    teams[0].setPieces(new Piece[]{pieces[0]});

    assertFalse(gameBoardHelper.canMoveTo(teams, gameState, 3, 3, 11, 3));
  }

  @Test
  public void testInvalidRookMovePieceIsNull() {
    initializeTeams(4);
    setSpecificGrid();

    Piece piece = null;
    teams[0].setPieces(new Piece[]{piece});

    assertFalse(gameBoardHelper.canMoveTo(teams, gameState, 3, 3, 4, 3));
  }

  // Test für das Blockieren durch ein Hindernis
  @Test
  public void testRookBlockedByObstacle() {
    initializeTeams(4);
    setSpecificGrid();
    grid[4][3] = "b";
    gameState.setGrid(grid);

    pieces[0].setTeamId("1");
    pieces[0].setId("p:1_0");
    pieces[0].setPosition(new int[]{3, 3});
    teams[0].setPieces(new Piece[]{pieces[0]});

    assertFalse(gameBoardHelper.canMoveTo(teams, gameState, 3, 3, 5, 3));
  }


  @Test
  public void testValidRookMove() {
    setSpecificGrid();

    initializeTeams(4);
    pieces[0].setPosition(new int[]{3, 3});
    assertTrue(gameBoardHelper.canMoveTo(teams, gameState, 3, 3, 4, 3));
  }

  @Test
  public void testInvalidCapturePieceSameTeam() {
    setSpecificGrid();

    initializeTeams(4);
    pieces[0].setTeamId("1");
    pieces[1].setTeamId("1");
    pieces[0].setId("p:1_0");
    pieces[1].setId("p:1_1");
    pieces[0].setPosition(new int[]{3, 3});
    pieces[1].setPosition(new int[]{3, 2});
    assertFalse(gameBoardHelper.canMoveTo(teams, gameState, 3, 3, 3, 2));
  }

  @Test
  public void testInvalidCapturePieceAttackPower() {
    setSpecificGrid();

    initializeTeams(4);
    pieces[0].setTeamId("1");
    pieces[1].setTeamId("1");
    pieces[0].setId("p:1_0");
    pieces[1].setId("p:1_1");
    pieces[0].setPosition(new int[]{3, 3});
    pieces[1].setPosition(new int[]{3, 2});
    pieces[1].getDescription().setAttackPower(5);
    assertFalse(gameBoardHelper.canMoveTo(teams, gameState, 3, 3, 3, 2));
  }

  @Test
  public void testValidCapturePiece() {
    setSpecificGrid();

    initializeTeams(4);
    pieces[0].setTeamId("1");
    pieces[1].setTeamId("2");
    teams[0].setPieces(new Piece[]{pieces[0]});
    teams[1].setPieces(new Piece[]{pieces[1]});

    pieces[0].setId("p:1_0");
    pieces[1].setId("p:2_0");
    pieces[0].setPosition(new int[]{3, 3});
    pieces[1].setPosition(new int[]{6, 3});

    assertTrue(gameBoardHelper.canMoveTo(teams, gameState, 3, 3, 6, 3));
  }

  /**
   * placeBlocks Tests
   **/

  @Test
  public void testPlaceBlocksNullInput() {
    String[][] grid = null;
    try {
      this.gameBoardHelper.placeBlocks(grid, 5);
      fail("Should have thrown NullPointerException");
    } catch (NullPointerException e) {
      // Test passed - the expected exception was thrown
    }
  }

  @Test
  public void testPlaceBlocksEmptyGrid() {
    String[][] grid = new String[0][0];
    try {
      this.gameBoardHelper.placeBlocks(grid, 5);
      assertNotNull(grid, "Grid should not be null after method call");
    } catch (Exception e) {
      fail("No exception should be thrown for an empty grid");
    }
  }

  @Test
  public void testPlaceBlocksNormalOperation() {
    initializeTeams(4);

    try {
      grid = this.gameBoardHelper.placeBlocks(grid, 2);
      assertNotNull(grid, "Grid should not be null after method call");
      // Optionally, add more checks to verify that blocks are placed correctly
    } catch (Exception e) {
      fail("No exception should be thrown under normal conditions");
    }
  }

  /**
   * placePieceBackToBase Tests
   **/

  @Test
  public void testPlacePieceBackToBaseNormal() {
    initializeTeams(4);
    setSpecificGrid();

    pieces[0].setTeamId("1");
    teams[0].setPieces(new Piece[]{pieces[0]});

    pieces[0].setId("p:1_0");
    pieces[0].setPosition(new int[]{6, 6});

    int[] result = gameBoardHelper.placePieceBackToBase(grid, pieces[0], 1, 1);
    assertNotNull(result, "The method should return a non-null result");
    assertTrue(grid[result[0]][result[1]].contains("p:1_0"),
        "The grid should have the piece placed correctly.");
  }

  @Test
  public void testPlacePieceBackToBaseNullInput() {
    String[][] grid = null;
    Piece piece = null;
    assertNull(gameBoardHelper.placePieceBackToBase(grid, piece, 0, 0),
        "Expected null return for null inputs");
  }

  @Test
  public void testPlacePieceOutOfBounds() {
    initializeTeams(4);
    setSpecificGrid();

    pieces[0].setTeamId("1");
    teams[0].setPieces(new Piece[]{pieces[0]});

    pieces[0].setId("p:1_0");
    pieces[0].setPosition(new int[]{6, 6});

    assertNull(gameBoardHelper.placePieceBackToBase(grid, pieces[0], 11, 11),
        "Expected null output for out of bounds coordinates");
  }
}
