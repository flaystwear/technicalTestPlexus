package com.plexus.infraestructure.persistance.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.plexus.domain.model.dto.Asset;
import com.plexus.infraestructure.persistance.entity.AssetEntity;

class AssetEntityMapperTest {

    private AssetEntityMapper mapper;
    private AssetEntity mockEntity;

    @BeforeEach
    void setUp() {
        mapper = new AssetEntityMapper();
        
        mockEntity = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000")
                .size(1024L)
                .uploadDate(OffsetDateTime.parse("2025-01-15T10:30:00Z"))
                .build();
    }

    @Test
    void mapAssetEntityToAsset_ShouldMapAllFields() {
        // When
        Asset result = mapper.mapAssetEntityToAsset(mockEntity);

        // Then
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
        assertEquals("test.pdf", result.getFilename());
        assertEquals("application/pdf", result.getContentType());
        assertEquals("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000", result.getUrl());
        assertEquals(1024L, result.getSize());
        assertEquals("2025-01-15T10:30Z", result.getUploadDate());
    }

    @Test
    void mapAssetEntityToAsset_ShouldHandleNullUrl() {
        // Given
        AssetEntity entityWithNullUrl = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url(null)
                .size(1024L)
                .uploadDate(OffsetDateTime.parse("2025-01-15T10:30:00Z"))
                .build();

        // When
        Asset result = mapper.mapAssetEntityToAsset(entityWithNullUrl);

        // Then
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
        assertEquals("test.pdf", result.getFilename());
        assertEquals("application/pdf", result.getContentType());
        assertEquals(null, result.getUrl());
        assertEquals(1024L, result.getSize());
        // assertEquals("2025-01-15T10:30:00Z", result.getUploadDate());
    }

    @Test
    void mapAssetEntityToAsset_ShouldHandleZeroSize() {
        // Given
        AssetEntity entityWithZeroSize = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000")
                .size(0L)
                .uploadDate(OffsetDateTime.parse("2025-01-15T10:30:00Z"))
                .build();

        // When
        Asset result = mapper.mapAssetEntityToAsset(entityWithZeroSize);

        // Then
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
        assertEquals("test.pdf", result.getFilename());
        assertEquals("application/pdf", result.getContentType());
        assertEquals("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000", result.getUrl());
        assertEquals(0L, result.getSize());
        assertEquals("2025-01-15T10:30Z", result.getUploadDate());
    }

    @Test
    void mapAssetEntityToAsset_ShouldConvertDateToString() {
        // Given
        OffsetDateTime specificDate = OffsetDateTime.parse("2025-01-15T10:30:45Z");
        AssetEntity entityWithSpecificDate = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("test.pdf")
                .contentType("application/pdf")
                .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000")
                .size(1024L)
                .uploadDate(specificDate)
                .build();

        // When
        Asset result = mapper.mapAssetEntityToAsset(entityWithSpecificDate);

        // Then
        assertNotNull(result);
        assertEquals("2025-01-15T10:30:45Z", result.getUploadDate());
    }
}
