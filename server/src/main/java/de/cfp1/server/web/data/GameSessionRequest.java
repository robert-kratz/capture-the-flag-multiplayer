package de.cfp1.server.web.data;

import de.cfp1.server.game.map.MapTemplate;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class is used to represent a request to create a new game session.
 */
public class GameSessionRequest {

  @Schema(
      description = "the map to use for a new game session"
  )
  public MapTemplate template;

  public MapTemplate getTemplate() {
    return template;
  }

  public void setTemplate(MapTemplate template) {
    this.template = template;
  }
}

