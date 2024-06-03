package de.cfp1.server.entities;

/**
 * @author robert.kratz
 */

public class User {

  public String id;
  public String username;
  public String password;
  public String email;
  public boolean guest;

  public boolean admin;

  /**
   * Constructor for the User class
   *
   * @param id       The id of the user (unique)
   * @param username The username of the user (unique)
   * @param password The password of the user
   * @param email    The email of the user (unique)
   * @param guest    If the user is a guest, if true the user is not allowed to login
   * @param admin    If the user is an admin
   * @author robert.kratz
   */
  public User(String id, String username, String password, String email, boolean guest,
      boolean admin) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.guest = guest;
    this.admin = admin;

      if (guest) {
          this.admin = false;
      }
  }

  /**
   * Constructor for the User class
   *
   * @param id       The id of the user (unique)
   * @param username The username of the user (unique)
   * @param password The password of the user
   * @param email    The email of the user (unique)
   * @author robert.kratz
   */
  public User(String id, String username, String password, String email) {
    this(id, username, password, email, false, false);
  }

  /**
   * Constructor for the User class
   *
   * @param id       The id of the user (unique)
   * @param username The username of the user (unique)
   * @param email    The email of the user (unique)
   * @param guest    If the user is a guest, if true the user is not allowed to login
   * @author robert.kratz
   */
  public User(String id, String username, String email, boolean guest) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.guest = guest;
  }

  /**
   * Constructor for the User class
   *
   * @param user The user to copy
   * @author robert.kratz
   */
  public User(User user) {
    this.id = user.id;
    this.username = user.username;
    this.password = user.password;
    this.email = user.email;
    this.guest = user.guest;
    this.admin = user.admin;
  }

  /**
   * @return
   * @author robert.kratz
   */
  public String getUsername() {
    return username;
  }

  /**
   * @return password of the user
   * @author robert.kratz
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return id of the user
   * @author robert.kratz
   */
  public String getId() {
    return id;
  }

  /**
   * @return email of the user
   * @author robert.kratz
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return true if the user is a guest, false otherwise
   * @author robert.kratz
   */
  public boolean isGuest() {
    return guest;
  }

  /**
   * @return true if the user is an admin, false otherwise
   * @author robert.kratz
   */
  public boolean isAdmin() {
    return admin;
  }

  /**
   * Set the admin status of the user
   *
   * @param admin
   * @author robert.kratz
   */
  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  /**
   * Set the guest status of the user
   *
   * @param guest Set to true if the user is a guest
   * @author robert.kratz
   */
  public void setGuest(boolean guest) {
    this.guest = guest;
  }

  /**
   * Set the password of the user
   *
   * @param password The new password of the user
   * @author robert.kratz
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Set the email of the user
   *
   * @param email The new email of the user
   * @author robert.kratz
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Set the username of the user
   *
   * @param username The new username of the user
   * @author robert.kratz
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Set the id of the user
   *
   * @param id The new id of the user
   * @author robert.kratz
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns a string representation of the user
   *
   * @return String representation of the user
   * @author robert.kratz
   */
  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", email='" + email + '\'' +
        ", guest=" + guest +
        ", admin=" + admin +
        '}';
  }

  /**
   * Compares the user to another object
   *
   * @param obj The object to compare to
   * @return true if the objects are equal, false otherwise
   * @author robert.kratz
   */
  public boolean equals(Object obj) {
    if (obj instanceof User) {
      User user = (User) obj;
      return user.getUsername().equals(username);
    }
    return false;
  }
}
