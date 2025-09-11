package com.plexus.domain.exception;

/**
 * Exception thrown when search parameters are invalid or missing.
 */
public class InvalidSearchParametersException extends RuntimeException {
    
    public InvalidSearchParametersException(String message) {
        super(message);
    }

    
    public InvalidSearchParametersException() {
        super("At least one search parameter must be provided");
    }
}
