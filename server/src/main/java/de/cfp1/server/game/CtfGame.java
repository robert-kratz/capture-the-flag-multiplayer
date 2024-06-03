package de.cfp1.server.game;

import de.cfp1.server.Server;
import de.cfp1.server.game.exceptions.InvalidMove;
import de.cfp1.server.game.exceptions.NoMoreTeamSlots;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.map.PieceDescription;
import de.cfp1.server.game.map.PlacementType;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Move;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;

import de.cfp1.server.web.data.StatsResponse;
import java.util.*;

/**
 * @author robert.kratz, virgil.baclanov
 */

public class CtfGame implements Game, Runnable {

  private Thread gameThread = new Thread(this);

  private final GameBoardHelper gameBoardHelper = new GameBoardHelper();
  private GameTeamHelper gameTeamHelper;
  private CurrentGameState currentGameState = CurrentGameState.LOBBY;

  private GameState gameState;
  private MapTemplate mapTemplate;

  private int totalMaxTime, totalMoveTime;
  private Date startTime, endTime, lastMoveBeginTime;
  private ArrayList<Team> originalTeams = new ArrayList<>();

  @Override
  public void run() {
    while (!this.currentGameState.equals(CurrentGameState.FINISHED) && !Thread.interrupted()) {

      if (isTimeOver()) {
        this.currentGameState = CurrentGameState.FINISHED;
        this.gameThread.interrupt();
        System.out.println("Game over by time");

        //TODO: set winner
      }

      boolean isThereTeamMove = this.gameBoardHelper.availableTeamMoves(this.gameState,
          this.gameState.getTeams());

      if (!isThereTeamMove) {
        this.currentGameState = CurrentGameState.FINISHED;
        this.gameThread.interrupt();
        System.out.println("Game over by no more moves");
      }

      Team currentTeam = this.gameState.getTeams()[this.gameState.getCurrentTeam()];

      if (currentTeam == null) {
        skipCurrentPlayer();
        continue;
      }

      if (this.getRemainingMoveTimeInSeconds() <= 0) {
        skipCurrentPlayer();
        System.out.println("Player skipped to next team " + this.gameState.getCurrentTeam());
      }

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    System.out.println("Game Over, calculating winner");

    //TODO: set statistics for all users
    updateStatistics();
  }

  /**
   * Create a new game
   *
   * @param template {@link MapTemplate}
   * @return {@link GameState}
   * @author robert.kratz
   */
  @Override
  public GameState create(MapTemplate template) {
    this.mapTemplate = template;

    this.totalMaxTime = template.getTotalTimeLimitInSeconds();
    this.totalMoveTime = template.getMoveTimeLimitInSeconds();

    this.gameTeamHelper = new GameTeamHelper(template.getTeams(), template.getFlags());
    this.gameState = new GameState();
    this.gameState.setCurrentTeam(0);

    this.gameState.setGrid(this.gameBoardHelper.createEmptyGrid(template.getGridSize()));
    this.gameState.setTeams(new Team[template.getTeams()]);

    //check if the grid is valid
    isGridValid(template); //throws IllegalArgumentException if the grid is not valid

    return this.gameState;
  }

  /**
   * Join a game
   *
   * @param teamId Team ID
   * @return Team
   * @author robert.kratz
   */
  @Override
  public Team joinGame(String teamId) {
    System.out.println("Joining game");
    if (isStarted()
        || this.gameTeamHelper.getTeamsOnline(this.gameState) >= this.mapTemplate.getTeams()) {
      System.out.println("Game already started or no more team slots available");
      throw new NoMoreTeamSlots();
    }

    this.gameState = this.gameTeamHelper.addTeam(this.gameState, teamId, this.mapTemplate);

    Team team = this.gameTeamHelper.getTeamById(this.gameState, teamId);

    System.out.println(
        "Team joined: " + teamId + " " + team.getColor() + " " + team.getBase()[0] + " "
            + team.getBase()[1] + " " + team.getFlag() + " " + team.getPieces().length);

    if (this.gameTeamHelper.getTeamsOnline(this.gameState)
        == this.mapTemplate.getTeams()) { //if all teams are in the game
      String[][] populatedGrid = this.gameBoardHelper.placePiecesAndBase(this.gameState,
          this.gameBoardHelper.createEmptyGrid(this.mapTemplate), this.gameState.getTeams(),
          this.mapTemplate);
      populatedGrid = this.gameBoardHelper.placeBlocks(populatedGrid, this.mapTemplate.getBlocks());
      //Print the grid to the console
      this.gameBoardHelper.printGridToConsole(populatedGrid); //DEBUG

      gameState.setGrid(populatedGrid);

      //init game
      this.startTime = new Date();
      this.endTime = new Date(startTime.getTime() + totalMaxTime * 1000L);
      this.lastMoveBeginTime = new Date();
      this.gameState.setCurrentTeam(0);

      for (Team bt : this.gameState.getTeams()) {
        if (bt != null) {
          originalTeams.add(bt);
        }
      }

      this.currentGameState = CurrentGameState.RUNNING;

      this.startTimer();

      System.out.println("Game started");
    }

    return team;
  }

  /**
   * Make a move
   *
   * @param move {@link Move}
   * @author robert.kratz
   * @author virgil.baclanov
   */
  @Override
  public void makeMove(Move move) {

    String pieceId = move.getPieceId();
    Piece piece = this.gameTeamHelper.getPieceById(this.gameState, pieceId);

    if (piece == null) {
      System.out.println("Piece not found");
      throw new InvalidMove();
    }

    Team team = this.gameTeamHelper.getTeamById(this.gameState, piece.getTeamId());

    if (team == null) {
      System.out.println("Team not found");
      throw new InvalidMove();
    }

    //check weather its the teams turn
    if (!team.getId().equals(this.gameState.getTeams()[this.gameState.getCurrentTeam()].getId())) {
      System.out.println("Not the teams turn");
      throw new InvalidMove(); //not users turn
    }

    //check if the move is valid
    if (!isValidMove(move)) {
      System.out.println("This is not valid move");
      throw new InvalidMove(); //invalid move
    }

    String objectOrigin = this.gameState.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]];
    String objectTarget = this.gameState.getGrid()[move.getNewPosition()[0]][move.getNewPosition()[1]];

    //Check attack power for kill
    if (!objectTarget.isEmpty() && this.gameBoardHelper.isPiece(objectTarget)) {
      if (!gameBoardHelper.canTakePiece(this.gameState, objectOrigin, objectTarget)) {
        System.out.println("Cannot take piece");
        throw new InvalidMove(); //cannot take piece
      } else {
        Piece targetPiece = this.gameTeamHelper.getPieceById(this.gameState, objectTarget);
        Team targetPieceTeam = this.gameTeamHelper.getTeamById(this.gameState,
            targetPiece.getTeamId());

        //change the gameState Grid
        String[][] grid = this.gameState.getGrid();
        grid[piece.getPosition()[0]][piece.getPosition()[1]] = "";
        grid[move.getNewPosition()[0]][move.getNewPosition()[1]] = objectOrigin;
        this.gameState.setGrid(grid);

        //update piece position in teams in game state
        piece.setPosition(move.getNewPosition());
        this.gameState = this.gameTeamHelper.updatePieceInTeam(this.gameState, piece);

        //delete the taken piece from the team
        targetPiece.setPosition(new int[]{-1, -1});
        this.gameState = this.gameTeamHelper.updatePieceInTeam(this.gameState, targetPiece);

        if (gameBoardHelper.getRemainingPiecesFromTeam(this.gameState, targetPieceTeam.getId())
            == 0) {
          this.gameState = this.gameBoardHelper.removeAllPiecesOfTeam(this.gameState,
              targetPieceTeam.getId());
          this.gameState = this.gameTeamHelper.removeTeam(this.gameState, targetPieceTeam.getId());

          String[][] removedTeamGrid = this.gameBoardHelper.removeTeamFromGrid(
              this.gameState.getGrid(), this.getGameTeamHelper()
                  .getIndexOfTeamId(this.gameState.getTeams(), targetPieceTeam.getId()));
          this.gameState.setGrid(removedTeamGrid);

          if (this.gameTeamHelper.getTeamsOnline(this.gameState) == 1) {
            this.currentGameState = CurrentGameState.FINISHED;
            this.gameThread.interrupt();
            System.out.println("Game over");
          }
        }
      }
    }

    //Block
    if (!objectTarget.isEmpty() && this.gameBoardHelper.isBlock(objectTarget)) {
      System.out.println("Cannot move to block");
      throw new InvalidMove();
    }

    if (objectTarget.isEmpty()) {
      String[][] grid = this.gameState.getGrid();
      grid[piece.getPosition()[0]][piece.getPosition()[1]] = "";
      grid[move.getNewPosition()[0]][move.getNewPosition()[1]] = objectOrigin;
      this.gameState.setGrid(grid);

      //update piece position in teams in game state
      piece.setPosition(move.getNewPosition());
      this.gameState = this.gameTeamHelper.updatePieceInTeam(this.gameState, piece);
    }

    //Flag
    if (!objectTarget.isEmpty() && this.gameBoardHelper.isFlag(objectTarget)) {

      //check if the flag is from the same team
      if (team.getBase()[0] == move.getNewPosition()[0]
          && team.getBase()[1] == move.getNewPosition()[1]) {
        System.out.println("Cannot take your own flag");
        throw new InvalidMove();
      }

      int flagCountTeam = this.gameTeamHelper.getFlagCount(this.gameState, team.getId());

      //get the flag of the team
      if (flagCountTeam > 1) {
        this.gameState = this.gameTeamHelper.setFlagCount(this.gameState, team.getId(),
            flagCountTeam - 1);

        System.out.println(
            "Base des aktuellen pieces: " + team.getBase()[0] + " " + team.getBase()[1]);
        int[] teleportCoordinates = this.gameBoardHelper.placePieceBackToBase(
            this.gameState.getGrid(), piece, team.getBase()[0], team.getBase()[1]);

        //update grid
        String[][] updatedGrid = this.gameState.getGrid();
        updatedGrid[piece.getPosition()[0]][piece.getPosition()[1]] = "";
        updatedGrid[teleportCoordinates[0]][teleportCoordinates[1]] = objectOrigin;
        this.gameState.setGrid(updatedGrid);

        //update piece position in teams in game state
        piece.setPosition(teleportCoordinates);
        this.gameState = this.gameTeamHelper.updatePieceInTeam(this.gameState, piece);

        System.out.println(
            "Flag taken from team " + team.getId() + " by piece " + piece.getId() + " at position "
                + piece.getPosition()[0] + " " + piece.getPosition()[1]);

      } else {
        int opponentTeamId = this.gameBoardHelper.getTeamIndexByFlagId(objectTarget);
        Team opponentTeam = this.gameState.getTeams()[opponentTeamId - 1];

        String[][] removedTeamGrid = this.gameBoardHelper.removeTeamFromGrid(
            this.gameState.getGrid(), opponentTeamId);
        this.gameState.setGrid(removedTeamGrid);

        String[][] updatedGrid = this.gameState.getGrid();
        updatedGrid[piece.getPosition()[0]][piece.getPosition()[1]] = "";
        updatedGrid[move.getNewPosition()[0]][move.getNewPosition()[1]] = objectOrigin;
        this.gameState.setGrid(updatedGrid);

        piece.setPosition(move.getNewPosition());
        this.gameState = this.gameTeamHelper.updatePieceInTeam(this.gameState, piece);

        this.gameState = this.gameBoardHelper.removeAllPiecesOfTeam(this.gameState,
            opponentTeam.getId());
        this.gameState = this.gameTeamHelper.removeTeam(this.gameState, opponentTeam.getId());

        int teamAmount = this.gameTeamHelper.getTeamsOnline(this.gameState);

        if (teamAmount == 1) {
          this.currentGameState = CurrentGameState.FINISHED;
          System.out.println("Game over");
          this.gameThread.interrupt();
        }
        System.out.println("Flag taken");
      }
    }

    this.gameState.setLastMove(move);
    this.lastMoveBeginTime = new Date();

    skipCurrentPlayer();
  }

  /**
   * Check if a move is valid
   *
   * @param move {@link Move}
   * @return boolean true if the move is valid, false otherwise
   * @author virgil.baclanov
   * @author robert.kratz
   */
  @Override
  public boolean isValidMove(Move move) {
    try {
      //check if the move is valid
      Piece currentPiece = this.gameTeamHelper.getPieceById(this.gameState, move.getPieceId());
      int xPiece = currentPiece.getPosition()[0];
      int yPiece = currentPiece.getPosition()[1];
      int xTarget = move.getNewPosition()[0];
      int yTarget = move.getNewPosition()[1];

      String objectOrigin = this.gameState.getGrid()[xPiece][yPiece];
      String objectTarget = this.gameState.getGrid()[xTarget][yTarget];

      if (currentPiece == null) {
        System.out.println("Invalid move: piece not found");
        return false;
      } else if (xTarget >= this.gameState.getGrid().length
          || yTarget >= this.gameState.getGrid()[0].length) {
        //check if the move is within the grid
        System.out.println("Invalid move: out of bounds");
        return false;
      } else if (this.gameBoardHelper.isPiece(objectTarget) && (xPiece == xTarget) && (yPiece
          == yTarget)) {
        return false;
      } else if (this.gameBoardHelper.isBlock(objectTarget)) {
        return false;
      } else {
        int deltaX = Math.abs(xPiece - xTarget);
        int deltaY = Math.abs(yPiece - yTarget);

        switch (currentPiece.getDescription().getType()) {
          case "Knight":
            if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
              if (this.gameBoardHelper.isPiece(objectTarget) || this.gameBoardHelper.isFlag(
                  objectTarget)) {
                return gameBoardHelper.isValidActionTarget(this.gameState, objectOrigin,
                    objectTarget);
              }
              return true;
            }
            return false;
          case "Pawn":
          case "Rook":
            //if the move is not vertical, nor horizontal, nor within reach
            if ((xPiece != xTarget) && (yPiece != yTarget)) {
              return false;
            } else if (!this.gameBoardHelper.isWithinReachVerticalHorizontal(xPiece, yPiece,
                currentPiece, xTarget, yTarget)) {
              return false;
            }

            //  if there is no obstacle between the two points, check if the target is a piece;
            // if the target is a piece, check if it is an enemy piece and if it has more or less attack power;

            else if (!this.gameBoardHelper.isObstacleBetweenVerticalHorizontal(xPiece, yPiece,
                xTarget, yTarget, this.gameState.getGrid())) {
              if (this.gameBoardHelper.isPiece(objectTarget) || this.gameBoardHelper.isFlag(
                  objectTarget)) {
                return gameBoardHelper.isValidActionTarget(this.gameState, objectOrigin,
                    objectTarget);
              }
              return true;
            }
            return false;
          case "Bishop":
            if (deltaX != deltaY) {
              return false;
            } else if (!this.gameBoardHelper.isWithinReachDiagonal(xPiece, yPiece, currentPiece,
                xTarget, yTarget)) {
              return false;
            } else if (!this.gameBoardHelper.isObstacleBetweenDiagonal(xPiece, yPiece, xTarget,
                yTarget, this.gameState.getGrid())) {
              if (this.gameBoardHelper.isPiece(objectTarget) || this.gameBoardHelper.isFlag(
                  objectTarget)) {
                return gameBoardHelper.isValidActionTarget(this.gameState, objectOrigin,
                    objectTarget);
              }
              return true;
            }
          case "King":
          case "Queen":
            //queen is essentially a combo of rook and bishop
            if ((xPiece != xTarget) && (yPiece != yTarget) && (deltaX != deltaY)) {
              return false;
            } else if (
                !this.gameBoardHelper.isWithinReachVerticalHorizontal(xPiece, yPiece, currentPiece,
                    xTarget, yTarget) || !this.gameBoardHelper.isWithinReachDiagonal(xPiece, yPiece,
                    currentPiece, xTarget, yTarget)) {
              return false;
            } else if (
                !this.gameBoardHelper.isObstacleBetweenVerticalHorizontal(xPiece, yPiece, xTarget,
                    yTarget, this.gameState.getGrid())
                    && !this.gameBoardHelper.isObstacleBetweenDiagonal(xPiece, yPiece, xTarget,
                    yTarget, this.gameState.getGrid())) {
              if (this.gameBoardHelper.isPiece(objectTarget) || this.gameBoardHelper.isFlag(
                  objectTarget)) {
                return gameBoardHelper.isValidActionTarget(this.gameState, objectOrigin,
                    objectTarget);
              }
              return true;
            }
          default:
            return false;
        }
      }
    } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Give up the game for a team
   *
   * @param teamId Team ID
   * @author robert.kratz
   */
  @Override
  public void giveUp(String teamId) {
    try {
      System.out.println(this.gameTeamHelper.getTeamsOnline(this.gameState));

      if (this.currentGameState == CurrentGameState.LOBBY) {
        this.gameState = this.gameTeamHelper.removeTeam(this.gameState, teamId);
        System.out.println("Team " + teamId + " gave up");
        return;
      }

      if (this.currentGameState == CurrentGameState.FINISHED) {
        return;
      }

      int teamIndex = this.gameTeamHelper.getIndexOfTeamId(this.gameState.getTeams(), teamId);

      System.out.println("Team index: " + teamIndex);

      if (teamIndex == -1) {
        return;
      }

      String[][] removedTeamGrid = this.gameBoardHelper.removeTeamFromGrid(this.gameState.getGrid(),
          teamIndex);

      System.out.println("removed from grid");
      for (String[] strings : removedTeamGrid) {
        for (String string : strings) {
          System.out.print(string + " ");
        }
        System.out.println();
      }
      System.out.println(".----------");

      this.gameState.setGrid(removedTeamGrid);
      this.gameState = this.gameTeamHelper.removeTeam(this.gameState, teamId);

      if (this.gameTeamHelper.getTeamsOnline(this.gameState) == 1) {
        this.currentGameState = CurrentGameState.FINISHED;
        this.gameThread.interrupt();
        System.out.println("Game over");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Check if the game is started
   *
   * @return boolean
   * @author robert.kratz
   */
  @Override
  public boolean isStarted() {
    return this.currentGameState == CurrentGameState.RUNNING
        || this.currentGameState == CurrentGameState.FINISHED;
  }

  /**
   * Check if the game is over
   *
   * @return boolean
   * @author robert.kratz
   */
  @Override
  public boolean isGameOver() {
    return this.currentGameState == CurrentGameState.FINISHED;
  }

  /**
   * Get the current game state
   *
   * @return {@link GameState}
   * @author robert.kratz
   */
  @Override
  public GameState getCurrentGameState() {
    return this.gameState;
  }

  /**
   * Get the remaining team slots in the game
   *
   * @return int
   * @author robert.kratz
   */
  @Override
  public int getRemainingTeamSlots() {
    return this.mapTemplate.getTeams() - this.gameTeamHelper.getTeamsOnline(this.gameState);
  }

  /**
   * Get the remaining game time in seconds
   *
   * @return int
   * @author robert.kratz
   */
  @Override
  public int getRemainingGameTimeInSeconds() {
    if (this.currentGameState == CurrentGameState.RUNNING) {
      return (int) (this.totalMaxTime - (new Date().getTime() - this.startTime.getTime()) / 1000);
    } else {
      return -1;
    }
  }

  /**
   * Get the remaining move time in seconds for the current team
   *
   * @return int
   * @author robert.kratz
   */
  @Override
  public int getRemainingMoveTimeInSeconds() {
    if (this.currentGameState == CurrentGameState.RUNNING) {
      return (int) (this.totalMoveTime
          - (new Date().getTime() - this.lastMoveBeginTime.getTime()) / 1000);
    } else {
      return -1;
    }
  }

  /**
   * Get the winner of the game
   *
   * @return String[]
   * @author robert.kratz
   */
  @Override
  public String[] getWinner() {
    if (this.currentGameState == CurrentGameState.FINISHED) {
      //return all remaining team ids as winners which are not null
      List<String> winners = new ArrayList<>();
      for (Team team : this.gameState.getTeams()) {
        if (team != null) {
          winners.add(team.getId());
        }
      }
      return winners.toArray(new String[0]);
    } else {
      return new String[0];
    }
  }

  /**
   * Get the started date of the game
   *
   * @return Date
   * @author robert.kratz
   */
  @Override
  public Date getStartedDate() {
    return this.startTime;
  }

  /**
   * Get the end date of the game
   *
   * @return Date
   * @author robert.kratz
   */
  @Override
  public Date getEndDate() {
    return this.endTime;
  }

  /**
   * Turn time limit in seconds (<= 0 if none)
   *
   * @return
   * @author robert.kratz
   */
  @Override
  public int getTurnTimeLimit() {
    //use the last move begin time to calculate the remaining move time to return the seconds
    return this.lastMoveBeginTime != null ? (int) (this.totalMoveTime
        - (new Date().getTime() - this.lastMoveBeginTime.getTime()) / 1000) : -1;
  }

  public void startTimer() {
    startTime = new Date();
    lastMoveBeginTime = new Date();
    endTime = new Date(startTime.getTime() + totalMaxTime * 1000L);

    this.gameState.setCurrentTeam(0);

    gameThread.start();
  }

  public boolean isTimeOver() {
    return new Date().after(endTime) || this.gameState.getTeams().length < 2;
  }

  public void skipCurrentPlayer() {
    //get the current team index from the game state, the array of teams can be a max length of four. if a team is null skip the team so the next team is the current team if the current team is the last team in the array the next team is the first team in the array
    int currentTeamIndex = this.gameState.getCurrentTeam();
    int nextTeamIndex = getNextTeamIndex(currentTeamIndex);

    this.gameState.setCurrentTeam(nextTeamIndex);
    this.lastMoveBeginTime = new Date();
  }

  private int getNextTeamIndex(int currentTeamIndex) {
    int nextTeamIndex = currentTeamIndex + 1;

    //if the next team index is greater than the length of the array of teams set the next team index to 0
    if (nextTeamIndex >= this.gameState.getTeams().length) {
      nextTeamIndex = 0;
    }

    //if the next team is null skip the team
    while (this.gameState.getTeams()[nextTeamIndex] == null) {
      nextTeamIndex++;
      if (nextTeamIndex >= this.gameState.getTeams().length) {
        nextTeamIndex = 0;
      }
    }
    return nextTeamIndex;
  }

  /**
   * Check if the game is full
   *
   * @param template {@link MapTemplate}
   * @author robert.kratz
   */
  public void isGridValid(MapTemplate template) {
    //check if the number of teams is between 2 and 4
    if (template.getTeams() < 2 || template.getTeams() > 4) {
      throw new IllegalArgumentException("The number of teams must be between 2 and 4");
    }

    //check if the board size is between 6 and 21
    if (template.getGridSize()[0] < 6 || template.getGridSize()[0] > 20
        || template.getGridSize()[1] < 6 || template.getGridSize()[1] > 20) {
      throw new IllegalArgumentException("The board size must be between 6 and 20");
    }

    if (template.getFlags() < 1) {
      throw new IllegalArgumentException("The number of flags must be at least 1");
    }

    if (template.getBlocks() < 0) {
      throw new IllegalArgumentException("The number of blocks must be at least 0");
    }

    //Check max grid size
    int totalTeams = template.getTeams();
    int gridX = template.getGridSize()[0], gridY = template.getGridSize()[1];
    int totalPiecesInTeams = template.getPieces().length;

    //check if the number of flags is too high for the number of teams
    //if(template.getFlags() > totalTeams) {
    //    throw new IllegalArgumentException("The number of flags is too high for the number of teams");
    //}
  }

  /**
   * Update the statistics of the game
   *
   * @author robert.kratz
   */
  public void updateStatistics() {
    try {
      String[] winners = getWinner();
      String[] losers = new String[originalTeams.size() - winners.length];

      for (String winner : winners) {
        System.out.println("Winner: " + winner);
      }

      for (String loser : losers) {
        System.out.println("Loser: " + loser);
      }

      boolean isDraw = winners.length > 1;

      int loserIndex = 0;
      for (Team originalTeam : originalTeams) {
        if (originalTeam != null) {
          boolean isWinner = false;
          for (String winner : winners) {
            if (originalTeam.getId().equals(winner)) {
              isWinner = true;
              break;
            }
          }
          if (!isWinner) {
            losers[loserIndex] = originalTeam.getId();
            loserIndex++;
          }
        }
      }

      for (String winner : winners) {
        try {
            if (winner != null && winner.length() <= 16) {
                continue; //prechek if the winner is a valid UUID
            }

          StatsResponse statsResponse = Server.getDbManager().getUserStatistics(winner);

            if (statsResponse == null) {
                continue;
            }

          if (isDraw) {
            statsResponse.setDraws(statsResponse.getDraws() + 1);
            statsResponse.setElo(statsResponse.getElo() + 15);
          } else {
            statsResponse.setWins(statsResponse.getWins() + 1);
            statsResponse.setElo(statsResponse.getElo() + 30);
          }
          Server.getDbManager().updateUserStatistics(statsResponse);

          System.out.println("Updated statistics for winner user " + winner);
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("User not found in database");
        }
      }

      for (String loser : losers) {
        try {
            if (loser != null && loser.length() <= 16) {
                continue; //prechek if the winner is a valid UUID
            }

          StatsResponse statsResponse = Server.getDbManager().getUserStatistics(loser);

            if (statsResponse == null) {
                continue;
            }

          statsResponse.setLosses(statsResponse.getLosses() + 1);
          statsResponse.setElo(statsResponse.getElo() - 15);

          Server.getDbManager().updateUserStatistics(statsResponse);

          System.out.println("Updated statistics for loser user " + loser);
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("User not found in database");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public GameState getGameState() {
    return this.gameState;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public GameTeamHelper getGameTeamHelper() {
    return gameTeamHelper;
  }

  public void setTotalMoveTime(int totalMoveTime) {
    this.totalMoveTime = totalMoveTime;
  }

  public void setTotalMaxTime(int totalMaxTime) {
    this.totalMaxTime = totalMaxTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public void setCurrentGameState(CurrentGameState currentGameState) {
    this.currentGameState = currentGameState;
  }

  public MapTemplate getMapTemplate() {
    return mapTemplate;
  }

  public Team getCurrentTeam() {
    return this.gameState.getTeams()[this.gameState.getCurrentTeam()];
  }

  public Date getStartTime() {
    return startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public Date getLastMoveBeginTime() {
    return lastMoveBeginTime;
  }

  public CurrentGameState getCurrentGameStateProperty() {
    return this.currentGameState;
  }
}
