package com.lukash0lm.filmaficionado.Application.ControlObjects;

public interface MovieDAO {
    void addMovie(Movie movie);
    void deleteMovie(Movie movie);
    void updateMovie(Movie movie);
    Movie getMovie(String title);
    Movie[] getAllMovies();
}
