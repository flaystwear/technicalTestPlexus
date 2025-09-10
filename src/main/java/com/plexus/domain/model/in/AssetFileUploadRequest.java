package com.plexus.domain.model.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetFileUploadRequest {
    private String filename;
    private String encodedFile;
    private String contentType;
}
