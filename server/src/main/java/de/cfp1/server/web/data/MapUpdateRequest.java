package de.cfp1.server.web.data;

import de.cfp1.server.game.map.MapTemplate;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author robert.kratz
 */

public class MapUpdateRequest {

  @Schema(
      description = "The name of the map",
      example = "map"
  )
  public String name;

  @Schema(
      description = "The description of the map",
      example = "description"
  )
  public boolean isPublic;

  @Schema(
      description = "The map template",
      example = "template"
  )
  public MapTemplate mapTemplate;

  public String getName() {
    return name;
  }

  public MapTemplate getMapTemplate() {
    return mapTemplate;
  }

  public boolean isPublic() {
    return isPublic;
  }
}
