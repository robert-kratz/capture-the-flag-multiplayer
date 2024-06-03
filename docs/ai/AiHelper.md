# AiHelper Class Documentation

## Package
`de.cfp1.ai.utils`

## Imports
- `de.cfp1.client.game.GameHandler`
- `de.cfp1.server.game.GameBoardHelper`
- `de.cfp1.server.game.map.Directions`
- `de.cfp1.server.game.state.GameState`
- `de.cfp1.server.game.state.Piece`
- `de.cfp1.server.game.state.Team`
- `java.util.*`

## Class Description
`AiHelper` provides utility methods to support AI decision-making in a game, focusing on managing game state, piece movements, and interactions between game elements.

### Constructors

#### AiHelper(GameHandler gameHandler)
- **Description**: Constructs an `AiHelper` instance, initializing it with the game handler and fetching the current game state.
- **Parameters**:
    - `gameHandler`: An instance of `GameHandler` that provides access to the current game state.

### Methods

#### Public Methods

##### `setGameHandler(GameHandler gameHandler)`
- **Description**: Updates the game handler and refreshes the game state accordingly.
- **Parameters**:
    - `gameHandler`: The new `GameHandler` to set.

##### `setGameState(GameState gameState)`
- **Description**: Directly sets the game state.
- **Parameters**:
    - `gameState`: The new `GameState` to set.

##### `getMyTeam()`
- **Description**: Retrieves the player's own team.
- **Returns**: `Team` representing the player's team.

##### `getEnemyTeams()`
- **Description**: Fetches a list of all enemy teams.
- **Returns**: `ArrayList<Team>` containing all enemy teams.

##### `getAllTeams()`
- **Description**: Retrieves all teams participating in the game.
- **Returns**: Array of `Team`.

##### `getAllEnemyPieces()`
- **Description**: Compiles a list of all pieces belonging to enemy teams.
- **Returns**: `ArrayList<Piece>` of enemy pieces.

##### `calcDistance(int[] position1, int[] position2)`
- **Description**: Calculates the Euclidean distance between two positions on the grid.
- **Parameters**:
    - `position1`: The first position.
    - `position2`: The second position.
- **Returns**: The calculated distance as an integer.

##### `hasHigherAtk(Piece pawn1, Piece pawn2)`
- **Description**: Compares the attack power of two pieces.
- **Parameters**:
    - `pawn1`: The first piece.
    - `pawn2`: The second piece.
- **Returns**: `true` if `pawn1` has a higher or equal attack power compared to `pawn2`.

##### `getPieceMoves(Piece piece)`
- **Description**: Determines all possible moves for a given piece based on its type.
- **Parameters**:
    - `piece`: The piece to evaluate.
- **Returns**: A 2D array of potential move coordinates.

##### `getType(Piece piece)`
- **Description**: Fetches the type of a given piece.
- **Parameters**:
    - `piece`: The piece whose type is queried.
- **Returns**: Type of the piece as a `String`.

##### `getGrid()`
- **Description**: Provides access to the current game grid.
- **Returns**: A 2D array representing the game grid.

##### `getAllBlocks()`
- **Description**: Lists all blocked positions on the game grid.
- **Returns**: `ArrayList<int[]>` representing blocked positions.

##### `isPieceOnTile(int[] tile)`
- **Description**: Checks if there is a piece at the specified grid location.
- **Parameters**:
    - `tile`: The grid position to check.
- **Returns**: `true` if a piece is present at the location.

##### `getPieceByTile(int[] tile)`
- **Description**: Retrieves the piece located at a specific tile.
- **Parameters**:
    - `tile`: The tile to check.
- **Returns**: `Piece` located at the tile.

##### `isDestOutOfBounds(int[] dest)`
- **Description**: Determines if a destination is outside the game grid boundaries.
- **Parameters**:
    - `dest`: The destination to check.
- **Returns**: `true` if the destination is out of bounds.

##### `getAllBases()`
- **Description**: Retrieves the base positions for all teams except the player's own team.
- **Returns**: `ArrayList<int[]>` representing the base positions.

##### `calculateDistanceBetweenPieceAndFlag(Piece piece)`
- **Description**: Calculates the shortest path from a piece to the nearest enemy flag, considering obstacles.
- **Parameters**:
    - `piece`: The piece from which to calculate distance.
- **Returns**: The calculated distance to the closest flag.

##### `getClosestFlag(Piece piece)`
- **Description**: Identifies the closest enemy flag position relative to a given piece.
- **Parameters**:
    - `piece`: The piece used as a reference.
- **Returns**: Coordinates of the closest flag as `int[]`.

##### `getPieceById(String id)`
- **Description**: Retrieves a piece by its ID from any team.
- **Parameters**:
    - `id`: The ID of the piece to find.
- **Returns**: The `Piece` with the specified ID, or `null` if not found.

##### `getWorstPiece(ArrayList<EvaluatedPiece> evalPieces)`
- **Description**: Identifies the piece with the lowest evaluation value from a list.
- **Parameters**:
    - `evalPieces`: List of evaluated pieces.
- **Returns**: `EvaluatedPiece` with the lowest value.

#### `isFlagOnTile(Piece piece, int[] tile)`
- **Description**: Checks if the closest flag is located on the specified tile.
- **Parameters**:
    - `piece`: The piece in question.
    - `tile`: The coordinates of the tile to check.
- **Returns**: `true` if the closest flag to the piece is on the specified tile.

#### `filterIllegalFlagMoves(Piece piece)`
- **Description**: Filters out any potential moves that would result in illegal positions or capture of own flag.
- **Parameters**:
    - `piece`: The piece to evaluate moves for.
- **Returns**: `ArrayList<int[]>` containing only the legal moves that do not result in flag capture.

#### `getValidMoves(Piece piece)`
- **Description**: Compiles all valid moves for a piece, considering both game rules and strategic benefits like capturing a flag.
- **Parameters**:
    - `piece`: The piece for which to determine valid moves.
- **Returns**: `ArrayList<int[]>` of all valid moves from the current position of the piece.

#### `canCaptureFlag(int[] move)`
- **Description**: Determines if a move results in the capture of an enemy flag.
- **Parameters**:
    - `move`: The coordinates of the move to evaluate.
- **Returns**: `true` if the move captures a flag.

#### `canCaptureEnemy(Piece p, int[] move)`
- **Description**: Checks if the specified move allows a piece to capture an enemy piece.
- **Parameters**:
    - `p`: The piece making the move.
    - `move`: The destination coordinates of the move.
- **Returns**: `true` if the move results in an enemy capture.

#### `isBlockOnTile(int[] tile)`
- **Description**: Verifies if the specified tile is blocked.
- **Parameters**:
    - `tile`: The coordinates of the tile to check.
- **Returns**: `true` if the tile is blocked.

#### `filterDeadPieces(ArrayList<EvaluatedPiece> evalPieces)`
- **Description**: Filters out and removes dead pieces from a list of evaluated pieces.
- **Parameters**:
    - `evalPieces`: List of pieces to filter.
- **Returns**: `ArrayList<EvaluatedPiece>` containing only alive pieces.

### Author
Joel Bakirel
