package ru.practicum.explore.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
@Getter
public class NotFoundException extends RuntimeException {

    private String reason;

    public NotFoundException(final String message, final String reason) {
        super(message);
        this.reason = reason;
    }

}