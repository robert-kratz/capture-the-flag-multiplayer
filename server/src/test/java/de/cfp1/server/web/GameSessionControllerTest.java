package de.cfp1.server.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.cfp1.server.game.CtfGame;
import de.cfp1.server.game.exceptions.ForbiddenMove;
import de.cfp1.server.game.exceptions.GameSessionNotFound;
import de.cfp1.server.game.exceptions.NoMoreTeamSlots;
import de.cfp1.server.game.map.*;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;
import de.cfp1.server.web.data.GameSessionRequest;
import de.cfp1.server.web.data.GameSessionResponse;
import de.cfp1.server.web.data.GiveupRequest;
import de.cfp1.server.web.data.JoinGameRequest;
import de.cfp1.server.web.data.JoinGameResponse;
import de.cfp1.server.web.data.MoveRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple system 'live' test for {@link GameSessionController}.
 *
 * @author gabriel.victor.arthur.himmelein
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GameSessionControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

    private Map<String, GameSession> gameSessions;
    private GameSessionController gameSessionController;
    private String sessionId = "1";
    private MoveRequest moveRequest;
    private MapTemplate mapTemplate;
    private CtfGame game;
    private GiveupRequest giveupRequest;

  /**
   * @author gabriel.victor.arthur.himmelein
   */
  @BeforeEach
    public void setup() {
        gameSessionController = new GameSessionController();
        gameSessions = gameSessionController.getGameSessions();

        this.moveRequest = new MoveRequest();
        this.moveRequest.setPieceId("p:1_0");
        this.moveRequest.setTeamId("1");
        this.moveRequest.setNewPosition(new int[]{4,3});

        this.mapTemplate = new MapTemplate();
        this.mapTemplate.setGridSize(new int[]{10, 10});  // Beispielwert für Gridgröße
        this.mapTemplate.setTeams(2);  // Beispielwert für Teams
        this.mapTemplate.setBlocks(4);  // Beispielwert für Blocks
        this.mapTemplate.setPlacement(PlacementType.symmetrical);  // Beispielwert für Placement
        this.mapTemplate.setFlags(2);  // Beispielwert für Flags

        Directions directions = new Directions(3, 3, 3, 3, 0, 0, 0, 0);
        PieceDescription pd1 = new PieceDescription("Rook", 3, 3, new Movement(directions, null));

        mapTemplate.setPieces(new PieceDescription[]{pd1});

        // Einrichtung einer gültigen Spielsitzung
        game = new CtfGame();
        Team team = new Team();
        Piece piece = new Piece();

        piece.setDescription(pd1);
        piece.setPosition(new int[]{3,3});
        piece.setId("p:1_0");
        piece.setTeamId("1");

        team.setPieces(new Piece[]{piece});
        team.setId("1");

        GameState gameState = game.create(mapTemplate);
        gameState.setTeams(new Team[]{team});
        gameState.setCurrentTeam(0);
        gameState.setGrid(this.setSpecificGrid());

        game.setGameState(gameState);
        GameSession gameSession = new GameSession(game);
        gameSessionController.getGameSessions().put(sessionId, gameSession);

        String secret = gameSession.createTeamSecret("1");
        moveRequest.setTeamSecret(secret);

        giveupRequest = new GiveupRequest();
        giveupRequest.setTeamId("1");
        giveupRequest.setTeamSecret(secret);
    }

    public String[][] setSpecificGrid() {
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
        return grid;
    }

    /**
     * Test the creation of a game session.
     * @author gabriel.victor.arthur.himmelein
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateGameSession() throws Exception {
        // create a new game session
        GameSessionRequest gameSessionRequest = new GameSessionRequest();
        gameSessionRequest.setTemplate(createGameTemplate());

        GameSessionResponse gameSession = restTemplate.postForObject("http://localhost:" + port + "/api/gamesession",
            gameSessionRequest, GameSessionResponse.class);
        assertNotNull(gameSession.getId());

        String gameSessionId = gameSession.getId();

        // check game session
        gameSession = restTemplate.getForObject("http://localhost:" + port + "/api/gamesession/{gameSessionId}",
            GameSessionResponse.class, gameSessionId);
        assertEquals(gameSessionId, gameSession.getId());

        // check game state
        GameState gameState = restTemplate.getForObject("http://localhost:" + port + "/api/gamesession/{gameSessionId}/state",
            GameState.class, gameSessionId);

        // let two teams join
        JoinGameRequest team1 = new JoinGameRequest();
        team1.setTeamId("team1");
        JoinGameResponse joinGameResponse1 = restTemplate.postForObject("http://localhost:" + port + "/api/gamesession/{gameSessionId}/join",
            team1, JoinGameResponse.class, gameSessionId);
        JoinGameRequest team2 = new JoinGameRequest();
        team2.setTeamId("team2");
        JoinGameResponse joinGameResponse2 = restTemplate.postForObject("http://localhost:" + port + "/api/gamesession/{gameSessionId}/join",
            team2, JoinGameResponse.class, gameSessionId);

        // check game state again
        gameState = restTemplate.getForObject("http://localhost:" + port + "/api/gamesession/{gameSessionId}/state",
            GameState.class, gameSessionId);

        // make some fake move
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setTeamId(joinGameResponse1.getTeamId());
        moveRequest.setTeamSecret(joinGameResponse1.getTeamSecret());
        moveRequest.setPieceId("somepieceid");
        moveRequest.setNewPosition(new int[]{1,2});

        restTemplate.postForObject("http://localhost:" + port + "/api/gamesession/{gameSessionId}/move",
            moveRequest, void.class, gameSessionId);

        // give up
        GiveupRequest giveupRequest = new GiveupRequest();
        giveupRequest.setTeamId(joinGameResponse1.getTeamId());
        giveupRequest.setTeamSecret(joinGameResponse1.getTeamSecret());

        restTemplate.postForObject("http://localhost:" + port + "/api/gamesession/{gameSessionId}/giveup",
            giveupRequest, void.class, gameSessionId);

        // delete game
        restTemplate.delete("http://localhost:" + port + "/api/gamesession/{sessionId}", gameSessionId);
    }

    /**
     * Test the creation of a game session with too many players trying to join.
     * @author gabriel.victor.arthur.himmelein
     * @throws Exception if an error occurs
     */
    @Test
    public void testTooManyJoin() throws Exception {
    // create a new game session
    GameSessionRequest gameSessionRequest = new GameSessionRequest();
    gameSessionRequest.setTemplate(createGameTemplate());

    GameSessionResponse gameSession = restTemplate.postForObject(
        "http://localhost:" + port + "/api/gamesession",
        gameSessionRequest, GameSessionResponse.class);
    assertNotNull(gameSession.getId());

    String gameSessionId = gameSession.getId();

    // check game session
    gameSession = restTemplate.getForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}",
        GameSessionResponse.class, gameSessionId);
    assertEquals(gameSessionId, gameSession.getId());

    // check game state
    GameState gameState = restTemplate.getForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}/state",
        GameState.class, gameSessionId);

    // let two teams join
    JoinGameRequest team1 = new JoinGameRequest();
    team1.setTeamId("team1");
    JoinGameResponse joinGameResponse1 = restTemplate.postForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}/join",
        team1, JoinGameResponse.class, gameSessionId);
    JoinGameRequest team2 = new JoinGameRequest();
    team2.setTeamId("team2");
    JoinGameResponse joinGameResponse2 = restTemplate.postForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}/join",
        team2, JoinGameResponse.class, gameSessionId);

    // check game state again
    gameState = restTemplate.getForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}/state",
        GameState.class, gameSessionId);

    // make some fake move
    MoveRequest moveRequest = new MoveRequest();
    moveRequest.setTeamId(joinGameResponse1.getTeamId());
    moveRequest.setTeamSecret(joinGameResponse1.getTeamSecret());
    moveRequest.setPieceId("somepieceid");
    moveRequest.setNewPosition(new int[]{1, 2});

    restTemplate.postForObject("http://localhost:" + port + "/api/gamesession/{gameSessionId}/move",
        moveRequest, void.class, gameSessionId);

    // give up
    GiveupRequest giveupRequest = new GiveupRequest();
    giveupRequest.setTeamId(joinGameResponse1.getTeamId());
    giveupRequest.setTeamSecret(joinGameResponse1.getTeamSecret());

    restTemplate.postForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}/giveup",
        giveupRequest, void.class, gameSessionId);

    // delete game
    restTemplate.delete("http://localhost:" + port + "/api/gamesession/{sessionId}", gameSessionId);
  }

  /**
   * Test the creation of a game session with too many players trying to join.
   *
   * @throws Exception if an error occurs
   * @author gabriel.victor.arthur.himmelein
   */
  @Test
  void testToManyJoin() throws Exception {
    // create a new game session
    GameSessionRequest gameSessionRequest = new GameSessionRequest();
    gameSessionRequest.setTemplate(createGameTemplate());

    GameSessionResponse gameSession = restTemplate.postForObject(
        "http://localhost:" + port + "/api/gamesession",
        gameSessionRequest, GameSessionResponse.class);
    assertNotNull(gameSession.getId());

    String gameSessionId = gameSession.getId();

    // check game session
    gameSession = restTemplate.getForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}",
        GameSessionResponse.class, gameSessionId);
    assertEquals(gameSessionId, gameSession.getId());

    // check game state
    GameState gameState = restTemplate.getForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}/state",
        GameState.class, gameSessionId);

    // let two teams join
    JoinGameRequest team1 = new JoinGameRequest();
    team1.setTeamId("team1");
    JoinGameResponse joinGameResponse1 = restTemplate.postForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}/join",
        team1, JoinGameResponse.class, gameSessionId);
    JoinGameRequest team2 = new JoinGameRequest();
    team2.setTeamId("team2");
    JoinGameResponse joinGameResponse2 = restTemplate.postForObject(
        "http://localhost:" + port + "/api/gamesession/{gameSessionId}/join",
        team2, JoinGameResponse.class, gameSessionId);

    try {
      JoinGameRequest team3 = new JoinGameRequest();
      team3.setTeamId("team3");
      JoinGameResponse joinGameResponse3 = restTemplate.postForObject(
          "http://localhost:" + port + "/api/gamesession/{gameSessionId}/join",
          team2, JoinGameResponse.class, gameSessionId);

      if (joinGameResponse3 != null) { //if no return value, throw:
        throw new NoMoreTeamSlots();
      }

      fail("Expected an NoMoreSlots to be thrown");
    } catch (Exception e) {
      System.out.println("Player limit reached");
    }
  }

    /** makeMove method test cases **/

  /**
   * @author gabriel.victor.arthur.himmelein
   */
  @Test
    public void testMakeValidMove() {
        try {
            gameSessionController.makeMove(sessionId, moveRequest);
            // Erwartetes Verhalten: Kein Fehler geworfen
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

  /**
   * @author gabriel.victor.arthur.himmelein
   */
  @Test
    public void testMakeInvalidMove() {
        moveRequest.setNewPosition(new int[]{0,0});
        try {
            gameSessionController.makeMove(sessionId, moveRequest);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // Erwartetes Verhalten: Fehler geworfen
        }
    }

  /**
   * @author gabriel.victor.arthur.himmelein
   */
  @Test
    public void testMakeMove_SessionNotFound() {
        try {
            sessionId = "2";
            // Versuch, einen Zug mit einer nicht existierenden Session durchzuführen
            gameSessionController.makeMove(sessionId, moveRequest);

            fail("Should have thrown an exception");
        } catch (GameSessionNotFound e) {
            // Erwartetes Verhalten: Fehler geworfen
        } catch (Exception e) {
            fail("Should have thrown a GameSessionNotFound exception");
        }
    }

    // Test zur Überprüfung des verbotenen Zuges

  /**
   * @author gabriel.victor.arthur.himmelein
   */
    @Test
    public void testMakeMove_ForbiddenMove() {
        try {
            GameSession gameSession = new GameSession(game) {
                @Override
                public boolean isAllowed(String teamId, String teamSecret) {
                    // Simulieren, dass der Zug nicht erlaubt ist
                    return false;
                }
            };

            gameSessionController.getGameSessions().put(sessionId, gameSession);
            gameSessionController.makeMove(sessionId, moveRequest);

            fail("Should have thrown an exception");
        } catch (ForbiddenMove e) {
            // Erwartetes Verhalten: Fehler geworfen
        } catch (Exception e) {
            fail("Should have thrown a ForbiddenMove exception");
        }
    }

    /** giveUp method test cases **/

  /**
   * @author gabriel.victor.arthur.himmelein
   */
  @Test
    public void testGiveUp_SessionNotFound() {
        try {
            sessionId = "2";
            // Versuch, das Spiel für eine nicht existierende Session aufzugeben
            gameSessionController.giveUp(sessionId, giveupRequest);

            fail("Should have thrown an exception");
        } catch (GameSessionNotFound e) {
            // Erwartetes Verhalten: Fehler geworfen
        } catch (Exception e) {
            fail("Should have thrown a GameSessionNotFound exception");
        }
    }

  /**
   * @author gabriel.victor.arthur.himmelein
   */
  @Test
    public void testGiveUp_ValidGiveUp() {
        try {
            gameSessionController.giveUp(sessionId, giveupRequest);
            // Erwartetes Verhalten: Kein Fehler geworfen
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

  /**
   * @author gabriel.victor.arthur.himmelein
   */
  @Test
    public void testGiveUp_ForbiddenMove() {
        try {
            giveupRequest.setTeamId("2");
            gameSessionController.giveUp(sessionId, giveupRequest);

            fail("Should have thrown an exception");
        } catch (ForbiddenMove e) {
            // Erwartetes Verhalten: Fehler geworfen
        } catch (Exception e) {
            fail("Should have thrown a ForbiddenMove exception");
        }
    }

    /**
     * Create a game template.
     * @author markus.kessel, robert.kratz, @author gabriel.victor.arthur.himmelein
     * @return the game template
     * @throws IOException if an error occurs
     */
    public MapTemplate createGameTemplate() throws IOException {
        return new ObjectMapper().readValue(
            getClass().getResourceAsStream("/maptemplates/10x10_2teams_example.json"), MapTemplate.class);
    }
}

