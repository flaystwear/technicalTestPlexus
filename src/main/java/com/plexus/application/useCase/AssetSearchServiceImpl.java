package com.plexus.application.useCase;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.in.AssetSearchService;
import com.plexus.application.ports.out.AssetSearchPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.domain.model.out.AssetSearchResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetSearchServiceImpl implements AssetSearchService {

    @Autowired
    private AssetSearchPort assetSearchPort;





    public List<AssetSearchResponse> search(String uploadDateStart, String uploadDateEnd, String filename, String filetype, String sortDirection) {
        log.info("Processing asset search request in service layer");
        log.debug("Search parameters - uploadDateStart: {}, uploadDateEnd: {}, filename: {}, filetype: {}, sortDirection: {}", 
                uploadDateStart, uploadDateEnd, filename, filetype, sortDirection);
        
        OffsetDateTime start = uploadDateStart != null ? OffsetDateTime.parse(uploadDateStart) : null;
        OffsetDateTime end = uploadDateEnd != null ? OffsetDateTime.parse(uploadDateEnd) : null;
        
        log.debug("Parsed date parameters - start: {}, end: {}", start, end);
        
        List<Asset> assets = assetSearchPort.findByFilters(filename, filetype, start, end, sortDirection);
        log.info("Retrieved {} assets from repository", assets.size());
        log.debug("Raw assets from repository: {}", assets);
        
        List<AssetSearchResponse> response = buildResponse(assets);
        log.info("Built {} search responses", response.size());
        log.debug("Final search response DTOs: {}", response);
        
        return response;
    }
    private List<AssetSearchResponse> buildResponse(List<Asset> assets) {
        return assets.stream().map(asset->AssetSearchResponse.buildResponse(asset)).collect(Collectors.toList());
    }

}
