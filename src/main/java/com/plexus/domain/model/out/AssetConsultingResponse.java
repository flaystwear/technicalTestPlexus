package com.plexus.domain.model.out;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetConsultingResponse {
    private String id;
    private String filename;
    private String contentType;
    private String url;
    private Long size;
    private String uploadDate;

}


