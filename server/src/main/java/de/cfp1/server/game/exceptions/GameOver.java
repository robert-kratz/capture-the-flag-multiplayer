package de.cfp1.server.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents a special exception (game is over) that is marked with a HTTP status if thrown.
 */
@ResponseStatus(value = HttpStatus.GONE, reason = "Game is over")
public class GameOver extends RuntimeException {

}
