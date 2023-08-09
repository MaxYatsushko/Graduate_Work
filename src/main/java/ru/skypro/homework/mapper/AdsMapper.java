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

    public static Ad createOrUpdateAdsToAd(Ad ad, CreateOrUpdateAdDto updateAd){
        ad.setDescription(updateAd.getDescription());
        ad.setPrice(updateAd.getPrice());
        ad.setTitle(updateAd.getTitle());
        return ad;
    }

    public static AdDto adToResponseAd(Ad ad){
        return new AdDto(
                ad.getAuthor().getId(),
                "/images/" + ad.getImage().getFileName(),
                ad.getId(),ad.getPrice(),
                ad.getTitle()
        );
    }

    public static ExtendedAdDto adToResponseFullAd(Ad ad){
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

    public static AdsDto adsToResponseWrapperAds (List<Ad> ads){
        AdsDto results = new AdsDto();
        results.setCount(ads.size());
        List<AdDto> responseAds= new ArrayList<>();
        for (Ad ad : ads) {
            responseAds.add(adToResponseAd(ad));
        }
        Collections.shuffle(responseAds);
        results.setResults(responseAds);
        return results;
    }
}
