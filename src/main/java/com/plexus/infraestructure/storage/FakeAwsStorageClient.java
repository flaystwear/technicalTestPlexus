package com.plexus.infraestructure.storage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.plexus.application.ports.out.FileStorageUploader;
import com.plexus.domain.exception.FileStorageException;

import java.util.concurrent.CompletableFuture;

@Component
public class FakeAwsStorageClient implements FileStorageUploader {

    @Async("asyncExecutor")
    public CompletableFuture<String> uploadAsync(String assetId, byte[] content) {
        try {
            // Simulate network latency/upload
            Thread.sleep(1500);
            
            // Simulate occasional failures (10% chance)
            if (Math.random() < 0.1) {
                throw new FileStorageException("upload", assetId, "Network timeout");
            }
            
            String url = "https://fake-aws.example.com/assets/" + assetId;
            return CompletableFuture.completedFuture(url);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FileStorageException("upload", assetId, "Upload interrupted");
        } catch (FileStorageException e) {
            throw e;
        } catch (Exception e) {
            throw new FileStorageException("upload", assetId, e.getMessage());
        }
    }
}


