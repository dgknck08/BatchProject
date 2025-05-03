package com.example.springProject.exception;


@SuppressWarnings("serial")
public class ApiException extends RuntimeException {
    private final int statusCode; 
    private final String query;    

    public ApiException(String message, int statusCode, String query) {
        super(message); 
        this.statusCode = statusCode;
        this.query = query;
    }

    public int getStatusCode() {
        return statusCode; 
    }

    public String getQuery() {
        return query;
    }
}