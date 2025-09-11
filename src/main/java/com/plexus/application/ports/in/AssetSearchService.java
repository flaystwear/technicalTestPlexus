package com.plexus.application.ports.in;

import java.util.List;
import java.util.Optional;

import com.plexus.domain.model.out.AssetSearchResponse;

public interface AssetSearchService {

    List<AssetSearchResponse> search(String uploadDateStart, String uploadDateEnd, String filename, String filetype, String sortDirection);
    Optional<AssetSearchResponse> searchById(String id); 
} 
