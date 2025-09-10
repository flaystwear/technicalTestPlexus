package com.plexus.infraestructure.persistance.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity 
@Table(name = "assets")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

   
}


