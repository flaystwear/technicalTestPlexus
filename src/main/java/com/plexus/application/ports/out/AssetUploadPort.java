package com.plexus.application.ports.out;


import com.plexus.domain.model.dto.Asset;


public interface AssetUploadPort {
    Asset upload(String filename, String contentType, byte[] encodedFile);
}
