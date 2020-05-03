package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

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

    @FXML
    Label exceptionLabel;

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
        nodes_view.getSelectionModel().select("A");
        reglages.setOnMouseClicked((e) ->{
            try {
                BorderPane pane = FXMLLoader.load(getClass().getResource("../reglages.fxml"));
                rootPane.getChildren().setAll(pane);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        play.setOnMouseClicked((e) ->{
            try {
                String selected_node= nodes_view.getSelectionModel().getSelectedItem();
                String client_name = name.getText();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../plateau.fxml"));
                BorderPane pane = loader.load();
                PlateauController controller = (PlateauController)loader.getController();
                clientController.init(client_name,selected_node, controller);
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setScene(new Scene(pane));
                stage.setMinHeight(500);
                stage.setMinWidth(700);
                controller.mainListener(stage,selected_node,clientController);
                stage.setOnCloseRequest(event->{
                    clientController.disconnect();
                    Platform.exit();
                    System.exit(0);
                });
            }catch(Exception exp){
                try {
                    String selected_node= nodes_view.getSelectionModel().getSelectedItem();
                    String client_name = name.getText();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../plateau.fxml"));
                    BorderPane pane = loader.load();
                    PlateauController controller = (PlateauController)loader.getController();
                    clientController.init(client_name,selected_node, controller);
                    Stage stage = (Stage) rootPane.getScene().getWindow();
                    stage.setScene(new Scene(pane));
                    stage.setMinWidth(700);
                    stage.setMinHeight(500);
                    controller.mainListener(stage,selected_node,clientController);
                    stage.setOnCloseRequest(event->{
                        clientController.disconnect();
                        Platform.exit();
                        System.exit(0);
                    });
                }catch(Exception exp2){
                    exceptionLabel.setText("Erreur de connexion : reseau");
                }

            }

        });
    }
}
