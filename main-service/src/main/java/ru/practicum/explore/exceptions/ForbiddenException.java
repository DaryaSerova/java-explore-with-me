package ru.practicum.explore.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
@Getter
public class ForbiddenException extends RuntimeException {

    private String reason;

    public ForbiddenException(final String message, final String reason) {
        super(message);
        this.reason = reason;
    }

}