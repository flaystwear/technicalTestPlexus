package com.plexus.domain.exception;

/**
 * Exception thrown when file storage operations fail.
 */
public class FileStorageException extends RuntimeException {
    
    public FileStorageException(String message) {
        super(message);
    }
    
    
    public FileStorageException(String operation, String assetId, String reason) {
        super(String.format("File storage %s failed for asset '%s': %s", operation, assetId, reason));
    }
}
