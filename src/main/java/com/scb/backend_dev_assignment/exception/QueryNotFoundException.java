package com.scb.backend_dev_assignment.exception;

public class QueryNotFoundException extends RuntimeException {
    public QueryNotFoundException(String message) {
        super(message);
    }
}
