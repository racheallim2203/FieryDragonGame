package com.example.fit3077;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;

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
    private Player inPlayPlayer;

    @FXML
    void startGame() {
        System.out.println("Starting game...");

        shuffleAndDisplayAnimals(); // Shuffle habitats and animals
        displayShuffledDeck(); // Shuffle deck images
        initializeDeck();
        initializeImageView();


    }



    private final double radius = 180; // Adjust as needed for your layout

    private GameMap gameMap;

    private final String[] animalPositions = new String[24]; // Assuming 24 positions available on the game board


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameMap = new GameMap(); // Initialize gameMap which sets up the habitats
        startGame();
        updateCurrentPlayerDisplay("Fish"); // Assuming Fish starts the game


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

                int animalIndex = 0; // index for positioning and array storage

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
                        animalPositions[animalIndex % animalPositions.length] = card.getAnimalType();
                        animalIndex++; // Increment the index to update position index
                        arrangeAnimalTokens();
                    }

                }
                System.out.println(Arrays.toString(animalPositions));
            }
        });

    }

    private void updateGameBoard() {
        // Clear existing tokens from the board for repositioning
        for (Node child : boardcards.getChildren()) {
            if (child instanceof ImageView && "token".equals(child.getUserData())) {
                boardcards.getChildren().remove(child);
            }
        }

        // Iterate through each player's token and reposition it on the game board
        for (Player player : gameMap.getPlayers()) {
            AnimalToken token = player.getAnimalToken();
            int position = player.getPosition();
            double angle = Math.toRadians(360.0 * position / gameMap.get());
            double paneCenterX = boardcards.getWidth() / 2;
            double paneCenterY = boardcards.getHeight() / 2;

            ImageView tokenView = new ImageView(token.getImage());
            tokenView.setFitWidth(50);  // Adjust token size as needed
            tokenView.setFitHeight(50);
            tokenView.setPreserveRatio(true);
            tokenView.setUserData("token");  // Mark this ImageView as a token for easy identification

            double xOffset = radius * Math.cos(angle) - tokenView.getFitWidth() / 2;
            double yOffset = radius * Math.sin(angle) - tokenView.getFitHeight() / 2;

            tokenView.setLayoutX(paneCenterX + xOffset);
            tokenView.setLayoutY(paneCenterY + yOffset);

            // Add the updated token view to the board
            boardcards.getChildren().add(tokenView);
        }
    }

    private void arrangeAnimalTokens() {
        double paneCenterX = boardcards.getWidth() / 2;
        double paneCenterY = boardcards.getHeight() / 2;
        double tokenRadius = radius + 40; // Increase radius by 40 units to place tokens outside the habitat circle

        for (AnimalCave cave : gameMap.getAnimalCaves()) {
            ImageView tokenView = new ImageView(cave.getTokenImage());
            tokenView.setFitWidth(50); // Width of the token
            tokenView.setFitHeight(50); // Height of the token
            tokenView.setPreserveRatio(true);

            double angle = Math.toRadians(360.0 * cave.getLocation() / gameMap.getHabitats().size());
            double xOffset = tokenRadius * Math.cos(angle);
            double yOffset = tokenRadius * Math.sin(angle);

            tokenView.setLayoutX(paneCenterX + xOffset - tokenView.getFitWidth() / 2);
            tokenView.setLayoutY(paneCenterY + yOffset - tokenView.getFitHeight() / 2);

            boardcards.getChildren().add(tokenView);
        }
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

    private void flipCard(int indexOfCard) {
        System.out.println("Flipping card at index " + indexOfCard);
        ImageView imageView = (ImageView) decks.getChildren().get(indexOfCard);

        if (imageView != null && cardsInGame.size() > indexOfCard) {
            Card card = cardsInGame.get(indexOfCard);
            String flippedCard = card.getType() + card.getCount();
            System.out.println("Flipped card: " + flippedCard);

            Image image = card.getImage();
            if (image != null) {
                imageView.setImage(image); // Set the card image to show its face
                processCardEffect(card);   // Process the effect of the card
            } else {
                System.err.println("Card image is null.");
            }
        } else {
            System.err.println("ImageView is null or index is out of bounds.");
        }
    }


    private void processCardEffect(Card card) {
        Player currentPlayer = getCurrentPlayer();
        int currentPlayerPosition = currentPlayer.getPosition();
        String currentAnimalTypeAtPosition = animalPositions[currentPlayerPosition];

        System.out.println("Current player: " + currentPlayer.getAnimalToken().getType());
        System.out.println("Current position: " + currentPlayerPosition);
        System.out.println("Animal at current position: " + currentAnimalTypeAtPosition);

        if (card instanceof AnimalCard) {
            AnimalCard animalCard = (AnimalCard) card;
            // Check if the card type matches the animal at the current player's position
            if (animalCard.getAnimalType().equalsIgnoreCase(currentAnimalTypeAtPosition)) {
                // Apply card effect which includes moving the player forward
                animalCard.applyEffect(currentPlayer, gameMap);
                currentPlayer.moveToken(animalCard.getCount(),gameMap);
                System.out.println(currentPlayer.getAnimalToken().getType() + " moves " + animalCard.getCount() + " steps forward to position " + currentPlayer.getPosition());
            } else {
                System.out.println("No match found, turn ends.");
            }
        } else if (card instanceof PirateCard) {
            PirateCard pirateCard = (PirateCard) card;
            // Apply the effect with a negative count to move backwards
            int backwardSteps = -pirateCard.getCount(); // Make the steps negative here
            currentPlayer.moveToken(backwardSteps, gameMap);
            System.out.println(currentPlayer.getAnimalToken().getType() + " moves " + backwardSteps + " steps backward to position " + currentPlayer.getPosition());
        }

        // Optionally, after processing the card effect, update the UI or game state
        // updateGameBoard();
    }

    private void updateCurrentPlayerDisplay(String animalType) {
        currentPlayer.setText(animalType + "'s turn");
    }

    public Player getCurrentPlayer() {
        if (inPlayPlayer == null) {
            inPlayPlayer = new Player(new AnimalToken("fish", 0)); // Initial setup
        }
        return inPlayPlayer;
    }
}

