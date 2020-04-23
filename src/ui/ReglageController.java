package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ReglageController {

    @FXML
    BorderPane rootPane;

    @FXML
    Button retour;


    public ReglageController() {
    }

    @FXML
    private void initialize() {
        retour.setOnMouseClicked((e) ->{
            try {
                BorderPane pane = FXMLLoader.load(getClass().getResource("menu.fxml"));
                rootPane.getChildren().setAll(pane);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
    }
}
