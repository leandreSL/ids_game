package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

import core.player.ClientController;

public class MenuController {

    @FXML
    Button reglages;

    @FXML
    Button play;

    @FXML
    TextField name;

    @FXML
    BorderPane rootPane;

    @FXML
    ListView<String> nodes_view;

    final String[] nodes_names = {"A", "B", "C", "D"};
    
    ClientController clientController;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MenuController() {
    	clientController = new ClientController();
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            adjustMenu();
        });

    }

    public void adjustMenu(){
        nodes_view.getItems().addAll(nodes_names);
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
                String selected_node= nodes_view.getSelectionModel().getSelectedItem();
                String client_name = name.getText();
                clientController.init(client_name,selected_node, controller);
                stage.setOnCloseRequest(event->{
                    clientController.disconnect();
                    Platform.exit();
                    System.exit(0);
                });
                System.out.println("Name : " + client_name + " / Node : " + selected_node);
                controller.mainListener(stage,selected_node,clientController);
                
                //clientController.init(playerName, nodeName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
    }
}
