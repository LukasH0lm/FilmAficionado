package com.lukash0lm.filmaficionado.Application.ControlObjects;

public interface CategoryDAO {
    void addCategory(Category category);
    void deleteCategory(Category category);
    void updateCategory(Category category);
    Category getCategory(String title);
    Category[] getAllCategories();
}
