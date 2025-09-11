package com.plexus.application.useCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.in.AssetUploadService;
import com.plexus.application.ports.out.AssetUploadPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.domain.model.out.AssetFileUploadResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetUploadServiceImpl implements AssetUploadService {

    @Autowired
    private AssetUploadPort assetUploadPort;


    public AssetFileUploadResponse upload(String filename, String contentType, byte[] encodedFile) {
        log.info("Processing asset upload in service layer for filename: {}, contentType: {}", filename, contentType);
        log.debug("Upload file size: {} bytes", encodedFile != null ? encodedFile.length : 0);
        
        // Here we could store the file and build the URL; for now, we simulate
        Asset asset = assetUploadPort.upload(filename, contentType, encodedFile);
        
        log.info("Asset created successfully: {}", asset);
        log.debug("Upload result DTO: {}", asset);
        
        return new AssetFileUploadResponse(asset.getId());
    }

    
}


