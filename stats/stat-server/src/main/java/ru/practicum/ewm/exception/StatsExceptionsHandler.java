package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class StatsExceptionsHandler {

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String validationException(final RuntimeException e) {
        return e.getMessage();
    }

}
