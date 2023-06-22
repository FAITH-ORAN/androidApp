package com.example.foodapp.Models;

public class FoodItem {
    private String id;
    private String title;
    private String difficulty;
    private String image;

    public FoodItem(String id, String title, String difficulty, String image) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getImage() {
        return image;
    }
}
