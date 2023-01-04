module com.lukash0lm.filmaficionado {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.lukash0lm.filmaficionado.Controller;
    opens com.lukash0lm.filmaficionado.Controller to javafx.fxml;
    exports com.lukash0lm.filmaficionado.Application.BuisnessLogic;
    opens com.lukash0lm.filmaficionado.Application.BuisnessLogic to javafx.fxml;
}