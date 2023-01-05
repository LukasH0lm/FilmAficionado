package com.lukash0lm.filmaficionado.Controller;

import com.lukash0lm.filmaficionado.Application.ControlObjects.CategoryDAOImpl;
import com.lukash0lm.filmaficionado.Application.ControlObjects.Movie;
import com.lukash0lm.filmaficionado.Application.ControlObjects.MovieDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.Rating;


import java.sql.SQLException;
import java.util.LinkedList;

public class FilmAficionadoController {

    LinkedList<Movie> allMovieList;
    LinkedList<Movie> filteredMovieList;

    MovieDAOImpl movieDAO = new MovieDAOImpl();
    CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    public FilmAficionadoController() throws SQLException {
    }

    public void initialize() {
        TableviewMovieColTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableviewMovieColDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        TableviewMovieColYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        allMovieList = new LinkedList<>();


        updateTableviewMovies();



    }



    @FXML
    private TableView<Movie> TableviewMovies;

    @FXML
    private TableColumn<Movie, String> TableviewMovieColTitle;

    @FXML
    private TableColumn<Movie, String> TableviewMovieColDirector;

    @FXML
    private TableColumn<Movie, String> TableviewMovieColYear;




    @FXML
    private TextArea MovieDescriptionTextField;

    @FXML
    private ImageView MovieImageView;

    @FXML
    private Button closeButton;

    @FXML
    private Button deleteSongButton;

    @FXML
    private Button editSongButton;

    @FXML
    private TextField filterTextField;

    @FXML
    private Button newSongButton;

    @FXML
    private Rating ratingBar;

    @FXML
    public void updateTableviewMovies() {
        TableviewMovies.getItems().clear();
        for (Movie movie : movieDAO.getAllMovies()) {
            TableviewMovies.getItems().add(movie);

        }

    }



    @FXML
    void deleteMovie(MouseEvent event) {

    }

    @FXML
    void editMovie(MouseEvent event) {

    }

    @FXML
    void movieChosen(MouseEvent event) {

    }

    @FXML
    void newMovie(MouseEvent event) {

    }

    @FXML
    void playMovie(MouseEvent event) {
        updateUIRating(3.0);
    }

    @FXML
    void updateRating(MouseEvent event) {

    }

    void updateUIRating(double newRating){
        ratingBar.setRating(newRating);
    }

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

}
