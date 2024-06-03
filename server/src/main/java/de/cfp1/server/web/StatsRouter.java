package de.cfp1.server.web;

import de.cfp1.server.Server;
import de.cfp1.server.web.data.LeaderboardResponse;
import de.cfp1.server.web.data.StatsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author robert.kratz
 */

@RestController
@RequestMapping("/api/stats")
@CrossOrigin("*")
public class StatsRouter {

  /**
   * Get the leaderboard
   *
   * @return The leaderboard
   * @author robert.kratz
   */
  @GetMapping("/leaderboard")
  @Operation(summary = "Returns the 100 top players by elo", responses = {
      @ApiResponse(responseCode = "200", description = "Leaderboard successfully retrieved"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<LeaderboardResponse[]> getStatistics() {
    try {
      LeaderboardResponse[] leaderboard = Server.getDbManager().getLeaderboard();

      return ResponseEntity.ok(leaderboard);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  /**
   * Get the stats of a user
   *
   * @param userId The id of the user
   * @return The stats of the user
   * @author robert.kratz
   */
  @GetMapping("/stats/{userId}")
  @Operation(summary = "Returns the stats of the user", responses = {
      @ApiResponse(responseCode = "200", description = "Stats successfully retrieved"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<StatsResponse> getStats(@PathVariable("userId") String userId) {
    try {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(400).build();
        }

      StatsResponse stats = Server.getDbManager().getUserStatistics(userId);

        if (stats == null) {
            return ResponseEntity.status(404).build();
        }

      return ResponseEntity.ok(stats);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }
}
