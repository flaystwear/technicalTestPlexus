package com.plexus.application.useCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.in.AssetUploadService;
import com.plexus.application.ports.out.AssetUploadPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.domain.model.out.AssetFileUploadResponse;

@Service
public class AssetUploadServiceImpl implements AssetUploadService {

    @Autowired
    private AssetUploadPort assetUploadPort;


    public AssetFileUploadResponse upload(String filename, String contentType, byte[] encodedFile) {
        // Aquí podríamos almacenar el archivo y construir la URL; por ahora, simulamos
        Asset asset= assetUploadPort.upload(filename, contentType, encodedFile);
        return null;
    }

    
}


