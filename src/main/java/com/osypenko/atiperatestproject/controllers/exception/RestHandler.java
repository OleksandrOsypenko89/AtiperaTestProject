package com.osypenko.atiperatestproject.controllers.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.osypenko.atiperatestproject.exception.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestHandler {

    @ExceptionHandler(
            value = {
                    Exception.class,
                    RuntimeException.class,
                    MismatchedInputException.class
            }
    )
    protected ResponseEntity<Object> handleConflict(Exception ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage("404", ex.getMessage());
        log.error(exceptionMessage.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionMessage);
    }
}
