package com.osypenko.atiperatestproject.controllers.exception;

import com.osypenko.atiperatestproject.exception.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestHandler {

    @ExceptionHandler(ExceptionMessage.class)
    protected ResponseEntity<ExceptionMessage> handleConflict(ExceptionMessage ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage(ex.getStatus(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatusCode.valueOf(exceptionMessage.getStatus()))
                .body(exceptionMessage);
    }
}