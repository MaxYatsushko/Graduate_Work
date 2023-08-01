package ru.skypro.homework.dto;

public class CreateOrUpdateAds {
    private String description;
    private int price;
    private String title;

    public CreateOrUpdateAds() {
    }

    public CreateOrUpdateAds(String description, int price, String title) {
        this.description = description;
        this.price = price;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}