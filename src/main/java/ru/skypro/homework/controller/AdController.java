package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.AdService;

import java.util.Optional;

@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdController {

    private final AdService adService;

    public AdController(AdService adsService) {
        this.adService = adsService;
    }

    @Operation(summary = "Get all ads", tags = "Ads",
          responses = {
                    @ApiResponse(
                          responseCode = "200", description = "OK",
                           content = {@Content(mediaType = "application/json",
                                   schema = @Schema(implementation = AdsDto.class))})
            }
    )
    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(summary = "Add ad", tags = "Ads",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Created",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestPart("image") MultipartFile image,
                                        @RequestPart("properties") CreateOrUpdateAdDto adProperties) {

        AdDto createdAd = adService.addAd(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                image,
                adProperties);

        return  new ResponseEntity<AdDto>(createdAd, HttpStatus.CREATED);
    }

    @Operation(summary = "Get ads", tags = "Ads",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExtendedAdDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(
                            responseCode = "404", description = "Not found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<ExtendedAdDto> getAds(@PathVariable("id") Integer id) {

        return adService.getAd(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Remove ad", tags = "Ads",
            responses = {
                    @ApiResponse(
                            responseCode = "204", description = "No Content"),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(
                            responseCode = "403", description = "Forbidden"),
                    @ApiResponse(
                            responseCode = "404", description = "Not found")
            }
    )
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer id) {

        if(adService.deleteAdById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update ads", tags = "Ads",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(
                            responseCode = "403", description = "Forbidden"),
                    @ApiResponse(
                            responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    public ResponseEntity<AdDto> updateAds(@PathVariable("id") Integer id,
                                           @RequestBody CreateOrUpdateAdDto updatedAd) {

        return adService.updateAd(id, updatedAd)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @Operation(summary = "Get ads me", tags = "Ads",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping("me")
    public ResponseEntity<AdsDto> getMyAds(){

        return ResponseEntity.ok(adService.getMyAds(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        ));
    }

    @Operation(summary = "Update image", tags = "Ads",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/octet-stream",
                                    array  = @ArraySchema(schema = @Schema(type = "String",format = "byte")))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(
                            responseCode = "403", description = "Forbidden"),
                    @ApiResponse(
                            responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping("{id}/image")
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    public ResponseEntity<?> updateAdImage(@PathVariable("id") Integer id,
                                           @RequestPart MultipartFile image) {

        Optional<String> responseStringOptional = adService.updateAdImage(id, image);
        if(responseStringOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(responseStringOptional.get());
    }
}