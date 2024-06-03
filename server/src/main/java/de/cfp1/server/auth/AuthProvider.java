package de.cfp1.server.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.cfp1.server.Server;
import de.cfp1.server.entities.User;
import de.cfp1.server.exception.InvalidTokenException;
import de.cfp1.server.exception.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author robert.kratz
 */

public class AuthProvider {

  private static final String TOKEN_PREFIX = "Bearer ";

  private static final String SECRET_KEY = "jnasdhuwehdusdbfuhshdfuzg283z2zehz8adasdad2hfzadsasdasdasdasdhz72rg3z27hud2h3uzgre"; // Use a strong, unique key for JWT signing
  private static final long REFRESH_TOKEN_EXP = 1000 * 60 * 60 * 24 * 7; // 7 days
  private static final long ACCESS_TOKEN_EXP = 1000 * 60 * 60 * 24; // 24 hours
  //private static final long ACCESS_TOKEN_EXP = 1000; // 1 second REMOVE FOR TESTING - robert.kratz

  /**
   * Generate both access and refresh tokens
   *
   * @param userid The id of the user associated with the token
   * @return TokenPair
   * @author robert.kratz
   */
  public TokenPair generateTokens(String userid) {

    String accessToken = Jwts.builder()
        .setSubject("{\"type\":\"access\",\"userid\":\"" + userid + "\"}")
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();

    String refreshToken = Jwts.builder()
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP))
        .setSubject("{\"type\":\"refresh\",\"userid\":\"" + userid + "\"}")
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();

    return new TokenPair(accessToken, refreshToken);
  }

  /**
   * Validate the token and extract the user id
   *
   * @param token The token to validate
   * @return User id
   * @author robert.kratz
   */
  public String validateTokenAndGetUserId(String token) {
    JwtParser parser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();
    Claims claims = parser.parseClaimsJws(token).getBody();
    //parse the subject to get the user id
    JsonObject sub = new Gson().fromJson(claims.getSubject(), JsonObject.class);

    return sub.get("userid").getAsString();
  }

  /**
   * Get the expiration date of the token
   *
   * @param token The token to get the expiration date from
   * @return Expiration date of the token
   * @author robert.kratz
   */
  public Date getExpirationDate(String token) {
    JwtParser parser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();
    Claims claims = parser.parseClaimsJws(token).getBody();
    return claims.getExpiration();
  }

  /**
   * Check if the token is expired
   *
   * @param token The token to check
   * @return true if the token is expired, false otherwise
   * @author robert.kratz
   */
  public boolean isTokenExpired(String token) {
    return getExpirationDate(token).before(new Date());
  }

  /**
   * Check if the header is valid
   *
   * @param header The header to check
   * @return true if the header is valid, false otherwise
   * @author robert.kratz
   */
  public boolean isHeaderValid(String header) {
    return header == null || !header.startsWith(TOKEN_PREFIX);
  }

  /**
   * Extract the token from the header by splitting the header at the space
   *
   * @param header The header to extract the token from
   * @return The token
   * @author robert.kratz
   */
  public String extractToken(String header) {
    return header.split(" ")[1];
  }

  /**
   * Check if the token is a JWT token
   *
   * @param token The token to check
   * @return true if the token is a JWT token, false otherwise
   * @author robert.kratz
   */
  public boolean isJwtToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
      Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token)
          .getBody();

      return claims.get("userid", String.class) != null && claims.get("type", String.class) != null;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Get the token type of jwt token
   *
   * @param token The token to get the type from
   * @return TokenType
   * @author robert.kratz
   */
  public TokenType getTokenType(String token) {
    JwtParser parser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();
    Claims claims = parser.parseClaimsJws(token).getBody();
    JsonObject sub = new Gson().fromJson(claims.getSubject(), JsonObject.class);
    return sub.get("type").toString().equals("\"access\"") ? TokenType.ACCESS : TokenType.REFRESH;
  }

  /**
   * Validate the token and extract the username
   *
   * @param token The token to validate
   * @return User
   * @throws UserNotFoundException If the token is invalid or the user is not found
   * @throws InvalidTokenException If the user is not found
   * @apiNote This method accesses the database to get the user associated with the token, database
   * access is required
   * @author robert.kratz
   */
  public User validateTokenAndGetUser(String token)
      throws UserNotFoundException, InvalidTokenException {

      if (isJwtToken(token)) {
          throw new InvalidTokenException("Invalid token");
      }
      if (isTokenExpired(token)) {
          throw new InvalidTokenException("Token expired");
      }

    String userId = validateTokenAndGetUserId(token);
      if (userId == null) {
          throw new UserNotFoundException("User not found");
      }

    User user = Server.getDbManager().getUserById(userId);
      if (user == null) {
          throw new UserNotFoundException("User not found");
      }

    return user;
  }

  /**
   * Validate the token in the header and extract the user
   *
   * @param header The header containing the token from incoming request
   * @return User
   * @throws UserNotFoundException If the token is invalid or the user is not found
   * @throws InvalidTokenException If the user is not found
   */
  public User validateHeaderAndGetUser(String header, TokenType type)
      throws UserNotFoundException, InvalidTokenException {
      if (isHeaderValid(header)) {
          throw new InvalidTokenException("Invalid Header");
      }

    String token = extractToken(header);

      if (getTokenType(token) != type) {
          throw new InvalidTokenException("Invalid token type");
      }

    User user = validateTokenAndGetUser(token);
      if (user == null) {
          throw new UserNotFoundException("User not found");
      }

    return user;
  }

  /**
   * Refresh the access token using the refresh token
   *
   * @param refreshToken The refresh token to use to generate a new access token
   * @return New access token
   * @throws UserNotFoundException If the user is not found associated with the refresh token
   * @throws InvalidTokenException If the refresh token is invalid
   */
  public TokenPair refreshAccessToken(String refreshToken)
      throws UserNotFoundException, InvalidTokenException {
    // Validate refresh token. If valid, generate a new access token.
      if (isJwtToken(refreshToken)) {
          throw new InvalidTokenException("Invalid token");
      }
      if (isTokenExpired(refreshToken)) {
          throw new InvalidTokenException("Token expired");
      }
      if (getTokenType(refreshToken) != TokenType.REFRESH) {
          throw new InvalidTokenException("Invalid token");
      }

    String userId = validateTokenAndGetUserId(refreshToken);
      if (userId == null) {
          throw new UserNotFoundException("User not found");
      }

    return generateTokens(userId);
  }

  /**
   * Check if the password is secure Accepts a password if it meets the following criteria: - At
   * least one digit - At least one lowercase letter - At least one uppercase letter - At least one
   * special character - At least 8 characters long
   *
   * @param password The password to check
   * @return true if the password is secure, false otherwise
   * @author robert.kratz
   */
  public boolean isPasswordInsecure(String password) {
    //must contain at least one digit, one lowercase, one uppercase, one special character and have a length of at least 8 characters
    return !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
  }
}
