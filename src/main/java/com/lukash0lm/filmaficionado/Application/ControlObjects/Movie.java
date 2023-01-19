package com.lukash0lm.filmaficionado.Application.ControlObjects;

import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Movie {
    private int id;
    private String title;
    private String director;
    private String year;
    private double rating;
    private String description;
    private final Image image;
    private LinkedList<Category> categories;
    private double imdbRating;

    private static final LinkedList<Movie> allMovies = new LinkedList<>();


    public Movie(int id, String title, String director, int year, double rating, double imdbRating, String description, LinkedList<Category> categories) throws IOException {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = Integer.toString(year);
        this.description = description;
        this.imdbRating = imdbRating;
        this.rating = rating;


        this.image = retrieveImage(this);
        this.categories = categories;

        addToList(this);

    }

    public Image retrieveImage(Movie movie) {

        String fileExtension;

        Path imagePathjpg = Paths.get("C:\\Users\\lukas\\IdeaProjects\\FilmAficionado\\src\\main\\resources\\images\\" + movie.getTitle() + "." + "jpg");
        if (Files.exists(imagePathjpg)) {
            fileExtension = "jpg";

        } else {
            Path imagePathpng = Paths.get("C:\\Users\\lukas\\IdeaProjects\\FilmAficionado\\src\\main\\resources\\images\\" + movie.getTitle() + "." + "png");
            if (Files.exists(imagePathpng)) {
                fileExtension = "png";

            } else {
                Path imagePathgif = Paths.get("C:\\Users\\lukas\\IdeaProjects\\FilmAficionado\\src\\main\\resources\\images\\" + movie.getTitle() + "." + "gif");
                if (Files.exists(imagePathgif)) {
                    fileExtension = "gif";
                } else {
                    return null;
                }
            }
        }
        return new Image("file:src/main/resources/images/" + movie.getTitle() + "." + fileExtension);
    }

    public static void addToList(Movie movie) {
        allMovies.add(movie);
    }

    public static Movie getMovieFromID(int movieID) {
        for (Movie movie : allMovies) {
            if (movie.getID() == movieID) {
                return movie;
            }
        }
        return null;
    }


    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public String getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getID() {
        return id;
    }

    public LinkedList<Category> getCategories() {
        return categories;
    }

    public void setCategories(LinkedList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categories=" + categories +
                '}';
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public LinkedList<Category> getBestInCategories() {
        LinkedList<Category> bestInCategories = new LinkedList<>();
        for (Category category : categories) {
            if (category.getBestMovie() != null) {
                if (category.getBestMovie().equals(this)) {
                    bestInCategories.add(category);

                }
            }
        }
        return bestInCategories;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = Integer.toString(year);
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }


    public String getMoviePath() {
        return "C:\\Users\\lukas\\IdeaProjects\\FilmAficionado\\src\\main\\resources\\movies\\" + this.getTitle() + ".mp4";
    }
}
