### Class: CtfGame

`CtfGame` implements the `Game` interface and uses additional helper classes (`GameTeamHelper` and `GameBoardHelper`) to manage specific aspects of the game such as team and board management. The class is designed to handle game setup, turn management, and termination conditions while providing mechanisms to interact with the game programmatically.

### Properties

- **gameThread**: A thread in which the game loop runs.
- **gameBoardHelper**: An instance of `GameBoardHelper` used to manage game board operations.
- **gameTeamHelper**: An instance of `GameTeamHelper` used for team-related operations.
- **currentGameState**: Tracks the current state of the game (e.g., LOBBY, RUNNING, FINISHED).
- **gameState**: Holds the current state of the game including all teams and game board status.
- **mapTemplate**: The template defining the game board configuration.
- **totalMaxTime**, **totalMoveTime**: Time limits for the game and individual moves.
- **startTime**, **endTime**, **lastMoveBeginTime**: Timestamps used to manage game timing.

### Key Methods

#### Game Initialization and Setup

- **create(MapTemplate template)**: Initializes a new game based on a provided map template. This sets up the initial state of the game, including the configuration of the game board, the initialization of teams, and setting time limits based on the template settings.

#### Game Participation

- **joinGame(String teamId)**: Allows a player to join the game by assigning them to a team. If all teams are filled, the game starts.

#### Game Progression

- **makeMove(Move move)**: Processes a player's move, ensuring it is valid within the game rules. This method updates the game state to reflect the move, handles captures, and manages the transition of turns between players.

- **giveUp(String teamId)**: Allows a team to concede. This method removes the team from the game, adjusts the game state accordingly, and checks if the game should end due to the concession.

#### Game State Management

- **skipCurrentPlayer()**: Advances the game to the next player, skipping over the current player's turn. This is used to enforce move timeouts.

- **startTimer()**: Starts the game timer, setting up the overall game duration and handling timeouts.

#### Game Termination Checks

- **isStarted()**: Checks whether the game has started.
- **isGameOver()**: Checks whether the game has ended.
- **isTimeOver()**: Determines if the game time has expired or if insufficient teams remain to continue play.

#### Utility Methods

- **getNextTeamIndex(int currentTeamIndex)**: Calculates the index of the next team to take a turn, skipping over any null (inactive) teams.
- **isGridValid(MapTemplate template)**: Validates the game board configuration against the game rules, ensuring that the setup is legal and playable.

### Game Lifecycle

1. **Initialization**: A game is created with a map template defining the structure and rules.
2. **Joining**: Players join the game and are assigned to teams.
3. **Running**: The game loop runs in its own thread, handling moves and turn transitions until the game ends.
4. **Ending**: The game concludes when a win condition is met or when no valid moves remain.

robert.kratz May 2. 2024
