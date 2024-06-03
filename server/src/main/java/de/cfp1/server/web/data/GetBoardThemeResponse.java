package de.cfp1.server.web.data;

import de.cfp1.server.game.BoardTheme;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author robert.kratz
 */

public class GetBoardThemeResponse {

  @Schema(
      description = "the board theme"
  )
  public BoardTheme boardTheme;

  /**
   * Default constructor
   *
   * @author robert.kratz
   */
  public GetBoardThemeResponse() {
  }

  public GetBoardThemeResponse(BoardTheme boardTheme) {
    this.boardTheme = boardTheme;
  }

  public BoardTheme getBoardTheme() {
    return boardTheme;
  }

  public void setBoardTheme(BoardTheme boardTheme) {
    this.boardTheme = boardTheme;
  }
}
