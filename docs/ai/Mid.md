# AiMedium Class Documentation

## Package
`de.cfp1.ai.utils`

## Imports
- `de.cfp1.client.game.GameHandler`
- `de.cfp1.server.game.state.Move`
- `de.cfp1.server.game.state.Piece`
- `java.util.ArrayList`

## Class Description
`AiMedium` is a class designed to calculate medium difficulty moves in a game. It utilizes helper and evaluator classes to assess the game state and make strategic decisions based on the positions and potential of game pieces.

### Constructors

#### AiMedium(GameHandler gameHandler)
- **Description**: Constructs an `AiMedium` object, initializing helpers and evaluating the pieces based on the current game state.
- **Parameters**:
    - `gameHandler`: An instance of `GameHandler` used to interact with the game state.
- **Implementation**:
    - Initializes `AiHelper` and `AiEvaluator` with the provided `gameHandler`.
    - Retrieves all pieces from the player's team and wraps them into `EvaluatedPiece` objects for further analysis.

### Methods

#### public Move calcMediumMove()
- **Description**: Calculates a move considered to be of medium difficulty. This method looks for optimal moves by assessing the capability of capturing the flag or an enemy piece. If no immediate advantageous moves are found, it will select the worst performing piece and attempt to improve its position.
- **Returns**: `Move` - The calculated move that the AI decides to make.
- **Implementation**:
    - Filters out dead pieces.
    - Evaluates all pieces for current game state importance.
    - Checks each piece for possible moves that could capture the flag or an enemy piece.
    - If no direct advantages are found, selects the piece performing the worst and attempts to move it to a better position.

#### private void evalPieces()
- **Description**: Evaluates all pieces on the board, updating each with new strategic values such as distance to flag and mobility based on the current game state.
- **Implementation**:
    - Iterates through all evaluated pieces and recalculates their strategic importance based on their current positions and possible movements.

### Helper Classes Used
- **AiHelper**: Provides utility functions for game state manipulation and move validation.
- **AiEvaluator**: Assesses pieces based on their strategic value and potential impact on the game.
- **EvaluatedPiece**: A wrapper class for `Piece` that includes additional evaluation metrics.

### Example Usage
```java
GameHandler gameHandler = new GameHandler();
AiMedium ai = new AiMedium(gameHandler);
Move nextMove = ai.calcMediumMove();

### Author
Joel Bakirel