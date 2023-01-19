package com.lukash0lm.filmaficionado.Controller;

import com.lukash0lm.filmaficionado.Application.BuisnessLogic.CategoryInputDialog;
import com.lukash0lm.filmaficionado.Application.BuisnessLogic.FilmApplication;
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
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

public class FilmAficionadoController {

    LinkedList<Movie> allMovieList;
    public static Movie currentMovie = null;
    public static boolean isEdit = false;
    MovieDAOImpl movieDAO;
    CategoryDAOImpl categoryDAO;

    public FilmAficionadoController() {
    }

    public void initialize() throws IOException, SQLException {

        categoryDAO = new CategoryDAOImpl();
        movieDAO = new MovieDAOImpl();


        TableviewMovieColTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableviewMovieColDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        TableviewMovieColYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableviewMovieColImdbRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));


        allMovieList = new LinkedList<>();


        updateTableviewMovies();
        updateCategoryList();


        ObservableList<Movie> data = TableviewMovies.getItems();
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

        if (!MovieDAOImpl.getLowestRatedMovies().isEmpty()){

        Stage primaryStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("/view/RemoveLowestRated-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Stage is closing");
            updateTableviewMovies();
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Remove low rated movies");
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);
        }
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
    private Button newMovieButton;

    @FXML
    private Button editMovieButton;

    @FXML
    private Button deleteMovieButton;

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
    void deleteMovie(MouseEvent event) throws SQLException {
        Movie movie = TableviewMovies.getSelectionModel().getSelectedItem();
        MovieDAOImpl.deleteMovie(movie);
        updateTableviewMovies();

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

        onStageClose(primaryStage);

    }

    private void onStageClose(Stage primaryStage) {
        //updates ui when stage is closed
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Stage is closing");

            if (currentMovie != null) {
                CategoryTextField.setText(currentMovie.getCategories().toString());
                try {
                    bestMovieInCategoryTextField.setText(movieDAO.getBestInCategories(currentMovie).toString());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            updateTableviewMovies();
        });
    }

    @FXML
    void movieChosen(MouseEvent event) throws SQLException {
        currentMovie = TableviewMovies.getSelectionModel().getSelectedItem();
        if (currentMovie == null){
            return;
        }
        updateUIRating(currentMovie.getRating());
        MovieDescriptionTextField.setText(currentMovie.getDescription());
        MovieImageView.setImage(currentMovie.getImage());
        CategoryTextField.setText(currentMovie.getCategories().toString());
        if (movieDAO.getBestInCategories(currentMovie) != null) {
            bestMovieInCategoryTextField.setText(movieDAO.getBestInCategories(currentMovie).toString());

        } else {
            bestMovieInCategoryTextField.setText("");
        }

    }

    @FXML
    void newMovie(MouseEvent event) throws IOException {

        isEdit = false;

        Stage primaryStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("/view/MovieInput-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        onStageClose(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.setTitle("New Movie");
        primaryStage.show();


    }


    @FXML
    void newCategory(MouseEvent event) throws SQLException {
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

    void updateUIRating(double newRating) {
        ratingBar.setRating(newRating);
    }

    public void updateCategoryList() throws SQLException {
        if (CategoryComboBox != null) {
            CategoryComboBox.getItems().clear();
            CategoryComboBox.getItems().add(categoryDAO.getCategory("All"));

        }

        for (Category category : CategoryDAOImpl.getAllCategories()) {
            assert CategoryComboBox != null;
            CategoryComboBox.getItems().add(category);
        }
    }


    public void changeCategoryView() {
        Category categoryBox = CategoryComboBox.getValue();
        if (categoryBox == null) {
            updateTableviewMovies();
        } else {
            TableviewMovies.getItems().clear();
            for (Movie movie : movieDAO.getAllMovies()) {
                for (Category category : movie.getCategories()) {
                    if (category.getTitle().equals(categoryBox.getTitle())) {
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
        }else{
            System.out.println("No movie selected");
        }
    }

    @FXML
    void nominateForBestInCategory(ActionEvent event) throws SQLException {
        if (currentMovie != null) {
            Category category = CategoryComboBox.getSelectionModel().getSelectedItem();
            boolean hasCategory = false;
            if (category != null && !category.getTitle().equals("All")) {

                for (Category movieCategory : currentMovie.getCategories()) {
                    if (movieCategory.getTitle().equals(category.getTitle())) {
                       hasCategory = true;
                       break;
                    }

                }

                if (hasCategory) {
                    categoryDAO.AddingBestMovieToCategory(currentMovie, category);
                    category.setBestMovie(currentMovie.getID());
                    bestMovieInCategoryTextField.setText(currentMovie.getBestInCategories().toString());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Movie is not in this category");
                    alert.showAndWait();
                }


            } else {
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
