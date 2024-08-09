package com.osypenko.atiperatestproject.controllers.exception;

import com.osypenko.atiperatestproject.exception.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestHandler {

    @ExceptionHandler(ExceptionMessage.class)
    protected ResponseEntity<ExceptionMessage> handleConflict(ExceptionMessage ex) {
        log.error("Handled ExceptionMessage: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(ex.getStatus()))
                .body(new ExceptionMessage(ex.getStatus(), ex.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    protected ResponseEntity<ExceptionMessage> handleGenericException(Exception ex) {
        log.error("Handled Exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
    }
}