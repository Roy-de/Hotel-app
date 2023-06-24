package com.example.hotelapp.ExceptionHandlers;

import com.example.hotelapp.ExceptionHandlers.Exception.EmailAlreadyRegisteredException;
import com.example.hotelapp.ExceptionHandlers.Exception.UsernameAlreadyTakenException;
import com.example.hotelapp.ExceptionHandlers.Response.DatabaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DatabaseExceptionHandler {
    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<String> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex) {
        // Log the error
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<String> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException ex) {
        // Log the error
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // Log the error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
}
