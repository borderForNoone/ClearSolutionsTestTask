package org.example.handler;

import org.example.exceptions.InvalidAgeException;
import org.example.exceptions.InvalidDateException;
import org.example.exceptions.ObjectNotValidException;
import org.example.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<?> handleInvalidAgeException(InvalidAgeException invalidAgeException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(invalidAgeException.getMessage());
    }

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<?> handleObjectNotValidException(ObjectNotValidException objectNotValidException) {
        return ResponseEntity
                .badRequest()
                .body(objectNotValidException.getErrorMessages());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(userNotFoundException.getMessage());
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<?> handleInvalidDateException(InvalidDateException invalidDateException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(invalidDateException.getMessage());
    }
}
