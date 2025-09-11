package com.plexus.infraestructure.persistance.repository.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.out.AssetUploadPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.infraestructure.persistance.repository.AssetRepository;

@Service
public class AssetUploadFileImpl implements AssetUploadPort{
    @Autowired
    private AssetRepository assetRepository;
    
    public  Asset upload(String filename, String contentType, byte[] encodedFile){
        return null;
    }
}
