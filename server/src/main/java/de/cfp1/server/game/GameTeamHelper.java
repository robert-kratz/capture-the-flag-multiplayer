package de.cfp1.server.game;

import de.cfp1.server.game.exceptions.NoMoreTeamSlots;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.map.Movement;
import de.cfp1.server.game.map.PieceDescription;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author robert.kratz
 */

public class GameTeamHelper {

  private int totalTeamSlots, totalFlagsPerTeam, totalPiecesPerTeam;
  private final String[] colors = {"BLUE", "RED", "YELLOW", "GREEN"};

  /**
   * Constructor for the GameTeamHelper
   *
   * @param totalTeamSlots    the total amount of team slots
   * @param totalFlagsPerTeam the total amount of flags per team
   * @author robert.kratz
   */
  public GameTeamHelper(int totalTeamSlots, int totalFlagsPerTeam) {
    this.totalTeamSlots = totalTeamSlots;
    this.totalFlagsPerTeam = totalFlagsPerTeam;
  }

  /**
   * Add a team to the game state
   *
   * @param gameState   the current game state
   * @param teamId      the id of the team
   * @param mapTemplate the map template
   * @return the updated game state
   * @author robert.kratz, virgil.baclanov
   */
  public GameState addTeam(GameState gameState, String teamId, MapTemplate mapTemplate) {
    if (gameState.getTeams() == null) {
      gameState.setTeams(new Team[totalTeamSlots]);
    }

    if (getTeamsOnline(gameState) == totalTeamSlots) {
      throw new NoMoreTeamSlots();
    }

    this.totalPiecesPerTeam = this.getTotalPiecesForMapTemplate(mapTemplate.getPieces());

    Team newTeam = new Team();
    newTeam.setId(teamId);
    newTeam.setColor(colors[this.getTeamsOnline(gameState)]);
    newTeam.setFlag(totalFlagsPerTeam);
    newTeam.setBase(new int[]{0, 0}); //only temporary, will be overwritten by the game board helper

    Piece[] teamPieces = new Piece[totalPiecesPerTeam];

    int pieceId = 0;
    int teamIndex = getTeamsOnline(gameState);
    for (PieceDescription pieceDescription : mapTemplate.getPieces()) {
      int pieceTypeCountPerTeam = pieceDescription.getCount();

      while (pieceTypeCountPerTeam > 0) {
        Piece piece = new Piece();
        piece.setId("p:" + (teamIndex + 1) + "_" + pieceId);
        piece.setDescription(pieceDescription);
        piece.setTeamId(newTeam.getId());
        piece.setPosition(
            new int[]{0, 0}); //only temporary, will be overwritten by the game board helper
        teamPieces[pieceId++] = piece;

        pieceTypeCountPerTeam--;
      }
    }

    newTeam.setPieces(teamPieces);

    gameState = this.setTeamToIndexInArray(gameState, this.getTeamsOnline(gameState), newTeam);

    return gameState;
  }

  /**
   * Remove a team from the game state
   *
   * @param gameState the current game state
   * @param teamId    the id of the team
   * @return the updated game state
   * @author robert.kratz, virgil.baclanov
   */
  public GameState removeTeam(GameState gameState, String teamId) {
    Team[] teams = gameState.getTeams();
    for (int i = 0; i < teams.length; i++) {
        if (teams[i] == null) {
            continue;
        }
      if (teams[i].getId().equals(teamId)) {
        teams[i] = null;
        break;
      }
    }
    gameState.setTeams(teams);
    return gameState;
  }

  /**
   * Get the amount of flags a team has
   *
   * @param gameState the current game state
   * @param teamId    the id of the team
   * @return the amount of flags
   * @author robert.kratz
   */
  public int getFlagCount(GameState gameState, String teamId) {
    for (Team team : gameState.getTeams()) {
        if (team == null) {
            continue;
        }
      if (team.getId().equals(teamId)) {
        return team.getFlag();
      }
    }
    return -1;
  }

  /**
   * Set the amount of flags a team has
   *
   * @param gameState the current game state
   * @param teamId    the id of the team
   * @param flagCount the amount of flags
   * @return the updated game state
   * @author robert.kratz
   */
  public GameState setFlagCount(GameState gameState, String teamId, int flagCount) {
    for (Team team : gameState.getTeams()) {
        if (team == null) {
            continue;
        }
      if (team.getId().equals(teamId)) {
        team.setFlag(flagCount);
      }
    }
    return gameState;
  }

  /**
   * Get the amount of pieces a team has
   *
   * @return the amount of pieces
   * @author robert.kratz
   */
  public int getTotalPiecesPerTeam() {
    return this.totalPiecesPerTeam;
  }

  /**
   * Get the total amount of team slots
   *
   * @param mapTemplate the map template
   * @author robert.kratz
   */
  public void setTotalPiecesPerTeam(MapTemplate mapTemplate) {
    for (PieceDescription pieceDescription : mapTemplate.getPieces()) {
      this.totalPiecesPerTeam += pieceDescription.getCount();
    }
  }

  /**
   * Get the total amount of team slots
   *
   * @param totalTeamSlots the total amount of team slots
   * @author robert.kratz
   */
  public void setTotalTeamSlots(int totalTeamSlots) {
    this.totalTeamSlots = totalTeamSlots;
  }

  /**
   * Get the total amount of flags per team
   *
   * @param totalFlagsPerTeam
   * @author robert.kratz
   */
  public void setTotalFlagsPerTeam(int totalFlagsPerTeam) {
    this.totalFlagsPerTeam = totalFlagsPerTeam;
  }

  /**
   * Get the team by id
   *
   * @param gameState the current game state
   * @param teamId    the id of the team
   * @return the team
   * @author robert.kratz
   */
  public Team getTeamById(GameState gameState, String teamId) {
    for (Team team : gameState.getTeams()) {
      if (team == null) {
        continue;
      }
      if (team.getId().equals(teamId)) {
        return team;
      }
    }
    return null;
  }

  /**
   * Get the team by color
   *
   * @param gameState the current game state
   * @param pieceId   the id of the piece
   * @return the team
   * @author robert.kratz
   */
  public Piece getPieceById(GameState gameState, String pieceId) {
    for (Team team : gameState.getTeams()) {
      if (team == null) {
        continue;
      }
      for (Piece piece : team.getPieces()) {
        if (piece.getId().equals(pieceId)) {
          return piece;
        }
      }
    }
    return null;
  }

  /**
   * Get the piece by piece id
   *
   * @param gameState the current game state
   * @param pieceId   the id of the piece
   * @return the piece
   * @author robert.kratz
   */
  public Piece getPieceByPieceId(GameState gameState, String pieceId) {
    for (Team team : gameState.getTeams()) {
      if (team == null) {
        continue;
      }
      for (Piece piece : team.getPieces()) {
        if (piece == null) {
          continue;
        }
        if (piece.getId().equals(pieceId)) {
          return piece;
        }
      }
    }
    return null;
  }

  /**
   * Get the amount of teams which are not null
   *
   * @param gameState the current game state
   * @return the amount of teams
   * @author robert.kratz
   */
  public int getTeamsOnline(GameState gameState) {
    //when init, the array size is set to max team slots, now return the number of teams which are not null
    int count = 0;
    for (Team team : gameState.getTeams()) {
      if (team != null) {
        count++;
      }
    }
    return count;
  }

  /**
   * Get the index of the team in the array
   *
   * @param gameState the current game state
   * @param index     the index of the team
   * @param team      the team
   * @return the updated game state
   * @author robert.kratz
   */
  public GameState setTeamToIndexInArray(GameState gameState, int index, Team team) {
    Team[] teams = gameState.getTeams();

    teams[index] = team;
    gameState.setTeams(teams);

    return gameState;
  }

  /**
   * Get the index of the team in the array
   *
   * @param gameState the current game state
   * @param piece     the piece
   * @return the updated game state
   * @author robert.kratz
   */
  public GameState updatePieceInTeam(GameState gameState, Piece piece) {
    Team[] teams = gameState.getTeams();
    for (Team team : teams) {
        if (team == null) {
            continue;
        }
      for (int j = 0; j < team.getPieces().length; j++) {
        if (team.getPieces()[j].getId().equals(piece.getId())) {
          team.getPieces()[j] = piece;
          break;
        }
      }
    }
    gameState.setTeams(teams);
    return gameState;
  }

  /**
   * Get the total amount of pieces for a map template
   *
   * @param pieces the pieces of the map template
   * @return the total amount of pieces
   */
  public int getTotalPiecesForMapTemplate(PieceDescription[] pieces) {
    int i = 0;

    for (PieceDescription piece : pieces) {
      i += piece.getCount();
    }
    return i;
  }

  /**
   * Get the index of the team in the array
   *
   * @param teams  the teams
   * @param teamId the id of the team
   * @return the index of the team
   * @author robert.kratz
   */
  public int getIndexOfTeamId(Team[] teams, String teamId) {
    for (int i = 0; i < teams.length; i++) {
      if (teams[i] != null && teams[i].getId().equals(teamId)) {
        System.out.println("teamId: " + teams[i].getId() + " found at index: " + i);
        return i;
      }
    }
    return -1;
  }
}
