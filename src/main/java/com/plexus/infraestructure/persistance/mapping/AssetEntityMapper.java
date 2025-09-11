package com.plexus.infraestructure.persistance.mapping;

import org.springframework.stereotype.Service;

import com.plexus.domain.model.dto.Asset;
import com.plexus.infraestructure.persistance.entity.AssetEntity;

@Service
public class AssetEntityMapper {

    public Asset mapAssetEntityToAsset(AssetEntity entity){
        return Asset.builder()
            .contentType(entity.getContentType())
            .filename(entity.getFilename())
            .id(entity.getId())
            .size(entity.getSize())
            .uploadDate(entity.getUploadDate().toString())
            .url(entity.getUrl())
            .build();
    }
}
