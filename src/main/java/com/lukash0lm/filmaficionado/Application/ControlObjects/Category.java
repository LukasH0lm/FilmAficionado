package com.lukash0lm.filmaficionado.Application.ControlObjects;

public class Category {
    private final String title;
    private final String description;
    private final String image;

    public Category(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }



    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

}
