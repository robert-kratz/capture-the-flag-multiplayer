package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.server.game.CtfGame;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.map.*;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Team;
import javafx.scene.control.RadioButton;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import de.cfp1.server.entities.Map;

import java.util.Arrays;

@Disabled("These tests only work with a running server. For this purpose, the server must be started and the server URL must be set in the NetworkHandler.")
class EditMapControllerTest {

  EditMapController editMapController = new EditMapController();
  GameBoardHelper gameBoardHelper = new GameBoardHelper();
  private Map currentMap;  // Das ist deine Klasse, die "isEnoughPlace()" enthält.
  private boolean isNewMap;
  private Team[] teams;

  @BeforeEach
  public void setUp() {
    // Setze eine Standardkonfiguration für currentMap auf

    currentMap = new Map();
    isNewMap = true;  // Angenommen, eine neue Karte wird erstellt

    // Erstelle eine Beispielkarte
    currentMap = new Map();
    currentMap.setId("12345");
    currentMap.setMapTemplate(new MapTemplate());
    currentMap.setName("Test Map");

    // Simuliere ein MapTemplate mit einigen Beispielwerten
    MapTemplate mapTemplate = new MapTemplate();
    mapTemplate.setGridSize(new int[]{10, 10});
    mapTemplate.setTeams(2);
    mapTemplate.setTotalTimeLimitInSeconds(100);
    mapTemplate.setMoveTimeLimitInSeconds(100);
    mapTemplate.setFlags(1);
    mapTemplate.setBlocks(10);

    // Füge einige Stückbeschreibungen hinzu
    Directions directionsPawn = new Directions(1, 1, 1, 1, 0, 0, 0, 0);
    Directions directionsBishop = new Directions(0, 0, 0, 0, 2, 2, 2, 2);
    Directions directionsRook = new Directions(2, 2, 2, 2, 0, 0, 0, 0);
    Directions directionsQueen = new Directions(2, 2, 2, 2, 2, 2, 2, 2);
    Directions directionsKing = new Directions(1, 1, 1, 1, 1, 1, 1, 1);
    Movement movementPawn = new Movement(directionsPawn);
    Movement movementKnight = new Movement(new Shape());
    Movement movementBishop = new Movement(directionsBishop);
    Movement movementRook = new Movement(directionsRook);
    Movement movementQueen = new Movement(directionsQueen);
    Movement movementKing = new Movement(directionsKing);
    PieceDescription pawnDescription = new PieceDescription("Pawn", 1, 2, movementPawn);
    PieceDescription knightDescription = new PieceDescription("Knight", 3, 1, movementKnight);
    PieceDescription bishopDescription = new PieceDescription("Bishop", 3, 1, movementBishop);
    PieceDescription rookDescription = new PieceDescription("Rook", 4, 1, movementRook);
    PieceDescription queenDescription = new PieceDescription("Queen", 5, 1, movementQueen);
    PieceDescription kingDescription = new PieceDescription("King", 5, 1, movementKing);

    mapTemplate.setPlacement(PlacementType.symmetrical);

    mapTemplate.setPieces(new PieceDescription[]{
        pawnDescription, knightDescription, bishopDescription, rookDescription, queenDescription,
        kingDescription
    });

    currentMap.setMapTemplate(mapTemplate);

    this.teams = editMapController.getUpdatedTeam(currentMap);

    this.editMapController.setCurrentMap(currentMap);

  }

  @Test
  public void testIsEnoughPlace_true() {
    // Erwartet, dass genügend Platz ist
    boolean result = this.editMapController.isEnoughPlace();
    assertTrue(result, "Es sollte genügend Platz vorhanden sein.");
  }

  @Test
  public void testIsEnoughPlaceGridSize_false() {
    // Ändere die Gridgröße auf einen kleineren Wert, der nicht genug Platz bietet
    currentMap.getMapTemplate().setGridSize(new int[]{6, 6});
    boolean result = this.editMapController.isEnoughPlace();
    assertFalse(result, "Es sollte nicht genügend Platz vorhanden sein.");
  }

  @Test
  public void testIsEnoughPlaceMorePieces_false() {
    // pawn to 10 pieces
    currentMap.getMapTemplate().getPieces()[0].setCount(10);
    currentMap.getMapTemplate().setGridSize(new int[]{10, 10});
    boolean result = this.editMapController.isEnoughPlace();
    assertFalse(result, "Es sollte nicht genügend Platz vorhanden sein.");
  }

  @Test
  public void testIsEnoughPlace_nullPointer() {
    // Setze currentMap oder das MapTemplate auf null, um den NullPointerException zu simulieren
    currentMap.setMapTemplate(null);
    boolean result = editMapController.isEnoughPlace();
    assertFalse(result, "No map or maptemplate found.");
  }

  @Test
  public void testGetUpdatedGameStateGrid_correctOutput() {
    // Prüft, ob `getUpdatedGameStateGrid` einen gefüllten Grid zurückgibt
    String[][] emptyGrid = gameBoardHelper.createEmptyGrid(currentMap.getMapTemplate());
    String[][] updatedGrid = gameBoardHelper.placePiecesAndBase(new GameState(), emptyGrid, teams,
        currentMap.getMapTemplate());

    assertArrayEquals(updatedGrid,
        editMapController.getUpdatedGameStateGrid(this.currentMap, teams),
        "Das ausgefüllte Grid sollte dem erwarteten entsprechen.");
  }

  @Test
  public void testGetUpdatedGameStateGrid_nullMapTemplate() {
    // Testet, wenn die mapTemplate null ist
    this.currentMap.setMapTemplate(null);
    assertThrows(NullPointerException.class, () -> {
      gameBoardHelper.placePiecesAndBase(new GameState(), new String[10][10], teams,
          currentMap.getMapTemplate());
    }, "Wenn das MapTemplate null ist, sollte das Ergebnis auch null sein.");
  }

  @Test
  public void testGetUpdatedGameStateGrid_invalidTeam() {
    // Testet, ob das Verhalten korrekt ist, wenn ein Team fehlt
    teams[1] = null;  // Ein ungültiges Team
    assertThrows(NullPointerException.class, () -> {
      gameBoardHelper.placePiecesAndBase(new GameState(), new String[10][10], teams,
          currentMap.getMapTemplate());
    }, "Expected NullPointerException for non-existing team.");
  }

  @Test
  public void testSaveMap_mapNull() {
    // Testet, ob eine NullPointerException korrekt behandelt wird
    currentMap = null;  // Setzt die aktuelle Karte auf null
    assertFalse(editMapController.saveMap(),
        "Wenn die aktuelle Karte null ist, sollte das Speichern nicht erfolgreich sein.");
  }

  @Test
  public void testSaveMap_publicFlag() {
    // Testet, ob das öffentliche Flag korrekt gesetzt wird
    boolean mapPublic = true;  // Angenommen, die Karte soll öffentlich sein

    currentMap.setPublic(mapPublic);  // Setzt das öffentliche Flag
    editMapController.saveMap();  // Aufrufen der Methode

    assertTrue(currentMap.isPublic(),
        "Die Karte sollte öffentlich sein, wenn das Flag gesetzt ist.");
  }
}