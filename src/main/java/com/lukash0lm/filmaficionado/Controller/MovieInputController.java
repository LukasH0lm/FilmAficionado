package com.lukash0lm.filmaficionado.Controller;

import com.lukash0lm.filmaficionado.Application.ControlObjects.Category;
import com.lukash0lm.filmaficionado.Application.ControlObjects.CategoryDAOImpl;
import com.lukash0lm.filmaficionado.Application.ControlObjects.Movie;
import com.lukash0lm.filmaficionado.Application.ControlObjects.MovieDAOImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.LinkedList;

import static com.lukash0lm.filmaficionado.Controller.FilmAficionadoController.currentMovie;
import static com.lukash0lm.filmaficionado.Controller.FilmAficionadoController.isEdit;

public class MovieInputController {


    Movie movie;
    LinkedList<Category> allCategoryList = new LinkedList<>();
    LinkedList<Category> categoryList = new LinkedList<>();

    String movieTitle;
    String moviePath;

    String imageTitle;
    String imagePath;


    public MovieInputController() {
    }

    public void initialize() throws SQLException {
        addMovieButton.setDisable(true);
        allCategoryList.addAll(CategoryDAOImpl.getAllCategories());

        if (isEdit) {

            addMovieButton.setDisable(false);

            moviePath = currentMovie.getMoviePath();

            movie = currentMovie;
            movieInputTextField.setText(movie.getTitle());
            directorTextfield.setText(movie.getDirector());
            yearTextfield.setText(movie.getYear());
            imdbTextfield.setText(Double.toString(movie.getImdbRating()));
            //description.setText(movie.getDescription());
            categoryList.addAll(movie.getCategories());
            //imageInputTextField.setText(movie.getImagePath());
            System.out.println("categorylist: " + categoryList);
            System.out.println("before: " + allCategoryList);

            for (Category category : categoryList) {
                allCategoryList.removeIf(category1 -> category.getTitle().equals(category1.getTitle()));
            }

            System.out.println("after: " + allCategoryList);
        }

        categoriesTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        movieCategoriesTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        for (Category category : allCategoryList) {
            categoriesTableView.getItems().add(category);
        }

        for (Category category : categoryList) {
            movieCategoriesTableView.getItems().add(category);
        }




    }


    @FXML
    private TableView<Category> categoriesTableView;

    @FXML
    private TableColumn<Category, String> categoriesTableColumn;

    @FXML
    private TableView<Category> movieCategoriesTableView;

    @FXML
    private TableColumn<Category, String> movieCategoriesTableColumn;

    @FXML
    private TextField directorTextfield;

    @FXML
    private TextField yearTextfield;

    @FXML
    private TextArea imageInputTextField;

    @FXML
    private TextField imdbTextfield;

    @FXML
    private Button movieInputButton;

    @FXML
    private Button ImageInputButton;

    @FXML
    private TextArea movieInputTextField;

    @FXML
    private Button transferCategoryButton1;

    @FXML
    private Button transferCategoryButton2;

    @FXML
    private Button addMovieButton;


    @FXML
    void selectImage(MouseEvent event) {

        System.out.println("adding new image");
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Choose image to add");

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            imageInputTextField.setText(selectedFile.getName().substring(0, selectedFile.getName().length() - 4));
            imageTitle = selectedFile.getName().substring(0, selectedFile.getName().length() - 4);
            imagePath = selectedFile.getAbsolutePath();
        }

    }

    @FXML
    void selectMovie(MouseEvent event) {

        System.out.println("adding new movie");
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Choose movie to add");

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video Files", "*.mp4"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            movieInputTextField.setText(selectedFile.getName().substring(0, selectedFile.getName().length() - 4));
            movieTitle = selectedFile.getName().substring(0, selectedFile.getName().length() - 4);
            moviePath = selectedFile.getAbsolutePath();
            System.out.println("movieTitle: " + movieTitle);
            System.out.println("moviePath: " + moviePath);
            addMovieButton.setDisable(false);
        } else {
            addMovieButton.setDisable(true);
        }


    }

    @FXML
    void transferCategoryToMovieCategories(MouseEvent event) {
        Category category = categoriesTableView.getSelectionModel().getSelectedItem();
        if (category != null) {
            movieCategoriesTableView.getItems().add(category);
            categoriesTableView.getItems().remove(category);
        }
    }

    @FXML
    void transferMovieCategoryToCategories(MouseEvent event) {
        Category category = movieCategoriesTableView.getSelectionModel().getSelectedItem();
        if (category != null) {
            categoriesTableView.getItems().add(category);
            movieCategoriesTableView.getItems().remove(category);
        }
    }

    @FXML
    void addMovie(MouseEvent event) throws SQLException, IOException {

        Path movieSource = Paths.get(moviePath);
        if (Files.exists(movieSource)) {
            Path movieTarget = Paths.get("C:\\Users\\lukas\\IdeaProjects\\FilmAficionado\\src\\main\\resources\\movies\\" + movieTitle + ".mp4");

            if (!Files.exists(movieTarget)) {
                Files.createFile(movieTarget);
            }
            Files.copy(movieSource, movieTarget, StandardCopyOption.REPLACE_EXISTING);
            LinkedList<Category> movieCategories = new LinkedList<>(movieCategoriesTableView.getItems());

            if (imagePath != null) {
                Path imageSource = Paths.get(imagePath);

                String imageExtension = imagePath.substring(imagePath.length() - 3);

                Path imageTarget = Paths.get("C:\\Users\\lukas\\IdeaProjects\\FilmAficionado\\src\\main\\resources\\images\\" + movieTitle + "." + imageExtension);

                if (!Files.exists(imageTarget)) {
                    Files.createFile(imageTarget);
                }
                Files.copy(imageSource, imageTarget, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("image added");

            }


            if (isEdit) {
                movie.setTitle(movieInputTextField.getText());
                movie.setDirector(directorTextfield.getText());
                movie.setYear(Integer.parseInt(yearTextfield.getText()));
                movie.setImdbRating(Double.parseDouble(imdbTextfield.getText()));
                movie.setCategories(movieCategories);

                MovieDAOImpl.updateMovie(movie);

                Stage stage = (Stage) addMovieButton.getScene().getWindow();
                stage.close();

            } else {

                movie = new Movie(0, movieTitle, directorTextfield.getText(), Integer.parseInt(yearTextfield.getText()), 0, Double.parseDouble(imdbTextfield.getText()), "", movieCategories);

                System.out.println("adding movie: " + movie + " to database");
                System.out.println("categories: " + movieCategories);
                MovieDAOImpl.addMovie(movie);
                System.out.println("movie added to database");

                Stage stage = (Stage) addMovieButton.getScene().getWindow();
                stage.close();


            }
        } else {
            System.out.println("movie not added to database");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Movie does not exist, please try again");
            alert.showAndWait();
        }

    }


}
