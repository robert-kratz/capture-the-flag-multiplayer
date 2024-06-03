package de.cfp1.server.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents a special exception (game session not found) that is marked with an HTTP status if
 * thrown.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Game session not found")
public class GameSessionNotFound extends RuntimeException {

}
