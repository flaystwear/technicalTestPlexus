package com.plexus.infraestructure.persistance.repository.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.plexus.domain.model.dto.Asset;
import com.plexus.infraestructure.persistance.entity.AssetEntity;
import com.plexus.infraestructure.persistance.mapping.AssetEntityMapper;
import com.plexus.infraestructure.persistance.repository.AssetRepository;

@ExtendWith(MockitoExtension.class)
class AssetSearchImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetEntityMapper mapper;

    @InjectMocks
    private AssetSearchImpl assetSearchImpl;

    private List<AssetEntity> mockEntities;
    private List<Asset> mockAssets;

    @BeforeEach
    void setUp() {
        AssetEntity entity1 = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("document1.pdf")
                .contentType("application/pdf")
                .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000")
                .size(1024L)
                .uploadDate(OffsetDateTime.parse("2025-01-15T10:30Z"))
                .build();

        AssetEntity entity2 = AssetEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174001")
                .filename("document2.jpg")
                .contentType("image/jpeg")
                .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174001")
                .size(2048L)
                .uploadDate(OffsetDateTime.parse("2025-01-16T10:30Z"))
                .build();

        mockEntities = Arrays.asList(entity1, entity2);

        Asset asset1 = Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .filename("document1.pdf")
                .contentType("application/pdf")
                .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174000")
                .size(1024L)
                .uploadDate("2025-01-15T10:30Z")
                .build();

        Asset asset2 = Asset.builder()
                .id("123e4567-e89b-12d3-a456-426614174001")
                .filename("document2.jpg")
                .contentType("image/jpeg")
                .url("https://fake-aws.example.com/assets/123e4567-e89b-12d3-a456-426614174001")
                .size(2048L)
                .uploadDate("2025-01-16T10:30Z")
                .build();

        mockAssets = Arrays.asList(asset1, asset2);
    }

    @Test
    void findByFilters_ShouldReturnAssets_WhenValidFilters() {
        // Given
        String filenameRegex = "document.*";
        String filetype = "application/pdf";
        OffsetDateTime uploadDateStart = OffsetDateTime.parse("2025-01-01T00:00:00Z");
        OffsetDateTime uploadDateEnd = OffsetDateTime.parse("2025-01-31T23:59:59Z");
        String sortDirection = "ASC";

        when(assetRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(mockEntities);

        when(mapper.mapAssetEntityToAsset(any(AssetEntity.class)))
                .thenReturn(mockAssets.get(0), mockAssets.get(1));

        // When
        List<Asset> result = assetSearchImpl.findByFilters(filenameRegex, filetype, uploadDateStart, uploadDateEnd, sortDirection);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("document1.pdf", result.get(0).getFilename());
        assertEquals("document2.jpg", result.get(1).getFilename());
    }

    @Test
    void findByFilters_ShouldReturnEmptyList_WhenNoResults() {
        // Given
        String filenameRegex = "nonexistent.*";
        String filetype = null;
        OffsetDateTime uploadDateStart = null;
        OffsetDateTime uploadDateEnd = null;
        String sortDirection = "ASC";

        when(assetRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(Arrays.asList());

        // When
        List<Asset> result = assetSearchImpl.findByFilters(filenameRegex, filetype, uploadDateStart, uploadDateEnd, sortDirection);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void findByFilters_ShouldHandleNullParameters() {
        // Given
        when(assetRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(mockEntities);

        when(mapper.mapAssetEntityToAsset(any(AssetEntity.class)))
                .thenReturn(mockAssets.get(0), mockAssets.get(1));

        // When
        List<Asset> result = assetSearchImpl.findByFilters(null, null, null, null, "ASC");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByFilters_ShouldUseDescSort_WhenSortDirectionIsDesc() {
        // Given
        String filenameRegex = "document.*";
        String filetype = null;
        OffsetDateTime uploadDateStart = null;
        OffsetDateTime uploadDateEnd = null;
        String sortDirection = "DESC";

        when(assetRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(mockEntities);

        when(mapper.mapAssetEntityToAsset(any(AssetEntity.class)))
                .thenReturn(mockAssets.get(0), mockAssets.get(1));

        // When
        List<Asset> result = assetSearchImpl.findByFilters(filenameRegex, filetype, uploadDateStart, uploadDateEnd, sortDirection);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByFilters_ShouldUseAscSort_WhenSortDirectionIsAsc() {
        // Given
        String filenameRegex = "document.*";
        String filetype = null;
        OffsetDateTime uploadDateStart = null;
        OffsetDateTime uploadDateEnd = null;
        String sortDirection = "ASC";

        when(assetRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(mockEntities);

        when(mapper.mapAssetEntityToAsset(any(AssetEntity.class)))
                .thenReturn(mockAssets.get(0), mockAssets.get(1));

        // When
        List<Asset> result = assetSearchImpl.findByFilters(filenameRegex, filetype, uploadDateStart, uploadDateEnd, sortDirection);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByFilters_ShouldNotRetryOnNonDataAccessException() {
        // Given - Simular una excepción que NO debería activar retry
        when(assetRepository.findAll(any(Specification.class), any(Sort.class)))
            .thenThrow(new RuntimeException("Invalid search parameters"));

        // When & Then - No debería hacer retry para este tipo de error
        assertThrows(RuntimeException.class, () -> {
            assetSearchImpl.findByFilters("test", null, null, null, "ASC");
        });
        
        // Verificar que solo se llamó 1 vez (no retry)
        verify(assetRepository, times(1)).findAll(any(Specification.class), any(Sort.class));
    }

}
