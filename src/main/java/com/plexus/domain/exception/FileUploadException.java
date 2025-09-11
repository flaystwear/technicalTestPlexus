package com.plexus.domain.exception;

/**
 * Exception thrown when file upload operations fail.
 */
public class FileUploadException extends RuntimeException {
    
    public FileUploadException(String message) {
        super(message);
    }
    
    
    public FileUploadException(String filename, String reason) {
        super(String.format("Failed to upload file '%s': %s", filename, reason));
    }
}
