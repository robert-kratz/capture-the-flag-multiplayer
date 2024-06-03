package de.cfp1.client.net;

import com.google.gson.Gson;
import de.cfp1.server.entities.Map;
import de.cfp1.server.entities.User;
import de.cfp1.server.exception.MapNotFoundException;
import de.cfp1.server.exception.RequestErrorException;
import de.cfp1.server.exception.ServerTimeoutException;
import de.cfp1.server.exception.UserNotFoundException;
import de.cfp1.server.web.data.LeaderboardResponse;
import de.cfp1.server.web.data.StatsResponse;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author robert.kratz
 */

public class StatisticsHandler {

  private final String BASE_URL = NetworkHandler.getBaseUrl();
  private final NetworkHandler networkHandler;

  /**
   * Constructor
   *
   * @param networkHandler NetworkHandler
   */
  public StatisticsHandler(NetworkHandler networkHandler) {
    this.networkHandler = networkHandler;
  }

  /**
   * Get the leaderboard of the top 100 players by elo
   *
   * @return The leaderboard
   * @throws ServerTimeoutException ServerTimeoutException
   * @author robert.kratz
   */
  public ArrayList<LeaderboardResponse> getLeaderboard() throws ServerTimeoutException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/stats/leaderboard");
      request.method("GET");
      String tokenJson = request.send();

      LeaderboardResponse[] leaderboard = new Gson().fromJson(tokenJson,
          LeaderboardResponse[].class);

      System.out.println("Loaded leaderboard with " + leaderboard.length + " entries");

      //format leaderboard into arraylist
      ArrayList<LeaderboardResponse> leaderboardList = new ArrayList<>();
      Collections.addAll(leaderboardList, leaderboard);

        if (networkHandler != null) {
            networkHandler.getEventListener()
                .onEvent(NetworkEventType.FETCH_LEADERBOARD, leaderboardList);
        }

      return leaderboardList;
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get the leaderboard
   *
   * @param userid The id of the user
   * @return The stats of the user
   * @throws ServerTimeoutException ServerTimeoutException
   * @throws UserNotFoundException  Userid does not have stats or does not exist
   * @author robert.kratz
   */
  public StatsResponse getStats(String userid)
      throws ServerTimeoutException, UserNotFoundException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/stats/stats/" + userid);
      request.method("GET");
      String tokenJson = request.send();

      StatsResponse stats = new Gson().fromJson(tokenJson, StatsResponse.class);

      System.out.println("Loaded stats for user " + userid);

        if (networkHandler != null) {
            networkHandler.getEventListener().onEvent(NetworkEventType.FETCH_USER_STATS, stats);
        }

      return stats;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("User not found")) {
        throw new UserNotFoundException(e.getMessage());
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Check if the user has stats by checking if the user is a guest
   *
   * @param user The user
   * @return True if the user has stats, false if not
   */
  public static boolean userHasStats(User user) {
    return user.isGuest();
  }
}
