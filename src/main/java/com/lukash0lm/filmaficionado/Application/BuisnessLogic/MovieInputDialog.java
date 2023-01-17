package com.lukash0lm.filmaficionado.Application.BuisnessLogic;// Java Program to create a text input dialog
// and add a label to display the text entered
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDate;
public class MovieInputDialog extends Application {

    // launch the application
    public void start(Stage s)
    {
        // set title for the stage
        s.setTitle("creating textInput dialog");

        // create a tile pane
        TilePane r = new TilePane();

        // create a label to show the input in text dialog
        Label l = new Label("no text input");

        TextField title = new TextField();

        TextField director = new TextField();

        TextField genre = new TextField();



        // create a button
        Button submitButton = new Button("click");

        // create a event handler
        EventHandler<ActionEvent> submitEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // show the text input dialog
                System.out.println("Title: " + title.getText());
            }
        };

        // set on action of event
        submitButton.setOnAction(submitEvent);

        r.getChildren().add(title);
        r.getChildren().add(director);
        r.getChildren().add(genre);

        r.getChildren().add(submitButton);


        // create a scene
        Scene sc = new Scene(r, 500, 300);

        // set the scene
        s.setScene(sc);

        s.show();
    }


}