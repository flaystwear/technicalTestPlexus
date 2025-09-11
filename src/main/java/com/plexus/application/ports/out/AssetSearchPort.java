package com.plexus.application.ports.out;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import com.plexus.domain.model.dto.Asset;

public interface AssetSearchPort {
    Optional<Asset> findById(String id);
    List<Asset> findByFilters(String filenameRegex, String filetype, OffsetDateTime uploadDateStart, OffsetDateTime uploadDateEnd, String sortDirection);
}


