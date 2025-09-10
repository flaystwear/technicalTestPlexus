package com.plexus.infraestructure.asset.adapter;

import com.plexus.domain.asset.model.Asset;
import com.plexus.domain.asset.port.AssetRepositoryPort;
import com.plexus.infraestructure.asset.jpa.AssetEntity;
import com.plexus.infraestructure.asset.jpa.AssetJpaRepository;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AssetRepositoryAdapter implements AssetRepositoryPort {

    private final AssetJpaRepository jpaRepository;

    public AssetRepositoryAdapter(AssetJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Asset save(Asset asset) {
        AssetEntity entity = toEntity(asset);
        AssetEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Asset> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Asset> findByFilters(String filenameRegex, String filetype, OffsetDateTime uploadDateStart, OffsetDateTime uploadDateEnd, String sortDirection) {
        // Por simplicidad, usamos findAll y filtramos en memoria. En producción, construir Specification.
        return jpaRepository.findAll().stream()
                .filter(e -> filenameRegex == null || e.getFilename().matches(filenameRegex))
                .filter(e -> filetype == null || filetype.equalsIgnoreCase(e.getContentType()))
                .filter(e -> uploadDateStart == null || (e.getUploadDate() != null && !e.getUploadDate().isBefore(uploadDateStart)))
                .filter(e -> uploadDateEnd == null || (e.getUploadDate() != null && !e.getUploadDate().isAfter(uploadDateEnd)))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private AssetEntity toEntity(Asset asset) {
        AssetEntity e = new AssetEntity();
        e.setId(asset.getId());
        e.setFilename(asset.getFilename());
        e.setContentType(asset.getContentType());
        e.setUrl(asset.getUrl());
        e.setSize(asset.getSize());
        e.setUploadDate(asset.getUploadDate());
        return e;
    }

    private Asset toDomain(AssetEntity e) {
        Asset a = new Asset();
        a.setId(e.getId());
        a.setFilename(e.getFilename());
        a.setContentType(e.getContentType());
        a.setUrl(e.getUrl());
        a.setSize(e.getSize());
        a.setUploadDate(e.getUploadDate());
        return a;
    }
}


