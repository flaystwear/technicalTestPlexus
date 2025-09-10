package com.plexus.infraestructure.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity 
@Table(name = "assets")
public class AssetEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String contentType;

    @Column
    private String url;

    @Column
    private Long size;

    @Column
    private OffsetDateTime uploadDate;

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


