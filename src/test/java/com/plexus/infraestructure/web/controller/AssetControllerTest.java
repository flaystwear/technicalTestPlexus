package com.plexus.infraestructure.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.plexus.application.ports.in.AssetSearchService;
import com.plexus.application.ports.in.AssetUploadService;
import com.plexus.domain.exception.InvalidSearchParametersException;
import com.plexus.domain.model.out.AssetFileUploadResponse;
import com.plexus.domain.model.in.AssetFileUploadRequest;
import com.plexus.domain.model.out.AssetSearchResponse;

@WebFluxTest(AssetController.class)
class AssetControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AssetUploadService assetUploadService;

    @MockBean
    private AssetSearchService assetSearchService;

    private AssetFileUploadRequest uploadRequest;
    private AssetFileUploadResponse mockUploadResponse;
    private List<AssetSearchResponse> mockSearchResults;

    @BeforeEach
    void setUp() {
        uploadRequest = AssetFileUploadRequest.builder()
                .filename("test.pdf")
                .contentType("application/pdf")
                .encodedFile("JVBERi0xLjQK") // Base64 encoded content
                .build();

        mockUploadResponse = new AssetFileUploadResponse("123e4567-e89b-12d3-a456-426614174000");

        mockSearchResults = Arrays.asList(
                AssetSearchResponse.builder()
                        .id("123e4567-e89b-12d3-a456-426614174000")
                        .filename("document1.pdf")
                        .contentType("application/pdf")
                        .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000")
                        .size(1024L)
                        .uploadDate("2025-01-15T10:30:00Z")
                        .build()
        );
    }

    @Test
    void upload_ShouldReturnAccepted_WhenValidRequest() throws Exception {
        // Given
        when(assetUploadService.upload(anyString(), anyString(), any(byte[].class)))
                .thenReturn(mockUploadResponse); 

        // When & Then
        webTestClient.post()
                .uri("/api/mgmt/1/assets/actions/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(uploadRequest)
                .exchange()
                .expectStatus().isAccepted()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("123e4567-e89b-12d3-a456-426614174000");
    }

    @Test
    void upload_ShouldReturnBadRequest_WhenEmptyFields() throws Exception {
        // Given
        AssetFileUploadRequest invalidRequest = AssetFileUploadRequest.builder()
                .filename("")
                .contentType("")
                .encodedFile("")
                .build();

        // When & Then
        webTestClient.post()
                .uri("/api/mgmt/1/assets/actions/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Filename is required");
    }

    @Test
    void search_ShouldReturnOk_WhenValidFilters() throws Exception {
        // Given
        when(assetSearchService.search(any(), any(), any(), any(), any()))
                .thenReturn(mockSearchResults);

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/mgmt/1/assets/")
                        .queryParam("uploadDateStart", "2025-01-01T00:00:00Z")
                        .queryParam("uploadDateEnd", "2025-01-31T23:59:59Z")
                        .queryParam("filename", "document.*")
                        .queryParam("sortDirection", "ASC")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[*].id").value(org.hamcrest.Matchers.hasItem("123e4567-e89b-12d3-a456-426614174000"))
                .jsonPath("$[*].filename").value(org.hamcrest.Matchers.hasItem("document1.pdf"));
    }

    @Test
    void search_ShouldReturnBadRequest_WhenNoFilters() throws Exception {
        // Given
        when(assetSearchService.search(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new InvalidSearchParametersException());

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/mgmt/1/assets/")
                        .queryParam("sortDirection", "ASC")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void search_ShouldReturnOk_WhenEmptyResults() throws Exception {
        // Given
        when(assetSearchService.search(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Arrays.asList());

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/mgmt/1/assets/")
                        .queryParam("uploadDateStart", "2025-01-01T00:00:00Z")
                        .queryParam("sortDirection", "ASC")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isEmpty();
    }

    @Test
    void search_ShouldReturnBadRequest_WhenSortDirectionMissing() throws Exception {
        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/mgmt/1/assets/")
                        .queryParam("uploadDateStart", "2025-01-01T00:00:00Z")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }
}
