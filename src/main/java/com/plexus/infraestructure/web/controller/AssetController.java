package com.plexus.infraestructure.web.controller;

import java.util.HashSet;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plexus.application.asset.AssetService;
import com.plexus.domain.model.in.AssetFileUploadRequest;
import com.plexus.domain.model.out.AssetConsultingResponse;
import com.plexus.domain.model.out.AssetFileUploadResponse;

@RestController
@RequestMapping("/api/mgmt/1/assets")
@Tag(name = "asset")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("/actions/upload")
    @Operation(
            summary = "Performs an upload of the requested asset file.",
            description = "Performs an upload of the requested asset file. It communicates with the asset service to upload the file, but it ends without waiting for the file to be uploaded.",
            operationId = "uploadAssetFile",
            responses = {
                    @ApiResponse(responseCode = "202", description = "The operation was accepted by the backend.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = com.plexus.domain.model.out.AssetFileUploadResponse.class))),
                    @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
            }
    )
    public ResponseEntity<AssetFileUploadResponse> upload(@RequestBody AssetFileUploadRequest request) {
        byte[] bytes = request.getEncodedFile() != null ? java.util.Base64.getDecoder().decode(request.getEncodedFile()) : new byte[0];
        AssetConsultingResponse asset = assetService.upload(request.getFilename(), request.getContentType(), bytes);
        return new ResponseEntity<>(new AssetFileUploadResponse(asset.getId()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    @Operation(
            summary = "Allows searching (& filtering) all the uploaded/registered assets.",
            description = "Allows searching all the uploaded/registered assets by using all the given filters.",
            operationId = "getAssetsByFilter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns a list of assets matching the specified criteria.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = com.plexus.domain.model.out.AssetConsultingResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Malformed request."),
                    @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
            }
    )
    public ResponseEntity<HashSet<AssetConsultingResponse>> search(
            @Parameter(description = "The start date for the range.") @RequestParam(required = false) String uploadDateStart, 
            @Parameter(description = "The end date for the range.") @RequestParam(required = false) String uploadDateEnd,
            @Parameter(description = "The filename expression for file filtering (regex).") @RequestParam(required = false) String filename,
            @Parameter(description = "The file types for file filtering (one at a time).") @RequestParam(required = false) String filetype,
            @Parameter(description = "Sort direction") @RequestParam(required = false) String sortDirection
    ) {
        //Serching assets by filters
        List<AssetConsultingResponse> assets = assetService.search(uploadDateStart, uploadDateEnd, filename, filetype, sortDirection);
        //Returning assets as a set to avoid duplicates
        HashSet<AssetConsultingResponse> response = new HashSet<>(assets);
        return ResponseEntity.ok(response);
    }
}


