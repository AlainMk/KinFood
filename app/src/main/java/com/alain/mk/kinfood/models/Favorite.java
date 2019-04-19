package com.alain.mk.kinfood.models;

public class Favorite {

    private String foodName;

    public Favorite() {
    }

    public Favorite(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
