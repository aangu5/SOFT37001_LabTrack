package org.ordep.labtrack.controller;

import org.ordep.labtrack.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class APIControllerAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> resolveUserException(UserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
