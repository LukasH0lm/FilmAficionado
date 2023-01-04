package com.lukash0lm.filmaficionado.Application.ControlObjects;

public class Movie {
    private final String title;
    private final String director;
    private final String genre;
    private final String year;
    private final String rating;
    private final String description;
    private final String image;

    public Movie(String title, String director, String genre, String year, String rating, String description, String image) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getGenre() {
        return genre;
    }

    public String getYear() {
        return year;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

}
