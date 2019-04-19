package com.alain.mk.kinfood.models;


public class Food {

    private String foodName;
    private String price;
    private String urlImage;
    private String desc;

    public Food() {
    }

    public Food(String foodName, String price, String urlImage, String greatImage) {
        this.foodName = foodName;
        this.price = price;
        this.urlImage = urlImage;
        this.desc = greatImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getGreatImage() {
        return desc;
    }

    public void setGreatImage(String greatImage) {
        this.desc = greatImage;
    }
}