package ru.practicum.explore.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("ru.practicum.explore-with-me")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({EndpointNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        log.info(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({EndpointEmptyAppException.class, EndpointEmptyUriException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestFoundException(final RuntimeException e) {
        log.info(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info(e.getMessage(), e);
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
