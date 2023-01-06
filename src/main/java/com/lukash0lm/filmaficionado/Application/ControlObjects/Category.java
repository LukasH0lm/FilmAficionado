package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.util.LinkedList;

public class Category {
    private int id;
    private final String title;
    private final String description;
    private static LinkedList<Category> allCategories = new LinkedList<>();
    public Category(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        addToList();
    }

    public void addToList(){
        allCategories.add(this);
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

}
