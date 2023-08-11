package ru.skypro.homework.controller;

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

    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestPart("image") MultipartFile image,
                                        @RequestPart("properties") CreateOrUpdateAdDto adProperties) {

        AdDto createdAd = adService.addAd(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                image,
                adProperties);

        return  new ResponseEntity<AdDto>(createdAd, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ExtendedAdDto> getAds(@PathVariable("id") Integer id) {

        return adService.getAd(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer id) {

        if(adService.deleteAdById(id))
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    public ResponseEntity<AdDto> updateAds(@PathVariable("id") Integer id,
                                           @RequestBody CreateOrUpdateAdDto updatedAd) {

        return adService.updateAd(id, updatedAd)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("me")
    public ResponseEntity<AdsDto> getMyAds(){

        return ResponseEntity.ok(adService.getMyAds(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        ));
    }

    @PatchMapping("{id}/image")
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    public ResponseEntity<?> updateAdImage(@PathVariable("id") Integer id,
                                           @RequestPart MultipartFile image) {

        Optional<String> responseStringOptional = adService.updateAdImage(id, image);
        if(responseStringOptional.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(responseStringOptional.get());
    }
}