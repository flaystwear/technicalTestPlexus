package com.plexus.application.useCase;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.in.AssetSearchService;
import com.plexus.application.ports.out.AssetSearchPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.domain.model.out.AssetSearchResponse;

@Service
public class AssetSearchServiceImpl implements AssetSearchService {

    @Autowired
    private AssetSearchPort assetSearchPort;



    public List<AssetSearchResponse> search(String uploadDateStart, String uploadDateEnd, String filename, String filetype, String sortDirection) {
        OffsetDateTime start = uploadDateStart != null ? OffsetDateTime.parse(uploadDateStart) : null;
        OffsetDateTime end = uploadDateEnd != null ? OffsetDateTime.parse(uploadDateEnd) : null;
        List<Asset> assets= assetSearchPort.findByFilters(filename, filetype, start, end, sortDirection);
        return null;
    }
    public Optional<AssetSearchResponse> searchById(String id) {
        return null;
    }

}
