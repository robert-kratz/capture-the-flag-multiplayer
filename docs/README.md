# Capture The Flag Group 1 Documentation

Out application is a Capture The Flag game where players can create and play on custom maps. The game is implemented in Java using the Spring Boot framework for the backend and JavaFX for the frontend. The game engine is implemented in Java and uses a client-server architecture. Besides this, the game also has an AI component that allows players to play against AI opponents.

## Milestones

You can find the milestones [here](/docs/milestones.md).

## Third Party Libraries

You can find the third party libraries used in the project [here](/docs/used_libaries.md).

## Server

- ### Auth
  - [Auth Provider](server/auth/AuthProvider.md) to handle user authentication.
- ### Game
  - [Ctf Game](server/game/CtfGame.md) to manage game engine operations.
  - [Game Board Helper](server/game/GameBoardHelper.md) to manage game board operations.
  - [Game Team Helper](server/game/GameTeamHelper.md) to manage team-related operations.
- ### SQL
  - [Database](server/sql/Database.md) to handle database operations.
  - [Database Manager](server/sql/DatabaseManager.md) to manage database operations related to user and map data.
- ### Web
  - [Auth](server/web/Auth.md) to handle user authentication.
  - [Game](server/web/Game.md) to manage game operations.
  - [Map](server/web/Map.md) to manage map operations.
  - [Stats](server/web/Stats.md) to manage statistics operations.

## Client

- ### Game
  - [Game Event](client/game/GameEvent.md) interface to handle game events.
  - [Game Handler](client/game/GameHandler.md) to manage game operations.
- ### GUI

  - [CreateGameController](client/gui/CreateGameController.md) to handle the GUI logic for creating a game.
  - [EditMapController](client/gui/EditMapController.md) to handle the GUI logic for editing a map.
  - [EndGameController](client/gui/EndGameController.md) to handle the GUI logic for ending a game.
  - [GameBoard](client/gui/GameBoard.md) to handle the GUI logic for the game board.
  - [GameBoardController](client/gui/GameBoardController.md) to handle the GUI logic for the game board.
  - [GUIDriver](client/gui/GUIDriver.md) to handle the GUI logic for the game.
  - [JoinGameController](client/gui/JoinGameController.md) to handle the GUI logic for joining a game.
  - [LeaderboardController](client/gui/LeaderboardController.md) to handle the GUI logic for the leaderboard.
  - [LoginController](client/gui/LoginController.md) to handle the GUI logic for logging in.
  - [MainMenuController](client/gui/MainMenuController.md) to handle the GUI logic for the main menu.
  - [MapEditorController](client/gui/MapEditorController.md) to handle the GUI logic for the map editor.
  - [PlayMenuController](client/gui/PlayMenuController.md) to handle the GUI logic for the play menu.
  - [SettingsController](client/gui/SettingsController.md) to handle the GUI logic for the settings.
  - [SignUpController](client/gui/SignUpController.md) to handle the GUI logic for signing up.
  - [WaitingLobbyController](client/gui/WaitingLobbyController.md) to handle the GUI logic for the waiting lobby.

- ### Net
  - [Map Handler](client/net/MapHandler.md) to manage map-related operations on the client side.
  - [Network Handler](client/net/NetworkHandler.md) to handle overall network operations.
  - [Request Builder](client/net/RequestBuilder.md) to construct HTTP requests.
  - [Statistics Handler](client/net/StatisticsHandler.md) to manage statistics-related data.
- ### Utils
  - [Process Manager](client/utils/ProcessManager.md) to manage external processes like the ai client.

## AI

- [Easy](ai/Easy.md) calculates Easy Move 
- [Mid](ai/Mid.md) calculates Mid Move
- [Hard](ai/Hard.md) calculates Hard Move
- [Evaluator](ai/Evaluator.md) evaluates all Pieces
- [EvaluatedPiece](ai/EvaluatedPiece.md) class to store the evaluation of a piece
- [Helper](ai/AiHelper.md) class to help the AI calculate the best move

Last updated: May 7, 2024
