package com.lukash0lm.filmaficionado.Application.BuisnessLogic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FilmApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("/view/FilmAficionado-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 675);
        stage.setTitle("Film Aficionado");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}