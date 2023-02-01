package ru.practicum.explore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EndpointEmptyUriException extends RuntimeException {

    public EndpointEmptyUriException(final String message) {
        super(message);
    }

}
