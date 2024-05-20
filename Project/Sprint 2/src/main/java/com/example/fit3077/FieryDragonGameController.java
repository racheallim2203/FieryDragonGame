package com.example.fit3077;

import com.example.fit3077.cards.AnimalCard;
import com.example.fit3077.cards.Card;
import com.example.fit3077.cards.PirateCard;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

public class FieryDragonGameController{ //implements Initializable
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

    @FXML
    private ImageView winner;

    @FXML
    private Pane winbg;


    private ArrayList<Card> cardsInGame;
    private Player inPlayPlayer;
    private List<Player> playerList;    // list of all players in the game

    private final double radius = 180; // Adjust as needed for your layout

    private GameMap gameMap;

    private final AnimalType[] animalPositions = new AnimalType[24]; // Assuming 24 positions available on the game board

    private final Map<AnimalType, ImageView> tokenViews = new HashMap<>();// Stores token views by animal type

    private int userInput;

    public void setUserInput(int userInput) {
        this.userInput = userInput;
    }

    @FXML
    void startGame() {
        System.out.println("Restart Game");

        winbg.setVisible(false); // Hide the win background if visible from a previous game.

        // set players
        playerList = setPlayers(userInput);
        nextPlayer();   // set first player

        // Reset all game data and UI components to their initial state
        getCurrentPlayer().resetPosition();
        instructions.setText("-");

        // UI and game logic initialization
        Platform.runLater(() -> {
            shuffleAndDisplayAnimals(); // Shuffle animal positions and display on the board.
            initializeTokenViews(); // Set up visual representation of player tokens
            displayShuffledDeck(); // Display the shuffled deck of cards.
            initializeImageView(); // Initialize card images for interaction.

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

    public void initializeGame() {
        gameMap = new GameMap(userInput); // Initialize gameMap which sets up the habitats
        startGame();
    }

    private void displayShuffledDeck() {
        decks.getChildren().clear(); // Clear existing children to avoid duplicates
        // Instantiate the deck and shuffle it
        DeckOfCards deck = new DeckOfCards();
        deck.shuffle();
        // Retrieve the shuffled list of cards
        cardsInGame = new ArrayList<>(deck.getCards()); // Store the shuffled cards in the member variable.

        // Iterate over the shuffled deck and the imageView IDs together
        for (int i = 0; i < cardsInGame.size(); i++) {
            Card card = cardsInGame.get(i);
            int count = card.getCount();
            String cardFileName = "";
            cardFileName = card.getImageFileName();

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
            initialiseCaveView(); // Call to arrange tokens after animals are set
        });
    }

    private void shuffleAndDisplayAnimals() {
        // Shuffle the habitats to get a new random arrangement of the animals
        Collections.shuffle(gameMap.getVolcanoList());
        // Now call the method to display animals in a circle
        arrangeAnimalsInCircle();
    }

    private void initialiseCaveView() {
        double paneCenterX = boardcards.getWidth() / 2;
        double paneCenterY = boardcards.getHeight() / 2;
        double tokenRadius = radius + 60; // Increase radius by 40 units to place tokens outside the habitat circle

        double angleIncrement;
        int numTokens = gameMap.getAnimalCaves().size();
        if (gameMap.getAnimalCaves().size() == 3){
            angleIncrement = 360.0 / 4;
        }
        else {
            angleIncrement = 360.0 / numTokens;
        }

        for (int i = 0; i < numTokens; i++) {
            AnimalCave cave = gameMap.getAnimalCaves().get(i);
            ImageView tokenView = new ImageView(cave.getTokenImage());
            tokenView.setFitWidth(60); // Width of the token
            tokenView.setFitHeight(60); // Height of the token
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

            double angleIncrement;
            int numTokens = gameMap.getAnimalCaves().size();
            if (gameMap.getAnimalCaves().size() == 3){
                angleIncrement = 360.0 / 4;
            }
            else {
                angleIncrement = 360.0 / numTokens; // Calculate the angle increment based on the number of tokens.
            }

            for (int i = 0; i < numTokens; i++) {
                AnimalCave cave = gameMap.getAnimalCaves().get(i);
                AnimalType tokenType = cave.getType();
                Image tokenImage = new Image(getClass().getResourceAsStream("images/" + tokenType.toString().toLowerCase() + "token.png"));
                ImageView tokenView = new ImageView(tokenImage);
                tokenView.setFitWidth(50);
                tokenView.setFitHeight(50);
                tokenView.setPreserveRatio(true);

                // Calculate the angle for this token's position on the circle.
                double angle = Math.toRadians(angleIncrement * i);
                double xOffset = tokenRadius * Math.cos(angle);
                double yOffset = tokenRadius * Math.sin(angle);

                // Set the layout X and Y based on the calculated offsets.
                double tokenInitialLayoutX = paneCenterX + xOffset - tokenView.getFitWidth() / 2;
                double tokenInitialLayoutY = paneCenterY + yOffset - tokenView.getFitHeight() / 2;

                // Jeh Guan - save the location data of cave in animal token (one player only)
                // notes: this part should need to be modified for multiple player
                AnimalType currentPlayerTokenAnimalType = getCurrentPlayer().getAnimalToken().getType();
                System.out.println("Cave's type is: " + tokenType);
                System.out.println("Current Player's type is " + currentPlayerTokenAnimalType);

                if (currentPlayerTokenAnimalType == tokenType){
                    System.out.println("Match!!");
                    getCurrentPlayer().getAnimalToken().setInitialLayoutX(tokenInitialLayoutX);
                    getCurrentPlayer().getAnimalToken().setInitialLayoutY(tokenInitialLayoutY);
                    if(currentPlayerTokenAnimalType == AnimalType.DRAGON){
                        System.out.println("dragon initial x: " + tokenInitialLayoutX);
                        System.out.println("dragon initial y: " + tokenInitialLayoutY);

                    }
                }
                else {
                    System.out.println("Not match!!!");
                }

                tokenView.setLayoutX(tokenInitialLayoutX);
                tokenView.setLayoutY(tokenInitialLayoutY);

                // Add the ImageView to the boardcards Pane.
                boardcards.getChildren().add(tokenView);
                tokenViews.put(tokenType, tokenView); // Store the view by animal type in a map for easy access.

                nextPlayer();
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
        if (!cardsInGame.get(indexOfCard).isFlipped()){
//            cardsInGame.get(indexOfCard).setFlipped(true);
            System.out.println("Flipping card at index " + indexOfCard);
            ImageView imageView = (ImageView) decks.getChildren().get(indexOfCard);

            if (imageView != null && cardsInGame.size() > indexOfCard) {
                Card card = cardsInGame.get(indexOfCard);

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
    }


    private void processCardMovement(Card card) {
        Player currentPlayer = getCurrentPlayer();
        int currentPlayerPosition = currentPlayer.getPosition();
//          String currentAnimalTypeAtPosition = animalPositions[currentPlayerPosition];

        AnimalType currentAnimalTypeAtPosition;
        if (currentPlayer.getAnimalToken().getStepTaken() == 0 && !currentPlayer.getAnimalToken().getIsOut()){
            System.out.println("in the cave!!");
            currentAnimalTypeAtPosition = currentPlayer.getAnimalToken().getType();
        }
        else {
            currentAnimalTypeAtPosition = animalPositions[currentPlayerPosition];
        }

        System.out.println("Current player's token type: " + currentPlayer.getAnimalToken().getType());
        System.out.println("Current player position: " + currentPlayerPosition);
        System.out.println("Animal type of habitat at current position: " + currentAnimalTypeAtPosition);

        if (card.getCardType() == CardType.animalCard) {

//            AnimalCard animalCard = (AnimalCard) card;

            // Check if the card type matches the animal at the current player's position
            if (card.matchesType(currentAnimalTypeAtPosition)) {
                int predictStepTaken = currentPlayer.getAnimalToken().getStepTaken() + card.getCount();
                int newPosition = (currentPlayerPosition+card.getCount()) % animalPositions.length ;
                if (!gameMap.getHabitats().get(newPosition).isContainAnimalToken() || predictStepTaken == 26){
                    // Apply card effect which includes moving the player forward
                    card.applyMovement(currentPlayer, gameMap);
                    instructions.setText(" moves " + card.getCount() + " steps forward");
                    System.out.println(currentPlayer.getAnimalToken().getType() + " moves " + card.getCount() + " steps forward to position " + currentPlayer.getPosition());

                    // SAM - check if it reaches its cave - hvn tested, feel free to comment
                    if (currentPlayer.getAnimalToken().getStepTaken() == 26) {
                        System.out.println("WINNNNNNNNN");
                        showWin();

                    }
                }
                else {
                    for (int i = 0; i < decks.getChildren().size(); i++) {
                        cardsInGame.get(i).setFlipped(true);
                    }

                    // wait for 2 seconds to allow players to understand game state and display instruction text
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));

                    instructions.setText("Token can't move, occupied.");

                    pause.setOnFinished(event -> {
                        // change player turns and flip unfolded cards back
                        nextPlayer();
                        flipCardsBack();
                    });
                    pause.play();

                }

            } else {
                for (int i = 0; i < decks.getChildren().size(); i++) {
                    cardsInGame.get(i).setFlipped(true);
                }

                // wait for 2 seconds to allow players to understand game state and display instruction text
                PauseTransition pause = new PauseTransition(Duration.seconds(2));

                instructions.setText("No match found, turn ends.");

                pause.setOnFinished(event -> {
                    // change player turns and flip unfolded cards back
                    nextPlayer();
                    flipCardsBack();

                });
                pause.play();


            }

        } else if (card.getCardType() == CardType.pirateCard) {

            if (currentPlayer.getAnimalToken().getStepTaken() == 0 && !currentPlayer.getAnimalToken().getIsOut()) {

                for (int i = 0; i < decks.getChildren().size(); i++) {
                    cardsInGame.get(i).setFlipped(true);
                }

                // wait for 2 seconds to allow players to understand game state and display instruction text
                PauseTransition pause = new PauseTransition(Duration.seconds(2));

                instructions.setText("No match found, turn ends.");

                pause.setOnFinished(event -> {
                    // change player turns and flip unfolded cards back
                    nextPlayer();
                    flipCardsBack();

                });
                pause.play();

            } else {

                    int newPosition = ((currentPlayerPosition+ (-card.getCount())) + animalPositions.length) % animalPositions.length;
                if (!gameMap.getHabitats().get(newPosition).isContainAnimalToken()){
                    // wait for 2 seconds to allow players to understand game state and display instruction text
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));

//                    instructions.setText(" moves " + pirateCard.getCount() + " steps backward");
//                    pirateCard.applyMovement(currentPlayer, gameMap, pirateCard);
                instructions.setText(" moves " + card.getCount() + " steps backward");
                card.applyMovement(currentPlayer, gameMap);

                    // Jeh Guan - pirate card should not end the player's turn based on the basic game rule
//                pause.setOnFinished(event -> {
//                    // change player turns and flip unfolded cards back
//                    nextPlayer();
//                    flipCardsBack();
//
//                });
//                pause.play();

                    System.out.println(currentPlayer.getAnimalToken().getType() + " moves " + card.getCount() + " steps backward to position " + currentPlayer.getPosition());
                }
                else {
                    for (int i = 0; i < decks.getChildren().size(); i++) {
                        cardsInGame.get(i).setFlipped(true);
                    }

                    // wait for 2 seconds to allow players to understand game state and display instruction text
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));

                    instructions.setText("Token can't move, occupied.");

                    pause.setOnFinished(event -> {
                        // change player turns and flip unfolded cards back
                        nextPlayer();
                        flipCardsBack();
                    });
                    pause.play();
                }
            }
        }

        // Optionally, after processing the card effect, update the UI or game state
        updateGameBoard();
    }

    private void flipCardsBack(){
        // Iterate through the deck and flip over any uncovered cards
        for (int i = 0; i < decks.getChildren().size(); i++) {
            cardsInGame.get(i).setFlipped(false);
            ImageView imageView = (ImageView) decks.getChildren().get(i);
            // Check if the card is currently uncovered (i.e., showing its face)
            if (imageView.getImage() != null) {
                // Flip the card back over to cover it
//                imageView.setImage(new Image(getClass().getResourceAsStream("images/coveredcard.png")));
            }
        }
    }

    private void updateGameBoard() {

        Player currentPlayer = getCurrentPlayer();
        AnimalToken token = currentPlayer.getAnimalToken();
        ImageView tokenView = tokenViews.get(token.getType()); // Retrieve the token's ImageView
//        System.out.println("Token View is null");
        if (tokenView != null) {

            int currentStepTaken = token.getStepTaken();
            System.out.println("Player stepTaken after flip card: " + currentStepTaken);
            if (currentStepTaken == gameMap.getNumberOfStepToWin()){
                System.out.println("dragon cave x: " + token.getInitialLayoutX());
                System.out.println("dragon cave y: " + token.getInitialLayoutY());

                tokenView.setLayoutX(token.getInitialLayoutX());
                tokenView.setLayoutY(token.getInitialLayoutY());
            }
            else {
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


        }

        System.out.println("Game board updated.\n");
    }

    private void updateCurrentPlayerDisplay(AnimalType animalType) {
        currentPlayer.setText(animalType.toString().toLowerCase() + "'s turn");
    }

    public List<Player> setPlayers(int numberOfPlayer){
        List<Player> players = new ArrayList<>();

        if(numberOfPlayer == 2){
            players.add(new Player(new AnimalToken(AnimalType.FISH), 0));
            players.add(new Player(new AnimalToken(AnimalType.DRAGON), 1));
        }
        else {
            // create list of players
            players.add(new Player(new AnimalToken(AnimalType.FISH), 0));
            players.add(new Player(new AnimalToken(AnimalType.PUFFERFISH), 1));
            players.add(new Player(new AnimalToken(AnimalType.DRAGON), 2));
            players.add(new Player(new AnimalToken(AnimalType.OCTOPUS), 3));
            for (int j = players.size()-1; j >= numberOfPlayer; j--){
                players.remove(j);
            }
        }
        return players;
    }

    public void nextPlayer(){
        if (inPlayPlayer == null || inPlayPlayer.getPlayerID()+1 == playerList.size()){
            inPlayPlayer = playerList.get(0);
        } else {
            inPlayPlayer = playerList.get(inPlayPlayer.getPlayerID() + 1);
        }

        updateCurrentPlayerDisplay(inPlayPlayer.getAnimalToken().getType());
        instructions.setText("-");
    }

    public Player getCurrentPlayer() {
        return inPlayPlayer;
    }

    private void showWin(){
        System.out.println("YA LETS GO");

        winbg.setVisible(true);

        BackgroundFill backgroundFill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        winbg.setBackground(background);

        String animal = getCurrentPlayer().getAnimalToken().getType().toString().toLowerCase();
        String imageName = animal + "token.png";
        String pathName = "images/" + imageName;
        winner.setImage(new Image(getClass().getResourceAsStream(pathName)));
        System.out.println("pathName:" + pathName);
    }

}




