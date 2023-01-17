package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.sql.SQLException;
import java.util.LinkedList;

public interface MovieDAO {
    void deleteMovie(Movie movie);
    void updateMovieRating(Movie movie) throws SQLException;
    Movie getMovie(String title);
    LinkedList<Movie> getAllMovies();
}
