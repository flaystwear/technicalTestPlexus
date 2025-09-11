package com.plexus.application.ports.in;

import java.util.List;

import com.plexus.domain.model.out.AssetSearchResponse;

public interface AssetSearchService {

    List<AssetSearchResponse> search(String uploadDateStart, String uploadDateEnd, String filename, String filetype, String sortDirection); 
} 
