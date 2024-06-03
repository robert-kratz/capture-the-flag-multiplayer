package de.cfp1.client.net;

import com.google.gson.Gson;
import de.cfp1.server.entities.Map;
import de.cfp1.server.exception.*;
import de.cfp1.server.game.map.MapTemplate;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The map handler to handle all map related requests
 *
 * @author robert.kratz
 */

public class MapHandler {

  private final String BASE_URL = NetworkHandler.getBaseUrl();
  private final NetworkHandler networkHandler;

  public ArrayList<Map> userMaps, publicMaps;

  /**
   * Constructor
   *
   * @param networkHandler The network handler to communicate with the server
   * @author robert.kratz
   */
  public MapHandler(NetworkHandler networkHandler) {
    this.networkHandler = networkHandler;
    this.userMaps = new ArrayList<>();
    this.publicMaps = new ArrayList<>();
  }

  /**
   * Create a new map
   *
   * @return True if the map was created successfully
   * @throws UserSessionExpiredException If the user session expired
   * @throws ServerTimeoutException      If the server timed out
   * @author robert.kratz
   */
  public Map createNewMap() throws UserSessionExpiredException, ServerTimeoutException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/map/create");
      request.method("POST");
      request.useAuth(networkHandler.getAccessToken());
      String tokenJson = request.send();

      Map newMap = new Gson().fromJson(tokenJson, Map.class);

      networkHandler.getEventListener().onEvent(NetworkEventType.MAP_CREATED, newMap);

      this.userMaps.add(newMap);
      return newMap;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.networkHandler.refreshTokens();

          if (!this.networkHandler.authTokenValid) {
              throw new UserSessionExpiredException();
          }
        this.networkHandler.authTokenValid = false;
        return this.createNewMap();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Load all maps of the user
   *
   * @return True if the maps were loaded successfully
   * @throws UserSessionExpiredException If the user session expired
   * @throws ServerTimeoutException      If the server timed out
   * @author robert.kratz
   */
  public boolean loadMap() throws UserSessionExpiredException, ServerTimeoutException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/map/maps");
      request.method("GET");
      request.useAuth(networkHandler.getAccessToken());
      String tokenJson = request.send();

      Map[] userMaps = new Gson().fromJson(tokenJson, Map[].class);

      Collections.addAll(this.userMaps, userMaps);
      networkHandler.getEventListener().onEvent(NetworkEventType.LOADED_MAPS, this.userMaps);

      return true;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.networkHandler.refreshTokens();

          if (!this.networkHandler.authTokenValid) {
              throw new UserSessionExpiredException();
          }
        this.networkHandler.authTokenValid = false;
        return this.loadMap();
      }
      if (e.getMessage().equals("Fail to fetch maps")) {
        throw new ServerTimeoutException();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get a map by its id
   *
   * @param mapId The id of the map
   * @return The map
   * @throws UserSessionExpiredException If the user session expired
   * @throws ServerTimeoutException      If the server timed out
   * @throws MapNotFoundException        If the map was not found
   * @author robert.kratz
   */
  public Map getMap(String mapId)
      throws UserSessionExpiredException, ServerTimeoutException, MapNotFoundException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/map/search/" + mapId);
      request.method("GET");
      request.useAuth(networkHandler.getAccessToken());
      String tokenJson = request.send();

      return new Gson().fromJson(tokenJson, Map.class);
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.networkHandler.refreshTokens();

          if (!this.networkHandler.authTokenValid) {
              throw new UserSessionExpiredException();
          }

        this.networkHandler.authTokenValid = false;
        return this.getMap(mapId);
      }
      if (e.getMessage().equals("Map not found")) {
        throw new MapNotFoundException();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Update a map
   *
   * @param mapId       The id of the map
   * @param name        The name of the map
   * @param mapTemplate The map template
   * @param isPublic    Whether the map is public or not
   * @return True if the map was updated successfully
   * @throws UserSessionExpiredException If the user session expired
   * @throws ServerTimeoutException      If the server timed out
   * @throws MapNotFoundException        If the map was not found
   * @author robert.kratz
   */
  public Map updateMap(String mapId, String name, MapTemplate mapTemplate, boolean isPublic)
      throws UserSessionExpiredException, ServerTimeoutException, MapNotFoundException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/map/update/" + mapId);
      request.method("PUT");
      request.useAuth(networkHandler.getAccessToken());
      request.payload(
          "{\"name\":\"" + name + "\",\"mapTemplate\":" + mapTemplate.getAsJson() + ",\"isPublic\":"
              + isPublic + "}");
      request.contentTypeJson();
      String tokenJson = request.send();

      Map updatedMap = new Gson().fromJson(tokenJson, Map.class);

      this.userMaps.removeIf(map -> map.getId().equals(mapId));
      this.userMaps.add(updatedMap);

      networkHandler.getEventListener().onEvent(NetworkEventType.UPDATE_MAP, updatedMap);
      return updatedMap;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.networkHandler.refreshTokens();

          if (!this.networkHandler.authTokenValid) {
              throw new UserSessionExpiredException();
          }
        this.networkHandler.authTokenValid = false;
        return this.updateMap(mapId, name, mapTemplate, isPublic);
      }
      if (e.getMessage().equals("Map not found")) {
        throw new MapNotFoundException();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Delete a map by its id
   *
   * @param mapId The id of the map
   * @return True if the map was deleted successfully
   * @throws UserSessionExpiredException If the user session expired
   * @throws ServerTimeoutException      If the server timed out
   * @throws MapNotFoundException        If the map was not found
   * @author robert.kratz
   */
  public boolean deleteMap(String mapId)
      throws UserSessionExpiredException, ServerTimeoutException, MapNotFoundException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/map/delete/" + mapId);
      request.method("DELETE");
      request.useAuth(networkHandler.getAccessToken());
      request.send();

      this.userMaps.removeIf(map -> map.getId().equals(mapId));

      networkHandler.getEventListener().onEvent(NetworkEventType.DELETE_MAP, mapId);
      return true;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.networkHandler.refreshTokens();

          if (!this.networkHandler.authTokenValid) {
              return false;
          }
        this.networkHandler.authTokenValid = false;
        return this.deleteMap(mapId);
      }
      if (e.getMessage().equals("Map not found")) {
        throw new MapNotFoundException();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get all public maps
   *
   * @return The public maps
   * @throws ServerTimeoutException If the server timed out
   * @author robert.kratz
   * @apiNote No auth is required for public maps
   * @author robert.kratz
   */
  public ArrayList<Map> getPublicMaps() throws ServerTimeoutException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/map/public/maps");
      request.method("GET");
      request.useAuth(networkHandler.getAccessToken());
      String tokenJson = request.send();

      Map[] userMaps = new Gson().fromJson(tokenJson, Map[].class);

      this.publicMaps.clear();
      Collections.addAll(this.publicMaps, userMaps);
      System.out.println("Loaded " + userMaps.length + " public maps");

      networkHandler.getEventListener()
          .onEvent(NetworkEventType.FETCH_PUBLIC_MAPS, this.publicMaps);

      return this.publicMaps;
    } catch (
        Exception e) { //No custom error handling for public maps because no auth is required - robert.kratz
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get player maps
   *
   * @return The public map
   * @throws UserSessionExpiredException If the user session expired
   * @throws ServerTimeoutException      If the server timed out
   * @throws MapNotFoundException        If the map was not found
   * @author robert.kratz
   */
  public ArrayList<Map> getMapsFromDatabase()
      throws UserSessionExpiredException, ServerTimeoutException, MapNotFoundException {
    try {
      RequestBuilder request = new RequestBuilder(BASE_URL + "/map/maps");
      request.method("GET");
      request.useAuth(networkHandler.getAccessToken());
      String tokenJson = request.send();

      Map[] userMaps = new Gson().fromJson(tokenJson, Map[].class);

      this.userMaps.clear();
      Collections.addAll(this.userMaps, userMaps);
      networkHandler.getEventListener().onEvent(NetworkEventType.LOADED_MAPS, this.userMaps);

      return this.userMaps;
    } catch (RequestErrorException e) {
      if (e.getMessage().equals("Token expired") || e.getMessage().equals("Invalid token")) {
        this.networkHandler.refreshTokens();

          if (!this.networkHandler.authTokenValid) {
              throw new UserSessionExpiredException();
          }
        this.networkHandler.authTokenValid = false;
        return this.getMapsFromDatabase();
      }
      if (e.getMessage().equals("Fail to fetch maps")) {
        throw new ServerTimeoutException();
      }
      throw new ServerTimeoutException(e.getMessage());
    } catch (Exception e) {
      throw new ServerTimeoutException(e.getMessage());
    }
  }

  /**
   * Get a public map by its id
   *
   * @return The public map
   * @author robert.kratz
   */
  public ArrayList<Map> getUserMaps() {
    return userMaps;
  }

  /**
   * Get NetworkHandler
   *
   * @return The network handler
   * @author robert.
   */
  public NetworkHandler getNetworkHandler() {
    return networkHandler;
  }
}
