package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.AdsService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdController {

    private final AdsService adsService;

    public AdController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAds(@RequestPart("image") MultipartFile image,
                                        @RequestPart("properties") CreateOrUpdateAdDto adProperties) {

        AdDto responseAd = adsService.createOrUpdateAd(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                image,
                adProperties);
        return  new ResponseEntity<>(responseAd,HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ExtendedAdDto> getFullAd(@PathVariable("id") Long id) {
        Optional<ExtendedAdDto> adOptional =  adsService.getResponseFullAd(id);
        return adOptional.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removeAd(@PathVariable("id") Long id) {
        if(adsService.deleteAdById(id))
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<AdDto> updateAds(@PathVariable("id") Long id,
                                           @RequestBody CreateOrUpdateAdDto updatedAd) {
        Optional<AdDto> responseAdOptional = adsService.updateAd(id, updatedAd);
        return responseAdOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("me")
    public ResponseEntity<AdsDto> getMyAds(){
        AdsDto responseWrapperAds =
                adsService.getMyAds(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                );
        return ResponseEntity.ok(responseWrapperAds);
    }

    @PatchMapping("{id}/image")
    public ResponseEntity<?> updateAdImage(@PathVariable("id") Long id,
                                           @RequestPart MultipartFile image) {
        Optional<String> responseStringOptional = adsService.updateAdImage(id,image);
        if(responseStringOptional.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(responseStringOptional.get());
    }


}