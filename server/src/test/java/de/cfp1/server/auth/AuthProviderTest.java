package de.cfp1.server.auth;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Disabled(
    "Start the server and run test manually, "
)
class AuthProviderTest {

  @Test
  public void generateTokens_ReturnsValidTokens_WhenUserIdIsProvided() {
    // Arrange
    AuthProvider authProvider = new AuthProvider();
    String userId = UUID.randomUUID().toString();

    // Act
    TokenPair tokens = authProvider.generateTokens(userId);

    System.out.println("Access token: " + tokens.getAccessToken());
    System.out.println("Refresh token: " + tokens.getRefreshToken());

    // Assert not null
    assertNotNull(tokens.getAccessToken(), "Access token should not be null");
    assertNotNull(tokens.getRefreshToken(), "Refresh token should not be null");

    //check if token is valid JWT
    assertDoesNotThrow(() -> {
      String token = authProvider.validateTokenAndGetUserId(
          tokens.getAccessToken()), refreshToken = authProvider.validateTokenAndGetUserId(
          tokens.getRefreshToken());
      assertNotNull(token, "Access token should be valid");
      assertNotNull(refreshToken, "Refresh token should be valid");

      System.out.println("Access token user id: " + token);
      System.out.println("Refresh token user id: " + refreshToken);
    });
  }
}