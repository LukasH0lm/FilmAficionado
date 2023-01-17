package com.lukash0lm.filmaficionado.Application.BuisnessLogic;
import com.lukash0lm.filmaficionado.Application.ControlObjects.Category;
import com.lukash0lm.filmaficionado.Application.ControlObjects.CategoryDAOImpl;
import com.lukash0lm.filmaficionado.Controller.FilmAficionadoController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CategoryInputDialog extends Application {

    CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
    FilmAficionadoController controller;

    public CategoryInputDialog(FilmAficionadoController controller) throws SQLException {
        this.controller = controller;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("New Category");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Label userName = new Label("Name:");
        grid.add(userName, 0, 1);

        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 1);

        Label pw = new Label("Description:");
        grid.add(pw, 0, 2);

        TextField descriptionTextField = new TextField();
        grid.add(descriptionTextField, 1, 2);

        Button btn = new Button("Add Category");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Category added");
                System.out.println("name: " + nameTextField.getText());
                System.out.println("description: " + descriptionTextField.getText());
                Category newCategory = new Category(-1, nameTextField.getText(), descriptionTextField.getText(), null);
                try {
                    categoryDAO.addCategory(newCategory);
                    controller.updateCategoryList();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {

    }

}
