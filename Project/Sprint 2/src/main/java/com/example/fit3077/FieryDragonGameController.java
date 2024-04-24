package com.example.fit3077;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class FieryDragonGameController implements Initializable {

    @FXML
    private FlowPane boardcards;

    @FXML
    private FlowPane cardsDeck;

    @FXML
    private Label currentPlayer;

    @FXML
    private ImageView dragon;

    @FXML
    private ImageView dragon1;

    @FXML
    private ImageView dragon2;

    @FXML
    private ImageView dragon3;

    @FXML
    private ImageView fish;

    @FXML
    private ImageView fish1;

    @FXML
    private ImageView fish2;

    @FXML
    private ImageView fish3;

    @FXML
    private FlowPane gameBoard;

    @FXML
    private ImageView octopus;

    @FXML
    private ImageView octopus1;

    @FXML
    private ImageView octopus2;

    @FXML
    private ImageView octopus3;

    @FXML
    private ImageView pirate1;

    @FXML
    private ImageView pirate2;

    @FXML
    private ImageView puffer1;

    @FXML
    private ImageView puffer2;

    @FXML
    private ImageView puffer3;

    @FXML
    private ImageView pufferfish;

    @FXML
    void startGame(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPlayer.setText("Fish");
    }
}
