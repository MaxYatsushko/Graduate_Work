package ru.skypro.homework.service.impl;

import lombok.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.exceptions.UserException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdService {

    private final UserService userService;
    private final AdRepository adRepository;
    private final ImageService imageService;

    public AdService(UserService userService, AdRepository adRepository, ImageService imageService) {
        this.userService = userService;
        this.adRepository = adRepository;
        this.imageService = imageService;
    }

    /**
     * gets ad by id from db
     * @return Optional'<'Ad'>' - got ad if exists
     */
    public Optional<Ad> getAdOptionalById(Integer id){
        return adRepository.findById(id);
    }

    /**
     * gets AdsDto from list of ads from db
     * @return AdsDto - created AdsDto
     */
    public AdsDto getAllAds(){
        return new AdsDto(adRepository.findAll());
    }

    /**
     * gets users' ads by login
     * @param login - string
     * @return adsDto - dto with ads
     */
    public AdsDto getMyAds(String login){

        Optional<User> userOptional = userService.getUserByLogin(login);
        if(userOptional.isEmpty())
            return new AdsDto(new ArrayList<>());

        return AdsMapper.adsToAdsDto(userOptional.get().getUserAds());
    }

    /**
     * creates ad and image in db. after creates AdDto as a response
     * @param login - string
     * @param image - MultipartFile
     * @param createOrUpdateAdDto - CreateOrUpdateAdDto
     * @return Optional'<'ExtendedAdDto'>' - extendedAdDto if exist
     * @throws UserException
     */
    public AdDto addAd(String login, MultipartFile image, CreateOrUpdateAdDto createOrUpdateAdDto) {

        Optional<User> userOptional = userService.getUserByLogin(login);
        if (userOptional.isEmpty())
            throw new UserException("User not found!");

        Ad newAd = adRepository.save(new Ad(userOptional.get(), createOrUpdateAdDto));

        Image savedImage;
        try {
            savedImage = imageService.addAdImage(image, newAd.getId());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        newAd.setImage(savedImage);
        return AdsMapper.adToAdDto(adRepository.save(newAd));
    }

    /**
     * gets ad from db via id of ad
     * @param id
     * @return Optional'<'ExtendedAdDto'>' - extendedAdDto if exist ad by id
     */
    public Optional<ExtendedAdDto> getAd(Integer id){

        Optional<Ad> adOptional = adRepository.findById(id);
        return adOptional.map(AdsMapper::adToExtendedAdDto);
    }

    /**
     * updates ad via createOrUpdateAdDto in db
     * @param id - id of ad
     * @param createOrUpdateAdDto - dto
     * @return dd - updated ad if exists
     */
    public Optional<AdDto> updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto) {

        Optional<Ad> adOptional = adRepository.findById(id);
        return adOptional.map(ad -> AdsMapper.adToAdDto(
                adRepository.save(
                        AdsMapper.updateAdFromCreateOrUpdateAdDto(ad, createOrUpdateAdDto)
                )
        ));
    }

    /**
     * updates ad's image in db
     * @param id - id of ad
     * @param image - MultipartFile
     * @return Optional'<'String'>' - filename
     */
    public Optional<String> updateAdImage(Integer id, MultipartFile image) {

        Optional<Ad> adOptional = adRepository.findById(id);
        if(adOptional.isEmpty())
            return Optional.empty();

        Image newImage;
        try {
            newImage = imageService.addAdImage(image, id);
            Ad updatedAd  = adOptional.get();
            updatedAd.setImage(newImage);

            adRepository.save(updatedAd);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(newImage.getFileName());
    }

    /**
     * deletes ad from db via id
     * @param id - id of ad
     * @return Boolean - true if deleted
     */
    public Boolean deleteAdById(Integer id) {

        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty())
            return false;

        adRepository.deleteById(id);
        return true;
    }

    /**
     * gets name of ad's author
     * @param id - id of ad
     * @return string - AuthorName
     * @throws UserException
     */
    public String getAdAuthorName(Integer id){
        return Objects.requireNonNull(adRepository.findById(id)
                .map(ad -> ad.getAuthor().getEmail()).orElseThrow(() -> new UserException("Name of author by id of ad did not find")));
    }
}