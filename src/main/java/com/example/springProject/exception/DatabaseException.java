package com.example.springProject.exception;


@SuppressWarnings("serial")
public class DatabaseException extends RuntimeException {

    private final String query;    

    public DatabaseException(String message, String query, Exception e) {
        super(message, e);  // Calls the parent constructor with the message and the exception
        this.query = query;
    }

    public DatabaseException(String message, Exception e) {
        super(message, e);  
        this.query = null; 
    }

    public String getQuery() {
        return query; 
    }
}