package com.lukash0lm.filmaficionado.Application.ControlObjects;

public class MovieDAOImpl implements MovieDAO {
    private Movie[] movies = new Movie[0];

    @Override
    public void addMovie(Movie movie) {
        Movie[] newMovies = new Movie[movies.length + 1];
        System.arraycopy(movies, 0, newMovies, 0, movies.length);
        newMovies[movies.length] = movie;
        movies = newMovies;
    }

    @Override
    public void deleteMovie(Movie movie) {
        Movie[] newMovies = new Movie[movies.length - 1];
        int index = 0;
        for (Movie m : movies) {
            if (!m.getTitle().equals(movie.getTitle())) {
                newMovies[index] = m;
                index++;
            }
        }
        movies = newMovies;
    }

    @Override
    public void updateMovie(Movie movie) {
        for (int i = 0; i < movies.length; i++) {
            if (movies[i].getTitle().equals(movie.getTitle())) {
                movies[i] = movie;
            }
        }
    }

    @Override
    public Movie getMovie(String title) {
        for (Movie m : movies) {
            if (m.getTitle().equals(title)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public Movie[] getAllMovies() {
        return movies;
    }

}
