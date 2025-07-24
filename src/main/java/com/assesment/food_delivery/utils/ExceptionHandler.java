package com.assesment.food_delivery.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadException(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage(), "status", HttpStatus.UNAUTHORIZED, "statusCode", 401));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
    public  ResponseEntity<?> handleUserNotFound (UsernameNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage(), "status", HttpStatus.NOT_FOUND, "statusCode", 404));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public  ResponseEntity<?> handleAllException (Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage(), "status", HttpStatus.INTERNAL_SERVER_ERROR, "statusCode", 500));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public  ResponseEntity<?> handleRuntimeException (Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage(), "status", HttpStatus.BAD_REQUEST, "statusCode", 400));
    }

}
