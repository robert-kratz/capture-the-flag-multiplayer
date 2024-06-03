package de.cfp1.server;

import de.cfp1.server.sql.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author robert.kratz
 */

@SpringBootApplication
public class Server {

  private static final Logger LOG = LoggerFactory.getLogger(Parameters.class);

  public static DatabaseManager dbManager;

  /**
   * Main method, entry point of the application server
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    LOG.info("Starting Application");
    //dbManager = new DatabaseManager(Parameters.DATABASE);
    dbManager = new DatabaseManager("storage.db");
    dbManager.createTable();
    SpringApplication.run(Server.class, args);
  }

  /**
   * Get the database manager
   *
   * @return the database manager {@link DatabaseManager}
   * @author robert.kratz
   */
  public static DatabaseManager getDbManager() {
    return dbManager;
  }
}