package com.plexus.application.asset.dto;

public class AssetDtos {
    public record AssetFileUploadRequest(String filename, String encodedFile, String contentType) {}
    public record AssetFileUploadResponse(String id) {}
    public record AssetResponse(String id, String filename, String contentType, String url, Long size, String uploadDate) {}
}


