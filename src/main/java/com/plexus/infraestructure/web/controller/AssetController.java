package com.plexus.infraestructure.web.controller;

import java.util.LinkedHashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plexus.application.ports.in.AssetSearchService;
import com.plexus.application.ports.in.AssetUploadService;
import com.plexus.domain.exception.InvalidSearchParametersException;
import com.plexus.domain.model.in.AssetFileUploadRequest;
import com.plexus.domain.model.out.AssetFileUploadResponse;
import com.plexus.domain.model.out.AssetSearchResponse;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequestMapping("/api/mgmt/1/assets")
@Tag(name = "asset")
public class AssetController {

    @Autowired
    private AssetSearchService assetSearchService;

    @Autowired
    private AssetUploadService assetUploadService;

    @PostMapping("/actions/upload")
    @Operation(
            summary = "Performs an upload of the requested asset file.",
            description = "Performs an upload of the requested asset file. It communicates with the asset service to upload the file, but it ends without waiting for the file to be uploaded.",
            operationId = "uploadAssetFile",
            responses = {
                    @ApiResponse(responseCode = "202", description = "The operation was accepted by the backend.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = com.plexus.domain.model.out.AssetFileUploadResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "An unexpected error occurred.", content = @Content())
            }
    )
    public Mono<ResponseEntity<AssetFileUploadResponse>> upload(@RequestBody AssetFileUploadRequest request) {
        log.info("Received upload request: {}", request);
        log.info("Starting asset upload process for filename: {}, contentType: {}", request.getFilename(), request.getContentType());
        
        // Validate required fields
        if (request.getFilename() == null || request.getFilename().isBlank()) {
            log.warn("Upload request rejected: filename is required");
            throw new IllegalArgumentException("Filename is required");
        }
        
        if (request.getContentType() == null || request.getContentType().isBlank()) {
            log.warn("Upload request rejected: contentType is required");
            throw new IllegalArgumentException("ContentType is required");
        }
        
        if (request.getEncodedFile() == null || request.getEncodedFile().isBlank()) {
            log.warn("Upload request rejected: encodedFile is required");
            throw new IllegalArgumentException("EncodedFile is required");
        }
        
        // Decode the file for storage
        byte[] bytes = java.util.Base64.getDecoder().decode(request.getEncodedFile());
        log.debug("Decoded file size: {} bytes", bytes.length);
        
        // WebFlux call using threads
        return Mono.fromCallable(() -> {
                    log.info("Processing upload in background thread");
                    return assetUploadService.upload(request.getFilename(), request.getContentType(), bytes);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> {
                    log.info("Upload completed successfully for asset ID: {}", response.getId());
                    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
                });
    }

    @GetMapping("/")
    @RateLimiter(name = "search-endpoint")
    @Operation(
            summary = "Allows searching (& filtering) all the uploaded/registered assets.",
            description = "Allows searching all the uploaded/registered assets by using all the given filters.",
            operationId = "getAssetsByFilter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns a list of assets matching the specified criteria.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = com.plexus.domain.model.out.AssetSearchResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "An unexpected error occurred.", content = @Content())
            }
    )
    public Mono<ResponseEntity<LinkedHashSet<AssetSearchResponse>>> search(
            @Parameter(description = "The start date for the range.") @RequestParam(required = false) String uploadDateStart, 
            @Parameter(description = "The end date for the range.") @RequestParam(required = false) String uploadDateEnd,
            @Parameter(description = "The filename expression for file filtering (regex).") @RequestParam(required = false) String filename,
            @Parameter(description = "The file types for file filtering (one at a time).") @RequestParam(required = false) String filetype,
            @Parameter(description = "Sort direction") @RequestParam(required = false) String sortDirection
    ) {
        log.info("Received search request - uploadDateStart: {}, uploadDateEnd: {}, filename: {}, filetype: {}, sortDirection: {}", 
                uploadDateStart, uploadDateEnd, filename, filetype, sortDirection);
        
        boolean noFilters = (uploadDateStart == null || uploadDateStart.isBlank())
                && (uploadDateEnd == null || uploadDateEnd.isBlank())
                && (filename == null || filename.isBlank())
                && (filetype == null || filetype.isBlank());
        
        if (noFilters) {
            log.warn("Search request rejected: no filters provided");
            throw new InvalidSearchParametersException();
        }
        
        if (sortDirection == null || sortDirection.isBlank()) {
            log.warn("Search request rejected: sortDirection is required");
            throw new InvalidSearchParametersException();
        }
        
        // Searching assets by filters
        return Mono.fromCallable(() -> {
                    log.info("Processing search in background thread");
                    return assetSearchService.search(uploadDateStart, uploadDateEnd, filename, filetype, sortDirection);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(list -> {
                    log.info("Search completed successfully. Found {} assets", list.size());
                    log.debug("Search results: {}", list);
                    return new LinkedHashSet<>(list);
                })
                .map(ResponseEntity::ok);
    }

}


