package ru.practicum.ewm.exception;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({ValidationException.class, NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationException(final RuntimeException e) {
        return new ErrorResponse(e.getStackTrace(), e.getMessage(), e.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, PSQLException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflictException(final RuntimeException e) {
        return new ErrorResponse(e.getStackTrace(), e.getMessage(), e.getLocalizedMessage(),
                HttpStatus.CONFLICT.toString(), LocalDateTime.now());
    }

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse itemNotFoundException(final RuntimeException e) {
        return new ErrorResponse(e.getStackTrace(), e.getMessage(), e.getLocalizedMessage(),
                HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
    }
}