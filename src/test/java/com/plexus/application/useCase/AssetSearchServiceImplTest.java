package com.plexus.application.useCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plexus.application.ports.out.AssetSearchPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.domain.model.out.AssetSearchResponse;

@ExtendWith(MockitoExtension.class)
class AssetSearchServiceImplTest {

    @Mock
    private AssetSearchPort assetSearchPort;

    @InjectMocks
    private AssetSearchServiceImpl assetSearchService;

    private List<Asset> mockAssets;

    @BeforeEach
    void setUp() {
        mockAssets = Arrays.asList(
                Asset.builder()
                        .id("123e4567-e89b-12d3-a456-426614174000")
                        .filename("document1.pdf")
                        .contentType("application/pdf")
                        .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000")
                        .size(1024L)
                        .uploadDate("2025-01-15T10:30Z")
                        .build(),
                Asset.builder()
                        .id("123e4567-e89b-12d3-a456-426614174001")
                        .filename("document2.jpg")
                        .contentType("image/jpeg")
                        .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174001")
                        .size(2048L)
                        .uploadDate("2025-01-16T10:30Z")
                        .build()
        );
    }

    @Test
    void search_ShouldReturnSearchResponses_WhenValidParameters() {
        // Given
        String uploadDateStart = "2025-01-01T00:00:00Z";
        String uploadDateEnd = "2025-01-31T23:59:59Z";
        String filename = "document.*";
        String filetype = "application/pdf";
        String sortDirection = "ASC";

        when(assetSearchPort.findByFilters(anyString(), anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class), anyString()))
                .thenReturn(mockAssets);

        // When
        List<AssetSearchResponse> result = assetSearchService.search(uploadDateStart, uploadDateEnd, filename, filetype, sortDirection);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.get(0).getId());
        assertEquals("document1.pdf", result.get(0).getFilename());
    }

    @Test
    void search_ShouldReturnEmptyList_WhenNoResults() {
        // Given
        String uploadDateStart = "2025-01-01T00:00:00Z";
        String uploadDateEnd = "2025-01-31T23:59:59Z";
        String filename = "nonexistent.*";
        String filetype = null;
        String sortDirection = "ASC";

        when(assetSearchPort.findByFilters(any(), any(), any(), any(), anyString()))
                .thenReturn(Arrays.asList());

        // When
        List<AssetSearchResponse> result = assetSearchService.search(uploadDateStart, uploadDateEnd, filename, filetype, sortDirection);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void search_ShouldHandleNullParameters() {
        // Given
        when(assetSearchPort.findByFilters(any(), any(), any(), any(), anyString()))
                .thenReturn(mockAssets);

        // When
        List<AssetSearchResponse> result = assetSearchService.search(null, null, null, null, "ASC");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void search_ShouldParseDatesCorrectly() {
        // Given
        String uploadDateStart = "2025-01-15T10:30Z";
        String uploadDateEnd = "2025-01-15T11:30Z";
        String filename = null;
        String filetype = null;
        String sortDirection = "DESC";

        when(assetSearchPort.findByFilters(any(), any(), any(), any(), anyString()))
                .thenReturn(mockAssets);

        // When
        List<AssetSearchResponse> result = assetSearchService.search(uploadDateStart, uploadDateEnd, filename, filetype, sortDirection);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void search_ShouldThrowException_WhenInvalidDateFormat() {
        // Given
        String invalidDate = "invalid-date-format";
        String filename = null;
        String filetype = null;
        String sortDirection = "ASC";

        // When & Then
        assertThrows(Exception.class, () -> {
            assetSearchService.search(invalidDate, null, filename, filetype, sortDirection);
        });
    }
}
