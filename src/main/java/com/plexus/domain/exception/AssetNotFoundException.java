package com.plexus.domain.exception;

/**
 * Exception thrown when an asset is not found in the system.
 */
public class AssetNotFoundException extends RuntimeException {
    
    public AssetNotFoundException(String message) {
        super(message);
    }
    
    
    public AssetNotFoundException(String assetId, String operation) {
        super(String.format("Asset with ID '%s' not found during %s operation", assetId, operation));
    }
}
