package com.plexus.infraestructure.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.plexus.domain.model.in.AssetFileUploadRequest;
import com.plexus.domain.model.out.AssetFileUploadResponse;
import com.plexus.domain.model.out.AssetSearchResponse;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testUpload_ShouldReturnAccepted_WhenValidRequest() {
        // Given
        String filename = "search-test.pdf";
        String contentType = "application/pdf";
        String encodedFile = "aGVsbG8=";
        String url = "http://localhost:" + port + "/api/mgmt/1/assets/actions/upload";
        AssetFileUploadRequest request = AssetFileUploadRequest.builder()
            .contentType(contentType)
            .encodedFile(encodedFile)
            .filename(filename)
        .build();
        // When
        ResponseEntity<AssetFileUploadResponse> response = this.restTemplate.postForEntity(
                url, request,
                AssetFileUploadResponse.class);

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void testUpload_ShouldReturnBadRequest_WhenMalformedRequest() {
        // Given
        String filename = ""; // Empty filename should cause validation error
        String contentType = "application/pdf";
        String encodedFile = "test content";
        String url = "http://localhost:" + port + "/api/mgmt/1/assets/actions/upload";
        
        // When
        ResponseEntity<String> response = this.restTemplate.postForEntity(
                url, new AssetFileUploadRequest(filename, contentType, encodedFile),
                String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Transactional
    @Rollback(false)
    void testSearch_ShouldReturnOk_WhenValidRequest() {
        // Given - First create an asset to search for
        String filename = "search-test.pdf";
        String contentType = "application/pdf";
        String encodedFile = "aGVsbG8=";
        String uploadUrl = "http://localhost:" + port + "/api/mgmt/1/assets/actions/upload";
        
        AssetFileUploadRequest request = AssetFileUploadRequest.builder()
            .contentType(contentType)
            .encodedFile(encodedFile)
            .filename(filename)
        .build();
        
        // Create asset first
        ResponseEntity<AssetFileUploadResponse> uploadResponse = this.restTemplate.postForEntity(
                uploadUrl, request,
                AssetFileUploadResponse.class);
        assertEquals(HttpStatus.ACCEPTED, uploadResponse.getStatusCode());
        
        // Now search for the asset        
        String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/" +
         "?filename=" + filename + "&sortDirection=ASC";
        
        // When
        ResponseEntity<AssetSearchResponse[]> response = this.restTemplate.getForEntity(
                searchUrl, AssetSearchResponse[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        
        AssetSearchResponse searchResult = response.getBody()[0];
        assertEquals("search-test.pdf", searchResult.getFilename());
        assertEquals("application/pdf", searchResult.getContentType());
        assertEquals(5, searchResult.getSize());
        assertNotNull(searchResult.getUploadDate());
        // No verificamos el ID ya que es generado automáticamente
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Transactional
    @Rollback(false)
    void testSearch_ShouldReturnMultipleAssets_WhenValidRequest() {
        // Given 
        String filetype= "image/jpeg";
        // Now search for the asset        
        String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/" +
         "?filetype=" + filetype + "&sortDirection=ASC";
        
        // When
        ResponseEntity<AssetSearchResponse[]> response = this.restTemplate.getForEntity(
                searchUrl, AssetSearchResponse[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        // No verificamos el ID ya que es generado automáticamente
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Transactional
    @Rollback(false)
    void testSearch_ShouldReturnAssetsByDateRange_WhenValidRequest() {
        // Given - Search for assets uploaded between day 10 and 12
        String uploadDateStart = "2025-01-10T00:00:00Z";
        String uploadDateEnd = "2025-01-12T23:59:59Z";
        String sortDirection = "ASC";
        
        // Now search for assets in date range        
        String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/" +
         "?uploadDateStart=" + uploadDateStart + "&uploadDateEnd=" + uploadDateEnd + "&sortDirection=" + sortDirection;
        
        // When
        ResponseEntity<AssetSearchResponse[]> response = this.restTemplate.getForEntity(
                searchUrl, AssetSearchResponse[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().length);
        // No verificamos el ID ya que es generado automáticamente
    }


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Transactional
    @Rollback(false)
    void testSearch_ShouldReturnAssetsByDateRangeAndFilename_WhenValidRequest() {
        // Given - Search for assets uploaded between day 10 and 12
        String uploadDateStart = "2025-01-10T00:00:00Z";
        String uploadDateEnd = "2025-01-14T23:59:59Z";
        String filename = "document2.jpg";
        String sortDirection = "ASC";
        
        // Now search for assets in date range        
        String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/" +
         "?uploadDateStart=" + uploadDateStart + "&uploadDateEnd=" + uploadDateEnd + "&sortDirection=" + sortDirection + "&filename=" + filename;
        
        // When
        ResponseEntity<AssetSearchResponse[]> response = this.restTemplate.getForEntity(
                searchUrl, AssetSearchResponse[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertEquals(filename, response.getBody()[0].getFilename());
        // No verificamos el ID ya que es generado automáticamente
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Transactional
    @Rollback(false)
    void testSearch_ShouldReturnBadRequest_WhenInvalidRequest() {
        // Given - Search for assets uploaded between day 10 and 12
        String uploadDateStart = "2025-01-10T00:00:00Z";
        String uploadDateEnd = "2025-01-14T23:59:59Z";
        String filename = "document2.jpg";
        
        // Now search for assets in date range        
        String searchUrl = "http://localhost:" + port + "/api/mgmt/1/assets/" +
         "?uploadDateStart=" + uploadDateStart + "&uploadDateEnd=" + uploadDateEnd + "&filename=" + filename;
        
        
        // When
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                searchUrl, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
