package com.gpb.exception;

public class DatabaseConnectionFailureException extends RuntimeException{
    public DatabaseConnectionFailureException(String message) {
        super(message);
    }
}
