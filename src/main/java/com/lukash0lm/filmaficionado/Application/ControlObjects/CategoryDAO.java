package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.util.LinkedList;

public interface CategoryDAO {
    void addCategory(Category category);
    void deleteCategory(Category category);
    void updateCategory(Category category);
    Category getCategory(String title);
    LinkedList<Category> getAllCategories();
}
