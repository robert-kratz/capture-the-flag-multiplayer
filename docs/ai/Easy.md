# AiEasy Class Documentation

## Package
`de.cfp1.ai.utils`

## Imports
- `de.cfp1.client.game.GameHandler`
- `de.cfp1.server.game.state.Move`
- `de.cfp1.server.game.state.Piece`
- `java.util.ArrayList`

## Class Description
`AiEasy` is a class designed to facilitate simple AI movements in a game environment. It utilizes helper classes to manage game pieces and evaluate an easy move based on the game's state.

### Constructors

#### AiEasy(GameHandler gameHandler)
Constructs an `AiEasy` object using a `GameHandler`.
- **Parameters:**
    - `gameHandler` (GameHandler): The game handler that manages the current game state.
- **Functionality:**
    - Initializes `AiHelper` and `AiEvaluator` with the given `GameHandler`.
    - Retrieves all pieces for the AI's team and wraps them in `EvaluatedPiece` objects for further processing.

### Methods

#### public Move calcEasyMove()
Calculates and returns the easiest move the AI can make based on the current game state.
- **Returns:**
    - `Move`: An object representing the move chosen by the AI, including the piece to move and the new position.
- **Functionality:**
    - Filters out any dead pieces from evaluation.
    - Evaluates all pieces to update their distances from the flag.
    - Checks for the possibility of capturing the flag with any valid moves.
    - If no flag capture is possible, it determines the move for the "worst" piece, aiming to improve its position or at least make a neutral move.

#### private void evalPieces()
Evaluates all active pieces by calculating their distances from the flag.
- **Functionality:**
    - Iterates over all evaluated pieces and updates their `disFlag` property, which likely represents the distance to the enemy's flag or a strategic position.

### Helper Classes and Usage
- `AiHelper`: Used for fetching game state details like team pieces and valid moves.
- `AiEvaluator`: Provides functionality to calculate distances and evaluate piece positions.
- `EvaluatedPiece`: A wrapper for pieces that includes evaluation metrics, such as distance to a target.

### Example Usage
```java
GameHandler gameHandler = new GameHandler();
AiEasy ai = new AiEasy(gameHandler);
Move nextMove = ai.calcEasyMove();

### Author
Joel Bakirel