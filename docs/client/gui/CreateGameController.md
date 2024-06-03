### Class: GameBoardHelper

`GameBoardHelper` offers a suite of methods aimed at initializing the game board, managing piece movements, and performing game state checks to support gameplay mechanics. Its functionalities are critical in determining the spatial dynamics of gameplay, including piece interactions and movement validations.

### Key Methods

#### Board Initialization

- **`createEmptyGrid(int[] gridSize)`**: Initializes an empty game board grid based on specified dimensions. The grid is filled with empty strings indicating unoccupied spaces.

- **`placePiecesAndBase(GameState gameState, String[][] grid, Team[] teams, MapTemplate mapTemplate)`**: Positions teams' bases and pieces on the game board according to the map template's specifications.

#### Piece Movement and Validation

- **`canMoveTo(Team[] teams, GameState gameState, int xPiece, int yPiece, int xTarget, int yTarget)`**: Checks if a piece at a specific position can legally move to a target position based on game rules and current board state.

- **`isValidMove(Move move)`**: Validates a proposed move by a piece to ensure it adheres to the movement rules specified by the piece's capabilities and other game rules.

- **`getPieceAt(Team[] teams, int x, int y)`**: Retrieves a piece located at specific coordinates on the game board, if any.

#### Game State Management

- **`printGridToConsole(String[][] grid)`**: Outputs the current state of the game board to the console, primarily for debugging purposes.

- **`getRemainingPiecesFromTeam(GameState gameState, String teamId)`**: Counts the number of pieces a team has left on the board.

- **`removeAllPiecesOfTeam(GameState gameState, String teamId)`**: Removes all pieces belonging to a specified team from the game state.

- **`isPiece(String string)`** and **`isFlag(String string)`**: Utility methods to determine whether a cell in the grid contains a piece or a flag, respectively.

#### Board Setup and Configuration

- **`placeBlocks(String[][] grid, int numberOfBlocks)`**: Randomly places blocks on the game board to create obstacles.

- **`isBlock(String string)`**: Checks if a specific grid cell contains a block.

#### Spatial Utilities

- **`isObstacleBetweenVerticalHorizontal(int xPiece, int yPiece, int xTarget, int yTarget, String[][] grid)`** and **`isObstacleBetweenDiagonal(int xPiece, int yPiece, int xTarget, int yTarget, String[][] grid)`**: Determine if there are any obstacles (blocks or other pieces) between two points on the grid, either in straight lines or diagonally.

- **`getMirroredPosition(String[][] grid, int row, int column)`**: Calculates the mirrored position on the board for a given cell, useful for symmetrically arranging items on the board.

### Usage in Game Flow

The `GameBoardHelper` is pivotal throughout the game:

1. **Setup Phase**: It initializes the game board and places all initial pieces and obstacles according to the game's map template.
2. **Gameplay Phase**: It validates all moves made by players, ensuring they adhere to the rules and updates the game board accordingly.
3. **Endgame Checks**: It assists in determining when the game ends, either through the capture of flags, elimination of teams, or exhaustion of legal moves.
