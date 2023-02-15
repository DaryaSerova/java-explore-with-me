package ru.practicum.explore.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
public class BadRequestException extends RuntimeException {

    private String reason;

    public BadRequestException(final String message, final String reason) {
        super(message);
        this.reason = reason;
    }

}