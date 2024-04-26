package com.example.fit3077;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Collections;

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

    private ArrayList<Card> cardsInGame;

    @FXML
    void startGame() {
        System.out.println("Starting game...");

        shuffleAndDisplayAnimals(); // Shuffle habitats and animals
        displayShuffledDeck(); // Shuffle deck images
        initializeDeck();
        initializeImageView();


    }



    private final double radius = 200; // Adjust as needed for your layout

    private GameMap gameMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameMap = new GameMap(); // Initialize gameMap which sets up the habitats
        startGame();

    }

    private void initializeDeck() {
        DeckOfCards deck = new DeckOfCards();
        deck.shuffle(); // Shuffle the deck

        // Initialize the list to hold the cards in play
        cardsInGame = new ArrayList<>();

        // Deal cards from the shuffled deck to the game
        for (int i = 0; i < decks.getChildren().size(); i++) {
            Card cardDealt = deck.dealTopCard();
            if (cardDealt != null) { // Ensure that a card was dealt
                // If the card type is "piratecard", create a PirateCard; otherwise, create an AnimalCard
                if ("piratecard".equals(cardDealt.getType())) {
                    cardsInGame.add(new PirateCard(cardDealt.getCount()));
                } else {
                    cardsInGame.add(new AnimalCard(cardDealt.getType(), cardDealt.getCount()));
                }
            }
        }
//        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);

    }


    private void displayShuffledDeck() {
        decks.getChildren().clear(); // Clear existing children to avoid duplicates
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
            System.out.println(decks.getChildren().size());


        }



    }

    /**
     * This method will arrange images in a circle within the game board.
     */
    private void arrangeAnimalsInCircle() {
        boardcards.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getWidth() > 0 && newValue.getHeight() > 0) {
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

    private void shuffleAndDisplayAnimals() {
        // Shuffle the habitats to get a new random arrangement of the animals
        Collections.shuffle(gameMap.getHabitats());
        // Now call the method to display animals in a circle
        arrangeAnimalsInCircle();
    }


    /**
     * This will add a number to each ImageView and set the image to be the back of a Card
     */
    private void initializeImageView()
    {
        System.out.println("Initializing image views for card flip.");
        for (int i = 0; i < decks.getChildren().size(); i++) {
            ImageView imageView = (ImageView) decks.getChildren().get(i);
            imageView.setImage(new Image(getClass().getResourceAsStream("images/coveredcard.png")));
            imageView.setUserData(i);

            imageView.setOnMouseClicked(event -> {
                int index = (int) imageView.getUserData();
                System.out.println("Card at index " + index + " clicked.");
                flipCard(index);
            });
        }
    }

    private void flipCard(int indexOfCard){

        System.out.println("Flipping card at index " + indexOfCard);
        ImageView imageView = (ImageView) decks.getChildren().get(indexOfCard);
        if (imageView != null && cardsInGame.size() > indexOfCard) {
            Card card = cardsInGame.get(indexOfCard);
            if (card != null) {
                Image image = card.getImage();
                if (image != null) {
                    imageView.setImage(image);
                } else {
                    System.err.println("Card image is null.");
                }
            } else {
                System.err.println("Card object is null.");
            }
        } else {
            System.err.println("ImageView is null or index is out of bounds.");
        }
    }
}

