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
        private ImageView uncoveredCards;

        @FXML
        private Label currentPlayer;

        @FXML
        private ImageView dragon;

        @FXML
        private ImageView fish;

        @FXML
        private FlowPane gameBoard;

        @FXML
        private ImageView octopus;

        @FXML
        private ImageView pufferfish;



        @FXML
        void startGame(ActionEvent event) {

        }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnimalCard animalCard = new AnimalCard("fish",1);
        PirateCard pirateCard = new PirateCard(1);
        uncoveredCards.setImage(animalCard.getImage());
    }


}
