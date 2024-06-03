### Class: GameTeamHelper

`GameTeamHelper` operates by providing methods to manipulate the state of teams within the game. It handles tasks such as adding new teams to the game, updating team information, and managing team-related game actions like placing and moving pieces or handling team elimination.

### Properties

- **totalTeamSlots**: The maximum number of teams allowed in the game.
- **totalFlagsPerTeam**: The number of flags each team starts with in the game.
- **totalPiecesPerTeam**: The number of pieces each team has, calculated based on the game map template.
- **colors**: An array of string representing team colors used to visually distinguish teams in the game interface.

### Key Methods

#### Team Management

- **addTeam(GameState gameState, String teamId, MapTemplate mapTemplate)**: Adds a new team to the game. It initializes the team with a unique ID, assigns a color, and sets up initial pieces based on the game's map template.

- **removeTeam(GameState gameState, String teamId)**: Removes a team from the game. This is used for handling team concessions or eliminations.

- **updatePieceInTeam(GameState gameState, Piece piece)**: Updates a piece's information within its team's context, such as its position on the game board after a move.

#### Team Information

- **getTeamById(GameState gameState, String teamId)**: Retrieves a team by its ID from the current game state.

- **getPieceById(GameState gameState, String pieceId)**: Fetches a piece by its ID, scanning through all teams in the game state.

- **getTeamsOnline(GameState gameState)**: Returns the count of non-null teams currently active in the game.

- **getFlagCount(GameState gameState, String teamId)**: Gets the number of flags a team has.

- **setFlagCount(GameState gameState, String teamId, int flagCount)**: Sets the number of flags for a team.

#### Utility Methods

- **setTeamToIndexInArray(GameState gameState, int index, Team team)**: Inserts a team at a specified index in the game state's team array.

- **getTotalPiecesForMapTemplate(PieceDescription[] pieces)**: Calculates the total number of pieces for all teams based on the map template.

- **getIndexOfTeamId(Team[] teams, String teamId)**: Finds the index of a team in the array using the team's ID.

### Usage in Game Flow

1. **Initialization**: Teams are created and initialized as players join the game.
2. **Game Progression**: Teams' states are updated as players make moves or perform actions affecting their pieces or flags.
3. **Game Termination**: Teams are removed or updated based on game end conditions such as concessions or completion of objectives.

robert.kratz May 2. 2024
