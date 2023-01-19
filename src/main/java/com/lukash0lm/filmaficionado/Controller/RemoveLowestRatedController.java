package com.lukash0lm.filmaficionado.Controller;

import com.lukash0lm.filmaficionado.Application.ControlObjects.Movie;
import com.lukash0lm.filmaficionado.Application.ControlObjects.MovieDAOImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RemoveLowestRatedController {

    private Movie currentMovie;

    @FXML
    private TableView<Movie> TableviewMovies;

    @FXML
    private TableColumn<Movie, String> TableviewMovieColTitle;

    @FXML
    private TableColumn<Movie, String> TableviewMovieColDirector;

    @FXML
    private TableColumn<Movie, String> TableviewMovieColYear;

    @FXML
    private TableColumn<Movie, String> TableviewMovieColImdbRating;


    @FXML
    private Button closeButton;

    @FXML
    private Button removeAllButton;

    @FXML
    private Button removeButton;

    public void initialize() {

        TableviewMovieColTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableviewMovieColDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        TableviewMovieColYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableviewMovieColImdbRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));

        for (Movie movie : MovieDAOImpl.getLowestRatedMovies()) {
            TableviewMovies.getItems().add(movie);
        }

    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void movieChosen(MouseEvent event) {
        currentMovie = TableviewMovies.getSelectionModel().getSelectedItem();

    }

    @FXML
    void removeAllMovies(MouseEvent event) {
        for (Movie movie : TableviewMovies.getItems()) {
            try {
                MovieDAOImpl.deleteMovie(movie);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void removeMovie(MouseEvent event) throws SQLException {
        MovieDAOImpl.deleteMovie(currentMovie);
    }

}
