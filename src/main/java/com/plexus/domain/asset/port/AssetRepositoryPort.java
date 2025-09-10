package com.plexus.domain.asset.port;

import com.plexus.domain.asset.model.Asset;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AssetRepositoryPort {
    Asset save(Asset asset);
    Optional<Asset> findById(String id);
    List<Asset> findByFilters(String filenameRegex, String filetype, OffsetDateTime uploadDateStart, OffsetDateTime uploadDateEnd, String sortDirection);
}


