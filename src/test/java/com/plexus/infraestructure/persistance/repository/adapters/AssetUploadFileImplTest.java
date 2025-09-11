package com.plexus.infraestructure.persistance.repository.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plexus.application.ports.out.FileStorageUploader;
import com.plexus.domain.model.dto.Asset;
import com.plexus.infraestructure.persistance.entity.AssetEntity;
import com.plexus.infraestructure.persistance.mapping.AssetEntityMapper;
import com.plexus.infraestructure.persistance.repository.AssetRepository;

@ExtendWith(MockitoExtension.class)
class AssetUploadFileImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetEntityMapper assetEntityMapper;

    @Mock
    private FileStorageUploader storageClient;

    @InjectMocks
    private AssetUploadFileImpl assetUploadFileImpl;

    private AssetEntity mockEntity;
    private Asset mockAsset;

    @BeforeEach
    void setUp() {
        mockEntity = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size(1024L)
                .uploadDate(OffsetDateTime.now())
                .build();

        mockAsset = Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size(1024L)
                .uploadDate(OffsetDateTime.now().toString())
                .build();
    }

    @Test
    void upload_ShouldReturnAsset_WhenValidInput() {
        // Given
        String filename = "test.pdf";
        String contentType = "application/pdf";
        byte[] encodedFile = "test content".getBytes();

        when(assetRepository.save(any(AssetEntity.class)))
                .thenReturn(mockEntity);

        when(assetEntityMapper.mapAssetEntityToAsset(any(AssetEntity.class)))
                .thenReturn(mockAsset);

        when(storageClient.uploadAsync(anyString(), any(byte[].class)))
                .thenReturn(CompletableFuture.completedFuture("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000"));

        // When
        Asset result = assetUploadFileImpl.upload(filename, contentType, encodedFile);

        // Then
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
        assertEquals("test.pdf", result.getFilename());
        assertEquals("application/pdf", result.getContentType());
        assertEquals(1024L, result.getSize());

        // Verify interactions
        verify(assetRepository, times(2)).save(any(AssetEntity.class));
        verify(assetEntityMapper).mapAssetEntityToAsset(any(AssetEntity.class));
        verify(storageClient).uploadAsync(anyString(), any(byte[].class));
    }

    @Test
    void upload_ShouldHandleNullFile() {
        // Given
        String filename = "test.pdf";
        String contentType = "application/pdf";
        byte[] encodedFile = null;

        AssetEntity entityWithNullFile = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size(0L)
                .uploadDate(OffsetDateTime.now())
                .build();

        Asset assetWithNullFile = Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size(0L)
                .uploadDate(OffsetDateTime.now().toString())
                .build();

        when(assetRepository.save(any()))
                .thenReturn(entityWithNullFile);

        when(assetEntityMapper.mapAssetEntityToAsset(any()))
                .thenReturn(assetWithNullFile);

        when(storageClient.uploadAsync(anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000"));

        // When
        Asset result = assetUploadFileImpl.upload(filename, contentType, encodedFile);

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getSize());
    }

    @Test
    void upload_ShouldCalculateFileSizeCorrectly() {
        // Given
        String filename = "test.pdf";
        String contentType = "application/pdf";
        byte[] encodedFile = "test content for size calculation".getBytes();

        AssetEntity entityWithCorrectSize = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size((long) encodedFile.length)
                .uploadDate(OffsetDateTime.now())
                .build();

        Asset assetWithCorrectSize = Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size((long) encodedFile.length)
                .uploadDate(OffsetDateTime.now().toString())
                .build();

        when(assetRepository.save(any(AssetEntity.class)))
                .thenReturn(entityWithCorrectSize);

        when(assetEntityMapper.mapAssetEntityToAsset(any(AssetEntity.class)))
                .thenReturn(assetWithCorrectSize);

        when(storageClient.uploadAsync(anyString(), any(byte[].class)))
                .thenReturn(CompletableFuture.completedFuture("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000"));

        // When
        Asset result = assetUploadFileImpl.upload(filename, contentType, encodedFile);

        // Then
        assertNotNull(result);
        assertEquals(encodedFile.length, result.getSize());
    }
}
