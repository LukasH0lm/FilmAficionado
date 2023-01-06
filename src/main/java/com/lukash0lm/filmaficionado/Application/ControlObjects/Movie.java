package com.lukash0lm.filmaficionado.Application.ControlObjects;

import javafx.scene.image.Image;

import java.util.LinkedList;

public class Movie {
    private int id;
    private final String title;
    private final String director;
    private final String year;
    private double rating;
    private final String description;
    private final Image image;
    private LinkedList<Category> categories;

    public Movie(int id, String title, String director, int year, double rating, String description, LinkedList<Category> categories) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = Integer.toString(year);
        this.rating = rating;
        this.description = description;
        this.image = new Image("file:src/main/resources/images/" + title + ".jpg");
        this.categories = categories;
    }



    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getID() {
        return id;
    }

    public LinkedList<Category> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categories=" + categories +
                '}';
    }
}
