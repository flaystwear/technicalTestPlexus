package com.plexus.infraestructure.storage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.plexus.application.ports.out.FileStorageUploader;

import java.util.concurrent.CompletableFuture;

@Component
public class FakeAwsStorageClient implements FileStorageUploader {

    @Async("asyncExecutor")
    public CompletableFuture<String> uploadAsync(String assetId, byte[] content) {
        try {
            Thread.sleep(1500); // simula latencia de red/subida
        } catch (InterruptedException ignored) {}
        String url = "https://fake-aws.example.com/assets/" + assetId;
        return CompletableFuture.completedFuture(url);
    }
}


