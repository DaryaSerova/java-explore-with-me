package ru.practicum.explore.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice("ru.practicum.explore-with-me")
@Slf4j
public class ErrorHandler {


    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.info(e.getMessage(), e);
        return new ApiError(HttpStatus.CONFLICT, e.getReason(), e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info(e.getMessage(), e);
        return new ApiError(HttpStatus.NOT_FOUND, e.getReason(), e.getMessage());
    }

    @ExceptionHandler({NumberFormatException.class})
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleNumberFormatException(final NumberFormatException e) {
        log.info(e.getMessage(), e);
        return new ApiError(BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleValidationExceptionException(final ValidationException e) {
        log.info(e.getMessage(), e);
        return new ApiError(BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }

    @ExceptionHandler({ForbiddenException.class})
    @ResponseStatus(FORBIDDEN)
    public ApiError handleForbiddenException(final ForbiddenException e) {
        log.info(e.getMessage(), e);
        return new ApiError(FORBIDDEN, e.getReason(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info(e.getMessage(), e);
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }


}


