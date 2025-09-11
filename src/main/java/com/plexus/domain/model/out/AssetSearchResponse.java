package com.plexus.domain.model.out;


import com.plexus.domain.model.dto.Asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetSearchResponse {
    private String id;
    private String filename;
    private String contentType;
    private String url;
    private Long size;
    private String uploadDate;

    public static AssetSearchResponse buildResponse(Asset asset){
        AssetSearchResponse response = new AssetSearchResponse();
        response.id = asset.getId();
        response.filename = asset.getFilename();
        response.contentType = asset.getContentType();
        response.url = asset.getUrl();
        response.size = asset.getSize();
        response.uploadDate = asset.getUploadDate();
        return response;
    }

}


