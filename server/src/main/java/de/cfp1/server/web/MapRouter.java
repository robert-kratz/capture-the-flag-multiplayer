package de.cfp1.server.web;

import com.google.gson.Gson;
import de.cfp1.server.Server;
import de.cfp1.server.auth.AuthProvider;
import de.cfp1.server.auth.TokenType;
import de.cfp1.server.entities.Map;
import de.cfp1.server.entities.User;
import de.cfp1.server.web.data.MapUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Router: /api/map/maps - GET - Get all maps Router: /api/map/public/maps - GET - Get all public
 * maps Router: /api/map/create - POST - Create a map Router: /api/map/update/{id} - PUT - Update a
 * map Router: /api/map/delete/{id} - POST - Delete a map
 *
 * @author robert.kratz
 */

@RestController
@RequestMapping("/api/map")
@CrossOrigin("*")
public class MapRouter {

  private static final Logger LOG = LoggerFactory.getLogger(MapRouter.class);

  private final AuthProvider authenticationManager = new AuthProvider(); // Assuming default constructor for simplicity

  /**
   * Get all maps
   *
   * @param authToken The authorization token
   * @return All maps
   * @author robert.kratz
   */
  @GetMapping("/maps")
  @Operation(summary = "Get all maps", responses = {
      @ApiResponse(description = "All maps", responseCode = "200",
          content = @Content(schema = @Schema(implementation = Map[].class))),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "Token invalid", responseCode = "403"),
      @ApiResponse(description = "Fail to fetch maps", responseCode = "500")
  })
  public ResponseEntity<?> getMaps(
      @RequestHeader(value = "Authorization", required = false) String authToken) {
    try {
      User userFromToken;
      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(403).body("Invalid token");
      }

        if (userFromToken == null) {
            return ResponseEntity.status(403).body("Invalid token");
        }

      ArrayList<Map> maps = Server.getDbManager().getMapTemplates(userFromToken.getId());

      return ResponseEntity.ok(maps);
    } catch (Exception e) {
      LOG.error("Fail to fetch maps", e);
      return ResponseEntity.status(500).body("Fail to fetch maps");
    }
  }

  /**
   * Get all public maps
   *
   * @return All public maps
   * @author robert.kratz
   */
  @GetMapping("/public/maps")
  @Operation(summary = "Get all public maps", responses = {
      @ApiResponse(description = "All public maps", responseCode = "200",
          content = @Content(schema = @Schema(implementation = Map[].class))),
      @ApiResponse(description = "Fail to fetch public maps", responseCode = "500")
  })
  public ResponseEntity<?> getPublicMaps() {
    try {
      ArrayList<Map> publicMaps = Server.getDbManager().getPublicMapTemplates();

      return ResponseEntity.ok(publicMaps);
    } catch (Exception e) {
      LOG.error("Fail to fetch public maps", e);
      return ResponseEntity.status(500).body("Fail to fetch public maps");
    }
  }

  /**
   * Get a map by id
   *
   * @param authToken The authorization token
   * @param id        The id of the map to get
   * @return The map
   */
  @GetMapping("/search/{id}")
  @Operation(summary = "Get a map by id", responses = {
      @ApiResponse(description = "Map", responseCode = "200",
          content = @Content(schema = @Schema(implementation = Map.class))),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "Token invalid", responseCode = "403"),
      @ApiResponse(description = "Map not found", responseCode = "404"),
      @ApiResponse(description = "Fail to fetch map", responseCode = "500")
  })
  public ResponseEntity<?> getMapById(@RequestHeader(value = "Authorization") String authToken,
      @PathVariable("id") String id) {
    try {
      User userFromToken;
      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(403).body("Invalid token");
      }

        if (userFromToken == null) {
            return ResponseEntity.status(403).body("Invalid token");
        }

      Map map = Server.getDbManager().getMapTemplateById(id);
        if (map == null) {
            return ResponseEntity.status(404).body("Map not found");
        }

      return ResponseEntity.ok(map);
    } catch (Exception e) {
      LOG.error("Fail to fetch map", e);
      return ResponseEntity.status(500).body("Fail to fetch map");
    }
  }

  /**
   * Create a map
   *
   * @param authToken The authorization token
   * @return The created map
   * @author robert.kratz
   */
  @PostMapping("/create")
  @Operation(summary = "Create a map", responses = {
      @ApiResponse(description = "Map created", responseCode = "200", content = @Content(schema = @Schema(implementation = Map.class))),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "Token invalid", responseCode = "403"),
      @ApiResponse(description = "Fail to create map", responseCode = "500")
  })
  public ResponseEntity<?> createMap(@RequestHeader(value = "Authorization") String authToken) {
    try {
      User userFromToken;
      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(403).body("Invalid token");
      }

        if (userFromToken == null) {
            return ResponseEntity.status(403).body("Invalid token");
        }

      int nextMapNumber = Server.getDbManager().getMapCount(userFromToken.getId()) + 1;
      String mapId = UUID.randomUUID().toString();

      Map newMap = Server.getDbManager()
          .createMapTemplate(userFromToken.getId(), "New Map " + nextMapNumber, mapId, false);

        if (newMap == null) {
            return ResponseEntity.status(500).body("Fail to create map");
        }

      return ResponseEntity.ok(newMap);
    } catch (Exception e) {
      LOG.error("Fail to create map", e);
      return ResponseEntity.status(500).body("Fail to create map");
    }
  }

  /**
   * Update a map
   *
   * @param authToken        The authorization token
   * @param mapUpdateRequest The map to update
   * @param id               The id of the map to update
   * @return The updated map
   * @author robert.kratz
   */
  @PutMapping("/update/{id}")
  @Operation(summary = "Update a map", responses = {
      @ApiResponse(description = "Map updated", responseCode = "200", content = @Content(schema = @Schema(implementation = Map.class))),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "Token invalid", responseCode = "403"),
      @ApiResponse(description = "Map not found", responseCode = "404"),
      @ApiResponse(description = "Fail to update map", responseCode = "500")
  })
  public ResponseEntity<?> updateMap(@RequestHeader(value = "Authorization") String authToken,
      @RequestBody MapUpdateRequest mapUpdateRequest, @PathVariable("id") String id) {
    try {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.status(404).body("Map not found");
        }

      User userFromToken;
      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(403).body("Invalid token");
      }

        if (userFromToken == null) {
            return ResponseEntity.status(403).body("Invalid token");
        }

      Map map = Server.getDbManager().getMapTemplateById(id);

        if (map == null) {
            return ResponseEntity.status(404).body("Map not found");
        }

      String mapTemplateJson = new Gson().toJson(mapUpdateRequest.getMapTemplate());

      System.out.println("Map template: " + mapTemplateJson);

      Server.getDbManager()
          .updateMapTemplate(mapUpdateRequest.getName(), id, mapUpdateRequest.isPublic(),
              mapTemplateJson);

      LOG.info("Map updated: " + id);

      Map updatedMap = Server.getDbManager().getMapTemplateById(id);

      return ResponseEntity.ok(updatedMap);
    } catch (Exception e) {
      LOG.error("Fail to update map", e);
      return ResponseEntity.status(500).body("Fail to update map");
    }
  }

  /**
   * Delete a map
   *
   * @param authToken The authorization token
   * @param id        The id of the map to delete
   * @return The deleted map
   * @author robert.kratz
   */
  @DeleteMapping("/delete/{id}")
  @Operation(summary = "Delete a map", responses = {
      @ApiResponse(description = "Map deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = boolean.class))),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "Token invalid", responseCode = "403"),
      @ApiResponse(description = "Map not found", responseCode = "404"),
      @ApiResponse(description = "Fail to delete map", responseCode = "500")
  })
  public ResponseEntity<?> deleteMap(@RequestHeader(value = "Authorization") String authToken,
      @PathVariable("id") String id) {
    try {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.status(404).body("Map not found");
        }

      User userFromToken;
      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(403).body("Invalid token");
      }

        if (userFromToken == null) {
            return ResponseEntity.status(403).body("Invalid token");
        }

      Map map = Server.getDbManager().getMapTemplateById(id);

        if (map == null) {
            return ResponseEntity.status(404).body("Map not found");
        }

      if (map.getUserId().equals(userFromToken.getId()) || userFromToken.isAdmin()) {
        Server.getDbManager().deleteMapTemplate(id);
        LOG.info("Map deleted: " + id);
        return ResponseEntity.status(200).body(true);
      } else {
        return ResponseEntity.status(403).body("Invalid token");
      }

    } catch (Exception e) {
      LOG.error("Fail to delete map", e);
      return ResponseEntity.status(500).body("Fail to delete map");
    }
  }
}
