package de.cfp1.client.net;

import de.cfp1.client.net.data.TokenRefreshEvent;
import de.cfp1.server.entities.User;
import de.cfp1.server.utils.NameGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the NetworkHandler
 *
 * @author robert.kratz
 */
@Disabled("These tests only work with a running server. For this purpose, the server must be started and the server URL must be set in the NetworkHandler.")
class NetworkHandlerTest {


  private final NetworkHandler networkHandler = new NetworkHandler(new NetworkEvent() {
    @Override
    public void onEvent(NetworkEventType type, Object... args) {
      switch (type) {
        case TOKEN_REFRESHED:
          TokenRefreshEvent event = (TokenRefreshEvent) args[0];
          System.out.println(
              "Token refreshed: " + event.getAccessToken() + " " + event.getRefreshToken());
          break;
        default:
          break;
      }
    }
  });

  /**
   * Test the createGuestUser method and check if the tokens are generated correctly and the user is
   * created correctly on the server
   *
   * @author robert.kratz
   */
  @Test
  void createGuestUserThenUpdateUserAccount() {
    networkHandler.signUpAsGuest();
    try {
      User user = networkHandler.getUserFromServer();
      assertNotNull(user);
      String username = NameGenerator.generateUsername(), email =
          username + "@" + username + ".com", password = "P4ssword_!1234";

      boolean success = networkHandler.upgradeToUser(username, email, password);
      assertTrue(success);

      networkHandler.login(username, password);

      User updatedUser = networkHandler.getUserFromServer();
      assertNotNull(updatedUser);

      networkHandler.deleteUserProfile();

      try {
        User deletedUser = networkHandler.getUserFromServer();

        fail("User was not deleted");
      } catch (Exception e) {
      }
    } catch (Exception e) {
      fail("Error:" + e);
    }
  }

  /**
   * Test the updateUsername method and check if the username is updated correctly on the server
   *
   * @author robert.kratz
   */
  @Test
  void updateUsername() {
    networkHandler.signUpAsGuest();
    try {
      User user = networkHandler.getUserFromServer();
      assertNotNull(user);
      String username = NameGenerator.generateUsername(), email =
          username + "@" + username + ".com", password = "P4ssword_!1234";

      boolean success = networkHandler.upgradeToUser(username, email, password);
      assertTrue(success);

      networkHandler.login(username, password);

      User updatedUser = networkHandler.getUserFromServer();
      assertNotNull(updatedUser);

      String newUsername = NameGenerator.generateUsername();
      success = networkHandler.updateUsername(newUsername);
      assertTrue(success);

      User updatedUsernameUser = networkHandler.getUserFromServer();
      assertNotNull(updatedUsernameUser);
      assertEquals(newUsername, updatedUsernameUser.getUsername());

      networkHandler.deleteUserProfile();

      try {
        User deletedUser = networkHandler.getUserFromServer();

        fail("User was not deleted");
      } catch (Exception e) {
      }
    } catch (Exception e) {
      fail("Error:" + e);
    }
  }

  /**
   * Test the updateEmail method and check if the email is updated correctly on the server
   *
   * @author robert.kratz
   */
  @Test
  void signup() {
    try {
      networkHandler.signup("use123123r1", "user@user.de", "P4ssword_!1234");

      login();

      assertNotNull(networkHandler.getAccessToken());
      assertNotNull(networkHandler.getRefreshToken());
    } catch (Exception e) {
      fail("Error:" + e);
    }
  }

  /**
   * Test the login method
   *
   * @author robert.kratz
   */
  @Test
  void login() {
    try {
      boolean success = networkHandler.login("use123123r1", "P4ssword_!1234");

      assertTrue(success);

      assertNotNull(networkHandler.getAccessToken());
      assertNotNull(networkHandler.getRefreshToken());
    } catch (Exception e) {
      fail("Error:" + e);
    }

  }

  /**
   * Test the getUserFromServer method
   *
   * @author robert.kratz
   */
  @Test
  void getUserFromServer() {
    login();

    User user = null;
    try {
      user = networkHandler.getUserFromServer();
    } catch (Exception e) {
      fail("Error:" + e);
    }

    assertNotNull(user);

    System.out.println(user.toString());
  }

  /**
   * Test the refreshToken method and check if the tokens are refreshed correctly
   *
   * @author robert.kratz
   */
  @Test
  void refreshToken() {
    login();

    boolean success = networkHandler.refreshTokens();

    assertTrue(success);

    assertNotNull(networkHandler.getAccessToken());
    assertNotNull(networkHandler.getRefreshToken());
  }

  /**
   * Test the refreshToken method with force refresh and check if the tokens are refreshed
   * correctly
   *
   * @author robert.kratz
   */
  @Test
  void refreshTokenByForceRefresh() {
    login();

    String firstAccessToken = networkHandler.getAccessToken(), firstRefreshToken = networkHandler.getRefreshToken();

    //wait for the token to expire
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    assertTrue(!networkHandler.isTokenValid(NetworkHandler.TokenType.ACCESS));

    User user = null;

    try {
      user = networkHandler.getUserFromServer();
    } catch (Exception e) {
      fail("Error:" + e);
    }

    if (firstAccessToken.equals(networkHandler.getAccessToken())) {
      fail("Access token was not refreshed");
    }

    if (firstRefreshToken.equals(networkHandler.getRefreshToken())) {
      fail("Refresh token was not refreshed");
    }

    assertNotNull(user);

    System.out.println(user.toString());
  }

  /**
   * Test the signUpAsGuest method and check if the tokens are generated correctly and the user is
   * created correctly on the server
   *
   * @author robert.kratz
   */
  @Test
  void signUpAsGuest() {
    NetworkHandler newNetworkHandler = new NetworkHandler(new NetworkEvent() {
      @Override
      public void onEvent(NetworkEventType type, Object... args) {
        switch (type) {
          case TOKEN_REFRESHED:
            TokenRefreshEvent event = (TokenRefreshEvent) args[0];
            System.out.println(
                "Token refreshed: " + event.getAccessToken() + " " + event.getRefreshToken());
            break;
          default:
            break;
        }
      }
    });

    newNetworkHandler.signUpAsGuest();

    assertNotNull(newNetworkHandler.getAccessToken());
    assertNotNull(newNetworkHandler.getRefreshToken());

    //get user data
    User user = null;

    try {
      user = newNetworkHandler.getUserFromServer();
    } catch (Exception e) {
      fail("Error:" + e);
    }

    assertNotNull(user);

    System.out.println(user.toString());
  }
}