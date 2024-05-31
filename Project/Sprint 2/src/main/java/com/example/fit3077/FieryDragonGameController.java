package com.example.fit3077;

import com.example.fit3077.cards.Card;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

public class FieryDragonGameController{ //implements Initializable

    @FXML
    private Button startTutorial;

    @FXML
    private Pane boardcards;

    @FXML
    private FlowPane indicator;

    @FXML
    private Label instructions;

    @FXML
    private Button backToGame;

    @FXML
    private Label tutorialMode;


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
    private Label steps;

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

    @FXML
    private Label tutorial;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label tutorialCards;

    @FXML
    private HBox hBox;

    @FXML
    private FlowPane tutorialPanel;

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

    private boolean inTutorialMode = false;

    // TUTORIAL MODE
    @FXML
    void startTutorial() {
        inTutorialMode = true;
        System.out.println("Starting Tutorial");
        tutorialMode.setStyle("-fx-font-size: 19px; -fx-font-weight: bold;");
        tutorialMode.setText("You are currently in Tutorial Mode!");
        steps.setText("Welcome to Fiery Dragon Game Tutorial");
        tutorialPanel.setVisible(true);
        // Hide GridPane and HBox during tutorial
        gridPane.setVisible(false);
        hBox.setVisible(false);
        // Update the game board to reflect these positions
        updateGameBoard();
        startGame();  // Start the game in tutorial mode

    }

    public void setTutorialPlayer(int numberOfPlayer){
        playerList.clear(); // Clear any existing players in the list
        // Add the movable FISH
        playerList.add(new Player(new AnimalToken(AnimalType.FISH), 0, -1, false)); // FISH can move from the start position

        // Add static tokens with their fixed positions
        playerList.add(new Player(new AnimalToken(AnimalType.PUFFERFISH), 1, 8, true));
        playerList.add(new Player(new AnimalToken(AnimalType.DRAGON), 2, 15, true));
        playerList.add(new Player(new AnimalToken(AnimalType.OCTOPUS), 3, 20, true));
    }

    @FXML
    void backToGame() {
        inTutorialMode = false;
        initializeGame();

    }

    @FXML
    void startGame() {
        System.out.println("Restart Game");

        winbg.setVisible(false); // Hide the win background if visible from a previous game.
        // Show GridPane and HBox if not in tutorial mode
        hBox.setVisible(!inTutorialMode);
        gridPane.setVisible(!inTutorialMode);
        indicator.setVisible(false);
        backToGame.setVisible(inTutorialMode);

        // Initialize or re-initialize player list based on the mode
        if (!inTutorialMode) {
            playerList = setPlayers(userInput);
        } else {
            setTutorialPlayer(4); // Ensure tutorial players are set if in tutorial mode
        }
        inPlayPlayer = null;
        nextPlayer();   // set first player

        // Reset all game data and UI components to their initial state
//        getCurrentPlayer().resetPosition();
        instructions.setText("-");

        // UI and game logic initialization
        Platform.runLater(() -> {
            shuffleAndDisplayAnimals(); // Shuffle animal positions and display on the board.
            initializeTokenViews(); // Set up visual representation of player tokens
            displayShuffledDeck(); // Display the shuffled deck of cards.
            initializeImageView(); // Initialize card images for interaction.

            // Set the current player to the initial state and update UI
//            resetPlayer();
//            updateCurrentPlayerDisplay(AnimalType.FISH);

            if (inTutorialMode) {
                steps.setText("You are in tutorial mode! Let's start by flipping a card! CLICK ON A CARD in the deck");
            }

            System.out.println("Game started.");
        });
    }



    private void resetPlayer() {
        // Get the current player and reset their position
        Player currentPlayer = getCurrentPlayer();
//        currentPlayer.resetPosition();
//        // Update the game board to reflect the reset state
//        updateGameBoard();
        currentPlayer.getAnimalToken().setStepTaken(0);
        currentPlayer.getAnimalToken().setIsOut(false);
        currentPlayer.resetPosition();
        updateGameBoard(); // Reflect the reset state on the game board
    }

    public void initializeGame() {
        gameMap = new GameMap(userInput); // Initialize gameMap which sets up the habitats
        tutorialPanel.setVisible(false);
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
        indicator.setVisible(inTutorialMode);
        if (!cardsInGame.get(indexOfCard).isFlipped()){
            cardsInGame.get(indexOfCard).setFlipped(true);
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
        System.out.println("Before card move: Player - " + currentPlayer.getAnimalToken().getType() + ", StepTaken - " + currentPlayer.getAnimalToken().getStepTaken() + ", IsOut - " + currentPlayer.getAnimalToken().getIsOut());
        int currentPlayerPosition = currentPlayer.getPosition();
        boolean conflictResolved = false; // To show tutorialText when token being sent back home

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

            // Check if the card type matches the animal at the current player's position
            if (card.matchesType(currentAnimalTypeAtPosition)) {
                int predictStepTaken = currentPlayer.getAnimalToken().getStepTaken() + card.getCount();
                int newPosition = (currentPlayerPosition+card.getCount()) % animalPositions.length ;

                // TO SEND CURRENT FIXED OTHER PLAYERS TOKEN BACK HOME IF REACHES THEIR HABITAT
                if (inTutorialMode) {
                    for (Player other : playerList) {
                        if (other != currentPlayer && other.getPosition() == newPosition) {
                            tutorialMode.setText("Good Job! You got to send" + " "  + other.getAnimalToken().getType() +" back home!");
                            steps.setText("OOF! You reach a habitat occupied by another player. You are able to send them back home!! FLIP AGAIN");
                            System.out.println(other.getAnimalToken().getType() + " found at the new position: " + newPosition + " and sent home.");
                            other.resetPosition();
                            updateGameBoard();
                            conflictResolved = true;
                            break;
                        }
                    }
                }
                if (!conflictResolved && (!gameMap.getHabitats().get(newPosition).isContainAnimalToken() || predictStepTaken == 26)) {
                    // Apply card effect which includes moving the player forward
                    card.applyMovement(currentPlayer, gameMap);
                    instructions.setText(" moves " + card.getCount() + " steps forward");
                    System.out.println(currentAnimalTypeAtPosition + " moves " + card.getCount() + " steps forward to position " + currentPlayer.getPosition());

                    if (inTutorialMode) {
                        tutorialMode.setText("NICE! You move " + card.getCount() + " steps forward");
                        steps.setText("GOOD JOB! You successfully flipped a card that matches the animal you are on! The number of animals in the card shows how many steps you can move forward. Its still your turn. Flip another card!");
                    }

                    // SAM - check if it reaches its cave - hvn tested, feel free to comment
                    if (currentPlayer.getAnimalToken().getStepTaken() == 26) {
                        System.out.println("WINNNNNNNNN");
                        tutorialMode.setText("CONGRATS! You won!");
                        steps.setText("WOHOO! You have successfully reached back your cave after one round! Its time to start the real game NOW :D");
                        showWin();

                    }
                }
                else {
                    // This block will now properly execute when there's either a conflict, or there's an animal token at the new position and the step count is not predicting a win.
                    for (int i = 0; i < decks.getChildren().size(); i++) {
                        cardsInGame.get(i).setFlipped(true);
                    }
                    // wait for 2 seconds to allow players to understand game state and display instruction text
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    instructions.setText("Token sent back home");
                    // RAC - Sending token back home
                    card.applyMovement(currentPlayer, gameMap);
                    sendTokenHome(newPosition);
                    updateGameBoard();
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
                if (inTutorialMode) {
                    tutorialMode.setText("Flipped card doesn't match to" + " " + currentAnimalTypeAtPosition);
                    steps.setText("Better Memory next time! You did not get the cards correct to continue moving forward! Look for" + " " + currentAnimalTypeAtPosition + " Card. FLIP AGAIN!");
                }

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
                if (inTutorialMode) {
                    tutorialMode.setText("Pirate Card no effect because you are not out of cave!");
                    steps.setText("Better Memory next time! You did not get the cards correct to continue moving forward! Look for" + " " + currentAnimalTypeAtPosition + " Card. FLIP AGAIN!");
                }

                pause.setOnFinished(event -> {
                    // change player turns and flip unfolded cards back
                    nextPlayer();
                    flipCardsBack();

                });
                pause.play();

            } else {
                int newPosition = ((currentPlayerPosition+ (-card.getCount())) + animalPositions.length) % animalPositions.length;
                // TO SEND CURRENT FIXED OTHER PLAYERS TOKEN BACK HOME IF REACHES THEIR HABITAT
                if (inTutorialMode) {
                    for (Player other : playerList) {
                        if (other != currentPlayer && other.getPosition() == newPosition) {
                            tutorialMode.setText("Good Job! You got to send" + " "  + other.getAnimalToken().getType() +" back home!");
                            steps.setText("OOF! You reach a habitat occupied by another player. You are able to send them back home! FLIP AGAIN!!");
                            System.out.println(other.getAnimalToken().getType() + " found at the new position: " + newPosition + " and sent home.");
                            other.resetPosition();
                            updateGameBoard();
                            conflictResolved = true;
                            break;
                        }
                    }
                }

                if (!conflictResolved && (!gameMap.getHabitats().get(newPosition).isContainAnimalToken())) {
                    // wait for 2 seconds to allow players to understand game state and display instruction text
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));

                    instructions.setText(" moves " + card.getCount() + " steps backward");
                    card.applyMovement(currentPlayer, gameMap);
                    if (inTutorialMode) {
                        tutorialMode.setText("OPPS! You move " + card.getCount() + " steps backward");
                        steps.setText("AISH! You flipped the pirate card that you should avoid! The number of pirates in the card shows how many steps you should move backward. Its still your turn. Flip another card!");
                    }

                    System.out.println(currentPlayer.getAnimalToken().getType() + " moves " + card.getCount() + " steps backward to position " + currentPlayer.getPosition());
                }
                else {
                    for (int i = 0; i < decks.getChildren().size(); i++) {
                        cardsInGame.get(i).setFlipped(true);
                    }

                    // wait for 2 seconds to allow players to understand game state and display instruction text
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));

                    // RAC - Sending token back home
                    card.applyMovement(currentPlayer, gameMap);
                    sendTokenHome(newPosition);

                    pause.setOnFinished(event -> {
                        // change player turns and flip unfolded cards back
                        nextPlayer();
                        flipCardsBack();
                    });
                    pause.play();
                }
            }
        }

        if (card.getCardType() == CardType.swapCard) {
            Player closestPlayer = null;
            int closestDistance = Integer.MAX_VALUE;
            int totalPositions = gameMap.getHabitats().size(); // Total number of positions on the board

            // Find the closest player who is out of their cave
            for (Player otherPlayer : playerList) {
                if (otherPlayer != currentPlayer && otherPlayer.getAnimalToken().getIsOut()) {
                    int forwardDistance = (otherPlayer.getPosition() - currentPlayer.getPosition() + totalPositions) % totalPositions;
                    int backwardDistance = (currentPlayer.getPosition() - otherPlayer.getPosition() + totalPositions) % totalPositions;

                    // Determine the smaller of the two possible distances
                    int smallerDistance = Math.min(forwardDistance, backwardDistance);

                    if (smallerDistance < closestDistance) {
                        closestDistance = smallerDistance;
                        closestPlayer = otherPlayer;
                    }
                }
            }

            if (closestPlayer != null) {
                int tempPosition = currentPlayer.getPosition();
                int newCurrentPlayerPosition = closestPlayer.getPosition();
                int newClosestPlayerPosition = tempPosition;

                // Calculate distance and direction to move each player
                int currentPlayerStepChange = (newCurrentPlayerPosition - tempPosition + totalPositions) % totalPositions;
                int closestPlayerStepChange = (newClosestPlayerPosition - closestPlayer.getPosition() + totalPositions) % totalPositions;

                // Adjust for backward movement
                if (currentPlayerStepChange > totalPositions / 2) { // It went the "long way" around, meaning backwards
                    currentPlayerStepChange -= totalPositions;
                }
                if (closestPlayerStepChange > totalPositions / 2) { // Similarly for the other player
                    closestPlayerStepChange -= totalPositions;
                }

                // Apply the calculated step changes
                currentPlayer.moveToken(currentPlayerStepChange, gameMap);
                closestPlayer.moveToken(closestPlayerStepChange, gameMap);

                System.out.println("Swapped positions: " + currentPlayer.getAnimalToken().getType() +
                        " (Player " + currentPlayer.getPlayerID() + ") with " +
                        closestPlayer.getAnimalToken().getType() + " (Player " + closestPlayer.getPlayerID() + ")");

                // Physically swap their positions on the board
                currentPlayer.setPosition(newCurrentPlayerPosition);
                closestPlayer.setPosition(newClosestPlayerPosition);

                // Update game UI or state as needed
                updateGameBoard();
                if (inTutorialMode) {
                    tutorialMode.setText("You swapped positions with " +
                            closestPlayer.getAnimalToken().getType());
                    steps.setText("You are now at " + closestPlayer.getAnimalToken().getType() + " Previous Spot" +
                            ". FLIP CARD AGAIN!");
                }

                // Wait for 2 seconds to allow players to understand game state and display instruction text
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> {
                    // Change player turns and flip unfolded cards back
                    nextPlayer();
                    flipCardsBack();
                });
                pause.play();

                System.out.println("After card move: Closest Player - " + closestPlayer.getAnimalToken().getType() + " StepTaken:" + closestPlayer.getAnimalToken().getStepTaken() );


            } else {
                System.out.println("No eligible player found to swap positions.");
                instructions.setText("No player found to swap positions.");
                for (int i = 0; i < decks.getChildren().size(); i++) {
                    cardsInGame.get(i).setFlipped(true);
                }
                if (inTutorialMode) {
                    tutorialMode.setText("No player found to swap positions!");
                    steps.setText("AHH! No player found to swap positions! FLIP CARD AGAIN!");
                }

                // Wait for 2 seconds to allow players to understand game state and display instruction text
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> {
                    // Change player turns and flip unfolded cards back
                    nextPlayer();
                    flipCardsBack();
                });
                pause.play();
            }
        }
        System.out.println("After card move: Player - " + currentPlayer.getAnimalToken().getType() + ", StepTaken - " + currentPlayer.getAnimalToken().getStepTaken() + ", IsOut - " + currentPlayer.getAnimalToken().getIsOut());

        // Optionally, after processing the card effect, update the UI or game state
        updateGameBoard();
    }

    // RAC- To send current player at position back home
    private void sendTokenHome(int position) {
        // Find and reset the player whose token is at the given position
        for (Player player : playerList) {
            if (player.getPosition() == position && player != getCurrentPlayer()) {
                player.resetPosition();  // Reset to start position
                gameMap.getHabitats().get(position).setContainAnimalToken(false);
                System.out.println(player.getAnimalToken().getType() + " token " + "Sent home");
                break;
            }
        }
    }


    private void flipCardsBack(){
        // Iterate through the deck and flip over any uncovered cards
        for (int i = 0; i < decks.getChildren().size(); i++) {
            cardsInGame.get(i).setFlipped(false);
            ImageView imageView = (ImageView) decks.getChildren().get(i);
            // Check if the card is currently uncovered (i.e., showing its face)
            if (imageView.getImage() != null) {
                // Flip the card back over to cover it
                imageView.setImage(new Image(getClass().getResourceAsStream("images/coveredcard.png")));
            }
        }
    }

    private void updateGameBoard() {
        double paneCenterX = boardcards.getWidth() / 2;
        double paneCenterY = boardcards.getHeight() / 2;
        double tokenRadius = radius + 30;

        // Iterate through all players to update each token's position based on current game state
        for (Player player : playerList) {
            AnimalToken token = player.getAnimalToken();
            ImageView tokenView = tokenViews.get(token.getType());

            if (tokenView != null) {
                if (token.getStepTaken() == gameMap.getNumberOfStepToWin()) {
                    // Token reaches its final destination
                    System.out.println(token.getType() + " reaches the win position at x: " + token.getInitialLayoutX() + ", y: " + token.getInitialLayoutY());
                    tokenView.setLayoutX(token.getInitialLayoutX());
                    tokenView.setLayoutY(token.getInitialLayoutY());
                } else {
                    int position = player.getPosition();
                    if (token.getStepTaken() == 0 && !token.getIsOut()) {
                        // If the token is not out, place it at its starting position
                        System.out.println("Updating position for " + token.getType() + ": InitialX=" + token.getInitialLayoutX() + ", InitialY=" + token.getInitialLayoutY() + ", StepTaken=" + token.getStepTaken() + ", IsOut=" + token.getIsOut());
                        tokenView.setLayoutX(token.getInitialLayoutX());
                        tokenView.setLayoutY(token.getInitialLayoutY());
                    } else {
                        // Update the position on the game board for tokens that are out
                        double angle = Math.toRadians(360.0 * position / 24); // Assuming 24 positions on the board
                        double xOffset = tokenRadius * Math.cos(angle);
                        double yOffset = tokenRadius * Math.sin(angle);

                        tokenView.setLayoutX(paneCenterX + xOffset - tokenView.getFitWidth() / 2);
                        tokenView.setLayoutY(paneCenterY + yOffset - tokenView.getFitHeight() / 2);
                    }
                }
            }
        }

        System.out.println("Game board updated.");
    }

    private void updateCurrentPlayerDisplay(AnimalType animalType) {
        currentPlayer.setText(animalType.toString().toLowerCase() + "'s turn");
    }

    public List<Player> setPlayers(int numberOfPlayer){
        List<Player> players = new ArrayList<>();

        if(numberOfPlayer == 2){
            players.add(new Player(new AnimalToken(AnimalType.FISH), 0, -1, false));
            players.add(new Player(new AnimalToken(AnimalType.DRAGON), 1, 11, false));
        }
        else {
            // create list of players
            players.add(new Player(new AnimalToken(AnimalType.FISH), 0, -1, false));
            players.add(new Player(new AnimalToken(AnimalType.PUFFERFISH), 1, 5, false));
            players.add(new Player(new AnimalToken(AnimalType.DRAGON), 2, 11, false));
            players.add(new Player(new AnimalToken(AnimalType.OCTOPUS), 3, 17, false));
            for (int j = players.size()-1; j >= numberOfPlayer; j--){
                players.remove(j);
            }
        }
        return players;
    }

    public void nextPlayer(){
        if (!inTutorialMode) {
            if (inPlayPlayer == null || inPlayPlayer.getPlayerID() + 1 >= playerList.size()) {
                inPlayPlayer = playerList.get(0);
            } else {
                inPlayPlayer = playerList.get(inPlayPlayer.getPlayerID() + 1);
            }
        } else {
            // In tutorial mode, always set to the single tutorial player
            inPlayPlayer = playerList.get(0);
        }
        updateCurrentPlayerDisplay(inPlayPlayer.getAnimalToken().getType());
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
