# EvaluatedPiece Class Documentation

## Package
`de.cfp1.ai.utils`

## Imports
- `de.cfp1.server.game.state.Piece`

## Class Description
`EvaluatedPiece` encapsulates a game piece and evaluates its strategic value based on its distance to the flag, mobility, defensive capabilities, and potential threats. This class provides a comprehensive way to assess pieces for decision-making in AI strategies.

### Constructors

#### EvaluatedPiece(Piece piece)
- **Description**: Initializes a new `EvaluatedPiece` with basic default values.
- **Parameters**:
    - `piece`: The game piece to evaluate.

#### EvaluatedPiece(Piece piece, int disFlag, int defensiveValue, int mobility, int threats)
- **Description**: Initializes a new `EvaluatedPiece` with specific values for various strategic metrics.
- **Parameters**:
    - `piece`: The game piece to evaluate.
    - `disFlag`: Distance to the closest flag.
    - `defensiveValue`: Defensive strength of the piece.
    - `mobility`: Mobility value indicating how many moves the piece can make.
    - `threats`: Potential threats to the piece from opponents.

### Methods

#### `getPiece()`
- **Description**: Retrieves the game piece associated with this evaluation.
- **Returns**: `Piece` - The underlying game piece.

#### `getDisFlag()`
- **Description**: Gets the calculated distance to the flag.
- **Returns**: `int` - The distance to the flag.

#### `getMobility()`
- **Description**: Gets the mobility score of the piece.
- **Returns**: `int` - The mobility score.

#### `getDefensiveValue()`
- **Description**: Gets the defensive value of the piece.
- **Returns**: `int` - The defensive value.

#### `getThreats()`
- **Description**: Gets the number of threats to the piece.
- **Returns**: `int` - The threat count.

#### `getValue()`
- **Description**: Gets the overall strategic value of the piece, calculated as the sum of its attributes.
- **Returns**: `int` - The total strategic value.

#### `setPiece(Piece piece)`
- **Description**: Sets the piece to be evaluated.
- **Parameters**:
    - `piece`: The piece to evaluate.

#### `setDisFlag(int disFlag)`
- **Description**: Sets the distance to the flag and recalculates the total value.
- **Parameters**:
    - `disFlag`: The distance to the flag.

#### `setMobility(int mobility)`
- **Description**: Sets the mobility score and updates the total value.
- **Parameters**:
    - `mobility`: The mobility score.

#### `setDefensiveValue(int defensiveValue)`
- **Description**: Sets the defensive value and adjusts the total strategic value.
- **Parameters**:
    - `defensiveValue`: The defensive value.

#### `setThreats(int threats)`
- **Description**: Sets the number of threats and recalculates the total value.
- **Parameters**:
    - `threats`: The count of threats.

#### `setValue()`
- **Description**: Recalculates the total value based on the current attributes.

#### `setValue(int value)`
- **Description**: Directly sets the total value.
- **Parameters**:
    - `value`: The new value to set.

### Author
Joel Bakirel
