package ru.practicum.explore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EndpointNotFoundException extends RuntimeException {

    public EndpointNotFoundException(final String message) {
        super(message);
    }

}
