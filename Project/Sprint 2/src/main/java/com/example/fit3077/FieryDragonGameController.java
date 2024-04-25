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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class FieryDragonGameController implements Initializable {

    @FXML
    private Pane boardcards;

    @FXML
    private FlowPane cardsDeck;

    @FXML
    private Label currentPlayer;

    @FXML
    private FlowPane decks;

    @FXML
    private ImageView dragon1;

    @FXML
    private ImageView dragon2;

    @FXML
    private ImageView dragon3;

    @FXML
    private ImageView fish1;

    @FXML
    private ImageView fish2;

    @FXML
    private ImageView fish3;

    @FXML
    private FlowPane gameBoard;

    @FXML
    private ImageView octopus1;

    @FXML
    private ImageView octopus2;

    @FXML
    private ImageView octopus3;

    @FXML
    private ImageView piratecard1;

    @FXML
    private ImageView piratecard2;

    @FXML
    private ImageView pufferfish1;

    @FXML
    private ImageView pufferfish2;

    @FXML
    private ImageView pufferfish3;

    @FXML
    void startGame(ActionEvent event) {
    }



    private final double radius = 200; // Adjust as needed for your layout

    private GameMap gameMap;

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        DeckOfCards deck = new DeckOfCards();
//        deck.shuffle(); // Shuffle the deck
//
//        // Display the first card from the shuffled deck
//        Card previewCard = deck.dealTopCard(); // Get the top card from the deck
//        uncoveredCards.setImage(previewCard.getImage()); // Display the image of the card
//        arrangeAnimalsInCircle();
//    }
//
//    /**
//     * This method will arrange images in a circle within the game board.
//     */
//    private void arrangeAnimalsInCircle() {
//        // Delay the arrangement to ensure the Pane has been laid out and has width and height
//        boardcards.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
//            // Check if the Pane has width and height greater than 0
//            if(newValue.getWidth() > 0 && newValue.getHeight() > 0){
//                double paneCenterX = newValue.getWidth() / 2;
//                double paneCenterY = newValue.getHeight() / 2;
//
//                String[] animals = {"dragon", "fish", "pufferfish", "octopus"};
//                double angleStep = 360.0 / (animals.length * numberOfAnimals);
//
//                for (int i = 0; i < animals.length * numberOfAnimals; i++) {
//                    String animalName = animals[i % animals.length];
//                    String imagePath = "images/" + animalName + ".png";
//                    Image animalImage;
//
//                    try {
//                        animalImage = new Image(getClass().getResourceAsStream(imagePath));
//                    } catch (NullPointerException e) {
//                        System.err.println("Could not load image at path: " + imagePath);
//                        continue; // Skip this iteration if image loading fails
//                    }
//
//                    ImageView imageView = new ImageView(animalImage);
//                    imageView.setFitWidth(50); // Adjust size as needed
//                    imageView.setFitHeight(50); // Adjust size as needed
//                    imageView.setPreserveRatio(true);
//
//                    double angle = Math.toRadians(i * angleStep);
//                    double xOffset = radius * Math.cos(angle);
//                    double yOffset = radius * Math.sin(angle);
//
//                    imageView.setLayoutX(paneCenterX + xOffset - imageView.getFitWidth() / 2);
//                    imageView.setLayoutY(paneCenterY + yOffset - imageView.getFitHeight() / 2);
//
//                    boardcards.getChildren().add(imageView);
//                }
//            }
//        });
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameMap = new GameMap(); // Initialize gameMap which sets up the habitats

        arrangeAnimalsInCircle();
        displayShuffledDeck();

    }

    private void displayShuffledDeck() {
        // Instantiate the deck and shuffle it
        DeckOfCards deck = new DeckOfCards();
        deck.shuffle();

        // Retrieve the shuffled list of cards
        List<Card> shuffledDeck = deck.getCards();


        // Iterate over the shuffled deck and the imageView IDs together
        for (int i = 0; i < shuffledDeck.size(); i++) {
            Card card = shuffledDeck.get(i);
            String cardType = card.getType();
            int count = card.getCount();
            String cardFileName = cardType.toLowerCase() + count + ".png"; // Construct the file name based on type and count

            Image cardImage;
            try {
                cardImage = new Image(getClass().getResourceAsStream("images/" + cardFileName));
            } catch (Exception e) {
                System.err.println("Could not load image for card: " + cardFileName);
                continue; // Skip this card if the image fails to load
            }

            // Create a new ImageView for the card
            ImageView cardImageView = new ImageView(cardImage);
            cardImageView.setFitHeight(50); // Adjust the size as needed
            cardImageView.setFitWidth(50);
            cardImageView.setPreserveRatio(true);

            // Add the ImageView to the container
            decks.getChildren().add(cardImageView);

            initializeImageView();
        }



    }

    /**
     * This method will arrange images in a circle within the game board.
     */
    private void arrangeAnimalsInCircle() {
        boardcards.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getWidth() > 0 && newValue.getHeight() > 0) {
                boardcards.getChildren().clear(); // Clear existing children to avoid duplicates

                double paneCenterX = newValue.getWidth() / 2;
                double paneCenterY = newValue.getHeight() / 2;
                List<Habitat> habitats = gameMap.getHabitats();

                // Debugging output
                System.out.println("Habitats size: " + habitats.size());

                int totalAnimals = habitats.stream().mapToInt(h -> h.getCards().size()).sum(); // More accurate count of total animals
                System.out.println("Total animals to arrange: " + totalAnimals);

                int animalIndex = 0; // Keep a separate index for correctly computing the angle

                for (Habitat habitat : habitats) {
//                    System.out.println("Habitat contains:"); // Debugging output for each habitat
                    for (AnimalCard card : habitat.getCards()) {
//                        System.out.println(card.getAnimalType() + " with count " + card.getCount()); // Print each card's animal type and count

                        Image animalImage = card.getHabitatImage();
                        ImageView imageView = new ImageView(animalImage);
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);
                        imageView.setPreserveRatio(true);

                        double angle = Math.toRadians(360.0 * animalIndex / totalAnimals);
                        double xOffset = radius * Math.cos(angle);
                        double yOffset = radius * Math.sin(angle);

                        imageView.setLayoutX(paneCenterX + xOffset - imageView.getFitWidth() / 2);
                        imageView.setLayoutY(paneCenterY + yOffset - imageView.getFitHeight() / 2);

                        boardcards.getChildren().add(imageView);

                        animalIndex++; // Increment the index used to calculate angles
                    }
                }
            }
        });

    }

    /**
     * This will add a number to each ImageView and set the image to be the back of a Card
     */
    private void initializeImageView()
    {
        for (int i=0; i<decks.getChildren().size();i++)
        {
            //"cast" the Node to be of type ImageView
            ImageView imageView = (ImageView) decks.getChildren().get(i);
            imageView.setImage(new Image(Card.class.getResourceAsStream("images/coveredcard.png")));
//            imageView.setUserData(i);

//            //register a click listener
            imageView.setOnMouseClicked(event -> {
                System.out.println(imageView.getUserData());;
            });
        }
    }
}

