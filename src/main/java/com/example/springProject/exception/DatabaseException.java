package com.example.springProject.exception;


@SuppressWarnings("serial")
public class DatabaseException extends RuntimeException {
    private final String failedQuery;

    public DatabaseException(String message, String failedQuery, Throwable cause) {
        super(message, cause);
        this.failedQuery = failedQuery;
    }

    public String getFailedQuery() {
        return failedQuery;
    }
}