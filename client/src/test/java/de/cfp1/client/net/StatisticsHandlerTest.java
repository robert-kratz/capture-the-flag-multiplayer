package de.cfp1.client.net;

import static org.junit.jupiter.api.Assertions.*;

import de.cfp1.server.entities.User;
import de.cfp1.server.web.data.LeaderboardResponse;
import de.cfp1.server.web.data.StatsResponse;
import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("These tests only work with a running server. For this purpose, the server must be started and the server URL must be set in the NetworkHandler.")
class StatisticsHandlerTest {

  @Test
  void getLeaderBoard() {
    // Arrange
    StatisticsHandler statisticsHandler = new StatisticsHandler(new NetworkHandler(
        new NetworkEvent() {
          @Override
          public void onEvent(NetworkEventType type, Object... args) {
            System.out.println("Event: " + type + " with args: ");
          }
        }));

    // Act
    ArrayList<LeaderboardResponse> leaderboard = statisticsHandler.getLeaderboard();

    // Assert
    assertNotNull(leaderboard);
  }

  @Test
  void getUserStatistics() {

    NetworkHandler networkHandler = new NetworkHandler(
        new NetworkEvent() {
          @Override
          public void onEvent(NetworkEventType type, Object... args) {
            System.out.println("Event: " + type + " with args: ");
          }
        });

    networkHandler.signUpAsGuest();

    // Arrange
    StatisticsHandler statisticsHandler = new StatisticsHandler(networkHandler);

    try {
      User user = networkHandler.getUserFromServer();

      StatsResponse statsResponse = statisticsHandler.getStats(user.getId());

      System.out.println("Wins: " + statsResponse.getWins());
      System.out.println("Losses: " + statsResponse.getLosses());
      System.out.println("Draws: " + statsResponse.getDraws());
      System.out.println("Total games: " + statsResponse.getElo());

      assertNotNull(statsResponse);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }
}