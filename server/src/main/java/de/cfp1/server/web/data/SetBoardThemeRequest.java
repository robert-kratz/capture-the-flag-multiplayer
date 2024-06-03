package de.cfp1.server.web.data;

import de.cfp1.server.game.BoardTheme;
import io.swagger.v3.oas.annotations.media.Schema;

public class SetBoardThemeRequest {

  @Schema(
      description = "the board theme"
  )
  public BoardTheme boardTheme;

  @Schema(
      description = "the session id"
  )
  public String sessionId;

  /**
   * Default constructor
   *
   * @author robert.kratz
   */
  public SetBoardThemeRequest() {
  }

  /**
   * Constructor with parameters
   *
   * @param boardTheme the board theme
   * @param sessionId  the session id
   * @author robert.kratz
   */
  public SetBoardThemeRequest(BoardTheme boardTheme, String sessionId) {
    this.boardTheme = boardTheme;
    this.sessionId = sessionId;
  }

  /**
   * Setter for the session id
   *
   * @param sessionId the session id
   * @author robert.kratz
   */
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * Setter for the board theme
   *
   * @param boardTheme the board theme
   * @author robert.kratz
   */
  public void setBoardTheme(BoardTheme boardTheme) {
    this.boardTheme = boardTheme;
  }

  /**
   * Getter for the session id
   *
   * @return the session id
   * @author robert.kratz
   */
  public String getSessionId() {
    return sessionId;
  }

  /**
   * Getter for the board theme
   *
   * @return the board theme
   * @author robert.kratz
   */
  public BoardTheme getBoardTheme() {
    return boardTheme;
  }
}
