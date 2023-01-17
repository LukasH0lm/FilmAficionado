package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.util.LinkedList;

public class Category {
    private int id;
    private final String title;
    private final String description;
    private Movie bestMovieInCategory;

    private static final LinkedList<Category> allCategories = new LinkedList<>();


    public Category(int id, String title, String description, Movie bestMovieInCategory) {
        this.id = id;
        this.title = title;
        this.description = description;
        addToList(this);
        this.bestMovieInCategory = bestMovieInCategory;
    }

    public static void addToList(Category category) {
        allCategories.add(category);
    }




    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public static Category getCategoryFromID(int id){
        for (Category category : allCategories) {
            if (category.getID() == id) {
                return category;
            }
        }
        return null;
    }

    public void setID(int currentID) {
        this.id = currentID;
    }

    public Movie getBestMovie() {
        return this.bestMovieInCategory;
    }

    public int getId() {
        return id;
    }

    public void setBestMovie(int movieID) {

        this.bestMovieInCategory = Movie.getMovieFromID(movieID);


    }
}
