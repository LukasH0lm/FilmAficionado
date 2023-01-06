package com.lukash0lm.filmaficionado.Controller;

import com.lukash0lm.filmaficionado.Application.BuisnessLogic.CategoryInputDialog;
import com.lukash0lm.filmaficionado.Application.BuisnessLogic.MovieInputDialog;
import com.lukash0lm.filmaficionado.Application.ControlObjects.Category;
import com.lukash0lm.filmaficionado.Application.ControlObjects.CategoryDAOImpl;
import com.lukash0lm.filmaficionado.Application.ControlObjects.Movie;
import com.lukash0lm.filmaficionado.Application.ControlObjects.MovieDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Objects;

public class FilmAficionadoController {

    LinkedList<Movie> allMovieList;
    LinkedList<String> categoryList;
    LinkedList<Movie> filteredMovieList;
    Movie currentMovie = null;

    MovieDAOImpl movieDAO = new MovieDAOImpl();
    CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    public FilmAficionadoController() throws SQLException {
    }

    public void initialize() throws IOException {
        TableviewMovieColTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableviewMovieColDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        TableviewMovieColYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        allMovieList = new LinkedList<>();


        updateTableviewMovies();
        updateCategoryList();


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
    private Button newCategoryButton;

    @FXML
    private Rating ratingBar;

    @FXML
    private ComboBox<Category> CategoryComboBox;

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
        currentMovie = TableviewMovies.getSelectionModel().getSelectedItem();
        updateUIRating(currentMovie.getRating());
        MovieDescriptionTextField.setText(currentMovie.getDescription());
        MovieImageView.setImage(currentMovie.getImage());
    }

    @FXML
    void newMovie(MouseEvent event) {

        //TODO: Add new movie to database

        MovieInputDialog movieInputDialog = new MovieInputDialog();
        movieInputDialog.start(new Stage());

    }


    @FXML
    void newCategory(MouseEvent event) {
        CategoryInputDialog categoryInputDialog = new CategoryInputDialog();
        categoryInputDialog.start(new Stage());
    }

    @FXML
    void playMovie(MouseEvent event) {
        Media media = new Media(new File("src/main/resources/movies/" + currentMovie.getTitle() + ".mp4").toURI().toString());
        MediaPlayer  mp = new MediaPlayer(media);
        mp.play();
    }

    @FXML
    void updateRating(MouseEvent event) throws SQLException {

        currentMovie.setRating(ratingBar.getRating());
        movieDAO.updateMovieRating(currentMovie);

    }

    void updateUIRating(double newRating){
        ratingBar.setRating(newRating);
    }

    public void updateCategoryList(){
        if (CategoryComboBox != null) {
            CategoryComboBox.getItems().clear();
            CategoryComboBox.getItems().add(categoryDAO.getCategory("All"));

        }

        for (Category category : categoryDAO.getAllCategories()) {
            CategoryComboBox.getItems().add(category);
        }
    }



    public void changeCategoryView(){
        Category categoryBox = CategoryComboBox.getValue();
        if (categoryBox == null){
            updateTableviewMovies();
        } else {
            TableviewMovies.getItems().clear();
            for (Movie movie : movieDAO.getAllMovies()) {
                for(Category category : movie.getCategories()){
                    if (category.getTitle().equals(categoryBox.getTitle())){
                        TableviewMovies.getItems().add(movie);
                    }
                }
            }
        }
    }

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

}
