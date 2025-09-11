package com.plexus.application.useCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plexus.application.ports.out.AssetUploadPort;
import com.plexus.domain.model.dto.Asset;

@ExtendWith(MockitoExtension.class)
class AssetUploadServiceImplTest {

    @Mock
    private AssetUploadPort assetUploadPort;

    @InjectMocks
    private AssetUploadServiceImpl assetUploadService;

    private Asset mockAsset;

    @BeforeEach
    void setUp() {
        mockAsset = Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size(1024L)
                .uploadDate("2025-01-15T10:30Z")
                .build();
    }

    @Test
    void upload_ShouldReturnAsset_WhenValidInput() {
        // Given
        String filename = "test.pdf";
        String contentType = "application/pdf";
        byte[] encodedFile = "test content".getBytes();

        when(assetUploadPort.upload(filename, contentType, encodedFile))
                .thenReturn(mockAsset);

        // When
        var result = assetUploadService.upload(filename, contentType, encodedFile);

        // Then
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
    }

    @Test
    void upload_ShouldHandleNullFile() {
        // Given
        String filename = "test.pdf";
        String contentType = "application/pdf";
        byte[] encodedFile = null;

        Asset assetWithNullFile = Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size(0L)
                .uploadDate("2025-01-15T10:30Z")
                .build();

        when(assetUploadPort.upload(filename, contentType, encodedFile))
                .thenReturn(assetWithNullFile);

        // When
        var result = assetUploadService.upload(filename, contentType, encodedFile);

        // Then
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
    }
}
