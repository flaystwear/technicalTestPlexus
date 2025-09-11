package com.plexus.application.ports.in;

import com.plexus.domain.model.out.AssetFileUploadResponse;

public interface AssetUploadService {
    AssetFileUploadResponse upload(String filename, String contentType, byte[] encodedFile);
}
