package com.plexus.infraestructure.persistance.repository.adapters;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.out.AssetUploadPort;
import com.plexus.application.ports.out.FileStorageUploader;
import com.plexus.domain.model.dto.Asset;
import com.plexus.infraestructure.persistance.entity.AssetEntity;
import com.plexus.infraestructure.persistance.mapping.AssetEntityMapper;
import com.plexus.infraestructure.persistance.repository.AssetRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetUploadFileImpl implements AssetUploadPort{
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetEntityMapper assetEntityMapper;
    @Autowired
    private FileStorageUploader storageClient;
    
    public  Asset upload(String filename, String contentType, byte[] encodedFile){
        long size = encodedFile != null ? encodedFile.length : 0L;
        AssetEntity newAsset= AssetEntity.builder()
        .contentType(contentType)
        .filename(filename)
        .size(size)
        .url(null)
        .uploadDate(OffsetDateTime.now())
        .build();
        AssetEntity saved = assetRepository.save(newAsset);
        
        log.info("Asset created and saved to database: {}", saved);
        
        // Launch asynchronous upload and, when finished, update URL in DB
        storageClient.uploadAsync(saved.getId(), encodedFile)
                .thenAccept(url -> {
                    saved.setUrl(url);
                    AssetEntity updatedAsset = assetRepository.save(saved);
                    log.info("Asset updated with URL: {}", updatedAsset);
                });
        
        // Respond immediately with current data; URL will be filled later
        Asset result = assetEntityMapper.mapAssetEntityToAsset(saved);
        log.info("Asset mapped to DTO: {}", result);
        return result;
    }
}
