package de.cfp1.server.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents a special exception (move request is forbidden for current team) that is marked with a
 * HTTP status if thrown.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Move is forbidden for current team")
public class ForbiddenMove extends RuntimeException {

}
