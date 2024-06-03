package de.cfp1.client.gui;

import de.cfp1.client.game.GameHandler;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.exceptions.ForbiddenMove;
import de.cfp1.server.game.exceptions.GameOver;
import de.cfp1.server.game.exceptions.GameSessionNotFound;
import de.cfp1.server.game.exceptions.InvalidMove;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Move;
import de.cfp1.server.game.state.Piece;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

/**
 * The game board is the visual representation of the game state. Due to its complexity and dynamic
 * nature, it is a separate class.
 *
 * @author virgil.baclanov, robert.kratz
 */

public class GameBoard extends GridPane {

  private GameBoardHelper gameBoardHelper;
  private GameState actualGameState;
  private BoardTheme boardTheme;
  private GameHandler gameHandler;
  private boolean isModifiable = true;
  private HashMap<String, Image> imageHashMap = new HashMap<>();

  private Rectangle currentlySelectedRectangle = null;
  private Color currentlySelectedRectangleColor = null;
  private int xCurrentlySelectedRectangle;
  private int yCurrentlySelectedRectangle;
  private Piece currentPiece = null;

  /**
   * Constructor for interacting with and visualizing the game board.
   *
   * @param boardTheme   The theme of the board
   * @param isModifiable If the board is modifiable
   * @author virgil.baclanov
   */
  public GameBoard(GameHandler gameHandler, BoardTheme boardTheme, boolean isModifiable) {
    this.gameBoardHelper = new GameBoardHelper();
    this.gameHandler = gameHandler;
    this.boardTheme = boardTheme;
    this.isModifiable = isModifiable;

    drawGameBoard(this.gameHandler.getGameState());
    populateGameBoard(this.gameHandler.getGameState());
  }

  /**
   * Constructor for only visualizing the game board.
   *
   * @param actualGameState The initial game state here in the beginning
   * @param boardTheme      The theme of the board
   * @param isModifiable    If the board is modifiable
   * @author virgil.baclanov, robert.kratz
   */
  public GameBoard(GameState actualGameState, BoardTheme boardTheme, boolean isModifiable) {
    this.gameBoardHelper = new GameBoardHelper();
    this.actualGameState = actualGameState;
    this.boardTheme = boardTheme;
    this.isModifiable = isModifiable;

    drawGameBoard(actualGameState);
    populateGameBoard(actualGameState);
  }

  /**
   * Create the game board visually. First, ensure responsiveness. Then, each tile on the board is
   * essentially a StackPane. Each StackPane contains a Rectangle and an ImageView. The rectangle is
   * used to display the color of the tile. The ImageView is used to display the icon of the piece.
   *
   * @param gameState The initial gameState.
   * @author virgil.baclanov
   */
  public void drawGameBoard(GameState gameState) {
    this.setAlignment(Pos.CENTER);

    // Additional configuration to ensure the container allows resizing
    this.setMinSize(0, 0); // Allow the GridPane to shrink
    this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Allow the GridPane to grow

        /*
        Dynamically select the smaller of the two dimensions.
        If the GridPane gets wider than it is tall, the width will become the limiting factor of the board.
        The ObservableValue is essentially a listener that automatically (!) updates the value
        when the GridPane's dimensions change.
        */
    ObservableValue<Number> updatedMinDimension = Bindings.min(
        this.widthProperty().divide(gameState.getGrid()[0].length),
        this.heightProperty().divide(gameState.getGrid().length)
    );

    for (int i = 0; i < gameState.getGrid().length; i++) {
      for (int j = 0; j < gameState.getGrid()[0].length; j++) {
        StackPane tileContainer = new StackPane();

        ImageView imageView = new ImageView();
        // Bind the ImageView's dimensions to the updatedMinDimension
        imageView.fitWidthProperty().bind(updatedMinDimension);
        imageView.fitHeightProperty().bind(updatedMinDimension);

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.WHITE);
        // Bind the rectangle's dimensions to the updatedMinDimension
        rectangle.widthProperty().bind(updatedMinDimension);
        rectangle.heightProperty().bind(updatedMinDimension);

        /* First add the rectangle to the tileContainer and then imageView
         * This is important because we want the imageView to be on top of the rectangle.
         * The rectangle will sometimes change its color, acting as a background to the image object on the table.
         * Thus, we achieve the desired selection visual effect.
         */
        tileContainer.getChildren().addAll(rectangle, imageView);

        //Add the tileContainer to the board (GridPane)
        setRowIndex(tileContainer, i);
        setColumnIndex(tileContainer, j);
        this.getChildren().add(tileContainer);

        //Add the selection listener to the rectangle
        this.selectionListener(tileContainer);
      }
    }
  }

  /**
   * Populate the game board with the pieces from the game state.
   *
   * @param gameState The initial gameState.
   * @author virgil.baclanov
   */
  private void populateGameBoard(GameState gameState) {
    try {
      String[][] gameStateGrid = gameState.getGrid();

      for (int i = 0; i < gameStateGrid.length; i++) {
        for (int j = 0; j < gameStateGrid[i].length; j++) {

          String currentTileText = gameStateGrid[i][j];
          String stringBoardTheme = switch (this.boardTheme) {
            case CLASSIC -> "classic_";
            case ZOMBIE -> "zombie_";
            case WEST -> "west_";
          };

          if (!currentTileText.isEmpty()) {
            if (gameBoardHelper.isBlock(currentTileText)) {
              changeIconPiece(i, j, stringBoardTheme + "block");
            } else {
              String team = currentTileText.substring(2, 3);
              if (gameBoardHelper.isFlag(currentTileText)) {
                changeIconPiece(i, j, stringBoardTheme + "flag_" + team);
              } else {
                Piece piece = gameBoardHelper.getPieceAt(gameState.getTeams(), i, j);

                if (piece == null) {
                  continue;
                }

                switch (piece.getDescription().getType()) {
                  case "Pawn":
                    changeIconPiece(i, j, stringBoardTheme + "pawn_" + team);
                    break;
                  case "Knight":
                    changeIconPiece(i, j, stringBoardTheme + "knight_" + team);
                    break;
                  case "Bishop":
                    changeIconPiece(i, j, stringBoardTheme + "bishop_" + team);
                    break;
                  case "King":
                    changeIconPiece(i, j, stringBoardTheme + "king_" + team);
                    break;
                  case "Queen":
                    changeIconPiece(i, j, stringBoardTheme + "queen_" + team);
                    break;
                  case "Rook":
                    changeIconPiece(i, j, stringBoardTheme + "rook_" + team);
                    break;
                  default:
                    changeIconPiece(i, j, "custom_piece");
                    break;
                }
              }
            }
          }
        }
      }
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      System.out.println("The gamestate grid does not match the game board dimensions.");
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("Piece not found when visually populating the game board.");
    }
  }

  /**
   * Manage the visual guidelines for the player's move upon clicking on a tile.
   *
   * @param newlySelectedRectangleContainer The newly selected rectangle that is clicked upon.
   * @author virgil.baclanov
   */
  public void selectionListener(StackPane newlySelectedRectangleContainer) {
    newlySelectedRectangleContainer.setOnMouseClicked(mouseEvent -> {
      try {
        if (isModifiable) {
          this.gameHandler.getGameState().getGrid();
          int x = GridPane.getRowIndex(newlySelectedRectangleContainer);
          int y = GridPane.getColumnIndex(newlySelectedRectangleContainer);
          System.out.println(
              "Mouse clicked on square: " + GridPane.getRowIndex(newlySelectedRectangleContainer)
                  + " " + GridPane.getColumnIndex(newlySelectedRectangleContainer));

          String newlySelectedObject = this.gameHandler.getGameState().getGrid()[x][y];
          Rectangle newlySelectedRectangle = (Rectangle) newlySelectedRectangleContainer.getChildren()
              .get(0);

          int currentTeamIndex = this.gameHandler.getGameState().getCurrentTeam();

          //if no rectangle is currently selected
          if (this.currentlySelectedRectangle == null) {
            if (gameBoardHelper.isPiece(newlySelectedObject)) {
              this.currentPiece = gameBoardHelper.getPieceAt(
                  this.gameHandler.getGameState().getTeams(), x, y);

              //if from own team and able to make move
              if (Integer.parseInt(this.currentPiece.getId().substring(2, 3))
                  == currentTeamIndex + 1 && (this.gameHandler.getClientTeamId()
                  == currentTeamIndex)) {
                //show where the newly selected piece can move
                this.currentlySelectedRectangle = newlySelectedRectangle;
                this.currentlySelectedRectangleColor = (Color) newlySelectedRectangle.getFill();
                this.xCurrentlySelectedRectangle = x;
                this.yCurrentlySelectedRectangle = y;
                this.currentlySelectedRectangle.setFill(Color.rgb(255, 255, 255, 0.1));
                displayMoveListener(this.currentPiece, Color.rgb(255, 255, 255, 0.4));
              }
            }
          }
          //if a piece is currently selected
          else {
            //if move is within the field of movement
            if (gameBoardHelper.canMoveTo(this.gameHandler.getGameState().getTeams(),
                this.gameHandler.getGameState(), this.xCurrentlySelectedRectangle,
                this.yCurrentlySelectedRectangle, x, y)) {
              currentlySelectedRectangle.setFill(currentlySelectedRectangleColor);
              displayMoveListener(this.currentPiece, Color.TRANSPARENT);

              makeMove(this.xCurrentlySelectedRectangle, this.yCurrentlySelectedRectangle, x, y);

              //if there is a flag at the target position, clear and repopulate the game board
              if (gameBoardHelper.isFlag(this.gameHandler.getGameState().getGrid()[x][y])) {
                clearGameBoard();
                populateGameBoard(this.gameHandler.getGameState());
              }
              //otherwise, just update the game board according to the move
              else {
                updateGameBoard(this.xCurrentlySelectedRectangle, this.yCurrentlySelectedRectangle,
                    x, y);
              }

              this.currentPiece.setPosition(new int[]{x, y});

              this.currentlySelectedRectangle = null;
              this.currentlySelectedRectangleColor = null;
              this.xCurrentlySelectedRectangle = -1;
              this.yCurrentlySelectedRectangle = -1;
            }

            //else if out of field of movement
            else {
              //if still piece of yours, continue displaying field of movement for your pieces
              int teamCurrentPiece = Integer.parseInt(this.gameHandler.getGameState()
                  .getGrid()[this.xCurrentlySelectedRectangle][this.yCurrentlySelectedRectangle].substring(
                  2, 3));
              if (gameBoardHelper.isPieceOfTeam(newlySelectedObject, teamCurrentPiece)) {
                this.currentlySelectedRectangle.setFill(this.currentlySelectedRectangleColor);
                displayMoveListener(this.currentPiece, Color.TRANSPARENT);
                this.currentPiece = gameBoardHelper.getPieceAt(
                    this.gameHandler.getGameState().teams, x, y);

                this.currentlySelectedRectangle = newlySelectedRectangle;
                this.currentlySelectedRectangleColor = (Color) newlySelectedRectangle.getFill();
                this.xCurrentlySelectedRectangle = x;
                this.yCurrentlySelectedRectangle = y;
                this.currentlySelectedRectangle.setFill(Color.rgb(255, 255, 255, 0.1));
                displayMoveListener(this.currentPiece, Color.rgb(255, 255, 255, 0.4));
              }
              //if piece cannot be moved anywhere and the player clicks on a piece that is not his own => deselect the piece
              else {
                this.currentlySelectedRectangle.setFill(this.currentlySelectedRectangleColor);
                displayMoveListener(this.currentPiece, Color.TRANSPARENT);
                this.currentlySelectedRectangle = null;
                this.currentlySelectedRectangleColor = null;
                this.xCurrentlySelectedRectangle = -1;
                this.yCurrentlySelectedRectangle = -1;
              }
            }
          }
        }
      } catch (IndexOutOfBoundsException e) {
        e.printStackTrace();
        System.out.println(
            "The rectangle at " + GridPane.getRowIndex(newlySelectedRectangleContainer) + " "
                + GridPane.getColumnIndex(newlySelectedRectangleContainer) + " does not exist.");
      } catch (NullPointerException e) {
        e.printStackTrace();
        System.out.println("Listener exception. The rectangle at " + GridPane.getRowIndex(
            newlySelectedRectangleContainer) + " " + GridPane.getColumnIndex(
            newlySelectedRectangleContainer) + " is null.");
      }
    });
  }

  /**
   * Visually update the game board after a move.
   *
   * @param xOrigin The x coordinate of the origin object.
   * @param yOrigin The y coordinate of the origin object.
   * @param xTarget The x coordinate of the target object.
   * @param yTarget The y coordinate of the target object.
   * @author virgil.baclanov
   */
  public void updateGameBoard(int xOrigin, int yOrigin, int xTarget, int yTarget) {
    try {
      Rectangle rectangleOrigin = (Rectangle) this.getTileAt(this, xOrigin, yOrigin).getChildren()
          .get(0);
      rectangleOrigin.setFill(Color.TRANSPARENT);
      Rectangle rectangleTarget = (Rectangle) this.getTileAt(this, xTarget, yTarget).getChildren()
          .get(0);
      rectangleTarget.setFill(Color.TRANSPARENT);

      ImageView imageViewOrigin = (ImageView) this.getTileAt(this, xOrigin, yOrigin).getChildren()
          .get(1);
      ImageView imageViewTarget = (ImageView) this.getTileAt(this, xTarget, yTarget).getChildren()
          .get(1);
      imageViewTarget.setImage(imageViewOrigin.getImage());
      imageViewOrigin.setImage(null);
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      System.out.println(
          "When updating visually after move: The rectangle at " + xOrigin + " " + yOrigin
              + " does not exist.");
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println(
          "When updating visually after move: The rectangle at " + xOrigin + " " + yOrigin
              + " is null.");
    }
  }

  /**
   * Display the possible moves of a piece. Go through the entire board and color the tiles where
   * the piece can move.
   *
   * @param piece The piece that is selected.
   * @param color The color of the tile suggesting the possible move.
   * @author virgil.baclanov
   */
  public void displayMoveListener(Piece piece, Color color) {
    try {
      int rowPiece = piece.getPosition()[0];
      int columnPiece = piece.getPosition()[1];
      for (int i = rowPiece - 3; i <= rowPiece + 3; i++) {
        for (int j = columnPiece - 3; j <= columnPiece + 3; j++) {
          if (i < 0 || j < 0 || i >= this.gameHandler.getGameState().getGrid().length
              || j >= this.gameHandler.getGameState().getGrid()[i].length) {
            continue;
          }
          if (gameBoardHelper.canMoveTo(this.gameHandler.getGameState().getTeams(),
              this.gameHandler.getGameState(), rowPiece, columnPiece, i, j)) {
            Rectangle rectangle = (Rectangle) this.getTileAt(this, i, j).getChildren().get(0);
            rectangle.setFill(color);
          }
        }
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      System.out.println("Illegal argument exception when showing visual aid.");
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      System.out.println("Index out of bounds when showing visual aid.");
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("Null pointer exception when showing visual aid.");
    }
  }

  /**
   * Change the icon of a piece on the game board.
   *
   * @param x         The x coordinate of the piece.
   * @param y         The y coordinate of the piece.
   * @param pieceType The type of the piece (e.g. "bishop_blue").
   * @author virgil.baclanov
   */
  public void changeIconPiece(int x, int y, String pieceType) {
    try {
      if (this.imageHashMap.containsKey(pieceType)) {
        ImageView imageView = (ImageView) this.getTileAt(this, x, y).getChildren().get(1);
        imageView.setImage(this.imageHashMap.get(pieceType));
      } else {
        InputStream url = getClass().getResourceAsStream("/icons/" + pieceType + ".png");
        Image image = new Image(Objects.requireNonNull(url));
        ImageView imageView = (ImageView) this.getTileAt(this, x, y).getChildren().get(1);
        imageView.setImage(image);
        this.imageHashMap.put(pieceType, image);
      }
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      System.out.println("The rectangle at " + x + " " + y + " does not exist.");
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("The rectangle at " + x + " " + y + " is null.");
    }
  }

  /**
   * Get the tileContainer at a certain position on the game board.
   *
   * @param gridPane The game board.
   * @param i        The x coordinate of the tile.
   * @param j        The y coordinate of the tile.
   * @return The StackPane (tileContainer) at the given position.
   * @author virgil.baclanov
   */
  public StackPane getTileAt(GridPane gridPane, int i, int j) {
    try {
      for (Node node : gridPane.getChildren()) {
        if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j) {
          return (StackPane) node;
        }
      }
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      System.out.println("The rectangle at " + i + " " + j + " does not exist.");
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("The rectangle at " + i + " " + j + " is null.");
    }
    return null;
  }

  /**
   * Clear the game board of all pieces and colors. This is useful when we take a flag and
   * reposition the piece back to its base.
   *
   * @author virgil.baclanov
   */
  public void clearGameBoard() {
    for (Node node : this.getChildren()) {
      Rectangle rectangle = (Rectangle) ((StackPane) node).getChildren().get(0);
      rectangle.setFill(Color.TRANSPARENT);
      ImageView imageView = (ImageView) ((StackPane) node).getChildren().get(1);
      imageView.setImage(null);
    }
  }

  /**
   * Make a move on the game board by updating the game state grid. Clear the piece from the origin
   * and place it at the target.
   *
   * @param xPiece  The x coordinate of the piece.
   * @param yPiece  The y coordinate of the piece.
   * @param xTarget The x coordinate of the target.
   * @param yTarget The y coordinate of the target.
   * @author virgil.baclanov, robert.kratz
   */
  public void makeMove(int xPiece, int yPiece, int xTarget, int yTarget) {
    String pieceCode = this.gameHandler.getGameState().getGrid()[xPiece][yPiece];

    Move move = new Move();
    move.setNewPosition(new int[]{xTarget, yTarget});
    move.setPieceId(pieceCode);

      if (this.gameHandler == null) {
          return;
      }

    try {
      this.gameHandler.makeMove(move);

      this.gameHandler.getGameState().getGrid()[xPiece][yPiece] = "";
      this.gameHandler.getGameState().getGrid()[xTarget][yTarget] = pieceCode;

    } catch (ServerTimeoutException | GameSessionNotFound | ForbiddenMove | GameOver e) {
      e.printStackTrace();
      System.out.println("Error while making move");
    } catch (InvalidMove e) {
      e.printStackTrace();
      System.out.println("Invalid move");
    }
  }

  /**
   * Setter method for the gameBoardHelper.
   *
   * @param gameBoardHelper The game board helper to set.
   * @author virgil.baclanov
   */
  public void setGameBoardHelper(GameBoardHelper gameBoardHelper) {
    this.gameBoardHelper = gameBoardHelper;
  }

  /**
   * Getter method for the gameBoardHelper.
   *
   * @return The game board helper of the game board.
   * @author virgil.baclanov
   */
  public GameBoardHelper getGameBoardHelper() {
    return gameBoardHelper;
  }

  /**
   * Setter method for the actualGameState
   *
   * @param actualGameState The actual game state of the game board.
   * @author virgil.baclanov
   */
  public void setActualGameState(GameState actualGameState) {
    this.actualGameState = actualGameState;
  }

  /**
   * Getter method for the actualGameState
   *
   * @return The actual game state of the game board.
   * @author virgil.baclanov
   */
  public GameState getActualGameState() {
    return actualGameState;
  }

  /**
   * Setter method for the board theme.
   *
   * @param boardTheme The board theme to set.
   * @author virgil.baclanov
   */
  public void setBoardTheme(BoardTheme boardTheme) {
    this.boardTheme = boardTheme;
  }

  /**
   * Getter method for the board theme.
   *
   * @return The board theme of the game board.
   * @author virgil.baclanov
   */
  public BoardTheme getBoardTheme() {
    return boardTheme;
  }

  /**
   * Setter method for the game handler.
   *
   * @param gameHandler The game handler to set.
   * @author robert.kratz
   */
  public void setGameHandler(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
  }

  /**
   * Getter method for the game handler.
   *
   * @return The game handler of the game board.
   * @author robert.kratz
   */
  public GameHandler getGameHandler() {
    return gameHandler;
  }
}
