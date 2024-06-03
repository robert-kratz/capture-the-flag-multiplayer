package de.cfp1.client.net;

import de.cfp1.server.entities.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author robert.kratz, benjamin.sander
 */

@Disabled("These tests only work with a running server. For this purpose, the server must be started and the server URL must be set in the NetworkHandler.")
class MapHandlerTest {

  private final NetworkHandler networkHandler = new NetworkHandler(new NetworkEvent() {
    @Override
    public void onEvent(NetworkEventType type, Object... args) {
      System.out.println("Event: " + type + " with args: " + args);
    }
  });

  /**
   * Test if the maps can be loaded
   *
   * @author robert.kratz, benjamin.sander
   */
  @Test
  void loadMaps() {
    try {
      networkHandler.signUpAsGuest();

      assertNotNull(networkHandler.getAccessToken());
      assertNotNull(networkHandler.getRefreshToken());

      MapHandler mapHandler = networkHandler.getMapHandler();

      assertNotNull(mapHandler);

      mapHandler.loadMap();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e);
    }
  }

  /**
   * Test if the maps can be received
   *
   * @author robert.kratz, benjamin.sander
   */
  @Test
  void getMapByID() {
    try {
      this.loadMaps();

      MapHandler mapHandler = networkHandler.getMapHandler();

      assertNotNull(mapHandler);

      Map newMap = mapHandler.createNewMap();

      assertNotNull(newMap);

      Map findMap = mapHandler.getMap(newMap.getId());

      assertNotNull(findMap);

      System.out.println("Received map: " + findMap.toJSON());

      networkHandler.deleteUserProfile();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e);
    }
  }

  /**
   * Test if the maps can be created
   *
   * @author robert.kratz, benjamin.sander
   */
  @Test
  void createMaps() {
    try {
      this.loadMaps();

      MapHandler mapHandler = networkHandler.getMapHandler();

      assertNotNull(mapHandler);

      Map newMap = mapHandler.createNewMap();

      assertNotNull(newMap);

      System.out.println("New map: " + newMap.toJSON());

      networkHandler.deleteUserProfile();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e);
    }
  }

  /**
   * Test if the maps can be deleted
   *
   * @author robert.kratz, benjamin.sander
   */
  @Test
  void deleteMaps() {
    try {
      this.loadMaps();

      MapHandler mapHandler = networkHandler.getMapHandler();

      assertNotNull(mapHandler);

      Map newMap = mapHandler.createNewMap();

      assertNotNull(newMap);

      Map findMap = mapHandler.getMap(newMap.getId());

      assertNotNull(findMap);

      mapHandler.deleteMap(newMap.getId());

      try {
        Map findMapAfterDelete = mapHandler.getMap(newMap.getId());

        assertNull(findMapAfterDelete);

        networkHandler.deleteUserProfile();

        fail();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
      fail(e);
    }
  }

  /**
   * Test if the maps can be updated
   *
   * @author robert.kratz, benjamin.sander
   */
  @Test
  void updateMaps() {
    try {
      this.loadMaps();

      MapHandler mapHandler = networkHandler.getMapHandler();

      assertNotNull(mapHandler);

      Map newMap = mapHandler.createNewMap();

      assertNotNull(newMap);

      Map findMap = mapHandler.getMap(newMap.getId());

      assertNotNull(findMap);

      findMap.setName("New Name");

      try {
        mapHandler.updateMap(findMap.getId(), findMap.getName(), findMap.getMapTemplate(),
            findMap.isPublic());
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Failed to update map: " + e.getMessage());
        fail(e);
      }

      Map findMapAfterUpdate = mapHandler.getMap(newMap.getId());

      assertNotNull(findMapAfterUpdate);
      assertEquals("New Name", findMapAfterUpdate.getName());

      networkHandler.deleteUserProfile();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e);
    }
  }

  /**
   * Test if the public maps can be received
   *
   * @author robert.kratz, benjamin.sander
   */
  @Test
  void getPublicMaps() {
    try {
      this.loadMaps();

      MapHandler mapHandler = networkHandler.getMapHandler();

      assertNotNull(mapHandler);

      ArrayList<Map> publicMaps = mapHandler.getPublicMaps();

      assertNotNull(publicMaps);

      for (Map map : publicMaps) {
        System.out.println("Received public map: " + map.toJSON());
      }

      networkHandler.deleteUserProfile();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e);
    }
  }
}