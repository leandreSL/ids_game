package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    Button reglages;

    @FXML
    Button play;

    @FXML
    TextField name;

    @FXML
    BorderPane rootPane;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MenuController() {
    }

    @FXML
    private void initialize() {
        reglages.setOnMouseClicked((e) ->{
            try {
                BorderPane pane = FXMLLoader.load(getClass().getResource("reglages.fxml"));
                rootPane.getChildren().setAll(pane);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        play.setOnMouseClicked((e) ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("plateau.fxml"));
                BorderPane pane = loader.load();
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setScene(new Scene(pane));
                PlateauController controller = (PlateauController)loader.getController();
                controller.mainListener(stage,"nodeD");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
    }
}
