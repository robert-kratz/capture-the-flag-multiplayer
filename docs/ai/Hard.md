# AiHard Class Documentation

## Package
`de.cfp1.ai.utils`

## Imports
- `de.cfp1.client.game.GameHandler`
- `de.cfp1.server.game.state.Move`
- `de.cfp1.server.game.state.Piece`
- `java.util.ArrayList`

## Class Description
`AiHard` is a class designed to handle more complex AI decision-making in a game, utilizing advanced strategies to determine optimal moves. This class is built to provide challenging gameplay by evaluating multiple aspects of each piece's strategic importance and current position.

### Constructors

#### AiHard(GameHandler gameHandler)
Initializes an `AiHard` object with a `GameHandler`.
- **Parameters:**
    - `gameHandler` (GameHandler): The game handler responsible for the game state management.
- **Functionality:**
    - Utilizes `AiHelper` and `AiEvaluator` for handling game piece interactions and evaluations.
    - Converts all pieces belonging to the AI's team into `EvaluatedPiece` instances for enhanced decision-making.

### Methods

#### public Move calcHardMove()
Calculates and returns the most strategically advantageous move based on current game conditions.
- **Returns:**
    - `Move`: The chosen move which may aim to capture a flag, capture an enemy piece, or improve a piece's position based on its evaluation.
- **Functionality:**
    - Filters out dead pieces to focus only on active ones.
    - Evaluates all pieces for various strategic metrics such as threat levels, mobility, defensive value, and distance to the flag.
    - Prioritizes moves that can capture the flag or an enemy.
    - If no immediate offensive moves are available, it selects the "worst" piece to move based on its current strategic value, attempting to improve its position or impact.

#### private void evalPieces()
Evaluates all pieces in `evalPieces`, updating their strategic values such as threat level, defensive value, mobility, and distance to flag.
- **Functionality:**
    - Iteratively updates each `EvaluatedPiece` in the list with new values, influencing decision-making in the `calcHardMove` method.

### Helper Classes and Usage
- `AiHelper`: Assists in retrieving game state details, such as team pieces and their statuses.
- `AiEvaluator`: Used for calculating various strategic values of the pieces.
- `EvaluatedPiece`: A wrapper class that enhances pieces with additional metrics for in-depth evaluation.

### Example Usage
```java
GameHandler gameHandler = new GameHandler();
AiHard ai = new AiHard(gameHandler);
Move nextMove = ai.calcHardMove();

### Author
Joel Bakirel
