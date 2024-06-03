# GameBoard Documentation

## GameBoard.java

This class handles the GUI logic for the game board. It includes methods for:

## Key Methods

### Initialization

`GameBoard(GameHandler gameHandler, BoardTheme boardTheme, boolean isModifiable)`: This constructor is used for interacting with and visualizing the game board.

`GameBoard(GameState actualGameState, BoardTheme boardTheme, boolean isModifiable)`: This constructor is used for only visualizing the game board.

### Game Board Creation and Update

`drawGameBoard(GameState gameState)`: This method is used to create the game board visually.

`populateGameBoard(GameState gameState)`: This method is used to populate the game board with pieces.

`updateGameBoard(int xOrigin, int yOrigin, int xTarget, int yTarget)`: This method is used to visually update the game board after a move.

`clearGameBoard()`: This method is used to clear the game board of all pieces and colors.

### Game Play

`selectionListener(StackPane newlySelectedRectangleContainer)`: This method is used to manage the visual guidelines for the player's move upon clicking on a tile.

`makeMove(int xPiece, int yPiece, int xTarget, int yTarget)`: This method is used to make a move on the game board by updating the game state grid.

`displayMoveListener(Piece piece, Color color)`: This method is used to display the possible moves of a piece.

### Icon Management

`changeIconPiece(int x, int y, String pieceType)`: This method is used to change the icon of a piece on the game board.

## Usage in Game Flow

The `GameBoard` is used during a game session. It displays the current state of the game, allows players to make moves, and updates the game board accordingly. It also provides visual guidelines for the player's move upon clicking on a tile.

robert.kratz May 2. 2024
