package com.scb.backend_dev_assignment.exception;

import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errType", "MethodArgumentNotValidException");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        ex.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        errors.put("errType", "Exception");
        errors.put("errMsg", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(QueryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleQueryNotFoundException(QueryNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errType", "QueryNotFoundException");
        errors.put("errMsg", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, String>> handleSQLException(SQLException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errType", "SQLException");
        errors.put("errMsg", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errType", "InvalidDataAccessResourceUsageException");
        errors.put("errMsg", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

