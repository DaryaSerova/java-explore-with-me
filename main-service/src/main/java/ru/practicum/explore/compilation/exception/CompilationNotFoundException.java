package ru.practicum.explore.compilation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CompilationNotFoundException extends RuntimeException {

    public CompilationNotFoundException(final String message) {
        super(message);
    }

}
