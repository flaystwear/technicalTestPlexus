package com.plexus.domain.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asset {
    private String id;
    private String filename;
    private String contentType;
    private String url;
    private Long size;
    private String uploadDate;
}



   



