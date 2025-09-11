package com.plexus.infraestructure.persistance.repository.adapters;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.out.AssetSearchPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.domain.model.out.AssetSearchResponse;
import com.plexus.infraestructure.persistance.entity.AssetEntity;
import com.plexus.infraestructure.persistance.repository.AssetRepository;

@Service
public class AssetSearchImpl implements AssetSearchPort {

    @Autowired
    private AssetRepository assetRepository;


    // @Override
    // public AssetSearchResponse save(AssetSearchResponse asset) {
    //     AssetEntity entity = toEntity(asset);
    //     AssetEntity saved = jpaRepository.save(entity);
    //     return toDomain(saved);
    // }

    @Override
    public Optional<Asset> findById(String id) {
        Optional <AssetEntity> asset =assetRepository.findById(id);
        return null;
    }

    @Override
    public List<Asset> findByFilters(String filenameRegex, String filetype, OffsetDateTime uploadDateStart, OffsetDateTime uploadDateEnd, String sortDirection) {
        // Por simplicidad, usamos findAll y filtramos en memoria. En producción, construir Specification.
    //    List<AssetEntity> jpaRepository.findAll().stream()
    //             .filter(e -> filenameRegex == null || e.getFilename().matches(filenameRegex))
    //             .filter(e -> filetype == null || filetype.equalsIgnoreCase(e.getContentType()))
    //             .filter(e -> uploadDateStart == null || (e.getUploadDate() != null && !e.getUploadDate().isBefore(uploadDateStart)))
    //             .filter(e -> uploadDateEnd == null || (e.getUploadDate() != null && !e.getUploadDate().isAfter(uploadDateEnd)))
    //             .map(this::toDomain)
    //             .collect(Collectors.toList());
        return null;
    }

    private AssetEntity toEntity(AssetSearchResponse asset) {
        AssetEntity e = new AssetEntity();
        e.setId(asset.getId());
        e.setFilename(asset.getFilename());
        e.setContentType(asset.getContentType());
        e.setUrl(asset.getUrl());
        e.setSize(asset.getSize());
        e.setUploadDate(asset.getUploadDate() != null ? OffsetDateTime.parse(asset.getUploadDate()) : null);
        return e;
    }

    private AssetSearchResponse toDomain(AssetEntity e) {
        AssetSearchResponse a = new AssetSearchResponse();
        a.setId(e.getId());
        a.setFilename(e.getFilename());
        a.setContentType(e.getContentType());
        a.setUrl(e.getUrl());
        a.setSize(e.getSize());
        a.setUploadDate(e.getUploadDate().toString());
        return a;
    }
}


