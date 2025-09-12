package com.plexus.infraestructure.persistance.repository.adapters;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.plexus.application.ports.out.AssetSearchPort;
import com.plexus.domain.model.dto.Asset;
import com.plexus.infraestructure.persistance.entity.AssetEntity;
import com.plexus.infraestructure.persistance.mapping.AssetEntityMapper;
import com.plexus.infraestructure.persistance.repository.AssetRepository;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetSearchImpl implements AssetSearchPort {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetEntityMapper mapper;


    @Override
    @Retry(name = "database-operations")
    public List<Asset> findByFilters(String filenameRegex, String filetype, OffsetDateTime uploadDateStart, OffsetDateTime uploadDateEnd, String sortDirection) {
        log.info("Starting database search with filters - filenameRegex: {}, filetype: {}, uploadDateStart: {}, uploadDateEnd: {}, sortDirection: {}", 
                filenameRegex, filetype, uploadDateStart, uploadDateEnd, sortDirection);
        
        Specification<AssetEntity> spec = Specification.where(null);

        if (filenameRegex != null && !filenameRegex.isBlank()) {
            spec = spec.and((root, cq, cb) -> cb.like(root.get("filename"), filenameRegex.replace(".*", "%")));
            log.debug("Added filename filter: {}", filenameRegex);
        }
        if (filetype != null && !filetype.isBlank()) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("contentType"), filetype));
            log.debug("Added filetype filter: {}", filetype);
        }
        if (uploadDateStart != null) {
            spec = spec.and((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("uploadDate"), uploadDateStart));
            log.debug("Added uploadDateStart filter: {}", uploadDateStart);
        }
        if (uploadDateEnd != null) {
            spec = spec.and((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("uploadDate"), uploadDateEnd));
            log.debug("Added uploadDateEnd filter: {}", uploadDateEnd);
        }

        var sort = Sort.by(
                (sortDirection != null && sortDirection.equalsIgnoreCase("DESC")) ? Sort.Direction.DESC : Sort.Direction.ASC,
                "uploadDate"
        );
        log.debug("Created sort specification: {}", sort);

        List<AssetEntity> assets= assetRepository.findAll(spec, sort);
        log.info("Retrieved {} entities from database", assets.size());
        log.debug("Raw entities from database: {}", assets);
        
        List<Asset> response=new ArrayList<>();
        if (!assets.isEmpty()){
           response= assets.stream().map(asset->mapper.mapAssetEntityToAsset(asset)).collect(Collectors.toList());
        }
        
        log.info("Mapped {} entities to DTOs", response.size());
        log.debug("Final mapped DTOs: {}", response);
        
        return response;
    }

    
}


