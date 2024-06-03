## Interface: GameEvent

**Package:** `de.cfp1.client.game`

### Description

`GameEvent` is an interface designed to handle various game events within the client-side application. This includes events related to the game's lifecycle, such as starting, ending, and during the turn of each player.

### Methods

#### void onWaiting()

Called repeatedly every second when the client is waiting in the lobby for other players to join.

- **Purpose:** Provides a callback for updating the GUI or handling tasks during the wait.

#### void onGameEnded()

Called once when the game has concluded.

- **Purpose:** Typically used to clean up resources, stop threads, or navigate away from the game scene.

#### void onGameStart()

Triggered once when the game starts.

- **Purpose:** Used to initialize game components or UI elements specific to the gameplay.

#### void onGameError(String message)

Invoked when an error occurs during the game.

- **Parameters:**
  - `message`: A string detailing the error that occurred.
- **Purpose:** Allows the GUI to display error messages or handle game failures gracefully.

#### void onMyTurn(GameState gameState)

Called every second during the client's turn.

- **Parameters:**
  - `gameState`: The current state of the game, providing context necessary for making decisions or updating the UI.
- **Purpose:** Enables the client to respond to their turn, typically by updating the game board or enabling user interactions.

#### void onOpponentTurn(GameState gameState)

Invoked every second when it is an opponent's turn.

- **Parameters:**
  - `gameState`: The current state of the game from the perspective of the opponent's turn.
- **Purpose:** Useful for updating the GUI to reflect changes made by the opponent or to disable user interactions.

#### void onGameDelete()

Called when the game session is deleted.

- **Purpose:** Cleans up resources or handles other necessary teardown processes when a game session is removed.

### Implementation Notes

- Implementers of this interface should handle UI updates and interactions efficiently to maintain responsive and performant gameplay.
- Error handling within these methods should be robust to prevent crashes and ensure a smooth user experience.

### Example Usage

```java
public class GameGUIController implements GameEvent {
    @Override
    public void onWaiting() {
        // Update waiting status on GUI
    }

    @Override
    public void onGameEnded() {
        // Display game over screen
    }

    @Override
    public void onGameStart() {
        // Prepare the game board for playing
    }

    @Override
    public void onGameError(String message) {
        // Show error message to the user
    }

    @Override
    public void onMyTurn(GameState gameState) {
        // Enable controls for user interaction
    }

    @Override
    public void onOpponentTurn(GameState gameState) {
        // Update game board based on opponent's actions
    }

    @Override
    public void onGameDelete() {
        // Handle game session cleanup
    }
}
```

robert.kratz May 2. 2024
