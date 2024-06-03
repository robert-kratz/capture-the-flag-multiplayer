package de.cfp1.server.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents a special exception (move request is invalid) that is marked with a HTTP status if
 * thrown.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Move is invalid")
public class InvalidMove extends RuntimeException {

}
