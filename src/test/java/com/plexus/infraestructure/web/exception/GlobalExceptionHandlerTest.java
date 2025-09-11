package com.plexus.infraestructure.web.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.plexus.domain.exception.AssetNotFoundException;
import com.plexus.domain.exception.FileStorageException;
import com.plexus.domain.exception.FileUploadException;
import com.plexus.domain.exception.InvalidSearchParametersException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleInvalidSearchParameters_ShouldReturnBadRequest() {
        // Given
        InvalidSearchParametersException exception = new InvalidSearchParametersException();

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleInvalidSearchParameters(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("At least one search parameter must be provided", response.getBody());
    }

    @Test
    void handleInvalidSearchParameters_ShouldReturnCustomMessage() {
        // Given
        String customMessage = "Custom validation error";
        InvalidSearchParametersException exception = new InvalidSearchParametersException(customMessage);

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleInvalidSearchParameters(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(customMessage, response.getBody());
    }

    @Test
    void handleAssetNotFound_ShouldReturnNotFound() {
        // Given
        AssetNotFoundException exception = new AssetNotFoundException("123", "search");

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleAssetNotFound(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Asset with ID '123' not found during search operation", response.getBody());
    }

    @Test
    void handleAssetNotFoundSingle_ShouldReturnNotFound() {
        // Given
        AssetNotFoundException exception = new AssetNotFoundException("Asset with ID '123' not found during search operation");

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleAssetNotFound(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Asset with ID '123' not found during search operation", response.getBody());
    }

    @Test
    void handleFileUploadException_ShouldReturnUnprocessableEntity() {
        // Given
        FileUploadException exception = new FileUploadException("test.pdf", "Invalid file format");

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleFileUploadException(exception);

        // Then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Failed to upload file 'test.pdf': Invalid file format", response.getBody());
    }
    @Test
    void handleFileUploadExceptionSingle_ShouldReturnUnprocessableEntity() {
        // Given
        FileUploadException exception = new FileUploadException("Failed to upload file 'test.pdf': Invalid file format");

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleFileUploadException(exception);

        // Then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Failed to upload file 'test.pdf': Invalid file format", response.getBody());
    }

    @Test
    void handleFileStorageException_ShouldReturnServiceUnavailable() {
        // Given
        FileStorageException exception = new FileStorageException("upload", "123", "Network timeout");

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleFileStorageException(exception);

        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("File storage upload failed for asset '123': Network timeout", response.getBody());
    }

    @Test
    void handleFileStorageExceptionSingle_ShouldReturnServiceUnavailable() {
        // Given
        FileStorageException exception = new FileStorageException("File storage upload failed for asset '123': Network timeout");

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleFileStorageException(exception);

        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("File storage upload failed for asset '123': Network timeout", response.getBody());
    }


    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        // Given
        RuntimeException exception = new RuntimeException("Unexpected error occurred");

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleGenericException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred. Please try again later.", response.getBody());
    }

    @Test
    void handleGenericException_ShouldReturnGenericMessage() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // When
        ResponseEntity<String> response = globalExceptionHandler.handleGenericException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred. Please try again later.", response.getBody());
    }
}
