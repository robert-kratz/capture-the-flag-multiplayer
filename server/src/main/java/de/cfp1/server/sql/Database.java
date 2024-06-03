package de.cfp1.server.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author robert.kratz
 */

public abstract class Database {

  protected static final Logger LOG = LoggerFactory.getLogger(Database.class);

  protected Connection connection;

  /**
   * Constructor for the Database, connects to the database
   *
   * @param file The file of the database
   * @author robert.kratz
   */
  public Database(String file) {
    this.connect(file);
  }

  /**
   * Connects to the database by creating a connection to the file specified in the parameter
   *
   * @param file The file of the database
   * @author robert.kratz
   */
  private void connect(String file) {
    try {
      Class.forName("org.sqlite.JDBC");
      this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);

      LOG.info("Connected to database: " + file);
    } catch (ClassNotFoundException e) {
      LOG.error("Could not connect to database: " + file);
    } catch (SQLException e) {
      LOG.error("Could not connect to database: " + file);
      e.printStackTrace();
    }
  }

  /**
   * Disconnects from the database
   *
   * @author robert.kratz
   */
  public void disconnect() {
    try {
      if ((connection != null) && (!connection.isClosed())) {
        connection.close();
      }
    } catch (SQLException e) {
      LOG.error("Could not disconnect from database");
    }
  }

}

