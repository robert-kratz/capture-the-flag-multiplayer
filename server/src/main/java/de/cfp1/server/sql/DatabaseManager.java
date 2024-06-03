package de.cfp1.server.sql;

import com.google.gson.Gson;
import de.cfp1.server.Parameters;
import de.cfp1.server.entities.Map;
import de.cfp1.server.entities.User;
import de.cfp1.server.game.map.MapTemplate;
import de.cfp1.server.web.data.LeaderboardResponse;
import de.cfp1.server.web.data.StatsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author robert.kratz
 */

public class DatabaseManager extends DatabaseHandler {

  private static final Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);

  Map[] defaultMaps = new Map[]{
      new Map(new Gson().fromJson(
          "{\"gridSize\":[8,8],\"teams\":2,\"flags\":1,\"pieces\":[{\"type\":\"Pawn\",\"attackPower\":1,\"count\":3,\"movement\":{\"directions\":{\"left\":1,\"right\":1,\"up\":1,\"down\":1,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Knight\",\"attackPower\":3,\"count\":1,\"movement\":{\"directions\":{\"left\":0,\"right\":0,\"up\":0,\"down\":0,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Bishop\",\"attackPower\":3,\"count\":0,\"movement\":{\"directions\":{\"left\":0,\"right\":0,\"up\":0,\"down\":0,\"upLeft\":2,\"upRight\":2,\"downLeft\":2,\"downRight\":2},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Rook\",\"attackPower\":4,\"count\":1,\"movement\":{\"directions\":{\"left\":2,\"right\":2,\"up\":2,\"down\":2,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Queen\",\"attackPower\":5,\"count\":0,\"movement\":{\"directions\":{\"left\":2,\"right\":2,\"up\":2,\"down\":2,\"upLeft\":2,\"upRight\":2,\"downLeft\":2,\"downRight\":2},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"King\",\"attackPower\":5,\"count\":1,\"movement\":{\"directions\":{\"left\":1,\"right\":1,\"up\":1,\"down\":1,\"upLeft\":1,\"upRight\":1,\"downLeft\":1,\"downRight\":1},\"shape\":{\"type\":\"lshape\"}}}],\"blocks\":2,\"placement\":\"symmetrical\",\"totalTimeLimitInSeconds\":300,\"moveTimeLimitInSeconds\":5}",
          MapTemplate.class), true, "1 vs. 1 Blitz", UUID.randomUUID().toString(),
          UUID.randomUUID().toString(), new Date(System.currentTimeMillis()),
          new Date(System.currentTimeMillis())),
      new Map(new Gson().fromJson(
          "{\"gridSize\":[14,15],\"teams\":4,\"flags\":3,\"pieces\":[{\"type\":\"Pawn\",\"attackPower\":1,\"count\":3,\"movement\":{\"directions\":{\"left\":1,\"right\":1,\"up\":1,\"down\":1,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Knight\",\"attackPower\":3,\"count\":1,\"movement\":{\"directions\":{\"left\":0,\"right\":0,\"up\":0,\"down\":0,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Bishop\",\"attackPower\":3,\"count\":1,\"movement\":{\"directions\":{\"left\":0,\"right\":0,\"up\":0,\"down\":0,\"upLeft\":2,\"upRight\":2,\"downLeft\":2,\"downRight\":2},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Rook\",\"attackPower\":4,\"count\":1,\"movement\":{\"directions\":{\"left\":2,\"right\":2,\"up\":2,\"down\":2,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Queen\",\"attackPower\":5,\"count\":1,\"movement\":{\"directions\":{\"left\":2,\"right\":2,\"up\":2,\"down\":2,\"upLeft\":2,\"upRight\":2,\"downLeft\":2,\"downRight\":2},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"King\",\"attackPower\":5,\"count\":1,\"movement\":{\"directions\":{\"left\":1,\"right\":1,\"up\":1,\"down\":1,\"upLeft\":1,\"upRight\":1,\"downLeft\":1,\"downRight\":1},\"shape\":{\"type\":\"lshape\"}}}],\"blocks\":5,\"placement\":\"symmetrical\",\"totalTimeLimitInSeconds\":900,\"moveTimeLimitInSeconds\":10}",
          MapTemplate.class), true, "4 Player Adventure", UUID.randomUUID().toString(),
          UUID.randomUUID().toString(), new Date(System.currentTimeMillis()),
          new Date(System.currentTimeMillis())),
      new Map(new Gson().fromJson(
          "{\"gridSize\":[15,8],\"teams\":2,\"flags\":1,\"pieces\":[{\"type\":\"Pawn\",\"attackPower\":1,\"count\":1,\"movement\":{\"directions\":{\"left\":1,\"right\":1,\"up\":1,\"down\":1,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Knight\",\"attackPower\":3,\"count\":1,\"movement\":{\"directions\":{\"left\":0,\"right\":0,\"up\":0,\"down\":0,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Bishop\",\"attackPower\":3,\"count\":1,\"movement\":{\"directions\":{\"left\":0,\"right\":0,\"up\":0,\"down\":0,\"upLeft\":2,\"upRight\":2,\"downLeft\":2,\"downRight\":2},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Rook\",\"attackPower\":4,\"count\":1,\"movement\":{\"directions\":{\"left\":2,\"right\":2,\"up\":2,\"down\":2,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Queen\",\"attackPower\":5,\"count\":1,\"movement\":{\"directions\":{\"left\":2,\"right\":2,\"up\":2,\"down\":2,\"upLeft\":2,\"upRight\":2,\"downLeft\":2,\"downRight\":2},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"King\",\"attackPower\":5,\"count\":1,\"movement\":{\"directions\":{\"left\":1,\"right\":1,\"up\":1,\"down\":1,\"upLeft\":1,\"upRight\":1,\"downLeft\":1,\"downRight\":1},\"shape\":{\"type\":\"lshape\"}}}],\"blocks\":1,\"placement\":\"symmetrical\",\"totalTimeLimitInSeconds\":300,\"moveTimeLimitInSeconds\":5}",
          MapTemplate.class), true, "Mats CR Match", UUID.randomUUID().toString(),
          UUID.randomUUID().toString(), new Date(System.currentTimeMillis()),
          new Date(System.currentTimeMillis())),
      new Map(new Gson().fromJson(
          "{\"gridSize\":[10,10],\"teams\":3,\"flags\":1,\"pieces\":[{\"type\":\"Pawn\",\"attackPower\":1,\"count\":0,\"movement\":{\"directions\":{\"left\":1,\"right\":1,\"up\":1,\"down\":1,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Knight\",\"attackPower\":3,\"count\":0,\"movement\":{\"directions\":{\"left\":0,\"right\":0,\"up\":0,\"down\":0,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Bishop\",\"attackPower\":3,\"count\":0,\"movement\":{\"directions\":{\"left\":0,\"right\":0,\"up\":0,\"down\":0,\"upLeft\":2,\"upRight\":2,\"downLeft\":2,\"downRight\":2},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Rook\",\"attackPower\":4,\"count\":1,\"movement\":{\"directions\":{\"left\":2,\"right\":2,\"up\":2,\"down\":2,\"upLeft\":0,\"upRight\":0,\"downLeft\":0,\"downRight\":0},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"Queen\",\"attackPower\":5,\"count\":2,\"movement\":{\"directions\":{\"left\":2,\"right\":2,\"up\":2,\"down\":2,\"upLeft\":2,\"upRight\":2,\"downLeft\":2,\"downRight\":2},\"shape\":{\"type\":\"lshape\"}}},{\"type\":\"King\",\"attackPower\":5,\"count\":0,\"movement\":{\"directions\":{\"left\":1,\"right\":1,\"up\":1,\"down\":1,\"upLeft\":1,\"upRight\":1,\"downLeft\":1,\"downRight\":1},\"shape\":{\"type\":\"lshape\"}}}],\"blocks\":2,\"placement\":\"symmetrical\",\"totalTimeLimitInSeconds\":600,\"moveTimeLimitInSeconds\":20}",
          MapTemplate.class), true, "Bennis three way", UUID.randomUUID().toString(),
          UUID.randomUUID().toString(), new Date(System.currentTimeMillis()),
          new Date(System.currentTimeMillis())),
  };

  /**
   * Constructor for the DatabaseManager, calls the super constructor and creates the table
   *
   * @param file The file of the database
   * @author robert.kratz
   */
  public DatabaseManager(String file) {
    super(file);
    this.createTable();
  }

  /**
   * Returns a prepared statement for the given query specified in the parameter
   *
   * @param query The query for the prepared statement
   * @return PreparedStatement
   * @author robert.kratz
   */
  @Override
  public synchronized PreparedStatement getPreparedStatement(String query) {
    try {
      return connection.prepareStatement(query);
    } catch (SQLException e) {
      LOG.error("Error while getting prepared statement", e);
    }
    return null;
  }

  /**
   * Returns a user by the given id specified in the parameter
   *
   * @param id The id of the user (unique)
   * @return User
   * @author robert.kratz
   * @synchronized
   */
  @Override
  public synchronized User getUserById(String id) {

    PreparedStatement getUserById = getPreparedStatement("SELECT * FROM users WHERE userid = ?");

    try {
      getUserById.setString(1, id);

      ResultSet rs = getUserById.executeQuery();
      if (rs.next()) {
        return new User(rs.getString("userid"), rs.getString("username"), rs.getString("password"),
            rs.getString("email"), rs.getBoolean("guest"), rs.getBoolean("admin"));
      }
    } catch (SQLException e) {
      LOG.error("Error while getting user by id", e);
    }
    return null;
  }

  /**
   * Returns a user by the given name specified in the parameter
   *
   * @param name The name of the user (unique)
   * @return User
   * @author robert.kratz
   * @synchronized
   */
  @Override
  public synchronized User getUserByName(String name) {

    PreparedStatement getUserByName = getPreparedStatement(
        "SELECT * FROM users WHERE username = ?");

    try {
      getUserByName.setString(1, name);

      ResultSet rs = getUserByName.executeQuery();
      if (rs.next()) {
        return new User(rs.getString("userid"), rs.getString("username"), rs.getString("password"),
            rs.getString("email"), rs.getBoolean("guest"), rs.getBoolean("admin"));
      }
    } catch (SQLException e) {
      LOG.error("Error while getting user by name", e);
    }
    return null;
  }

  /**
   * Returns a user by the given email specified in the parameter
   *
   * @param email The email of the user (unique)
   * @return User
   * @author robert.kratz
   * @synchronized
   */
  @Override
  public User getUserByEmailAddress(String email) {

    PreparedStatement getUserByEmailAddress = getPreparedStatement(
        "SELECT * FROM users WHERE email = ?");

    try {
      getUserByEmailAddress.setString(1, email);

      ResultSet rs = getUserByEmailAddress.executeQuery();
      if (rs.next()) {
        return new User(rs.getString("userid"), rs.getString("username"), rs.getString("password"),
            rs.getString("email"), rs.getBoolean("guest"), rs.getBoolean("admin"));
      }
    } catch (SQLException e) {
      LOG.error("Error while getting user by email", e);
    }
    return null;
  }

  /**
   * Creates a user with the given parameters specified in the parameter
   *
   * @param name     The name of the user (unique)
   * @param email    The email of the user (unique)
   * @param password The password of the user
   * @param guest    The guest status of the user, true if the user is a guest
   * @param admin    The admin status of the user, true if the user is an admin
   * @return User
   * @author robert.kratz
   */
  @Override
  public synchronized User createUser(String name, String email, String password, boolean guest,
      boolean admin) {

    PreparedStatement createUser = getPreparedStatement(
        "INSERT INTO users (userid, username, email, password, guest, admin) VALUES (?, ?, ?, ?, ?, ?)");

    String id = UUID.randomUUID().toString();
    try {
      createUser.setString(1, id);
      createUser.setString(2, name);
      createUser.setString(3, email);
      createUser.setString(4, password);
      createUser.setBoolean(5, guest);
      createUser.setBoolean(6, admin);

      this.createUserStatistics(id);

      createUser.executeUpdate();
      return new User(id, name, password, email, guest, admin);
    } catch (SQLException e) {
      LOG.error("Error while creating user", e);
    }
    return null;
  }

  /**
   * Deletes a user with the given id specified in the parameter
   *
   * @param id The id of the user (unique)
   * @return boolean
   * @author robert.kratz
   */
  @Override
  public synchronized boolean deleteUser(String id) {
    PreparedStatement deleteUser = getPreparedStatement("DELETE FROM users WHERE userid = ?");
    PreparedStatement deleteUserStats = getPreparedStatement("DELETE FROM stats WHERE userid = ?");

    try {
      deleteUser.setString(1, id);
      deleteUserStats.setString(1, id);

      deleteUserStats.executeUpdate();
      deleteUser.executeUpdate();
    } catch (SQLException e) {
      LOG.error("Error while deleting user", e);
    }
    return true;
  }

  /**
   * Updates a user with the given parameters specified in the parameter
   *
   * @param id       The id of the user (unique)
   * @param username The name of the user (unique)
   * @param email    The email of the user (unique)
   * @param password The password of the user
   * @param guest    The guest status of the user, true if the user is a guest
   * @param admin    The admin status of the user, true if the user is an admin
   * @return boolean
   * @author robert.kratz
   */
  @Override
  public synchronized boolean updateUser(String id, String username, String email, String password,
      boolean guest, boolean admin) {
    PreparedStatement updateUser = getPreparedStatement(
        "UPDATE users SET username = ?, password = ?, email = ?, guest = ?, admin = ? WHERE userid = ?");

    try {
      updateUser.setString(1, username);
      updateUser.setString(2, password);
      updateUser.setString(3, email);
      updateUser.setBoolean(4, guest);
      updateUser.setBoolean(5, admin);
      updateUser.setString(6, id);

      updateUser.executeUpdate();
    } catch (SQLException e) {
      LOG.error("Error while updating user", e);
    }
    return true;
  }

  /**
   * Returns all users
   *
   * @return ArrayList<User>
   * @author robert.kratz
   */
  @Override
  public ArrayList<User> getUsers() {
    ArrayList<User> users = new ArrayList<>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM users");
      while (rs.next()) {
        users.add(
            new User(rs.getString("userid"), rs.getString("username"), rs.getString("password"),
                rs.getString("email"), rs.getBoolean("guest"), rs.getBoolean("admin")));
      }
    } catch (SQLException e) {
      LOG.error("Error while getting users", e);
    }
    return users;
  }

  /**
   * Creates a map template with the given parameters specified in the parameter
   *
   * @param userId   The id of the user
   * @param name     The name of the map
   * @param id       The id of the map
   * @param isPublic Whether the map is public
   * @author robert.kratz
   */
  @Override
  public Map createMapTemplate(String userId, String name, String id, boolean isPublic) {
    MapTemplate defaultMap = new Gson().fromJson(
        Parameters.getResourceByName(Parameters.DEFAULT_MAP_TEMPLATE), MapTemplate.class);
    try {
      java.sql.Date createdAt = new Date(System.currentTimeMillis());

      PreparedStatement createMapTemplate = getPreparedStatement(
          "INSERT INTO maps (mapid, maptemplate, ispublic, name, userid, created, lastmodified) VALUES (?, ?, ?, ?, ?, ?, ?)");
      createMapTemplate.setString(1, id);
      createMapTemplate.setString(2, new Gson().toJson(defaultMap));
      createMapTemplate.setBoolean(3, isPublic);
      createMapTemplate.setString(4, name);
      createMapTemplate.setString(5, userId);
      createMapTemplate.setDate(6, createdAt);
      createMapTemplate.setDate(7, createdAt);
      createMapTemplate.executeUpdate();

      return new Map(defaultMap, isPublic, name, id, userId, createdAt, createdAt);
    } catch (SQLException e) {
      LOG.error("Error while creating map template", e);
    }
    return null;
  }

  /**
   * Updates a map template with the given parameters specified in the parameter
   *
   * @param name          The name of the map
   * @param mapTemplateId The id of the map template
   * @param isPublic      Whether the map is public or not
   * @param mapTemplate   The map template
   * @author robert.kratz
   */
  @Override
  public void updateMapTemplate(String name, String mapTemplateId, boolean isPublic,
      String mapTemplate) {
    java.sql.Date updatedAt = new Date(System.currentTimeMillis());
    try {
      PreparedStatement updateMapTemplate = getPreparedStatement(
          "UPDATE maps SET maptemplate = ?, ispublic = ?, name = ?, lastmodified = ? WHERE mapid = ?");
      updateMapTemplate.setString(1, mapTemplate);
      updateMapTemplate.setBoolean(2, isPublic);
      updateMapTemplate.setString(3, name);
      updateMapTemplate.setDate(4, updatedAt);
      updateMapTemplate.setString(5, mapTemplateId);

      updateMapTemplate.executeUpdate();
    } catch (SQLException e) {
      LOG.error("Error while updating map template", e);
    }
  }

  /**
   * Deletes a map template with the given id specified in the parameter
   *
   * @param id The id of the map template to delete
   * @author robert.kratz
   */
  @Override
  public void deleteMapTemplate(String id) {
    try {
      connection.createStatement().execute("DELETE FROM maps WHERE mapid = '" + id + "'");
    } catch (SQLException e) {
      LOG.error("Error while deleting map template", e);
    }
  }

  /**
   * Returns a map template by the given id specified in the parameter
   *
   * @param userId The id of the user
   * @return The map template
   * @author robert.kratz
   */
  @Override
  public ArrayList<Map> getMapTemplates(String userId) {
    ArrayList<Map> maps = new ArrayList<>();
    try {
      ResultSet rs = connection.createStatement()
          .executeQuery("SELECT * FROM maps WHERE userid = '" + userId + "'");
      while (rs.next()) {
        MapTemplate mapTemplate = new Gson().fromJson(rs.getString("maptemplate"),
            MapTemplate.class);
        maps.add(new Map(mapTemplate, rs.getBoolean("ispublic"), rs.getString("name"),
            rs.getString("mapid"), rs.getString("userid"), rs.getTimestamp("created"),
            rs.getTimestamp("lastmodified")));
      }
    } catch (SQLException e) {
      LOG.error("Error while getting map templates", e);
    }
    return maps;
  }

  @Override
  public ArrayList<Map> getPublicMapTemplates() {
    try {
      ResultSet rs = connection.createStatement()
          .executeQuery("SELECT * FROM maps WHERE ispublic = true");
      return getMaps(rs);
    } catch (SQLException e) {
      LOG.error("Error while getting public map templates", e);
    }
    return null;
  }

  /**
   * Returns a map template by the given id specified in the parameter
   *
   * @param id The id of the map template
   * @return The map template
   */
  @Override
  public Map getMapTemplateById(String id) {
    try {
      ResultSet rs = connection.createStatement()
          .executeQuery("SELECT * FROM maps WHERE mapid = '" + id + "'");
      if (rs.next()) {
        String mt = rs.getString("maptemplate");
        MapTemplate mapTemplate = new Gson().fromJson(mt, MapTemplate.class);
        System.out.println("MapTemplate: " + mt);
        return new Map(mapTemplate, rs.getBoolean("ispublic"), rs.getString("name"),
            rs.getString("mapid"), rs.getString("userid"), rs.getTimestamp("created"),
            rs.getTimestamp("lastmodified"));
      }
    } catch (SQLException e) {
      LOG.error("Error while getting map template by id", e);
    }
    return null;
  }

  private ArrayList<Map> getMaps(ResultSet rs) throws SQLException {
    ArrayList<Map> maps = new ArrayList<>();
    while (rs.next()) {
      MapTemplate mapTemplate = new Gson().fromJson(rs.getString("maptemplate"), MapTemplate.class);
      maps.add(new Map(mapTemplate, rs.getBoolean("ispublic"), rs.getString("name"),
          rs.getString("mapid"), rs.getString("userid"), rs.getTimestamp("created"),
          rs.getTimestamp("lastmodified")));
    }
    return maps;
  }

  /**
   * Deletes all map templates
   *
   * @param userId The id of the user
   */
  @Override
  public void deleteAllUserMaps(String userId) {
    try {
      connection.createStatement().execute("DELETE FROM maps WHERE userid = '" + userId + "'");
    } catch (SQLException e) {
      LOG.error("Error while deleting all user maps", e);
    }
  }

  /**
   * Get the count of maps
   *
   * @param userId The id of the user
   * @return The count of maps
   * @author robert.kratz
   */
  @Override
  public int getMapCount(String userId) {
    try {
      ResultSet rs = connection.createStatement()
          .executeQuery("SELECT COUNT(*) FROM maps WHERE userid = '" + userId + "'");
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      LOG.error("Error while getting map count", e);
    }
    return 0;
  }

  /**
   * Get the count of public maps
   *
   * @param userId The id of the user
   * @return The stats of the user
   * @author robert.kratz
   */
  @Override
  public StatsResponse getUserStatistics(String userId) {
    try {
      ResultSet rs = connection.createStatement()
          .executeQuery("SELECT * FROM stats WHERE userid = '" + userId + "'");
      if (rs.next()) {
        return new StatsResponse(rs.getString("userid"), rs.getInt("wins"), rs.getInt("losses"),
            rs.getInt("draws"), rs.getInt("elo"));
      }
    } catch (SQLException e) {
      LOG.error("Error while getting user statistics", e);
    }
    return null;
  }

  /**
   * Update the statistics
   *
   * @param stats The statistics
   * @author robert.kratz
   */
  @Override
  public void updateUserStatistics(StatsResponse stats) {
    try {
      PreparedStatement updateStats = getPreparedStatement(
          "UPDATE stats SET wins = ?, losses = ?, draws = ?, elo = ? WHERE userid = ?");

      updateStats.setInt(1, stats.getWins());
      updateStats.setInt(2, stats.getLosses());
      updateStats.setInt(3, stats.getDraws());
      updateStats.setInt(4, stats.getElo());
      updateStats.setString(5, stats.getUserId());

      updateStats.executeUpdate();
    } catch (SQLException e) {
      LOG.error("Error while updating statistics", e);
    }
  }

  /**
   * Get the leaderboard of the top 100 players by elo
   *
   * @return The leaderboard
   * @author robert.kratz
   */
  @Override
  public LeaderboardResponse[] getLeaderboard() {
    try {
      ResultSet rs = connection.createStatement().executeQuery(
          "SELECT s.*, u.username FROM stats s JOIN users u ON s.userid = u.userid ORDER BY s.elo DESC LIMIT 100;");

      ArrayList<LeaderboardResponse> leaderboard = new ArrayList<>();
      while (rs.next()) {
        leaderboard.add(new LeaderboardResponse(rs.getString("userid"), rs.getString("username"),
            getUserStatistics(rs.getString("userid"))));
      }

      return leaderboard.toArray(new LeaderboardResponse[0]);
    } catch (SQLException e) {
      LOG.error("Error while getting leaderboard", e);
    }
    return null;
  }

  /**
   * Create the statistics
   *
   * @param userId The id of the user
   * @author robert.kratz
   */
  @Override
  public void createUserStatistics(String userId) {
    try {
      PreparedStatement createStats = getPreparedStatement(
          "INSERT INTO stats (userid, wins, losses, draws, elo) VALUES (?, 0, 0, 0, 1000)");

      createStats.setString(1, userId);

      createStats.executeUpdate();
    } catch (SQLException e) {
      LOG.error("Error while creating statistics", e);
    }
  }

  /**
   * Creates the table if it does not exist, this method is called when the app starts
   *
   * @author robert.kratz
   */
  @Override
  public void createTable() {
    try {
      connection.createStatement().execute(
          "CREATE TABLE IF NOT EXISTS users (userid VARCHAR(36) PRIMARY KEY, username VARCHAR(16), password VARCHAR(64), email VARCHAR(64), guest BOOLEAN, admin BOOLEAN)");
      connection.createStatement().execute(
          "CREATE TABLE IF NOT EXISTS maps (mapid VARCHAR(36) PRIMARY KEY, maptemplate VARCHAR(64), ispublic BOOLEAN, name VARCHAR(64), userid VARCHAR(36), created TIMESTAMP, lastmodified TIMESTAMP)");
      connection.createStatement().execute(
          "CREATE TABLE IF NOT EXISTS stats (userid VARCHAR(36) PRIMARY KEY, wins INT, losses INT, draws INT, elo INT)");
    } catch (SQLException e) {
      LOG.error("Error while creating table", e);
    }

    ArrayList<Map> maps = getPublicMapTemplates();

    if (maps.isEmpty()) {
      System.out.println("Creating default maps");

      PreparedStatement createMapTemplate = getPreparedStatement(
          "INSERT INTO maps (mapid, maptemplate, ispublic, name, userid, created, lastmodified) VALUES (?, ?, ?, ?, ?, ?, ?)");

      for (Map map : defaultMaps) {
        try {
          createMapTemplate.setString(1, map.id);
          createMapTemplate.setString(2, new Gson().toJson(map.mapTemplate));
          createMapTemplate.setBoolean(3, map.isPublic);
          createMapTemplate.setString(4, map.name);
          createMapTemplate.setString(5, map.userId);
          createMapTemplate.setDate(6, new Date(System.currentTimeMillis()));
          createMapTemplate.setDate(7, new Date(System.currentTimeMillis()));
          createMapTemplate.executeUpdate();
        } catch (SQLException e) {
          LOG.error("Error while creating default map", e);
        }
      }

    }
  }
}
