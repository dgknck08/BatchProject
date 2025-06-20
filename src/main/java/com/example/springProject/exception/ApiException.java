package com.example.springProject.exception;


@SuppressWarnings("serial")
public class ApiException extends RuntimeException {
    private final int statusCode;
    private final String endpoint;

    public ApiException(String message, int statusCode, String endpoint) {
        super(message);
        this.statusCode = statusCode;
        this.endpoint = endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getEndpoint() {
        return endpoint;
    }
}