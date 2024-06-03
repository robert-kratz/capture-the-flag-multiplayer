package de.cfp1.server.entities;

import com.google.gson.Gson;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.game.state.GameState;

import java.util.Date;

/**
 * @author robertkratz
 */

public class Map {

  public MapTemplate mapTemplate;
  public GameState gameState;
  public boolean isPublic;
  public String name;
  public String id;
  public String userId;
  public Date created;
  public Date lastModified;

  /**
   * Constructor for Map
   *
   * @author robert.kratz
   */
  public Map() {
  }

  /**
   * Constructor for Map
   *
   * @param mapTemplate  mapTemplate of the map
   * @param isPublic     isPublic of the map
   * @param name         name of the map
   * @param id           id of the map
   * @param userId       id of the user
   * @param created      created date of the map
   * @param lastModified last modified date of the map
   * @author robert.kratz
   */
  public Map(MapTemplate mapTemplate, boolean isPublic, String name, String id, String userId,
      Date created, Date lastModified) {
    this.mapTemplate = mapTemplate;
    this.isPublic = isPublic;
    this.name = name;
    this.id = id;
    this.userId = userId;
    this.created = created;
    this.lastModified = lastModified;
  }

  public String toString() {
    return this.name;
  }

  public MapTemplate getMapTemplate() {
    return mapTemplate;
  }

  public void setMapTemplate(MapTemplate mapTemplate) {
    this.mapTemplate = mapTemplate;
  }

  public GameState getGameState() {
    return this.gameState;
  }

  public void setGameState(GameState initialGameStateGrid) {
    this.gameState = initialGameStateGrid;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPublic(boolean aPublic) {
    isPublic = aPublic;
  }

  public String getUserId() {
    return userId;
  }

  public String getId() {
    return id;
  }

  public Date getCreated() {
    return created;
  }

  public String getName() {
    return name;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public boolean isPublic() {
    return isPublic;
  }

  public String toJSON() {
    return new Gson().toJson(this);
  }
}
