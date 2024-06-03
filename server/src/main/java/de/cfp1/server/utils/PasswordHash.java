package de.cfp1.server.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @author robert.kratz
 */

public class PasswordHash {

  /**
   * Hash a password using BCrypt
   *
   * @param plainTextPassword the password to hash
   * @return the hashed password
   * @author robert.kratz                        l
   */
  public static String hashPassword(String plainTextPassword) {
    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
  }

  /**
   * Check a password against a hashed password
   *
   * @param plainTextPassword the password to check
   * @param hashedPassword    the hashed password to check against
   * @return true if the passwords match, false otherwise
   * @author robert.kratz
   */
  public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
    return BCrypt.checkpw(plainTextPassword, hashedPassword);
  }
}