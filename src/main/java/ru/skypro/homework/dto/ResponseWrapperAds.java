package ru.skypro.homework.dto;

import ru.skypro.homework.model.Ad;

import java.util.List;

public class ResponseWrapperAds {
    private int count;
    private List<Ad> results;

    public ResponseWrapperAds() {
    }

    public ResponseWrapperAds(int count, List<Ad> results) {
        this.count = count;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Ad> getResults() {
        return results;
    }

    public void setResults(List<Ad> results) {
        this.results = results;
    }
}