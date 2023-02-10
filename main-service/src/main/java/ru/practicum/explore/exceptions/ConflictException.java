package ru.practicum.explore.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
@Getter
public class ConflictException extends RuntimeException {

    private String reason;

    public ConflictException(final String message, final String reason) {
        super(message);
        this.reason = reason;
    }

}