package com.example.fit3077;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FieryDragonGameController implements Initializable {

        @FXML
        private Pane boardcards;

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
        private VBox gameBoardContainer;

        @FXML
        void startGame(ActionEvent event) {

        }

    private final int numberOfAnimals = 6;
    private final double radius = 200; // Adjust as needed for your layout

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DeckOfCards deck = new DeckOfCards();
        deck.shuffle(); // Shuffle the deck
        // Display the first card from the shuffled deck
        Card previewCard = deck.dealTopCard(); // Get the top card from the deck
        uncoveredCards.setImage(previewCard.getImage()); // Display the image of the card

        arrangeAnimalsInCircle();
    }

    /**
     * This method will arrange images in a circle within the game board.
     */
    private void arrangeAnimalsInCircle() {
        // Delay the arrangement to ensure the Pane has been laid out and has width and height
        boardcards.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the Pane has width and height greater than 0
            if(newValue.getWidth() > 0 && newValue.getHeight() > 0){
                double paneCenterX = newValue.getWidth() / 2;
                double paneCenterY = newValue.getHeight() / 2;

                String[] animals = {"dragon", "fish", "pufferfish", "octopus"};
                double angleStep = 360.0 / (animals.length * numberOfAnimals);

                for (int i = 0; i < animals.length * numberOfAnimals; i++) {
                    String animalName = animals[i % animals.length];
                    String imagePath = "images/" + animalName + ".png";
                    Image animalImage;

                    try {
                        animalImage = new Image(getClass().getResourceAsStream(imagePath));
                    } catch (NullPointerException e) {
                        System.err.println("Could not load image at path: " + imagePath);
                        continue; // Skip this iteration if image loading fails
                    }

                    ImageView imageView = new ImageView(animalImage);
                    imageView.setFitWidth(50); // Adjust size as needed
                    imageView.setFitHeight(50); // Adjust size as needed
                    imageView.setPreserveRatio(true);

                    double angle = Math.toRadians(i * angleStep);
                    double xOffset = radius * Math.cos(angle);
                    double yOffset = radius * Math.sin(angle);

                    imageView.setLayoutX(paneCenterX + xOffset - imageView.getFitWidth() / 2);
                    imageView.setLayoutY(paneCenterY + yOffset - imageView.getFitHeight() / 2);

                    boardcards.getChildren().add(imageView);
                }
            }
        });
    }
}
