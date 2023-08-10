package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.model.Ad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdsMapper {

    /**
     * updates ad via createOrUpdateAdDto
     * @param ad - exist entity ad
     * @param createOrUpdateAdDto - dto
     * @return dd - updated ad
     */
    public static Ad updateAdFromCreateOrUpdateAdDto(Ad ad, CreateOrUpdateAdDto createOrUpdateAdDto){

        ad.setDescription(createOrUpdateAdDto.getDescription());
        ad.setPrice(createOrUpdateAdDto.getPrice());
        ad.setTitle(createOrUpdateAdDto.getTitle());

        return ad;
    }

    /**
     * creates adDto from ad
     * @param ad
     * @return adDto - created adDto
     */
    public static AdDto adToAdDto(Ad ad) {

        return new AdDto(
                ad.getAuthor().getId(),
                "/images/" + ad.getImage().getFileName(),
                ad.getId(),
                ad.getPrice(),
                ad.getTitle()
        );
    }

    /**
     * creates adToExtendedAdDto from ad
     * @param ad
     * @return adToExtendedAdDto - created adDto
     */
    public static ExtendedAdDto adToExtendedAdDto(Ad ad){

        return new ExtendedAdDto(
                ad.getId(),
                ad.getAuthor().getFirstName(),
                ad.getAuthor().getLastName(),
                ad.getDescription(),
                ad.getAuthor().getEmail(),
                ("/images/" + ad.getImage().getFileName()),
                ad.getAuthor().getPhone(),
                ad.getPrice(),
                ad.getTitle()
        );
    }

    /**
     * creates AdsDto from list of ads
     * @param ads - list of ads
     * @return adsToAdsDto - created AdsDto
     */
    public static AdsDto adsToAdsDto (List<Ad> ads){

        AdsDto results = new AdsDto();
        results.setCount(ads.size());

        List<AdDto> responseAds= new ArrayList<>();
        for (Ad ad : ads) {
            responseAds.add(adToAdDto(ad));
        }

        Collections.shuffle(responseAds);
        results.setResults(responseAds);

        return results;
    }
}
