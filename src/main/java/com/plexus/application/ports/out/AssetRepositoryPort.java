package com.plexus.application.ports.out;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import com.plexus.domain.model.out.AssetConsultingResponse;

public interface AssetRepositoryPort {
    AssetConsultingResponse save(AssetConsultingResponse asset);
    Optional<AssetConsultingResponse> findById(String id);
    List<AssetConsultingResponse> findByFilters(String filenameRegex, String filetype, OffsetDateTime uploadDateStart, OffsetDateTime uploadDateEnd, String sortDirection);
}


