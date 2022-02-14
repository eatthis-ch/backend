package ch.eatthis.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoCalsFreeException extends RuntimeException {

    public NoCalsFreeException() {
        super("All calories already used in other recipes.");
    }
}
