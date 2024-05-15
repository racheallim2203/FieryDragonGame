package com.example.fit3077;

import com.example.fit3077.cards.AnimalCard;
import com.example.fit3077.cards.Card;
import com.example.fit3077.cards.PirateCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Label instructions;

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

    private final double radius = 180; // Adjust as needed for your layout

    private GameMap gameMap;

    private final AnimalType[] animalPositions = new AnimalType[24]; // Assuming 24 positions available on the game board

    private final Map<String, ImageView> tokenViews = new HashMap<>(); // Stores token views by animal type

    @FXML
    void startGame() {
        System.out.println("Restart Game");

        // Reset all game data and UI components to their initial state
        getCurrentPlayer().resetPosition();
        instructions.setText("-");

        // UI and game logic initialization
        Platform.runLater(() -> {
            shuffleAndDisplayAnimals();
            initializeTokenViews();
            displayShuffledDeck();
            initializeDeck();
            initializeImageView();

            // Set the current player to the initial state and update UI
            resetPlayer();
            updateCurrentPlayerDisplay(AnimalType.FISH);

            System.out.println("Game started.");
        });
    }


    private void resetPlayer() {
        // Get the current player and reset their position
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.resetPosition();
        // Update the game board to reflect the reset state
        updateGameBoard();
    }

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

        for (int i = 0; i < decks.getChildren().size(); i++) {
            Card cardDealt = deck.dealTopCard();                    // sam - IDK WHATS THIS
            if (cardDealt != null) { // Ensure that a card was dealt
                cardsInGame.add(cardDealt);
            }
        }

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
            int count = card.getCount();
            String cardFileName = "";

            if (AnimalCard.class.isInstance(card)){
                AnimalCard animalCard = (AnimalCard) card;               // TODO: downcast, can someone help me to modify it?
                cardFileName = animalCard.getAnimalType().toString().toLowerCase() + count + ".png";
            } else {
                cardFileName = "piratecard" + count + ".png"; // Construct the file name based on type and count
            }


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
//            System.out.println(decks.getChildren().size());


        }


    }

    /**
     * This method will arrange images in a circle within the game board.
     */
    private void arrangeAnimalsInCircle() {
        // Shuffle each habitat's animal cards and the list of habitats
        gameMap.getVolcanoList().forEach(Volcano::shuffle);
        Collections.shuffle(gameMap.getVolcanoList());

        Platform.runLater(() -> {
            boardcards.getChildren().clear();
            double paneCenterX = boardcards.getWidth() / 2;
            double paneCenterY = boardcards.getHeight() / 2;
            int totalAnimals = gameMap.getVolcanoList().stream().mapToInt(h -> h.getVolcanoCards().size()).sum();
            int animalIndex = 0;

            for (Volcano volcanoCard : gameMap.getVolcanoList()) {
                for (Habitat habitat : volcanoCard.getVolcanoCards()) {
                    Image habitatImage = habitat.getHabitatImage();
                    ImageView imageView = new ImageView(habitatImage);
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    imageView.setPreserveRatio(true);

                    double angle = Math.toRadians(360.0 * animalIndex / totalAnimals);
                    double xOffset = radius * Math.cos(angle);
                    double yOffset = radius * Math.sin(angle);

                    imageView.setLayoutX(paneCenterX + xOffset - imageView.getFitWidth() / 2);
                    imageView.setLayoutY(paneCenterY + yOffset - imageView.getFitHeight() / 2);

                    boardcards.getChildren().add(imageView);
                    animalPositions[animalIndex % animalPositions.length] = habitat.getAnimalType();
                    animalIndex++;
                }
            }
            arrangeAnimalTokens(); // Call to arrange tokens after animals are set
        });
    }

    private void shuffleAndDisplayAnimals() {
        // Shuffle the habitats to get a new random arrangement of the animals
        Collections.shuffle(gameMap.getVolcanoList());
        // Now call the method to display animals in a circle
        arrangeAnimalsInCircle();
    }

    private void arrangeAnimalTokens() {
        double paneCenterX = boardcards.getWidth() / 2;
        double paneCenterY = boardcards.getHeight() / 2;
        double tokenRadius = radius + 60; // Increase radius by 40 units to place tokens outside the habitat circle

        int numTokens = gameMap.getAnimalCaves().size();
        double angleIncrement = 360.0 / numTokens;

        for (int i = 0; i < numTokens; i++) {
            AnimalCave cave = gameMap.getAnimalCaves().get(i);
            ImageView tokenView = new ImageView(cave.getTokenImage());
            tokenView.setFitWidth(50); // Width of the token
            tokenView.setFitHeight(50); // Height of the token
            tokenView.setPreserveRatio(true);

            // Calculate the angle for this token's position on the circle
            double angle = Math.toRadians(angleIncrement * i);
            double xOffset = tokenRadius * Math.cos(angle);
            double yOffset = tokenRadius * Math.sin(angle);

            tokenView.setLayoutX(paneCenterX + xOffset - tokenView.getFitWidth() / 2);
            tokenView.setLayoutY(paneCenterY + yOffset - tokenView.getFitHeight() / 2);

            boardcards.getChildren().add(tokenView);
        }
    }

    private void initializeTokenViews() {
        Platform.runLater(() -> {

            double paneCenterX = boardcards.getWidth() / 2;
            double paneCenterY = boardcards.getHeight() / 2;
            double tokenRadius = radius + 60; // Adjust the radius to position tokens correctly.

            int numTokens = gameMap.getAnimalCaves().size();
            double angleIncrement = 360.0 / numTokens; // Calculate the angle increment based on the number of tokens.

            for (int i = 0; i < numTokens; i++) {
                AnimalCave cave = gameMap.getAnimalCaves().get(i);
                AnimalType tokenType = cave.getType();
                Image tokenImage = new Image(getClass().getResourceAsStream("images/" + tokenType + "token.png"));
                ImageView tokenView = new ImageView(tokenImage);
                tokenView.setFitWidth(50);
                tokenView.setFitHeight(50);
                tokenView.setPreserveRatio(true);

                // Calculate the angle for this token's position on the circle.
                double angle = Math.toRadians(angleIncrement * i);
                double xOffset = tokenRadius * Math.cos(angle);
                double yOffset = tokenRadius * Math.sin(angle);

                // Set the layout X and Y based on the calculated offsets.
                tokenView.setLayoutX(paneCenterX + xOffset - tokenView.getFitWidth() / 2);
                tokenView.setLayoutY(paneCenterY + yOffset - tokenView.getFitHeight() / 2);

                // Add the ImageView to the boardcards Pane.
                boardcards.getChildren().add(tokenView);
                tokenViews.put(String.valueOf(tokenType), tokenView); // Store the view by animal type in a map for easy access.
            }
        });
    }




    /**
     * This will add a number to each ImageView and set the image to be the back of a Card
     */
    private void initializeImageView() {
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
//            String flippedCard = card.getType() + card.getCount();
//            System.out.println("Flipped card: " + flippedCard);

            Image image = card.getImage();
            if (image != null) {
                imageView.setImage(image); // Set the card image to show its face
                processCardMovement(card);   // Process the effect of the card
            } else {
                System.err.println("Card image is null.");
            }
        } else {
            System.err.println("ImageView is null or index is out of bounds.");
        }
    }


    private void processCardMovement(Card card) {
        Player currentPlayer = getCurrentPlayer();
        int currentPlayerPosition = currentPlayer.getPosition();
//          String currentAnimalTypeAtPosition = animalPositions[currentPlayerPosition];

        AnimalType currentAnimalTypeAtPosition;
        if (currentPlayer.getAnimalToken().getStepTaken() == 0){
            currentAnimalTypeAtPosition = currentPlayer.getAnimalToken().getType();
        }
        else {
            currentAnimalTypeAtPosition = animalPositions[currentPlayerPosition];

        }

        System.out.println("Current player: " + currentPlayer.getAnimalToken().getType());
        System.out.println("Current position: " + currentPlayerPosition);
        System.out.println("Animal at current position: " + currentAnimalTypeAtPosition);

        if (card instanceof AnimalCard) {

            AnimalCard animalCard = (AnimalCard) card;

            // Check if the card type matches the animal at the current player's position
            if (animalCard.getAnimalType().equals(currentAnimalTypeAtPosition)) {

//                // Apply card effect which includes moving the player forward
                animalCard.applyMovement(currentPlayer, gameMap, animalCard);
                instructions.setText(" moves " + animalCard.getCount() + " steps forward");
                System.out.println(currentPlayer.getAnimalToken().getType() + " moves " + animalCard.getCount() + " steps forward to position " + currentPlayer.getPosition());

                // SAM - check if it reaches its cave - hvn tested, feel free to comment
                /*
                if (currentPlayer.getAnimalToken().getStepTaken() + animalCard.getCount() == 24) {
                    System.out.println("WINNNNNNNNN");
                    currentPlayer.moveTokenToCave(gameMap);

                } else {

                    // Apply card effect which includes moving the player forward
                    animalCard.applyMovement(currentPlayer, gameMap, animalCard);
                    instructions.setText(" moves " + animalCard.getCount() + " steps forward");
                    System.out.println(currentPlayer.getAnimalToken().getType() + " moves " + animalCard.getCount() + " steps forward to position " + currentPlayer.getPosition());
                }
                */

            } else {
                instructions.setText("No match found, turn ends.");
            }

        } else if (card instanceof PirateCard) {

            if (currentPlayer.getAnimalToken().getStepTaken() == 0 && !currentPlayer.getAnimalToken().getIsOut()) {
                instructions.setText("No match found, turn ends.");

            } else {
                PirateCard pirateCard = (PirateCard) card;
                pirateCard.applyMovement(currentPlayer, gameMap, pirateCard);
                instructions.setText(" moves " + pirateCard.getCount() + " steps backward");
                System.out.println(currentPlayer.getAnimalToken().getType() + " moves " + pirateCard.getCount() + " steps backward to position " + currentPlayer.getPosition());
            }
        }

        // Optionally, after processing the card effect, update the UI or game state
        updateGameBoard();
    }

    private void updateGameBoard() {
        Player currentPlayer = getCurrentPlayer();
        AnimalToken token = currentPlayer.getAnimalToken();
        ImageView tokenView = tokenViews.get(token.getType()); // Retrieve the token's ImageView

        if (tokenView != null) {
            double paneCenterX = boardcards.getWidth() / 2;
            double paneCenterY = boardcards.getHeight() / 2;
            int position = currentPlayer.getPosition();

            if (currentPlayer.getAnimalToken().getStepTaken() != 0 || currentPlayer.getAnimalToken().getIsOut()){
                double tokenRadius = radius + 30;

                //24 is the size of total number of animals in the list to form a gameboard
                double angle = Math.toRadians(360.0 * position / 24);
                double xOffset = tokenRadius * Math.cos(angle);
                double yOffset = tokenRadius * Math.sin(angle);

                tokenView.setLayoutX(paneCenterX + xOffset - tokenView.getFitWidth() / 2);
                tokenView.setLayoutY(paneCenterY + yOffset - tokenView.getFitHeight() / 2);
            }

        }

        System.out.println("Game board updated.");
    }

    private void updateCurrentPlayerDisplay(AnimalType animalType) {
        currentPlayer.setText(animalType.toString().toLowerCase() + "'s turn");
    }

    public Player getCurrentPlayer() {
        if (inPlayPlayer == null) {
            inPlayPlayer = new Player(new AnimalToken(AnimalType.PUFFERFISH)); // Initial setup

        }
        return inPlayPlayer;
    }
}




