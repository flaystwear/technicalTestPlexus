package com.plexus.domain.asset.model;

import java.time.OffsetDateTime;

public class Asset {
    private String id;
    private String filename;
    private String contentType;
    private String url;
    private Long size;
    private OffsetDateTime uploadDate;

    public Asset(String id, String filename, String contentType, String url, Long size, OffsetDateTime uploadDate) {
        this.id = id;
        this.filename = filename;
        this.contentType = contentType;
        this.url = url;
        this.size = size;
        this.uploadDate = uploadDate;
    }

    public Asset() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }
    public OffsetDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(OffsetDateTime uploadDate) { this.uploadDate = uploadDate; }
}


