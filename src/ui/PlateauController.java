package ui;

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

import java.util.Random;


public class PlateauController {

    @FXML
    BorderPane rootPane;

    @FXML
    Label nodeName;

    Rectangle grille [][];
    Scene scene;
    String node ="";


    int playerX;
    int playerY;

    String nextNode1;
    String nextNode2;

    Insets inMainNode;

    Color colorNode;
    Color colorNextNode1;
    Color colorNextNode2;
    Color colorPlayer;

    public PlateauController() {

    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            initPlateau();
            Random random = new Random();
            playerX = random.nextInt(6);
            playerY = random.nextInt(6);
            movePlayer(playerX,playerY,grille);
        });

    }

    //fixe les nodes adjacents en fonction du node courant, et aussi les couleurs
    public void adjustNodes(){
        colorPlayer = Color.PURPLE;
        switch (this.node){
            case "nodeA" :
                colorNode = Color.DARKGOLDENROD;
                colorNextNode1 = Color.GREEN;
                colorNextNode2 = Color.BROWN;
                nextNode1 = "nodeB";
                nextNode2 = "nodeC";
                inMainNode = new Insets(10, 0, 10, 50);
                break;
            case "nodeB" :
                colorNode = Color.GREEN;
                colorNextNode1 = Color.DARKGOLDENROD;
                colorNextNode2 = Color.BLUE;
                nextNode1 = "nodeA";
                nextNode2 = "nodeD";
                inMainNode = new Insets(10, 50, 10, 0);
                break;
            case "nodeC" :
                colorNode = Color.BROWN;
                colorNextNode1 = Color.BLUE;
                colorNextNode2 = Color.DARKGOLDENROD;
                nextNode1 = "nodeD";
                nextNode2 = "nodeA";
                inMainNode = new Insets(10, 0, 10, 50);
                break;
            case "nodeD" :
                colorNode = Color.BLUE;
                colorNextNode1 = Color.BROWN;
                colorNextNode2 = Color.GREEN;
                nextNode1 = "nodeC";
                nextNode2 = "nodeB";
                inMainNode = new Insets(10, 50, 10, 0);
                break;
        }

    }

    //fonction "moteur" qui gère les évènements des touches
    public void mainListener(Stage stage, String nodeN) {
        scene = stage.getScene();
        this.node = nodeN;
        this.nodeName.setText(nodeN);

        adjustNodes();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                switch (event.getCode()) {
                    case Z:
                    case UP:
                        if (playerX > 0) {
                            grille[playerX][playerY].setFill(colorNode);
                            playerX--;
                            grille[playerX][playerY].setFill(colorPlayer);
                        }else{
                            if (node.equals("nodeC") || node.equals("nodeD")){
                                node = nextNode2;
                                rootPane.getChildren().clear();
                                adjustNodes();
                                initPlateau();
                                movePlayer(6,playerY,grille);
                            }
                        }
                        break;
                    case Q:
                    case LEFT:
                        if (playerY > 0) {
                            grille[playerX][playerY].setFill(colorNode);
                            playerY--;
                            grille[playerX][playerY].setFill(colorPlayer);
                        }
                        else{
                            if (node.equals("nodeB") || node.equals("nodeD")){
                                node = nextNode1;
                                rootPane.getChildren().clear();
                                adjustNodes();
                                initPlateau();
                                movePlayer(playerX,6,grille);
                            }
                        }
                        break;
                    case S :
                    case DOWN:
                        if (playerX < 6) {
                            grille[playerX][playerY].setFill(colorNode);
                            playerX++;
                            grille[playerX][playerY].setFill(colorPlayer);
                        }else{
                            if (node.equals("nodeA") || node.equals("nodeB")){
                                node = nextNode2;
                                rootPane.getChildren().clear();
                                adjustNodes();
                                initPlateau();
                                movePlayer(0,playerY,grille);
                            }
                        }
                        break;
                    case D:
                    case RIGHT:
                        if (playerY < 6) {
                            grille[playerX][playerY].setFill(colorNode);
                            playerY++;
                            grille[playerX][playerY].setFill(colorPlayer);
                        }
                        else{
                            if (node.equals("nodeA") || node.equals("nodeC")){
                                node = nextNode1;
                                rootPane.getChildren().clear();
                                adjustNodes();
                                initPlateau();
                                movePlayer(playerX,0,grille);
                            }
                        }
                        break;
                }
            }
        });

    }

    //place le joueur sur une case (x,y)
    public void movePlayer(int x, int y, Rectangle grille [][]){
        playerX = x;
        playerY = y;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == x && j == y){
                    grille[i][j].setFill(colorPlayer);
                }
            }
        }
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

        switch (this.node){
            case "nodeA" :

                rootPane.setRight(next_node1);
                rootPane.setBottom(next_node2);
                break;
            case "nodeB" :
                rootPane.setLeft(next_node1);
                rootPane.setBottom(next_node2);
                break;
            case "nodeC" :
                rootPane.setRight(next_node1);
                rootPane.setTop(next_node2);
                break;
            case "nodeD" :
                rootPane.setLeft(next_node1);
                rootPane.setTop(next_node2);
                break;
        }
    }

}
