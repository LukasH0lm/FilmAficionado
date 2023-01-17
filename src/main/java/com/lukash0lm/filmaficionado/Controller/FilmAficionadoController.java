package com.lukash0lm.filmaficionado.Controller;

import com.lukash0lm.filmaficionado.Application.BuisnessLogic.CategoryInputDialog;
import com.lukash0lm.filmaficionado.Application.BuisnessLogic.FilmApplication;
import com.lukash0lm.filmaficionado.Application.BuisnessLogic.MovieInputDialog;
import com.lukash0lm.filmaficionado.Application.ControlObjects.Category;
import com.lukash0lm.filmaficionado.Application.ControlObjects.CategoryDAOImpl;
import com.lukash0lm.filmaficionado.Application.ControlObjects.Movie;
import com.lukash0lm.filmaficionado.Application.ControlObjects.MovieDAOImpl;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
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
    public static Movie currentMovie = null;
    public static boolean isEdit = false;
    MovieDAOImpl movieDAO = new MovieDAOImpl();
    CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    public FilmAficionadoController() throws SQLException {
    }

    public void initialize() throws IOException, SQLException {
        TableviewMovieColTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableviewMovieColDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        TableviewMovieColYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableviewMovieColImdbRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));


        allMovieList = new LinkedList<>();


        updateTableviewMovies();
        updateCategoryList();


        ObservableList<Movie> data =  TableviewMovies.getItems();
        filterTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                TableviewMovies.setItems(data);
            }
            String value = newValue.toLowerCase();
            ObservableList<Movie> subentries = FXCollections.observableArrayList();

            long count = TableviewMovies.getColumns().size();
            for (int i = 0; i < TableviewMovies.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "" + TableviewMovies.getColumns().get(j).getCellData(i);
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(TableviewMovies.getItems().get(i));
                        break;
                    }
                }
            }
            TableviewMovies.setItems(subentries);
        });




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
    private TableColumn<Movie, String> TableviewMovieColImdbRating;

    @FXML
    private TextField filterTextField;

    @FXML
    private TextArea MovieDescriptionTextField;

    @FXML
    private TextArea CategoryTextField;

    @FXML
    private TextArea bestMovieInCategoryTextField;

    @FXML
    private ImageView MovieImageView;

    @FXML
    private Button closeButton;

    @FXML
    private Button deleteSongButton;

    @FXML
    private Button editSongButton;

    @FXML
    private Button newSongButton;

    @FXML
    private Button newCategoryButton;

    @FXML
    private Button nominateButton;

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
    void editMovie(MouseEvent event) throws IOException {
        isEdit = true;

        Stage primaryStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("/view/MovieInput-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Edit Movie");
        primaryStage.show();

    }

    @FXML
    void movieChosen(MouseEvent event) throws SQLException {
        currentMovie = TableviewMovies.getSelectionModel().getSelectedItem();
        updateUIRating(currentMovie.getRating());
        MovieDescriptionTextField.setText(currentMovie.getDescription());
        MovieImageView.setImage(currentMovie.getImage());
        CategoryTextField.setText(currentMovie.getCategories().toString());
        if (movieDAO.getBestInCategories(currentMovie) != null) {
            bestMovieInCategoryTextField.setText(movieDAO.getBestInCategories(currentMovie).toString());

        }else{
            bestMovieInCategoryTextField.setText("");
        }

    }

    @FXML
    void newMovie(MouseEvent event) throws IOException {

        isEdit = false;

        Stage primaryStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("/view/MovieInput-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setScene(scene);
        primaryStage.setTitle("New Movie");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            updateTableviewMovies();
        });

    }


    @FXML
    void newCategory(MouseEvent event) throws SQLException, InterruptedException {
        CategoryInputDialog categoryInputDialog = new CategoryInputDialog(this);
        categoryInputDialog.start(new Stage());





    }

    @FXML
    void deleteCategory() throws SQLException {
        categoryDAO.deleteCategory(CategoryComboBox.getSelectionModel().getSelectedItem());
    }


    @FXML
    void playMovie(MouseEvent event) throws IOException {
        File file = new File("src/main/resources/movies/" + currentMovie.getTitle() + ".mp4");
        Desktop.getDesktop().open(file);
    }

    @FXML
    void updateRating(MouseEvent event) throws SQLException {

        currentMovie.setRating(ratingBar.getRating());
        movieDAO.updateMovieRating(currentMovie);

    }

    void updateUIRating(double newRating){
        ratingBar.setRating(newRating);
    }

    public void updateCategoryList() throws SQLException {
        if (CategoryComboBox != null) {
            CategoryComboBox.getItems().clear();
            CategoryComboBox.getItems().add(categoryDAO.getCategory("All"));

        }

        for (Category category : CategoryDAOImpl.getAllCategories()) {
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

    public void EditDescription() throws SQLException {
        if (currentMovie != null) {
            currentMovie.setDescription(MovieDescriptionTextField.getText());
            movieDAO.updateMovieDescription(currentMovie);
        }
    }

    @FXML
    void nominateForBestInCategory(ActionEvent event) throws SQLException {
        if (currentMovie != null) {
            Category category = CategoryComboBox.getSelectionModel().getSelectedItem();
            if (category != null && !category.getTitle().equals("All")) {
                categoryDAO.AddingBestMovieToCategory(currentMovie,category);
                category.setBestMovie(currentMovie.getID());
                bestMovieInCategoryTextField.setText(category.getTitle());

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No category selected");
                alert.setContentText("Please select a category to nominate the movie for");
                alert.showAndWait();
            }

        }
    }

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

}
