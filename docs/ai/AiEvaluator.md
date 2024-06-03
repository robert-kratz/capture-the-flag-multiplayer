# AiEvaluator Class Documentation

## Package
`de.cfp1.ai.utils`

## Imports
- `de.cfp1.client.game.GameHandler`
- `de.cfp1.server.game.state.Piece`
- `de.cfp1.server.game.state.Team`
- `java.util.ArrayList`

## Class Description
`AiEvaluator` is a class designed to evaluate various metrics of game pieces, such as threats, defensive value, mobility, and distance to flags. This class helps in assessing the position and potential moves of pieces in a game environment.

### Constructor

#### AiEvaluator(GameHandler gameHandler)
Initializes a new instance of the `AiEvaluator` class with a specific `GameHandler`.
- **Parameters:**
    - `gameHandler` (GameHandler): The game handler to manage game states and operations.

### Methods

#### public int calcThreats(Piece p)
Calculates and returns the threat level for a given piece based on its proximity to enemy pieces.
- **Parameters:**
    - `p` (Piece): The game piece to evaluate.
- **Returns:**
    - `int`: The calculated threat value, indicating the number of enemy pieces within a threatening range.
- **Details:**
    - Threat level increases when enemy pieces are within a range of 3 units and have a higher attack value than the piece being evaluated.

#### public int calcDefensiveValue(Piece p)
Evaluates and returns the defensive value of a piece based on its distance from the team's base.
- **Parameters:**
    - `p` (Piece): The game piece to evaluate.
- **Returns:**
    - `int`: The defensive value, which is higher when the piece is closer to its own base.
- **Details:**
    - Returns `2` if the piece is one away from the base, `1` if it's two units away, and `0` otherwise.

#### public int calcMobility(Piece p)
Calculates the mobility value of a piece, taking into account the valid moves it can make and any obstacles or restrictions.
- **Parameters:**
    - `p` (Piece): The game piece to evaluate.
- **Returns:**
    - `int`: The mobility value, representing the number of viable moves the piece can make.
- **Details:**
    - Mobility is reduced for moves that go out of bounds, land on occupied tiles, or move towards the flag.

#### public int calcDisFlag(Piece p)
Calculates the value related to the distance between the piece and the flag, with a higher value indicating closer proximity.
- **Parameters:**
    - `p` (Piece): The game piece to evaluate.
- **Returns:**
    - `int`: A value inversely related to the distance to the flag.
- **Details:**
    - The calculation adjusts the distance value to provide higher scores for shorter distances, useful for strategies aiming at flag capture.

### Example Usage
```java
GameHandler gameHandler = new GameHandler();
AiEvaluator evaluator = new AiEvaluator(gameHandler);
Piece piece = new Piece();
int threatLevel = evaluator.calcThreats(piece);

### Author
Joel Bakirel