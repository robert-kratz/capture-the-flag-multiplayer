package de.cfp1.server.game;

import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.map.PieceDescription;
import de.cfp1.server.game.map.PlacementType;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author robert.kratz, virgil.baclanov
 */

public class GameBoardHelper {

  /**
   * Create an empty grid based on the grid size
   *
   * @param gridSize {@link int[]}
   * @return {@link String[][]}
   * @author robert.kratz
   */
  public String[][] createEmptyGrid(int[] gridSize) {
    String[][] grid = new String[gridSize[0]][gridSize[1]];
    for (int i = 0; i < gridSize[0]; i++) {
      for (int j = 0; j < gridSize[1]; j++) {
        grid[i][j] = "";
      }
    }
    return grid;
  }

  /**
   * Check if a tile is a piece
   *
   * @param grid    {@link String[][]}
   * @param row     {@link int}
   * @param column  {@link int}
   * @param teamId  {@link String}
   * @param pieceId {@link String}
   * @return {@link String[][]}
   * @author robert.kratz, virgil.baclanov
   */
  public String[][] fillPositionGameState(String[][] grid, int row, int column, String teamId,
      String pieceId) {
    int numberOfRows = grid.length;
    int numberOfColumns = grid[0].length;
    if (row < numberOfRows && column < numberOfColumns) {
      grid[row][column] = "p:" + teamId + "_" + pieceId;
    }
    return grid;
  }

  /**
   * Place the bases in the middle of the quadrant and the pieces
   *
   * @param grid  {@link String[][]}
   * @param teams {@link Team[]}
   * @return {@link String[][]}
   * @author virgil.baclanov
   */
  public String[][] placePiecesAndBase(GameState gameState, String[][] grid, Team[] teams,
      MapTemplate mapTemplate) {
    int row = (int) Math.floor((double) (mapTemplate.getGridSize()[0] - 1) / 4);
    int column;
    switch (mapTemplate.getTeams()) {
      case 2:
        column = (int) Math.floor((double) (mapTemplate.getGridSize()[1] - 1) / 2);
        grid[row][column] = "b:1";
        teams[0].setBase(new int[]{row, column});
        choosePositionPieces(gameState, grid, teams, mapTemplate, column, row);

        row = getMirroredPosition(grid, row, column)[0];
        column = getMirroredPosition(grid, row, column)[1];
        grid[row][column] = "b:2";
        teams[1].setBase(new int[]{row, column});
        break;
      case 3:
        column = (int) Math.floor((double) (mapTemplate.getGridSize()[1] - 1) / 4);
        grid[row][column] = "b:1";
        teams[0].setBase(new int[]{row, column});
        choosePositionPieces(gameState, grid, teams, mapTemplate, column, row);

        row = getMirroredPosition(grid, row, column)[0];
        grid[row][column] = "b:2";
        teams[1].setBase(new int[]{row, column});

        column = getMirroredPosition(grid, row, column)[1];
        grid[row][column] = "b:3";
        teams[2].setBase(new int[]{row, column});
        break;
      case 4:
        column = (int) Math.floor((double) (mapTemplate.getGridSize()[1] - 1) / 4);
        grid[row][column] = "b:1";
        teams[0].setBase(new int[]{row, column});
        choosePositionPieces(gameState, grid, teams, mapTemplate, column, row);

        row = getMirroredPosition(grid, row, column)[0];
        grid[row][column] = "b:2";
        teams[1].setBase(new int[]{row, column});

        row = getMirroredPosition(grid, row, column)[0];
        column = getMirroredPosition(grid, row, column)[1];
        grid[row][column] = "b:3";
        teams[2].setBase(new int[]{row, column});

        row = getMirroredPosition(grid, row, column)[0];
        grid[row][column] = "b:4";
        teams[3].setBase(new int[]{row, column});
        break;
    }
    return grid;
  }

  /**
   * Get the mirrored position of a tile
   *
   * @param gameState   {@link GameState}
   * @param grid        {@link String[][]}
   * @param teams       {@link Team[]}
   * @param mapTemplate {@link MapTemplate}
   * @param xBase       {@link int}
   * @param yBase       {@link int}
   * @author virgil.baclanov
   */
  public void choosePositionPieces(GameState gameState, String[][] grid, Team[] teams,
      MapTemplate mapTemplate, int xBase, int yBase) {
    switch (mapTemplate.getPlacement()) {
      case symmetrical:
        setPositionPiecesSymmetrical(gameState, grid, teams, mapTemplate, xBase, yBase);
        break;
      case spaced_out:
        setPositionPiecesSpacedOut(gameState, grid, teams, mapTemplate, xBase, yBase);
        break;
      case defensive:
        setPositionPiecesDefensive(gameState, grid, teams, mapTemplate, xBase, yBase);
        break;
    }
  }

  /**
   * Populate the grid with the pieces in a symmetrical way
   *
   * @param grid        {@link String[][]}
   * @param teams       {@link Team[]}
   * @param mapTemplate {@link MapTemplate}
   * @return {@link String[][]}
   * @author virgil.baclanov
   */
  public GameState setPositionPiecesSymmetrical(GameState gameState, String[][] grid, Team[] teams,
      MapTemplate mapTemplate, int xBase, int yBase) {
    int numberPiecesOfOneTeam = teams[0].getPieces().length;
    if (numberPiecesOfOneTeam == 0) {
      throw new IllegalArgumentException("Number of pieces of one team is 0");
    }
    int numberOfRows = grid.length;
    int numberOfColumns = grid[0].length;
    int toBePlaced = numberPiecesOfOneTeam - 1;
    try {
      switch (mapTemplate.getTeams()) {
        case 2:
          int startingRow = yBase + 1;
          int piecesOnFirstRow = Math.min(numberPiecesOfOneTeam, numberOfColumns);
          int startingColumn = (int) Math.ceil((double) (numberOfColumns - piecesOnFirstRow) / 2);

          int steps = 0;
          //fill the lower half, centered
          while (toBePlaced >= 0) {
            int row = startingRow - steps / numberOfColumns;
            int column = startingColumn + steps % numberOfColumns;
            steps++;

            if (row >= 0 && row < numberOfRows && column >= 0 && column < numberOfColumns) {
              if (!isFlag(grid[row][column])) {
                //fill lower half and center pieces
                grid[row][column] = "p:1_" + toBePlaced;
                teams[0].getPieces()[toBePlaced].setPosition(new int[]{row, column});

                //calculate mirrored position
                int mirroredRow = getMirroredPosition(grid, row, column)[0];
                int mirroredColumn = getMirroredPosition(grid, row, column)[1];
                if (mirroredRow < numberOfRows && mirroredColumn < numberOfColumns && !isFlag(
                    grid[mirroredRow][mirroredColumn])) {
                  grid[mirroredRow][mirroredColumn] = "p:2_" + toBePlaced;
                  teams[1].getPieces()[toBePlaced].setPosition(
                      new int[]{mirroredRow, mirroredColumn});
                }
                toBePlaced--;
              }
            } else {
              toBePlaced = -1;
            }
          }
          break;
        case 3:
        case 4:
          int stepsright = 2;
          int stepsup = 1;
          int mirroredRow;
          int mirroredColumn;

          while (toBePlaced >= 0) {
            for (int layer = yBase + 1; layer < numberOfRows / 2; layer++) {
              int currentColumn = yBase;
              for (int i = 0; i < stepsright; i++) {
                if (toBePlaced < 0) {
                  break;
                }
                currentColumn = i + xBase;
                if (currentColumn >= 0 && currentColumn < numberOfColumns) {
                  grid[layer][currentColumn] = "p:1_" + toBePlaced;
                  teams[0].getPieces()[toBePlaced].setPosition(new int[]{layer, currentColumn});
                  mirroredRow = getMirroredPosition(grid, layer, currentColumn)[0];
                  mirroredColumn = getMirroredPosition(grid, layer, currentColumn)[1];

                  switch (mapTemplate.getTeams()) {
                    case 3:
                      grid[mirroredRow][currentColumn] = "p:2_" + toBePlaced;
                      teams[1].getPieces()[toBePlaced].setPosition(
                          new int[]{mirroredRow, currentColumn});

                      grid[mirroredRow][mirroredColumn] = "p:3_" + toBePlaced;
                      teams[2].getPieces()[toBePlaced].setPosition(
                          new int[]{mirroredRow, mirroredColumn});
                      break;
                    case 4:
                      grid[mirroredRow][currentColumn] = "p:2_" + toBePlaced;
                      teams[1].getPieces()[toBePlaced].setPosition(
                          new int[]{mirroredRow, currentColumn});

                      grid[layer][mirroredColumn] = "p:3_" + toBePlaced;
                      teams[2].getPieces()[toBePlaced].setPosition(
                          new int[]{layer, mirroredColumn});

                      grid[mirroredRow][mirroredColumn] = "p:4_" + toBePlaced;
                      teams[3].getPieces()[toBePlaced].setPosition(
                          new int[]{mirroredRow, mirroredColumn});
                      break;
                  }
                  toBePlaced--;
                } else {
                  break;
                }
              }

              int currentRow = xBase;
              for (int i = 0; i < stepsup; i++) {
                if (toBePlaced < 0) {
                  break;
                }
                currentRow = layer - i - 1;
                grid[currentRow][currentColumn] = "p:1_" + toBePlaced;
                teams[0].getPieces()[toBePlaced].setPosition(new int[]{currentRow, currentColumn});

                mirroredRow = getMirroredPosition(grid, currentRow, currentColumn)[0];
                mirroredColumn = getMirroredPosition(grid, currentRow, currentColumn)[1];

                switch (mapTemplate.getTeams()) {
                  case 2:
                    grid[mirroredRow][currentColumn] = "p:2_" + toBePlaced;
                    teams[1].getPieces()[toBePlaced].setPosition(
                        new int[]{mirroredRow, currentColumn});
                    break;
                  case 3:
                    grid[mirroredRow][currentColumn] = "p:2_" + toBePlaced;
                    teams[1].getPieces()[toBePlaced].setPosition(
                        new int[]{mirroredRow, currentColumn});

                    grid[mirroredRow][mirroredColumn] = "p:3_" + toBePlaced;
                    teams[2].getPieces()[toBePlaced].setPosition(
                        new int[]{mirroredRow, mirroredColumn});
                    break;
                  case 4:
                    grid[mirroredRow][currentColumn] = "p:2_" + toBePlaced;
                    teams[1].getPieces()[toBePlaced].setPosition(
                        new int[]{mirroredRow, currentColumn});

                    grid[currentRow][mirroredColumn] = "p:3_" + toBePlaced;
                    teams[2].getPieces()[toBePlaced].setPosition(
                        new int[]{currentRow, mirroredColumn});

                    grid[mirroredRow][mirroredColumn] = "p:4_" + toBePlaced;
                    teams[3].getPieces()[toBePlaced].setPosition(
                        new int[]{mirroredRow, mirroredColumn});
                    break;
                }
                toBePlaced--;
              }
              stepsright++;
              stepsup++;
            }
            if (toBePlaced >= 0) {
              System.out.println("Pieces that have not been placed: " + toBePlaced);
              throw new IndexOutOfBoundsException();
            }
            toBePlaced = -1;
          }
      }
    } catch (NullPointerException e) {
      System.out.println("Piece is null in symmetrical placement...");
    }
    gameState.setTeams(teams);
    gameState.setGrid(grid);

    return gameState;
  }

  /**
   * Get the piece on the coordinates
   *
   * @param teams {@link Team[]}
   * @param x     {@link int}
   * @param y     {@link int}
   * @return {@link Piece}
   * @author virgil.baclanov
   */
  public Piece getPieceAt(Team[] teams, int x, int y) {
    try {
      for (Team team : teams) {
        if (team == null) {
          continue;
        }
        for (Piece piece : team.getPieces()) {
          if (piece == null) {
            continue;
          }
          if (piece.getPosition()[0] == -1 || piece.getPosition()[1] == -1) {
            continue;
          }
          if (piece.getPosition()[0] == x && piece.getPosition()[1] == y) {
            return piece;
          }
        }
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("The piece at " + x + " " + y + " does not exist.");
    } catch (NullPointerException e) {
      System.out.println("The piece at " + x + " " + y + " is null.");
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get the piece on the coordinates
   *
   * @param gameState {@link GameState}
   * @param teams     {@link Team[]}
   * @return {@link Piece}
   * @author virgil.baclanov
   */
  public boolean availableTeamMoves(GameState gameState, Team[] teams) {
    try {
      String[][] grid = gameState.getGrid();
      //check if for every piece of every team that there is an available move
      int xPiece = -1;
      int yPiece = -1;
      for (Team team : teams) {
        if (team == null) {
          continue;
        }
        for (Piece piece : team.getPieces()) {
          if (piece == null) {
            continue;
          }

          xPiece = piece.getPosition()[0];
          yPiece = piece.getPosition()[1];

          if (xPiece == -1 || yPiece == -1) {
            continue;
          }
          for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
              if (canMoveTo(teams, gameState, xPiece, yPiece, i, j)) {
                return true;
              }
            }
          }
        }
      }
      return false;
    } catch (NullPointerException e) {
      System.out.println("Piece is null at availableTeamMoves");
      e.printStackTrace();
      return false;
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Index out of bounds at availableTeamMoves");
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Check if a piece can move to a certain tile.
   *
   * @param xPiece  The x coordinate of the piece.
   * @param yPiece  The y coordinate of the piece.
   * @param teams   All the teams with the pieces
   * @param xTarget The x coordinate of the target tile.
   * @param yTarget The y coordinate of the target tile.
   * @return True if the piece can move to the target tile, false otherwise.
   * @author virgil.baclanov
   */
  public boolean canMoveTo(Team[] teams, GameState gameState, int xPiece, int yPiece, int xTarget,
      int yTarget) {
    try {
      String[][] grid = gameState.getGrid();
      String objectOrigin = grid[xPiece][yPiece];
      String objectTarget = grid[xTarget][yTarget];
      Piece piece = getPieceOnCoordinates(teams, xPiece, yPiece);
      if (xPiece == xTarget && yPiece == yTarget) {
        return false;
      } else {
        int deltaX = Math.abs(xPiece - xTarget);
        int deltaY = Math.abs(yPiece - yTarget);

        switch (piece.getDescription().getType()) {
          case "Knight":
            if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
                if (isPiece(objectTarget) || isFlag(objectTarget)) {
                    return isValidActionTarget(gameState, objectOrigin, objectTarget);
                } else {
                    return !isBlock(objectTarget);
                }
            }
            return false;
          case "Pawn":
          case "Rook":
            //if the move is not vertical, nor horizontal, nor within reach
            if ((xPiece != xTarget) && (yPiece != yTarget)) {
              return false;
            } else if (!isWithinReachVerticalHorizontal(xPiece, yPiece, piece, xTarget, yTarget)) {
              return false;
            }
                    /*
                      if there is no obstacle between the two points, check if the target is a piece;
                      if the target is a piece, check if it is an enemy piece and if it has more or less attack power;
                    */
            else if (!isObstacleBetweenVerticalHorizontal(xPiece, yPiece, xTarget, yTarget, grid)) {
                if (isPiece(objectTarget) || isFlag(objectTarget)) {
                    return isValidActionTarget(gameState, objectOrigin, objectTarget);
                } else {
                    return !isBlock(objectTarget);
                }
            }
            return false;
          case "Bishop":
            if (deltaX != deltaY) {
              return false;
            } else if (!isWithinReachDiagonal(xPiece, yPiece, piece, xTarget, yTarget)) {
              return false;
            } else if (!isObstacleBetweenDiagonal(xPiece, yPiece, xTarget, yTarget, grid)) {
                if (isPiece(objectTarget) || isFlag(objectTarget)) {
                    return isValidActionTarget(gameState, objectOrigin, objectTarget);
                } else {
                    return !isBlock(objectTarget);
                }
            }
            return false;
          case "King":
          case "Queen":
            //queen is essentially a combo of rook and bishop
            if ((xPiece != xTarget) && (yPiece != yTarget) && (deltaX != deltaY)) {
              return false;
            } else if (!isWithinReachVerticalHorizontal(xPiece, yPiece, piece, xTarget, yTarget)
                || !isWithinReachDiagonal(xPiece, yPiece, piece, xTarget, yTarget)) {
              return false;
            } else if (!isObstacleBetweenVerticalHorizontal(xPiece, yPiece, xTarget, yTarget, grid)
                && !isObstacleBetweenDiagonal(xPiece, yPiece, xTarget, yTarget, grid)) {
                if (isPiece(objectTarget) || isFlag(objectTarget)) {
                    return isValidActionTarget(gameState, objectOrigin, objectTarget);
                } else {
                    return !isBlock(objectTarget);
                }
            }
            return false;
          default:
            return false;
        }
      }
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      System.out.println("Index out of bounds at canMoveTo");
      return false;
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("Piece is null at canMoveTo");
      return false;
    }
  }


  /**
   * Check if the target of an action is valid, as in taking a piece or a flag.
   *
   * @param gameState    The gamestate of the game.
   * @param objectOrigin The object that is selected.
   * @param objectTarget The object that is targeted.
   * @return True if the action is valid, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isValidActionTarget(GameState gameState, String objectOrigin,
      String objectTarget) {
      if (isPiece(objectTarget)) {
          if (areBothPiecesOfYourTeam(objectOrigin, objectTarget)) {
              return false;
          }
          return canTakePiece(gameState, objectOrigin, objectTarget);
      } else {
          return isFlag(objectTarget) && !isFlagOfTeam(objectTarget,
              Integer.parseInt(objectOrigin.substring(2, 3)));
      }
  }

  /**
   * Check if a piece can take another piece by comparing their attack powers.
   *
   * @param gameState    The gamestate of the game.
   * @param objectOrigin The gamestate id of the object that is selected.
   * @param objectTarget The gamestate id of the object that is targeted.
   * @return True if the piece can take the other piece, false otherwise.
   * @author virgil.baclanov, robert.kratz
   */
  public boolean canTakePiece(GameState gameState, String objectOrigin, String objectTarget) {
    try {
      if (isPiece(objectOrigin) && isPiece(objectTarget) && !areBothPiecesOfYourTeam(objectOrigin,
          objectTarget)) {
        //get pieces attack power of piece with id objectOrigin and objectTarget
        Piece originPiece = getPieceById(gameState, objectOrigin);
        Piece targetPiece = getPieceById(gameState, objectTarget);
        if (originPiece == null || targetPiece == null) {
          return false;
        }
        int attackPowerOrigin = originPiece.getDescription().getAttackPower();
        int attackPowerTarget = targetPiece.getDescription().getAttackPower();
        return attackPowerOrigin >= attackPowerTarget;
      }
      return false;
    } catch (NullPointerException e) {
      System.out.println("Piece is null at canTakePiece");
      return false;
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Index out of bounds at canTakePiece");
      return false;
    }
  }


  /**
   * Populate the grid with the pieces in a defensive way, placing the pieces around the base in a
   * defensive way
   *
   * @param grid        {@link String[][]}
   * @param teams       {@link Map<Team, ArrayList<Piece>>}
   * @param mapTemplate {@link MapTemplate}
   * @return {@link String[][]}
   * @author robert.kratz, virgil.baclanov
   */
  public GameState setPositionPiecesDefensive(GameState gameState, String[][] grid, Team[] teams,
      MapTemplate mapTemplate, int columnBase, int rowBase) {
    int numberPiecesOfOneTeam = teams[0].getPieces().length;
    if (numberPiecesOfOneTeam == 0) {
      throw new IllegalArgumentException("Number of pieces of one team is 0");
    }
    int numberOfRows = grid.length;
    int numberOfColumns = grid[0].length;

    // Directions: down, right, up, left
    int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    if (mapTemplate.getTeams() == 2) {
      directions = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    }
    int currentDirection = 0;
    int steps = 1; // Steps to move in the current direction

    int column = columnBase;
    int row = rowBase;
    int toBePlaced = numberPiecesOfOneTeam - 1; // Counter for placed pieces

    while (toBePlaced >= 0) {
      for (int i = 0; i < 2; i++) { // Each step size is used twice
        for (int j = 0; j < steps; j++) {
          if (toBePlaced < 0) {
            break;
          }
          int newColumn = column + directions[currentDirection][0];
          int newRow = row + directions[currentDirection][1];

          // Check bounds and that we are not revisiting the start cell
          if (newColumn >= 0 && newColumn < numberOfRows && newRow >= 0 && newRow < numberOfColumns
              && (newColumn != columnBase || newRow != rowBase)) {
            grid[newRow][newColumn] = "p:1_" + toBePlaced;
            teams[0].getPieces()[toBePlaced].setPosition(new int[]{newRow, newColumn});
            //now we place the pieces for the other bases
            int mirroredRow = getMirroredPosition(grid, newRow, newColumn)[0];
            int mirroredColumn = getMirroredPosition(grid, newRow, newColumn)[1];

            switch (mapTemplate.getTeams()) {
              case 2:
                grid[mirroredRow][mirroredColumn] = "p:2_" + toBePlaced;
                teams[1].getPieces()[toBePlaced].setPosition(
                    new int[]{mirroredRow, mirroredColumn});
                break;
              case 3:
                grid[mirroredRow][newColumn] = "p:2_" + toBePlaced;
                teams[1].getPieces()[toBePlaced].setPosition(new int[]{mirroredRow, newColumn});
                grid[mirroredRow][mirroredColumn] = "p:3_" + toBePlaced;
                teams[2].getPieces()[toBePlaced].setPosition(
                    new int[]{mirroredRow, mirroredColumn});
                break;
              case 4:
                grid[mirroredRow][newColumn] = "p:2_" + toBePlaced;
                teams[1].getPieces()[toBePlaced].setPosition(new int[]{mirroredRow, newColumn});
                grid[newRow][mirroredColumn] = "p:3_" + toBePlaced;
                teams[2].getPieces()[toBePlaced].setPosition(new int[]{newRow, mirroredColumn});
                grid[mirroredRow][mirroredColumn] = "p:4_" + toBePlaced;
                teams[3].getPieces()[toBePlaced].setPosition(
                    new int[]{mirroredRow, mirroredColumn});
                break;
            }
            toBePlaced--;
          }
          column = newColumn;
          row = newRow;
        }
        currentDirection = (currentDirection + 1) % 4; // Change direction
      }
      steps++; // Increase step size after every full cycle of directions
    }
    gameState.setTeams(teams);
    return gameState;
  }

  /**
   * Populate the grid with the pieces spaced out on the grid
   *
   * @param grid        {@link String[][]}
   * @param teams       {@link Map<Team, ArrayList<Piece>>}
   * @param mapTemplate {@link MapTemplate}
   * @return {@link String[][]}
   * @author robert.kratz, virgil.baclanov
   */
  public GameState setPositionPiecesSpacedOut(GameState gameState, String[][] grid, Team[] teams,
      MapTemplate mapTemplate, int columnBase, int rowBase) {
    int numberPiecesOfOneTeam = teams[0].getPieces().length;
    if (numberPiecesOfOneTeam == 0) {
      throw new IllegalArgumentException("Number of pieces of one team is 0");
    }

    int numberOfRowsQuadrant = grid.length / 2;
    int numberOfColumnsQuadrant = grid[0].length;
    if (mapTemplate.getTeams() > 2) {
      numberOfColumnsQuadrant = numberOfColumnsQuadrant / 2;
    }
    int allTilesOneQuadrant = numberOfRowsQuadrant * numberOfColumnsQuadrant;

    int horizontalContinuousSpacing = (int) Math.floor(
        (double) (allTilesOneQuadrant - numberPiecesOfOneTeam - 1) / numberPiecesOfOneTeam);
    //dont let the vertical spacing be too small
    ArrayList<Integer> goodRows = new ArrayList<Integer>();
    //if there is enough space to let rows free
    if (horizontalContinuousSpacing > numberOfColumnsQuadrant) {
      int verticalSpacing = numberOfRowsQuadrant / numberPiecesOfOneTeam - 1;
      for (int i = numberOfRowsQuadrant - 1; i >= 0; i--) {
        if (i % (verticalSpacing + 1) == 0) {
          goodRows.add(i);
        }
      }
    }
    int placed = 0;
    int skippedTiles = 0;

    //if there are blacklisted rows, we check for spacing within a row and skip the blacklisted rows
    if (goodRows.size() != numberOfRowsQuadrant && !goodRows.isEmpty()) {
      int distributionPerRow = (int) Math.ceil((double) numberPiecesOfOneTeam / goodRows.size());
      if (distributionPerRow == 0) {
        distributionPerRow = 1;
      }

      int spacingWithinARow = numberOfColumnsQuadrant / distributionPerRow;
      if (spacingWithinARow == numberOfColumnsQuadrant) {
        spacingWithinARow--;
      }

      for (int row = numberOfRowsQuadrant - 1; row >= 0; row--) {
        if (placed >= numberPiecesOfOneTeam) {
          break;
        }
        if (!goodRows.contains(row)) {
          continue;
        }
        for (int column = numberOfColumnsQuadrant - 1; column >= 0; column--) {
          //check for spacing within a row
          if (skippedTiles >= spacingWithinARow) {
            if (placed >= numberPiecesOfOneTeam) {
              break;
            }
            if (isFlag(grid[row][column])) {
              continue;
            }
            grid[row][column] = "p:1_" + placed;
            teams[0].getPieces()[placed].setPosition(new int[]{row, column});

            int mirroredRow = getMirroredPosition(grid, row, column)[0];
            int mirroredColumn = getMirroredPosition(grid, row, column)[1];

            switch (mapTemplate.getTeams()) {
              case 2:
                grid[mirroredRow][mirroredColumn] = "p:2_" + placed;
                teams[1].getPieces()[placed].setPosition(new int[]{mirroredRow, mirroredColumn});
                break;
              case 3:
                grid[mirroredRow][column] = "p:2_" + placed;
                teams[1].getPieces()[placed].setPosition(new int[]{mirroredRow, column});
                grid[mirroredRow][mirroredColumn] = "p:3_" + placed;
                teams[2].getPieces()[placed].setPosition(new int[]{mirroredRow, mirroredColumn});
                break;
              case 4:
                grid[mirroredRow][column] = "p:2_" + placed;
                teams[1].getPieces()[placed].setPosition(new int[]{mirroredRow, column});
                grid[row][mirroredColumn] = "p:3_" + placed;
                teams[2].getPieces()[placed].setPosition(new int[]{row, mirroredColumn});
                grid[mirroredRow][mirroredColumn] = "p:4_" + placed;
                teams[3].getPieces()[placed].setPosition(new int[]{mirroredRow, mirroredColumn});
                break;
            }
            skippedTiles = 0;
            placed++;
          } else {
            skippedTiles++;
          }
        }
      }
    }
    //if there are no blacklisted rows, we check for normal spacing
    else {
      for (int row = numberOfRowsQuadrant - 1; row >= 0; row--) {
        for (int column = numberOfColumnsQuadrant - 1; column >= 0; column--) {
          //check for normal spacing
          if (skippedTiles >= horizontalContinuousSpacing) {
            if (placed >= numberPiecesOfOneTeam) {
              break;
            }
            if (grid[row][column].startsWith("b:")) {
              continue;
            }
            grid[row][column] = "p:1_" + placed;
            teams[0].getPieces()[placed].setPosition(new int[]{row, column});

            int mirroredRow = getMirroredPosition(grid, row, column)[0];
            int mirroredColumn = getMirroredPosition(grid, row, column)[1];

            switch (mapTemplate.getTeams()) {
              case 2:
                grid[mirroredRow][mirroredColumn] = "p:2_" + placed;
                teams[1].getPieces()[placed].setPosition(new int[]{mirroredRow, mirroredColumn});
                break;
              case 3:
                grid[mirroredRow][column] = "p:2_" + placed;
                teams[1].getPieces()[placed].setPosition(new int[]{mirroredRow, column});

                grid[mirroredRow][mirroredColumn] = "p:3_" + placed;
                teams[2].getPieces()[placed].setPosition(new int[]{mirroredRow, mirroredColumn});
                break;
              case 4:
                grid[mirroredRow][column] = "p:2_" + placed;
                teams[1].getPieces()[placed].setPosition(new int[]{mirroredRow, column});
                grid[row][mirroredColumn] = "p:3_" + placed;
                teams[2].getPieces()[placed].setPosition(new int[]{row, mirroredColumn});
                grid[mirroredRow][mirroredColumn] = "p:4_" + placed;
                teams[3].getPieces()[placed].setPosition(new int[]{mirroredRow, mirroredColumn});
                break;
            }
            skippedTiles = 0;
            placed++;
          } else {
            skippedTiles++;
          }
        }
      }
    }
    gameState.setTeams(teams);

    return gameState;
  }

  /**
   * get the mirrored position of a piece on the grid
   *
   * @param row    row of the piece
   * @param column column of the piece
   * @return int[] coordinates of the mirrored position (row, column)
   * @author virgil.baclanov
   */
  public int[] getMirroredPosition(String[][] grid, int row, int column) {
    int numberOfRows = grid.length;
    int numberOfColumns = grid[0].length;
    int mirroredRow = numberOfRows - 1 - row;
    int mirroredColumn = numberOfColumns - 1 - column;
    return new int[]{mirroredRow, mirroredColumn};
  }

  /**
   * Check if a piece is a flag
   *
   * @param grid           {@link String}
   * @param numberOfBlocks {@link int}
   * @return {@link String[][]}
   * @author virgil.baclanov
   */
  public String[][] placeBlocks(String[][] grid, int numberOfBlocks) {
    try {
      int numberOfRows = grid.length;
      int numberOfColumns = grid[0].length;
      while (numberOfBlocks > 0) {
        int row = (int) (Math.random() * numberOfRows);
        int column = (int) (Math.random() * numberOfColumns);
        if (grid[row][column].isEmpty() && !isBaseNearby(grid, row, column)) {
          grid[row][column] = "b";

          int mirroredRow = getMirroredPosition(grid, row, column)[0];
          int mirroredColumn = getMirroredPosition(grid, row, column)[1];

          if (!isBaseNearby(grid, mirroredRow, mirroredColumn)
              && grid[mirroredRow][mirroredColumn].isEmpty()) {
            grid[mirroredRow][mirroredColumn] = "b";
          }
          numberOfBlocks--;
        }
      }
      System.out.println("Exiting...");
      return grid;
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Index at placing blocks is out of bounds");
    }
    return null;
  }

  /**
   * Check if a piece is a flag
   *
   * @param grid   {@link String}
   * @param row    {@link int}
   * @param column {@link int}
   * @return {@link boolean}
   * @author virgil.baclanov
   */
  public boolean isBaseNearby(String[][] grid, int row, int column) {
    try {
      int[] directionsX = {0, 0, -1, 1, -1, 1, -1, 1};
      int[] directionsY = {-1, 1, 0, 0, -1, 1, 1, -1};

      for (int i = 0; i < directionsX.length; i++) {
        int checkRow = row + directionsX[i];
        int checkColumn = column + directionsY[i];
        if (checkRow >= 0 && checkRow < grid.length && checkColumn >= 0
            && checkColumn < grid[0].length) {
          if (grid[checkRow][checkColumn].startsWith("b:")) {
            return true;
          }
        }
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Index at finding a nearby base is out of bounds");
    }
    return false;
  }

  /**
   * Check if a piece is a flag
   *
   * @param grid       {@link String}
   * @param piece      {@link Piece}
   * @param rowBase    {@link int}
   * @param columnBase {@link int}
   * @return {@link int[]}
   * @author virgil.baclanov
   */
  public int[] placePieceBackToBase(String[][] grid, Piece piece, int rowBase, int columnBase) {
    int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    int currentDirection = 0;
    int steps = 1;

    int row = rowBase;
    int column = columnBase;

    int newRow = -1;
    int newColumn = -1;

    while (true) {
      try {
        for (int i = 0; i < 2; i++) { // Each step size is used twice
          for (int j = 0; j < steps; j++) {
            newRow = row + directions[currentDirection][1];
            newColumn = column + directions[currentDirection][0];
            if (grid[newRow][newColumn].isEmpty()) {
              grid[newRow][newColumn] = "p:" + piece.getTeamId() + "_" + piece.getId();
              piece.setPosition(new int[]{newRow, newColumn});
              return new int[]{newRow, newColumn};
            }
            column = newColumn;
            row = newRow;
          }
          currentDirection = (currentDirection + 1) % 4; // Change direction
        }
        steps++;
      } catch (NullPointerException e) {
        System.out.println("Piece is null at placing piece back to base");
        return null;
      } catch (IndexOutOfBoundsException e) {
        System.out.println("Index out of bounds at placing piece back to base");
        return null;
      }
    }
  }

  /**
   * Check if a piece is a flag
   *
   * @param gameState {@link GameState}
   * @param pieceId   {@link String}
   * @return {@link Piece}
   * @author virgil.baclanov
   */
  public Piece getPieceById(GameState gameState, String pieceId) {
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
   * Check if a piece is a flag
   *
   * @param gameState {@link GameState}
   * @param teamId    {@link String}
   * @return {@link Team}
   * @author virgil.baclanov
   */
  public Team getTeamById(GameState gameState, String teamId) {
    for (Team team : gameState.getTeams()) {
      if (team.getId().equals(teamId)) {
        return team;
      }
    }
    return null;
  }

  /**
   * Check if a piece is a flag
   *
   * @param grid {@link String[][]}
   * @param team {@link Team}
   * @return {@link int[]}
   * @author virgil.baclanov
   */
  public int[] coordinatesBase(String[][] grid, Team team) {
    int teamId = Integer.parseInt(team.getId().substring(2, 3));
    int numberOfRows = grid.length;
    int numberOfColumns = grid[0].length;

    int[] coordinates = new int[2];
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        if (grid[i][j].equals("b:" + teamId)) {
          coordinates[0] = i;
          coordinates[1] = j;
        }
      }
    }
    return coordinates;
  }

  /**
   * Create a list of pieces based on the map template
   *
   * @param mapTemplate {@link MapTemplate}
   * @return {@link ArrayList<Piece>}
   * @apiNote This method is used to create a list of pieces based on the map template, this is not
   * a last of all pieces in the game
   * @author robert.kratz
   */
  public ArrayList<Piece> getPieceList(MapTemplate mapTemplate) {
    ArrayList<Piece> pieces = new ArrayList<>();
    for (PieceDescription pieceDescription : mapTemplate.getPieces()) {
      for (int i = 0; i < pieceDescription.getCount(); i++) {
        String uniqueId = "p:" + i;
        Piece piece = new Piece();
        piece.setId(uniqueId);
        piece.setTeamId("t:" + i);
        piece.setDescription(pieceDescription);
        piece.setPosition(new int[]{0, 0});
        pieces.add(piece);
      }
    }
    return pieces;
  }

  /**
   * Get the piece on the coordinates x and y
   *
   * @param teams {@link Team[]}
   * @param x     {@link int}
   * @param y     {@link int}
   * @return {@link Piece}
   * @author robert.kratz
   */
  public Piece getPieceOnCoordinates(Team[] teams, int x, int y) {
    for (Team team : teams) {
      if (team == null) {
        continue;
      }
      for (Piece piece : team.getPieces()) {
        if (piece == null) {
          continue;
        }
        if (piece.getPosition()[0] == -1 || piece.getPosition()[1] == -1) {
          continue;
        }
        if (piece.getPosition()[0] == x && piece.getPosition()[1] == y) {
          return piece;
        }
      }
    }
    return null;
  }

  /**
   * Get the piece from the grid based on the map template and the game state
   *
   * @param mapTemplate {@link MapTemplate}
   * @return String[][] grid
   */
  public String[][] createEmptyGrid(MapTemplate mapTemplate) {
    int numberOfRows = mapTemplate.getGridSize()[0];
    int numberOfColumns = mapTemplate.getGridSize()[1];
    String[][] grid = new String[numberOfRows][numberOfColumns];
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        grid[i][j] = "";
      }
    }
    return grid;
  }

  /**
   * Check if the move is valid, if the move is out of bounds or the cell is occupied, the move is
   * invalid
   *
   * @param grid {@link String[][]}
   * @param x    {@link int}
   * @param y    {@link int}
   * @return {@link boolean}
   * @author robert.kratz
   */
  public boolean isMoveValid(String[][] grid, int x, int y) {
    return !isMoveOutOfBounds(grid, x, y) && !isOccupied(grid, x, y);
  }

  /**
   * Check if the move is out of bounds, if the move is out of bounds, the move is invalid
   *
   * @param grid {@link String[][]}
   * @param x    {@link int}
   * @param y    {@link int}
   * @return {@link boolean}
   * @author robert.kratz
   */
  public boolean isMoveOutOfBounds(String[][] grid, int x, int y) {
    return x < 0 || x >= grid.length || y < 0 || y >= grid[0].length;
  }

  /**
   * Check if the cell is occupied, if the cell is occupied, the move is invalid
   *
   * @param grid {@link String[][]}
   * @param x    {@link int}
   * @param y    {@link int}
   * @return {@link boolean}
   * @apiNote This method checks if the cell is occupied, if the cell is occupied, the move is
   * invalid
   * @author robert.kratz
   */
  public boolean isOccupied(String[][] grid, int x, int y) {
    return !grid[x][y].isEmpty();
  }

  /**
   * Check if a target is within the reach of a piece by taking its movement directions into
   * account. Here only for vertical and horizontal movements.
   *
   * @param xPiece  The x coordinate of the piece.
   * @param yPiece  The y coordinate of the piece
   * @param piece   The piece that is selected
   * @param xTarget The x coordinate of the target
   * @param yTarget The y coordinate of the target
   * @return True if the target is within reach, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isWithinReachVerticalHorizontal(int xPiece, int yPiece, Piece piece, int xTarget,
      int yTarget) {
    int deltaX = Math.abs(xPiece - xTarget);
    int deltaY = Math.abs(yPiece - yTarget);

    //Look right
    if (xPiece == xTarget && yPiece < yTarget && deltaY > piece.getDescription().getMovement()
        .getDirections().getRight()) {
      return false;
    }
    //Look left
    if (xPiece == xTarget && yPiece > yTarget && deltaY > piece.getDescription().getMovement()
        .getDirections().getLeft()) {
      return false;
    }
    //Look down
    if (yPiece == yTarget && xPiece < xTarget && deltaX > piece.getDescription().getMovement()
        .getDirections().getDown()) {
      return false;
    }
    //Look up
    if (yPiece == yTarget && xPiece > xTarget && deltaX > piece.getDescription().getMovement()
        .getDirections().getUp()) {
      return false;
    }
    return true;
  }

  /**
   * Remove all pieces of a team from the game state
   *
   * @param gameState The game state of the game
   * @param teamId    The id of the team
   * @return The number of remaining pieces of the team on the board
   * @author robert.kratz
   */
  public GameState removeAllPiecesOfTeam(GameState gameState, String teamId) {
    Team[] teams = gameState.getTeams();
    for (Team team : teams) {
      if (team == null) {
        continue;
      }
      if (team.getId().equals(teamId)) {
        for (Piece piece : team.getPieces()) {
          if (piece == null) {
            continue;
          }
          piece.setPosition(new int[]{-1, -1});
        }
      }
    }
    gameState.setTeams(teams);
    return gameState;
  }

  /**
   * Remove all pieces of a team from the grid
   *
   * @param grid
   * @param teamIndex
   * @return
   * @author robert.kratz
   */
  public String[][] removeTeamFromGrid(String[][] grid, int teamIndex) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        String cell = grid[i][j];

        if (cell.startsWith("p:" + (teamIndex + 1)) || cell.startsWith("b:" + (teamIndex + 1))) {
          grid[i][j] = "";
        }
      }
    }
    return grid;
  }

  /**
   * Get the remaining pieces of a team on the board
   *
   * @param gameState The game state of the game
   * @param teamId    The id of the team
   * @return The number of remaining pieces of the team on the board
   * @author virgil.baclanov
   */
  public int getRemainingPiecesFromTeam(GameState gameState, String teamId) {
    int remainingPieces = 0;
    for (Team team : gameState.getTeams()) {
      if (team == null) {
        continue;
      }
      if (team.getId().equals(teamId)) {
        for (Piece piece : team.getPieces()) {
          if (piece == null) {
            continue;
          }
          if (piece.getPosition()[0] != -1 || piece.getPosition()[1] != -1) {
            remainingPieces++;
          }
        }
      }
    }
    return remainingPieces;
  }

  /**
   * Check if a target is within the reach of a piece by taking its movement directions into
   * account. Here only for diagonal movements.
   *
   * @param xPiece  The x coordinate of the piece.
   * @param yPiece  The y coordinate of the piece
   * @param piece   The piece that is selected
   * @param xTarget The x coordinate of the target
   * @param yTarget The y coordinate of the target
   * @return True if the target is within reach, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isWithinReachDiagonal(int xPiece, int yPiece, Piece piece, int xTarget,
      int yTarget) {
    int deltaX = Math.abs(xPiece - xTarget);

    //Quadrant 4
    if (xPiece < xTarget && yPiece < yTarget && deltaX > piece.getDescription().getMovement()
        .getDirections().getDownRight()) {
      return false;
    }
    //Quadrant 3
    if (xPiece < xTarget && yPiece > yTarget && deltaX > piece.getDescription().getMovement()
        .getDirections().getDownLeft()) {
      return false;
    }
    //Quadarant 2
    if (xPiece > xTarget && yPiece > yTarget && deltaX > piece.getDescription().getMovement()
        .getDirections().getUpLeft()) {
      return false;
    }
    //Quadrant 1
    if (xPiece > xTarget && yPiece < yTarget && deltaX > piece.getDescription().getMovement()
        .getDirections().getUpLeft()) {
      return false;
    }
    return true;
  }

  /**
   * Check if there is a block or a piece between the piece and the target. Here only for vertical
   * and horizontal movements.
   *
   * @param xPiece  The x coordinate of the piece.
   * @param yPiece  The y coordinate of the piece
   * @param xTarget The x coordinate of the target
   * @param yTarget The y coordinate of the target
   * @param grid    The game state grid
   * @return True if there is an obstacle, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isObstacleBetweenVerticalHorizontal(int xPiece, int yPiece, int xTarget,
      int yTarget, String[][] grid) {
    if (xPiece != xTarget && yPiece != yTarget) {
      return false;
    }

    if (xPiece == xTarget) {
      if (yPiece < yTarget) {
        for (int i = yPiece; i < yTarget; i++) {
          if (isBlock(grid[xPiece][i]) || (isPiece(grid[xPiece][i]) && (i != yPiece)) || isFlag(
              grid[xPiece][i])) {
            return true;
          }
        }
      } else if (yPiece > yTarget) {
        for (int i = yPiece; i > yTarget; i--) {
          if (isBlock(grid[xPiece][i]) || (isPiece(grid[xPiece][i]) && (i != yPiece)) || isFlag(
              grid[xPiece][i])) {
            return true;
          }
        }
      }
    } else if (yPiece == yTarget) {
      if (xPiece < xTarget) {
        for (int i = xPiece; i < xTarget; i++) {
          if (isBlock(grid[i][yPiece]) || (isPiece(grid[i][yPiece]) && (i != xPiece)) || isFlag(
              grid[i][yPiece])) {
            return true;
          }
        }
      } else {
        for (int i = xPiece; i > xTarget; i--) {
          if (isBlock(grid[i][yPiece]) || (isPiece(grid[i][yPiece]) && (i != xPiece)) || isFlag(
              grid[i][yPiece])) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Check if there is a block or a piece between the piece and the target. Here only for diagonal
   * movements.
   *
   * @param xPiece        The x coordinate of the piece.
   * @param yPiece        The y coordinate of the piece
   * @param xTarget       The x coordinate of the target
   * @param yTarget       The y coordinate of the target
   * @param gameStateGrid The game state grid
   * @return True if there is an obstacle, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isObstacleBetweenDiagonal(int xPiece, int yPiece, int xTarget, int yTarget,
      String[][] gameStateGrid) {
    if (xPiece == xTarget || yPiece == yTarget) {
      return false;
    }

    int xDirection = xPiece < xTarget ? 1 : -1;
    int yDirection = yPiece < yTarget ? 1 : -1;

    int deltaX = Math.abs(xPiece - xTarget);
    try {
      for (int i = 1; i < deltaX; i++) {
        int xCurrent = xPiece + i * xDirection;
        int yCurrent = yPiece + i * yDirection;
        if (isBlock(gameStateGrid[xCurrent][yCurrent]) || isPiece(gameStateGrid[xCurrent][yCurrent])
            || isFlag(gameStateGrid[xCurrent][yCurrent])) {
          return true;
        }
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println(
          "The diagonal between " + xPiece + " " + yPiece + " and " + xTarget + " " + yTarget
              + " is out of bounds.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }


  /**
   * Check if a certain string is a block.
   *
   * @param string The string from the game state to check.
   * @return True if the string is a block, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isBlock(String string) {
    return string.equals("b");
  }

  /**
   * Check if a certain string is a piece.
   *
   * @param string The string from the game state to check.
   * @return True if the string is a piece, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isPiece(String string) {
    return (!string.isEmpty() && string.charAt(0) == 'p');
  }

  /**
   * Check if a certain string is a piece of a certain team.
   *
   * @param string The string from the game state to check.
   * @param team   The team to check the piece for.
   * @return True if the string is a piece of the team, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isPieceOfTeam(String string, int team) {
    return (isPiece(string) && string.charAt(2) == Integer.toString(team).charAt(0));
  }

  /**
   * Check if a certain string is a flag.
   *
   * @param string The string from the game state to check.
   * @return True if the string is a flag, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isFlag(String string) {
    if (isBlock(string)) {
      return false;
    }
    return (!string.isEmpty() && string.charAt(0) == 'b' && string.charAt(1) == ':');
  }

  /**
   * Check if a certain string is a flag of a certain team.
   *
   * @param string The string from the game state to check.
   * @param team   The team to check the flag for.
   * @return True if the string is a flag of the team, false otherwise.
   * @author virgil.baclanov
   */
  public boolean isFlagOfTeam(String string, int team) {
    return (isFlag(string) && string.charAt(2) == Integer.toString(team).charAt(0));
  }

  /**
   * Check if a certain string is a piece of the enemy team.
   *
   * @param idPiece1 {@link String}
   * @param idPiece2 {@link String}
   * @return {@link boolean}
   */
  public boolean areBothPiecesOfYourTeam(String idPiece1, String idPiece2) {
    return (isPiece(idPiece1) && isPiece(idPiece2) && (idPiece1.charAt(2) == idPiece2.charAt(2)));
  }

  /**
   * Check if a certain string is a piece of the enemy team.
   *
   * @param grid {@link String[][]}
   * @author robert.kratz
   */
  public void printGridToConsole(String[][] grid) {
    for (String[] strings : grid) {
      for (String string : strings) {
        System.out.print(string + "     ");
      }
      System.out.println();
    }
  }

  /**
   * Check if a certain string is a piece of the enemy team.
   *
   * @param flagId {@link String}
   * @return {@link int}
   * @author robert.kratz
   */
  public int getTeamIndexByFlagId(String flagId) {
    if (!isFlag(flagId)) {
      return -1;
    }

    return Integer.parseInt(flagId.substring(2, 3));
  }

  /**
   * Grid to string
   *
   * @param grid {@link String[][]}
   * @return {@link String}
   * @author robert.kratz
   */
  public String gridToString(String[][] grid) {
    StringBuilder gridString = new StringBuilder();
    for (String[] strings : grid) {
      for (String string : strings) {
        gridString.append(string).append(" ");
      }
      gridString.append("\n");
    }
    return gridString.toString();
  }
}
