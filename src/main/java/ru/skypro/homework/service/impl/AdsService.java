package ru.skypro.homework.service.impl;

import lombok.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdsService {
    private final UserService userService;
    private final AdRepository adRepository;
    private final ImageService imageService;

    public AdsService(UserService userService, AdRepository adRepository, ImageService imageService) {
        this.userService = userService;
        this.adRepository = adRepository;
        this.imageService = imageService;
    }

    @NonNull
    public String getAdAuthorName(Long id){
        return Objects.requireNonNull(adRepository.findById(id)
                .map(ad -> ad.getAuthor().getEmail()).orElseThrow(RuntimeException::new));
    }

    public Optional<Ad> getAdOptionalById(Long id){
        return adRepository.findById(id);
    }

    public AdsDto getAllAds(){
        return new AdsDto(adRepository.findAll());
    }

    public AdsDto getMyAds(String login){
        Optional<User> userOptional = userService.getUserByLogin(login);
        if(userOptional.isEmpty())
            return new AdsDto(new ArrayList<>());
        List<Ad> adsList = userOptional.get().getUserAds();
        return AdsMapper.adsToResponseWrapperAds(userOptional.get().getUserAds());
    }
    public AdDto createOrUpdateAd(String login, MultipartFile image, CreateOrUpdateAdDto createOrUpdateAdsDto) {
        Optional<User> userOptional = userService.getUserByLogin(login);
        if (userOptional.isEmpty())
            throw new RuntimeException("User not found!");

        //TODO Check if addUpdate;

        Ad newAd = adRepository.save(new Ad(userOptional.get(), createOrUpdateAdsDto));

        Image savedImage;
        try {
            savedImage = imageService.addAdImage(image, newAd.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newAd.setImage(savedImage);
        return AdsMapper.adToResponseAd(adRepository.save(newAd));
    }

    public Optional<ExtendedAdDto> getResponseFullAd(Long id){
        Optional<Ad> adOptional = adRepository.findById(id);

        return adOptional.map(AdsMapper::adToResponseFullAd);

    }


    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adsService.getAdAuthorName(#id)")
    public Optional<AdDto> updateAd(Long id, CreateOrUpdateAdDto updatedAd) {
        Optional<Ad> adOptional = adRepository.findById(id);
        return adOptional.map(ad -> AdsMapper.adToResponseAd(
                adRepository.save(
                        AdsMapper.createOrUpdateAdsToAd(ad, updatedAd)
                )
        ));
    }

    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adsService.getAdAuthorName(#id)")
    public Optional<String> updateAdImage(Long id, MultipartFile image) {
        //TODO What string to return(for now it's path);

        Optional<Ad> adOptional = adRepository.findById(id);
        if(adOptional.isEmpty())
            return Optional.empty();
        Image newImage;
        try {
            newImage = imageService.addAdImage(image, id);
            Ad updatedAd  = adOptional.get();
            updatedAd.setImage(newImage);
            adRepository.save(updatedAd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(newImage.getFileName());
    }

    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @adsService.getAdAuthorName(#id)")
    public Boolean deleteAdById(Long id) {

        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            return false;
        }
        adRepository.deleteById(id);
        return true;
    }
}