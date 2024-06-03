package de.cfp1.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author robert.kratz
 */

public class Parameters {

  private static final Logger LOG = LoggerFactory.getLogger(Parameters.class);

  private static final String sep = FileSystems.getDefault().getSeparator();

  public static final String DATABASE =
      "server" + sep + "src" + sep + "main" + sep + "resources" + sep + "storage.db";

  public static final String DEFAULT_MAP_TEMPLATE =
      "server" + sep + "src" + sep + "main" + sep + "resources" + sep + "maptemplates" + sep
          + "default.json";

  /**
   * Reads the content of a file in the resources folder and returns it as a string
   *
   * @param fileName the name of the file to read
   * @return the content of the file as a string, or null if an error occurs
   * @author robert.kratz
   */
  public static String getResourceByName(String fileName) {
    ArrayList<String> content = new ArrayList<>();

    // Using the current thread's class loader to get the resource as an InputStream
    try (BufferedReader bufR = new BufferedReader(new FileReader(fileName))) {
      Scanner scanner = new Scanner(bufR);
      while (scanner.hasNextLine()) {
        content.add(scanner.nextLine());
      }
    } catch (IOException e) {
      LOG.error("Unable to read file " + fileName, e);
    }

    return String.join("\n", content);
  }

}
