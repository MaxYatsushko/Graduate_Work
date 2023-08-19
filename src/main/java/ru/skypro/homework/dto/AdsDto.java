package ru.skypro.homework.dto;

import lombok.Data;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.model.Ad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class AdsDto {

    private int count;
    private List<AdDto> results;

    public AdsDto() {}

    public AdsDto(List<Ad> ads) {

        this.count = ads.size();

        List<AdDto> responseAds = new ArrayList<>();
        for (Ad ad : ads) {
            responseAds.add(AdsMapper.adToAdDto(ad));
        }

        this.results = responseAds;
    }
}