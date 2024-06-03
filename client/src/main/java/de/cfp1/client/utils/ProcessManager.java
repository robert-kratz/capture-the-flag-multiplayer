package de.cfp1.client.utils;

import de.cfp1.client.Client;
import de.cfp1.client.net.NetworkHandler;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author robert.kratz
 */

public class ProcessManager {

  private static final String sep = FileSystems.getDefault().getSeparator();
  private final ArrayList<Thread> threads = new ArrayList<>();

  /**
   * Starts a new process with the given AI type and arguments.
   *
   * @throws IOException If the process could not be started
   * @author robert.kratz
   */
  public void startNewProcess() throws IOException {
    Thread thread = new Thread(() -> {
      File tempJar = null;
      try {
        // Extract the embedded JAR to a temporary file
        tempJar = new ProcessManager().extractJar(
            "client" + sep + "src" + sep + "main" + sep + "resources" + sep + "artifacts" + sep
                + "ai.jar");

        // Start the extracted JAR application
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar",
            tempJar.getAbsolutePath() + " " + (NetworkHandler.getBaseUrl()));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Read the combined output and error streams
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()))) {
          String line;
          while ((line = reader.readLine()) != null) {
            System.out.println(line);
          }
        }

        // Wait for the process to finish
        int exitCode = process.waitFor();
        System.out.println("Process exited with code: " + exitCode);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      } finally {
        // Clean up the temporary file
        if (tempJar != null) {
          tempJar.delete();
        }
      }
    });

    thread.start();
  }

  /**
   * Stops the process with the given index.
   *
   * @param index The index of the process to stop
   * @author robert.kratz
   */
  public void stopProcess(int index) {
    if (index >= 0 && index < threads.size()) {
      threads.get(index).interrupt();
    }
  }

  /**
   * Stops all processes.
   *
   * @author robert.kratz
   */
  public void stopAllProcesses() {
    for (Thread thread : threads) {
      thread.interrupt();
    }
  }

  /**
   * Returns the list of processes.
   *
   * @return The list of processes
   * @author robert.kratz
   */
  public ArrayList<Thread> getProcesses() {
    return threads;
  }

  /**
   * Returns the AI type as a string.
   *
   * @param aiType The AI type
   * @return The AI type as a string
   */
  private String getAiTypeString(AiType aiType) {
    return switch (aiType) {
      case EASY -> "--easy";
      case MEDIUM -> "--medium";
      case HARD -> "--hard";
    };
  }

  /**
   * Extracts a JAR file from the resources to a temporary file.
   *
   * @param resourcePath The path to the JAR file in the resources
   * @return The temporary file
   * @throws IOException If the JAR file could not be extracted
   * @author robert.kratz
   */
  private File extractJar(String resourcePath) throws IOException {
    // Convert resource path to a system path if running from IDE or directly from filesystem
    Path path = Paths.get(System.getProperty("user.dir"), resourcePath);

    // Ensure the path exists
    if (!Files.exists(path)) {
      throw new FileNotFoundException("File not found: " + path.toString());
    }

    // Create a temporary file
    File tempFile = File.createTempFile("ai_bot", ".jar");

    // Use NIO to copy the file
    try (FileChannel sourceChannel = new FileInputStream(path.toFile()).getChannel();
        FileChannel destChannel = new FileOutputStream(tempFile).getChannel()) {
      destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
    }

    return tempFile;
  }
}
