package ru.practicum.explore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EndpointEmptyAppException extends RuntimeException {

    public EndpointEmptyAppException(final String message) {
        super(message);
    }

}
