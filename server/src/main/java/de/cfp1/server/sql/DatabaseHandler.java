package de.cfp1.server.sql;

import de.cfp1.server.entities.Map;
import de.cfp1.server.entities.User;

import de.cfp1.server.web.data.LeaderboardResponse;
import de.cfp1.server.web.data.StatsResponse;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * @author robert.kratz
 */

public abstract class DatabaseHandler extends Database {

  /**
   * Constructor
   *
   * @param file The file of the database
   * @author robert.kratz
   */
  public DatabaseHandler(String file) {
    super(file);
  }

  /**
   * User
   */

  /**
   * Get a prepared statement
   *
   * @param query The query
   * @return The prepared statement
   * @author robert.kratz
   */
  public abstract PreparedStatement getPreparedStatement(String query);

  /**
   * Get a user by id
   *
   * @param id The id of the user
   * @return The user
   * @author robert.kratz
   */
  public abstract User getUserById(String id);

  /**
   * @param name The name of the user
   * @return The user
   * @author robert.kratz
   * @author robert.kratz
   */
  public abstract User getUserByName(String name);

  /**
   * Get a user by email address
   *
   * @param email The email address of the user
   * @return The user
   * @author robert.kratz
   */
  public abstract User getUserByEmailAddress(String email);

  /**
   * Create a user
   *
   * @param name     The name of the user
   * @param password The password of the user
   * @param email    The email address of the user
   * @param guest    Whether the user is a guest
   * @param admin    Whether the user is an admin
   * @return The user
   * @author robert.kratz
   */
  public abstract User createUser(String name, String password, String email, boolean guest,
      boolean admin);

  /**
   * Delete a user
   *
   * @param id The id of the user
   * @return Whether the user was deleted
   * @author robert.kratz
   */
  public abstract boolean deleteUser(String id);

  /**
   * Update a user
   *
   * @param id       The id of the user
   * @param name     The name of the user
   * @param password The password of the user
   * @param email    The email address of the user
   * @param guest    Whether the user is a guest
   * @param admin    Whether the user is an admin
   * @return Whether the user was updated
   * @author robert.kratz
   */
  public abstract boolean updateUser(String id, String name, String password, String email,
      boolean guest, boolean admin);

  /**
   * Get all users
   *
   * @return The users
   * @author robert.kratz
   */
  public abstract ArrayList<User> getUsers();

  /**
   * Map
   */

  /**
   * Create a map table
   *
   * @param userId   The id of the user
   * @param name     The name of the map
   * @param id       The id of the map
   * @param isPublic Whether the map is public
   * @author robert.kratz
   */
  public abstract Map createMapTemplate(String userId, String name, String id, boolean isPublic);

  /**
   * Update a map template
   *
   * @param name          The name of the map
   * @param mapTemplateId The id of the map template
   * @param isPublic      Whether the map is public or not
   * @author robert.kratz
   */
  public abstract void updateMapTemplate(String name, String mapTemplateId, boolean isPublic,
      String mapTemplate);

  /**
   * Delete a map template
   *
   * @param id The id of the map template to delete
   * @author robert.kratz
   */
  public abstract void deleteMapTemplate(String id);

  /**
   * Get a map template by id
   *
   * @param userId The id of the user
   * @return The map template
   * @author robert.kratz
   */
  public abstract ArrayList<Map> getMapTemplates(String userId);

  /**
   * Get all public map templates
   *
   * @return The public map templates
   */
  public abstract ArrayList<Map> getPublicMapTemplates();

  /**
   * Get a map template by id
   *
   * @param id The id of the map template
   * @return The map template
   * @author robert.kratz
   */
  public abstract Map getMapTemplateById(String id);

  /**
   * Delete all map templates
   *
   * @param userId The id of the user
   * @author robert.kratz
   */
  public abstract void deleteAllUserMaps(String userId);

  /**
   * Get the count of maps
   *
   * @param userId The id of the user
   * @return The count of maps
   * @author robert.kratz
   */
  public abstract int getMapCount(String userId);

  /**
   * Get the count of public maps
   *
   * @param userId The id of the user
   * @return The count of public maps
   * @author robert.kratz
   */
  public abstract StatsResponse getUserStatistics(String userId);

  /**
   * Update the statistics
   *
   * @param stats The statistics
   * @author robert.kratz
   */
  public abstract void updateUserStatistics(StatsResponse stats);

  /**
   * Get the leaderboard
   *
   * @return The leaderboard
   */
  public abstract LeaderboardResponse[] getLeaderboard();

  /**
   * Create the statistics
   *
   * @param userId The id of the user
   * @author robert.kratz
   */
  public abstract void createUserStatistics(String userId);

  /**
   * Create the user table
   *
   * @author robert.kratz
   */
  public abstract void createTable();
}