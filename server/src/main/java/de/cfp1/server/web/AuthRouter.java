package de.cfp1.server.web;

import de.cfp1.server.Server;
import de.cfp1.server.auth.AuthProvider;
import de.cfp1.server.auth.TokenPair;
import de.cfp1.server.auth.TokenType;
import de.cfp1.server.entities.User;
import de.cfp1.server.utils.NameGenerator;
import de.cfp1.server.utils.PasswordHash;
import de.cfp1.server.web.data.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author robert.kratz
 */

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthRouter {

  private static final Logger LOG = LoggerFactory.getLogger(AuthRouter.class);

  private final AuthProvider authenticationManager = new AuthProvider();

  /**
   * Router: /api/auth/login - POST - User login
   *
   * @param loginRequest The login request containing the username and password
   * @return The token pair containing the access and refresh tokens
   * @author robert.kratz
   */
  @PostMapping("/login")
  @Operation(summary = "User login", responses = {
      @ApiResponse(description = "Bad Request", responseCode = "401"),
      @ApiResponse(description = "Login failed", responseCode = "500"),
      @ApiResponse(description = "Invalid credentials", responseCode = "401"),
      @ApiResponse(description = "Authentication successful", responseCode = "200",
          content = @Content(schema = @Schema(implementation = TokenPair.class)))
  })
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    try {
      // Dummy validation of username and password
      if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty() ||
          loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }

      User userExists = Server.getDbManager().getUserByName(loginRequest.getUsername());

        if (userExists == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

      // Assuming any non-empty username and password is valid for demonstration
      if (PasswordHash.checkPassword(loginRequest.getPassword(), userExists.getPassword())) {
        TokenPair tokens = authenticationManager.generateTokens(userExists.getId());

        LOG.info("User " + loginRequest.getUsername() + " (" + userExists.getId()
            + ") logged in successfully");

        return ResponseEntity.ok(tokens);
      } else {
        return ResponseEntity.status(401).body("Invalid credentials");
      }
    } catch (Exception e) {
      LOG.error("Failed to login user " + loginRequest.getUsername(), e);
      return ResponseEntity.status(500).body("Login failed");
    }
  }

  /**
   * Router: /api/auth/signup - POST - User signup
   *
   * @param signupRequest The signup request containing the username, password, guest and email
   * @return The user data
   * @author robert.kratz
   */
  @PostMapping("/signup")
  @Operation(summary = "User signup", responses = {
      @ApiResponse(description = "Bad Request", responseCode = "401"),
      @ApiResponse(description = "Signup successful", responseCode = "200",
          content = @Content(schema = @Schema(implementation = SignupResponse.class))),
      @ApiResponse(description = "Signup failed", responseCode = "500"),
      @ApiResponse(description = "Password insecure", responseCode = "401"),
      @ApiResponse(description = "Username already exist", responseCode = "409"),
      @ApiResponse(description = "Email already exist", responseCode = "409"),
  })
  public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
    try {
      // Dummy validation of username and password
      if (signupRequest.getUsername() == null || signupRequest.getUsername().trim().isEmpty() ||
          signupRequest.getPassword() == null || signupRequest.getPassword().trim().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }

      //check if username is already in use
      User userExists = Server.getDbManager().getUserByName(signupRequest.getUsername());
        if (userExists != null) {
            return ResponseEntity.status(409).body("Username already exists");
        }

      //check if email is already in use
      User emailExists = Server.getDbManager().getUserByEmailAddress(signupRequest.getEmail());
        if (emailExists != null) {
            return ResponseEntity.status(409).body("Email already exists");
        }

        if (authenticationManager.isPasswordInsecure(signupRequest.getPassword())) {
            return ResponseEntity.status(401).body("Password insecure");
        }

      // Hash the password
      String hashedPassword = PasswordHash.hashPassword(signupRequest.getPassword());
      // Create the user
      User user = Server.getDbManager()
          .createUser(signupRequest.getUsername(), signupRequest.getEmail(), hashedPassword, false,
              false);

      LOG.info(
          "User " + signupRequest.getUsername() + " (" + user.getId() + ") signed up successfully");

      // Return the user data
      return ResponseEntity.ok(
          new SignupResponse(user.getUsername(), user.getEmail(), false, user.getId()));
    } catch (Exception e) {
      LOG.error("Failed to signup user " + signupRequest.getUsername(), e);
      return ResponseEntity.status(500).body("Signup failed");
    }
  }

  /**
   * Router: /api/auth/signup/guest - POST - Guest signup
   *
   * @return The token pair containing the access and refresh tokens
   * @author robert.kratz
   */
  @PostMapping("/signup/guest")
  @Operation(summary = "Guest signup", responses = {
      @ApiResponse(description = "Signup successful", responseCode = "200",
          content = @Content(schema = @Schema(implementation = TokenPair.class))),
  })
  public ResponseEntity<?> signupGuest() {
    try {
      String userName = null;
      boolean userNameExists = true;

      do {
        String newUsername = NameGenerator.generateUsername();

        if (Server.getDbManager().getUserByName(newUsername) == null) {
          userName = newUsername;
          userNameExists = false;
        }
      } while (userNameExists);

      String email = userName + "guest";

      User user = Server.getDbManager().createUser(userName, email, null, true, false);

      TokenPair tokens = authenticationManager.generateTokens(user.getId());

      LOG.info("Guest " + userName + " (" + user.getId() + ") signed up successfully");

      return ResponseEntity.ok(tokens);
    } catch (Exception e) {
      LOG.error("Failed to signup guest", e);
      return ResponseEntity.status(500).body("Signup failed");
    }
  }

  /**
   * Router: /api/auth/signup/guest/transform - POST - Transform guest to user
   *
   * @param authToken    The authorization token
   * @param loginRequest The login request containing the username, password and email
   * @return The response entity
   * @author robert.kratz
   */
  @PostMapping("/signup/guest/transform")
  @Operation(summary = "Transform guest to user", responses = {
      @ApiResponse(description = "Transformation successful", responseCode = "200",
          content = @Content(schema = @Schema(implementation = SignupResponse.class))),
      @ApiResponse(description = "Transformation failed", responseCode = "401"),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "Invalid token", responseCode = "401"),
      @ApiResponse(description = "User not found", responseCode = "404"),
      @ApiResponse(description = "Username not found", responseCode = "404"),
      @ApiResponse(description = "Username already exists", responseCode = "409"),
      @ApiResponse(description = "User is not a guest", responseCode = "401"),
      @ApiResponse(description = "Email already exists", responseCode = "409"),
      @ApiResponse(description = "Password insecure", responseCode = "401"),
      @ApiResponse(description = "Transformation failed", responseCode = "500"),
  })
  public ResponseEntity<?> transformGuest(
      @RequestHeader(value = "Authorization", required = false) String authToken,
      @RequestBody UserTransformationRequest loginRequest) {
    try {
      User userFromToken;

      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(403).body("Invalid token");
      }

        if (userFromToken == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (!userFromToken.isGuest()) {
            return ResponseEntity.status(401).body("User is not a guest");
        }

      //check if username is already in use
      User usernameExists = Server.getDbManager().getUserByName(loginRequest.getUsername());
        if (usernameExists != null) {
            return ResponseEntity.status(409).body("Username already exists");
        }

      //check if email is already in use
      User emailExists = Server.getDbManager().getUserByEmailAddress(loginRequest.getEmail());
        if (emailExists != null) {
            return ResponseEntity.status(409).body("Email already exists");
        }

        if (authenticationManager.isPasswordInsecure(loginRequest.getPassword())) {
            return ResponseEntity.status(401).body("Password insecure");
        }

      String hashedPassword = PasswordHash.hashPassword(loginRequest.getPassword());

      boolean success = Server.getDbManager()
          .updateUser(userFromToken.getId(), loginRequest.getUsername(), loginRequest.getEmail(),
              hashedPassword, false, false);
        if (!success) {
            return ResponseEntity.status(500).body("Transformation failed");
        }

      LOG.info("Guest " + userFromToken.getUsername() + " (" + userFromToken.getId()
          + ") transformed to user " + loginRequest.getUsername());

      return ResponseEntity.ok(
          new SignupResponse(loginRequest.getUsername(), loginRequest.getEmail(), false,
              userFromToken.getId()));
    } catch (Exception e) {
      LOG.error("Failed to transform guest", e);
      return ResponseEntity.status(500).body("Transformation failed");
    }
  }

  /**
   * Router: /api/auth/token/refresh - PUT - Refresh access token
   *
   * @param tokenRequest The token request containing the refresh token
   * @return The token pair containing the access and refresh tokens
   * @author robert.kratz
   */
  @PutMapping("/token/refresh")
  @Operation(summary = "Refresh access token", responses = {
      @ApiResponse(description = "Access token refreshed successfully", responseCode = "200",
          content = @Content(schema = @Schema(implementation = TokenPair.class))),
      @ApiResponse(description = "Invalid token", responseCode = "401"),
      @ApiResponse(description = "Invalid Header", responseCode = "404"),
      @ApiResponse(description = "Invalid token type", responseCode = "404"),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "User not found", responseCode = "404"),
  })
  public ResponseEntity<?> refreshAccessToken(@RequestBody TokenRequest tokenRequest) {
    try {
      User userFromToken = authenticationManager.validateTokenAndGetUser(tokenRequest.getToken());

      //check if refresh token is valid
        if (authenticationManager.getTokenType(tokenRequest.getToken()) != TokenType.REFRESH) {
            return ResponseEntity.status(401).body("Invalid token type");
        }

        if (userFromToken == null) {
            return ResponseEntity.status(401).body("User not found");
        }

      TokenPair tokenPair = authenticationManager.refreshAccessToken(tokenRequest.getToken());

      LOG.info("Access token refreshed for user " + userFromToken.getUsername() + " ("
          + userFromToken.getId() + ")");

      return ResponseEntity.ok(tokenPair);
    } catch (Exception e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  /**
   * Router: /api/auth/token/validate - POST - Validate token
   *
   * @param tokenRequest The token request containing the access token
   * @return The username response containing the username
   * @author robert.kratz
   */
  @PostMapping("/token/validate")
  @Operation(summary = "Validate token", responses = {
      @ApiResponse(description = "Token is valid", responseCode = "200",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(description = "Token is invalid or expired", responseCode = "401"),
      @ApiResponse(description = "Invalid token", responseCode = "401"),
      @ApiResponse(description = "User not found", responseCode = "404"),
  })
  public ResponseEntity<?> validateToken(@RequestBody TokenRequest tokenRequest) {
    try {
      User username = authenticationManager.validateTokenAndGetUser(tokenRequest.getToken());

        if (username == null) {
            return ResponseEntity.status(401).body("User not found");
        }

      return ResponseEntity.ok(true);
    } catch (Exception e) {
      LOG.error("Failed to validate token", e);
      return ResponseEntity.badRequest().body("Invalid token");
    }
  }

  /**
   * Router: /api/auth/delete - DELETE - Delete user
   *
   * @param authToken The authorization token
   * @return The response entity
   * @author robert.kratz
   */
  @DeleteMapping("/delete")
  @Operation(summary = "Delete user", responses = {
      @ApiResponse(description = "User deleted", responseCode = "200",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(description = "User not found", responseCode = "404"),
      @ApiResponse(description = "Invalid token", responseCode = "401"),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "Failed to delete user", responseCode = "500"),
  })
  public ResponseEntity<?> deleteUser(@RequestHeader(value = "Authorization") String authToken) {
    try {
      User userFromToken;
      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
          if (userFromToken == null) {
              return ResponseEntity.status(401).body("User not found");
          }
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(401).body(e.getMessage());
      }

      boolean success = Server.getDbManager().deleteUser(userFromToken.getId());
        if (!success) {
            return ResponseEntity.status(404).body("User not found");
        }

      //Delete All related maps
      Server.getDbManager().deleteAllUserMaps(userFromToken.getId());

      return ResponseEntity.ok(
          "Deleted user " + userFromToken.getUsername() + " (" + userFromToken.getId() + ")");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Failed to delete user");
    }
  }

  /**
   * Router: /api/auth/user - GET - Get user data
   *
   * @param authToken The authorization token
   * @return The response entity as User
   * @author robert.kratz
   */
  @GetMapping("/user")
  @Operation(summary = "Get user data", responses = {
      @ApiResponse(description = "User data", responseCode = "200",
          content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(description = "User not found", responseCode = "404"),
      @ApiResponse(description = "Invalid Header", responseCode = "401"),
      @ApiResponse(description = "Invalid token type", responseCode = "401"),
      @ApiResponse(description = "Token expired", responseCode = "401"),
      @ApiResponse(description = "Failed to get user data", responseCode = "500"),
  })
  public ResponseEntity<?> getUserData(@RequestHeader(value = "Authorization") String authToken) {
    try {
      User userFromToken;
      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
          if (userFromToken == null) {
              return ResponseEntity.status(401).body("User not found");
          }
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(401).body("Invalid token");
      }

      userFromToken.setPassword(null);

      return ResponseEntity.ok(userFromToken);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Failed to get user data");
    }
  }

  /**
   * Router: /api/auth/signup/guest - POST - Guest signup
   *
   * @param updateUserRequest The update user request containing the username to update
   * @param authToken         The authorization token in request header
   * @return The response entity as User
   * @author robert.kratz
   */
  @PutMapping("/user")
  @Operation(summary = "Update user", responses = {
      @ApiResponse(description = "User updated", responseCode = "200",
          content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(description = "User not found", responseCode = "404"),
      @ApiResponse(description = "Invalid token", responseCode = "401"),
      @ApiResponse(description = "Username already exists", responseCode = "409")
  })
  public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest,
      @RequestHeader
          (value = "Authorization", required = false) String authToken) {
    try {
        if (updateUserRequest.getUsername() == null || updateUserRequest.getUsername().trim()
            .isEmpty()) {
            return ResponseEntity.badRequest().build(
            );
        }

      User userFromToken;

      try {
        userFromToken = authenticationManager.validateHeaderAndGetUser(authToken, TokenType.ACCESS);
      } catch (Exception e) {
          if (e.getMessage().equals("Token expired")) {
              return ResponseEntity.status(401).body("Token expired");
          }

        return ResponseEntity.status(401).body("Invalid token");
      }

        if (userFromToken == null) {
            return ResponseEntity.status(404).body("User not found");
        }

      User usernameExists = Server.getDbManager().getUserByName(updateUserRequest.getUsername());

        if (usernameExists != null) {
            return ResponseEntity.status(409).body("Username already exists");
        }

      boolean success = Server.getDbManager()
          .updateUser(userFromToken.getId(), updateUserRequest.getUsername(),
              userFromToken.getEmail(), userFromToken.getPassword(), userFromToken.isGuest(),
              userFromToken.isAdmin());

        if (!success) {
            return ResponseEntity.status(500).body("Failed to update user");
        }

      userFromToken.setPassword(null);
      userFromToken.setUsername(updateUserRequest.getUsername());

      return ResponseEntity.ok(userFromToken);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Failed to update user");
    }
  }

  /**
   * Router: /api/auth/signup/guest - POST - Guest signup
   *
   * @param id The user id to get data from
   * @return The response entity as User
   * @author robert.kratz
   */
  @GetMapping("/user/{id}")
  @Operation(summary = "Get user data by id", responses = {
      @ApiResponse(description = "User data", responseCode = "200",
          content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(description = "User not found", responseCode = "404"),
      @ApiResponse(description = "Failed to get user data", responseCode = "500"),
  })
  public ResponseEntity<?> getUserDataById(@PathVariable String id) {
    try {
      User user = Server.getDbManager().getUserById(id);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

      user.setPassword(null);

      return ResponseEntity.ok(user);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Failed to get user data");
    }
  }
}