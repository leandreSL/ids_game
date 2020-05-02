package ui;

import core.player.ClientController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;


public class PlateauController {

    @FXML
    BorderPane rootPane;

    @FXML
    Label nodeName;

    @FXML
    Label score;

    @FXML
    Label message;

    @FXML
    HBox infos;

    Rectangle grille [][];
    Scene scene;
    private String node ="";

    String nextNode1;
    String nextNode2;

    Insets inMainNode;

    Color colorNode;
    Color colorNextNode1;
    Color colorNextNode2;
    Color colorPlayer;
    Color colorOthersPlayers;

    ClientController clientCtrl;

    public PlateauController() {

    }

    public Color getColorNode() {
        return colorNode;
    }

    public Color getColorPlayer() {
        return colorPlayer;
    }

    public Color getColorOthersPlayers() {
        return colorOthersPlayers;
    }

    @FXML
    private void initialize() {

    }

    //fixe les nodes adjacents en fonction du node courant, et aussi les couleurs
    public void adjustNodes(){
        colorPlayer = Color.PURPLE;
        colorOthersPlayers = Color.LIGHTGRAY;
        switch (this.node){
            case "A" :
                colorNode = Color.DARKGOLDENROD;
                colorNextNode1 = Color.GREEN;
                colorNextNode2 = Color.BROWN;
                nextNode1 = "B";
                nextNode2 = "C";
                inMainNode = new Insets(10, 0, 10, 50);
                break;
            case "B" :
                colorNode = Color.GREEN;
                colorNextNode1 = Color.DARKGOLDENROD;
                colorNextNode2 = Color.BLUE;
                nextNode1 = "A";
                nextNode2 = "D";
                inMainNode = new Insets(10, 50, 10, 0);
                break;
            case "C" :
                colorNode = Color.BROWN;
                colorNextNode1 = Color.BLUE;
                colorNextNode2 = Color.DARKGOLDENROD;
                nextNode1 = "D";
                nextNode2 = "A";
                inMainNode = new Insets(10, 0, 10, 50);
                break;
            case "D" :
                colorNode = Color.BLUE;
                colorNextNode1 = Color.BROWN;
                colorNextNode2 = Color.GREEN;
                nextNode1 = "C";
                nextNode2 = "B";
                inMainNode = new Insets(10, 50, 10, 0);
                break;
        }

    }

    //fonction "moteur" qui gère les évènements des touches
    public void mainListener(Stage stage, String nodeN, ClientController ctrl) {
        scene = stage.getScene();
        this.node = nodeN;
        this.nodeName.setText(nodeN);
        this.clientCtrl = ctrl;

        adjustNodes();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                switch (event.getCode()) {
                    case Z:
                    case UP:
                        clientCtrl.move(0,-1);
                        message.setText("");
                        break;
                    case Q:
                    case LEFT:
                        clientCtrl.move(-1,0);
                        message.setText("");
                        break;
                    case S :
                    case DOWN:
                        clientCtrl.move(0,1);
                        message.setText("");
                        break;
                    case D:
                    case RIGHT:
                        clientCtrl.move(1,0);
                        message.setText("");
                        break;
                }
            }
        });

    }

    // initialise le plateau avec une grille de 7x7 et un preview des grilles adjacentes
    public void initPlateau(){
        grille = new Rectangle[7][7];
        TilePane pane = new TilePane();

        pane.setAlignment(Pos.CENTER);
        pane.setHgap(3);
        pane.setVgap(3);
        pane.setPrefRows(7);
        pane.setMaxWidth(470);
        pane.setPadding(inMainNode);
        VBox next_node1 = new VBox();
        HBox next_node2 = new HBox();

        next_node1.setSpacing(3);
        next_node2.setSpacing(3);

        for (int i = 0; i < 7; i++) {
            Rectangle r2 = new Rectangle();
            Rectangle r3 = new Rectangle();
            r2.setHeight(50);
            r2.setWidth(50);

            r3.setHeight(50);
            r3.setWidth(50);

            r2.setFill(colorNextNode1);
            r3.setFill(colorNextNode2);

            for (int j = 0; j < 7; j++) {
                Rectangle r = new Rectangle();
                r.setHeight(50);
                r.setWidth(50);
                r.setFill(colorNode);

                grille[i][j] = r;
                pane.getChildren().add(r);
            }
            next_node1.getChildren().add(r2);
            next_node2.getChildren().add(r3);
        }
        next_node1.setAlignment(Pos.CENTER);
        next_node2.setAlignment(Pos.CENTER);
        rootPane.setCenter(pane);
        nodeName.setText(" Node " + node);
        switch (this.node){
            case "A" :
                rootPane.setTop(infos);
                rootPane.setRight(next_node1);
                rootPane.setBottom(next_node2);
                break;
            case "B" :
                rootPane.setTop(infos);
                rootPane.setLeft(next_node1);
                rootPane.setBottom(next_node2);
                break;
            case "C" :
                rootPane.setBottom(infos);
                rootPane.setRight(next_node1);
                rootPane.setTop(next_node2);
                break;
            case "D" :
                rootPane.setBottom(infos);
                rootPane.setLeft(next_node1);
                rootPane.setTop(next_node2);
                break;
        }
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
