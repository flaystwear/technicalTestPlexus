package com.plexus.application.ports.out;

import java.util.concurrent.CompletableFuture;

public interface FileStorageUploader {
    CompletableFuture<String> uploadAsync(String assetId, byte[] content);
}
