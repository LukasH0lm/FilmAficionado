package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.sql.SQLException;
import java.util.LinkedList;

public interface CategoryDAO {
    void addCategory(Category category) throws SQLException;
    void deleteCategory(Category category) throws SQLException;
    void updateCategory(Category category);
    Category getCategory(String title);
}
