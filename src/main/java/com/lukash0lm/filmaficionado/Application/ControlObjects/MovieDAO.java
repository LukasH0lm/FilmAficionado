package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.sql.SQLException;
import java.util.LinkedList;

public interface MovieDAO {
    void updateMovieRating(Movie movie) throws SQLException;

    LinkedList<Movie> getAllMovies();
}
