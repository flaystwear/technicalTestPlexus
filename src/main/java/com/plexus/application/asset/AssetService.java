package com.plexus.application.asset;

import com.plexus.domain.asset.model.Asset;
import com.plexus.domain.asset.port.AssetRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AssetService {

    private final AssetRepositoryPort assetRepository;

    public AssetService(AssetRepositoryPort assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset upload(String filename, String contentType, byte[] encodedFile) {
        // Aquí podríamos almacenar el archivo y construir la URL; por ahora, simulamos
        Asset asset = new Asset();
        asset.setId(java.util.UUID.randomUUID().toString());
        asset.setFilename(filename);
        asset.setContentType(contentType);
        asset.setUrl("/assets/" + asset.getId());
        asset.setSize((long) (encodedFile != null ? encodedFile.length : 0));
        asset.setUploadDate(OffsetDateTime.now());
        return assetRepository.save(asset);
    }

    public List<Asset> search(String uploadDateStart, String uploadDateEnd, String filename, String filetype, String sortDirection) {
        OffsetDateTime start = uploadDateStart != null ? OffsetDateTime.parse(uploadDateStart) : null;
        OffsetDateTime end = uploadDateEnd != null ? OffsetDateTime.parse(uploadDateEnd) : null;
        return assetRepository.findByFilters(filename, filetype, start, end, sortDirection);
    }
}


