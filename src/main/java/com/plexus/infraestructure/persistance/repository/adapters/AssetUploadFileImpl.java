package com.plexus.infraestructure.persistance.repository.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.out.AssetUploadPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.infraestructure.persistance.entity.AssetEntity;
import com.plexus.infraestructure.persistance.mapping.AssetEntityMapper;
import com.plexus.infraestructure.persistance.repository.AssetRepository;

import java.time.OffsetDateTime;

@Service
public class AssetUploadFileImpl implements AssetUploadPort{
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetEntityMapper assetEntityMapper;
    
    public  Asset upload(String filename, String contentType, byte[] encodedFile){
        long size = encodedFile != null ? encodedFile.length : 0L;
        AssetEntity newAsset= AssetEntity.builder()
        .contentType(contentType)
        .filename(filename)
        .size(size)
        .url(null)
        .uploadDate(OffsetDateTime.now())
        .build();
        assetRepository.save(newAsset);
        return assetEntityMapper.mapAssetEntityToAsset(newAsset);
    }
}
