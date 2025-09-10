package com.plexus.application.asset;

import com.plexus.application.asset.dto.AssetDtos;
import com.plexus.domain.asset.model.Asset;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mgmt/1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("/actions/upload")
    public ResponseEntity<AssetDtos.AssetFileUploadResponse> upload(@RequestBody AssetDtos.AssetFileUploadRequest request) {
        byte[] bytes = request.encodedFile() != null ? java.util.Base64.getDecoder().decode(request.encodedFile()) : new byte[0];
        Asset asset = assetService.upload(request.filename(), request.contentType(), bytes);
        return new ResponseEntity<>(new AssetDtos.AssetFileUploadResponse(asset.getId()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    public ResponseEntity<List<AssetDtos.AssetResponse>> search(
            @RequestParam(required = false) String uploadDateStart,
            @RequestParam(required = false) String uploadDateEnd,
            @RequestParam(required = false) String filename,
            @RequestParam(required = false) String filetype,
            @RequestParam(required = false) String sortDirection
    ) {
        List<Asset> assets = assetService.search(uploadDateStart, uploadDateEnd, filename, filetype, sortDirection);
        List<AssetDtos.AssetResponse> response = assets.stream()
                .map(a -> new AssetDtos.AssetResponse(
                        a.getId(), a.getFilename(), a.getContentType(), a.getUrl(), a.getSize(),
                        a.getUploadDate() != null ? a.getUploadDate().toString() : null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}


