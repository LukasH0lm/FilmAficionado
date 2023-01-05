package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.util.LinkedList;

public interface MovieDAO {
    void addMovie(Movie movie);
    void deleteMovie(Movie movie);
    void updateMovie(Movie movie);
    Movie getMovie(String title);
    LinkedList<Movie> getAllMovies();
}
