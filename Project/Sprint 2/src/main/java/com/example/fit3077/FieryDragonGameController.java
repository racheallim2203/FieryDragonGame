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

import java.io.*;
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

    private boolean isNewGame = true;

    // Existing game state file paths or configuration
    private static final String PLAYER_LIST_FILE = "player_list.txt";
    private static final String VOLCANO_CARD_FILE = "volcano_card_state.txt";

    private boolean inTutorialMode = false;

    public void setUserInput(int userInput) {
        this.userInput = userInput;
        System.out.println("User input set to: " + userInput);  // Debugging line
    }

    public void setIsNewGame(boolean isNew) {
        this.isNewGame = isNew;
        System.out.println("Game mode set to " + (isNew ? "new game." : "continue game."));
    }

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
        playerList.add(new Player(new AnimalToken(AnimalType.FISH), 0, -1, false,0,true)); // FISH can move from the start position

        // Add static tokens with their fixed positions
        playerList.add(new Player(new AnimalToken(AnimalType.PUFFERFISH), 1, 8, true,1,true));
        playerList.add(new Player(new AnimalToken(AnimalType.DRAGON), 2, 15, true,1,true));
        playerList.add(new Player(new AnimalToken(AnimalType.OCTOPUS), 3, 20, true,1,true));
    }

    @FXML
    void backToGame() {
        inTutorialMode = false;
        initializeGame();

    }

    @FXML
    void startGame() {
        if (!inTutorialMode) {
            if (isNewGame) {
                playerList = setPlayers(userInput);  // Set new players for a new game
                System.out.println("Initializing new game with " + userInput + " players.");
            } else {
                System.out.println("Using loaded players for continued game.");
                continueGame();
            }
        } else {
            setTutorialPlayer(4);  // Set tutorial players if in tutorial mode
        }
        System.out.println("Starting game. Player count: " + playerList.size());

        System.out.println("Restart Game");

        winbg.setVisible(false);  // Hide the win background if visible from a previous game.
        hBox.setVisible(!inTutorialMode);  // Show GridPane and HBox if not in tutorial mode
        gridPane.setVisible(!inTutorialMode);
        indicator.setVisible(false);
        backToGame.setVisible(inTutorialMode);

        // Check if playerList is not empty before setting the first player
        if (!playerList.isEmpty()) {
            inPlayPlayer = playerList.get(0);  // Set the first player
            updateCurrentPlayerDisplay(inPlayPlayer.getAnimalToken().getType());
        } else {
            System.err.println("Player list is empty, cannot start the game.");
            return;  // Exit the method if no players are set to avoid further errors
        }

        instructions.setText("-");

        // UI and game logic initialization on the UI thread
        Platform.runLater(() -> {
            displayShuffledDeck();  // Display the shuffled deck of cards
            if (!cardsInGame.isEmpty()) {
                shuffleAndDisplayAnimals();  // Shuffle animal positions and display on the board
            } else {
                System.err.println("Card list is empty, ensure cards are initialized before starting the game.");
            }
            initializeImageView();  // Initialize card images for interaction
            initializeTokenViews();  // Set up visual representation of player tokens

            if (inTutorialMode) {
                steps.setText("You are in tutorial mode! Let's start by flipping a card! CLICK ON A CARD in the deck");
            }

            System.out.println("Game started.");
        });
    }

    // Method to continue the game by loading the saved state
    public void continueGame() {
        isNewGame = false; // Ensure this is set before any potential early returns
        System.out.println("Loading existing game...");

        loadPlayerState();
        loadGameMapState();

        System.out.println("Restored player list: " + playerList);
        System.out.println("Restored game map: " + gameMap);

        Platform.runLater(() -> {
            initializeTokenViews();  // This should setup token views based on the newly loaded state
            System.out.println("Token views initialized, tokenViews size: " + tokenViews.size());

            updateGameBoard();  // Now update the game board to reflect the loaded state

            System.out.println("Game board updated after loading game.");
            System.out.println("Game continued successfully. UI updated with loaded data.");
        });
    }

    // Load players from saved state
    void loadPlayerState() {
        String playerData = readFromFile(PLAYER_LIST_FILE);
        System.out.println("Successfully read file");
        int numberOfPlayers = parsePlayerCount(playerData);
        System.out.println("Successfullyn passed player count");
        if (numberOfPlayers > 0) {
            updatePlayerList(playerData, numberOfPlayers);
            System.out.println("Successfully update player list");
        }
    }

    // Method to load the saved state of the volcanoes
    void loadGameMapState() {
        String volcanoCardState = readFromFile(VOLCANO_CARD_FILE);
        if (volcanoCardState != null) {
            List<Volcano> loadedVolcanoes = parseVolcanoState(volcanoCardState);
            if (loadedVolcanoes != null && !loadedVolcanoes.isEmpty()) {
                String playerData = readFromFile(PLAYER_LIST_FILE);
                int numberOfPlayers = parsePlayerCount(playerData);
                // Create a new GameMap instance using the loaded data
                gameMap = new GameMap(numberOfPlayers);  // Use the number of players to initialize the game map properly
                gameMap.setVolcanoes(loadedVolcanoes);  // Set the loaded volcanoes
                gameMap.setUpHabitats();  // Set up habitats after loading volcanoes

                System.out.println("Game map successfully loaded with " + loadedVolcanoes.size() + " volcanoes.");
            } else {
                System.err.println("No volcanoes were loaded, possibly due to parsing errors or empty data.");
            }
        } else {
            System.err.println("No volcano data found, check the file path and data.");
        }
    }

    // Parse the state of the volcanoes from a string
    private List<Volcano> parseVolcanoState(String data) {
        List<Volcano> volcanoes = new ArrayList<>();
        String[] volcanoEntries = data.split("\n");
        for (String entry : volcanoEntries) {
            List<Habitat> habitats = new ArrayList<>();
            String[] habitatEntries = entry.split(",");
            for (String habitatEntry : habitatEntries) {
                habitats.add(new Habitat(AnimalType.valueOf(habitatEntry.trim())));
            }
            volcanoes.add(new Volcano(habitats));
        }
        return volcanoes;
    }

    // read from a file
    private String readFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to read from file: " + e.getMessage());
        }
        return content.toString();
    }

    // Method to update the player list using loaded player data
    private void updatePlayerList(String playerData, int numberOfPlayers) {
        List<Player> loadedPlayers = parsePlayerData(playerData);

        if (loadedPlayers.size() != numberOfPlayers) {
            System.err.println("Mismatch in the number of players expected and loaded. Expected: " + numberOfPlayers + ", Loaded: " + loadedPlayers.size());
            return;
        }

        playerList = new ArrayList<>(loadedPlayers);  // Assign the new player list
        System.out.println("Player list updated successfully. Total players: " + playerList.size());
        for (Player player : playerList) {
            System.out.println("Player ID: " + player.getPlayerID() + ", Type: " + player.getAnimalToken().getType() + ", Position: " + player.getPosition() + ", IsOut: " + player.getAnimalToken().getIsOut() + ", StepsTaken: " + player.getAnimalToken().getStepTaken());
        }
    }

    // Parses the first line to get the number of players
    private int parsePlayerCount(String data) {
        try {
            return Integer.parseInt(data.trim().split("\n")[0].split(":")[1].trim());
        } catch (Exception e) {
            System.err.println("Error parsing player count: " + e.getMessage());
            return 0;
        }
    }

    // Parse player data from string data
    private List<Player> parsePlayerData(String data) {
        List<Player> players = new ArrayList<>();
        String[] entries = data.trim().split("\n");
        for (int i = 1; i < entries.length; i++) {  // Skip the first line which is the count
            try {
                String[] details = entries[i].split(", ");
                String type = details[0].split(": ")[1];
                int position = Integer.parseInt(details[1].split(": ")[1]);
                boolean isOut = Boolean.parseBoolean(details[2].split(": ")[1]);
                int stepsTaken = Integer.parseInt(details[3].split(": ")[1]);
                players.add(new Player(new AnimalToken(AnimalType.valueOf(type)), i - 1, position, isOut,stepsTaken, false));
                System.out.println("Player loaded: " + type + " at position " + position + " with steps " + stepsTaken + " and out status " + isOut);
            } catch (Exception e) {
                System.err.println("Error parsing player data: " + e.getMessage());
            }
        }
        return players;
    }


    public void saveGameState() {
        // Save Volcano state
        try {
            Volcano.saveVolcano(VolcanoList.getInstance(), "volcano_card_state.txt");
        } catch (IOException e) {
            System.err.println("Error saving Volcano state: " + e.getMessage());
            return;
        }

        // Save player data
        Player.savePlayers(playerList, "player_list.txt");
        System.out.println("Player state saved successfully.");
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
        System.out.println("Initializing game with user input: " + userInput); // Debug output
        if (userInput <= 0) {
            System.err.println("Invalid number of players: " + userInput);
            return; // Add a return to prevent further execution with invalid input
        }
        gameMap = new GameMap(userInput);
        tutorialPanel.setVisible(false);
        setPlayers(userInput); // Ensure this is called after user input is confirmed
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


                boardcards.getChildren().add(tokenView);
                tokenViews.put(tokenType, tokenView);

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

                // If the token is not out, adjust its position back to its starting position.
                if (!playerList.get(i).getAnimalToken().getIsOut()) {
                    tokenView.setLayoutX(playerList.get(i).getAnimalToken().getInitialLayoutX());
                    tokenView.setLayoutY(playerList.get(i).getAnimalToken().getInitialLayoutY());
                    System.out.println("Placing " + tokenType + " back to its initial position due to not being 'out'.");
                }
                nextPlayer();
            }
            updateGameBoard();
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
            int totalPositions = gameMap.getHabitats().size();
            Player closestPlayer = findClosestPlayer(currentPlayer, playerList, totalPositions);

            if (closestPlayer != null) {
                swapPositions(currentPlayer, closestPlayer, totalPositions);
                updateGameAfterSwap(currentPlayer, closestPlayer, inTutorialMode);
            } else {
                System.out.println("No eligible player found to swap positions.");
                instructions.setText("No player found to swap positions.");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> {
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


    private Player findClosestPlayer(Player currentPlayer, List<Player> playerList, int totalPositions) {
        Player closestPlayer = null;
        int closestDistance = Integer.MAX_VALUE;

        for (Player otherPlayer : playerList) {
            if (otherPlayer != currentPlayer && otherPlayer.getAnimalToken().getIsOut()) {
                int forwardDistance = (otherPlayer.getPosition() - currentPlayer.getPosition() + totalPositions) % totalPositions;
                int backwardDistance = (currentPlayer.getPosition() - otherPlayer.getPosition() + totalPositions) % totalPositions;
                int smallerDistance = Math.min(forwardDistance, backwardDistance);

                if (smallerDistance < closestDistance) {
                    closestDistance = smallerDistance;
                    closestPlayer = otherPlayer;
                }
            }
        }
        return closestPlayer;
    }

    private void swapPositions(Player currentPlayer, Player closestPlayer, int totalPositions) {
        // Retrieve initial positions in case they're needed for the swap
        int currentPlayerInitialPosition = currentPlayer.getInitialPosition();
        int closestPlayerInitialPosition = closestPlayer.getInitialPosition();

        int tempPosition = currentPlayer.getPosition();
        int newCurrentPlayerPosition = closestPlayer.getPosition();

        // Calculate directional steps to move considering the game board is circular
        int currentPlayerStepChange = (newCurrentPlayerPosition - tempPosition + totalPositions) % totalPositions;
        int closestPlayerStepChange = (tempPosition - closestPlayer.getPosition() + totalPositions) % totalPositions;

        // Shortest Path Calculation for wrapping around the board
        if (currentPlayerStepChange > totalPositions / 2) {
            currentPlayerStepChange -= totalPositions;
        }
        if (closestPlayerStepChange > totalPositions / 2) {
            closestPlayerStepChange -= totalPositions;
        }

        // Perform the movement
        currentPlayer.moveToken(currentPlayerStepChange, gameMap);
        closestPlayer.moveToken(closestPlayerStepChange, gameMap);

        // Set positions directly to handle cases where players should not move normally
        if (!currentPlayer.getAnimalToken().getIsOut()) {
            // Set closest player's position to current player's initial position
            closestPlayer.setPosition(currentPlayerInitialPosition);
            // Simulate that closest player is not out
            closestPlayer.getAnimalToken().setIsOut(false);
        } else {
            // Normal position swap if both players are out
            currentPlayer.setPosition(newCurrentPlayerPosition);
            closestPlayer.setPosition(tempPosition);
        }
    }

    private void updateGameAfterSwap(Player currentPlayer, Player closestPlayer, boolean inTutorialMode) {
        updateGameBoard();
        if (inTutorialMode) {
            tutorialMode.setText("You swapped positions with " +
                    closestPlayer.getAnimalToken().getType());
            steps.setText("You are now at position " + closestPlayer.getAnimalToken().getType() +
                    ". FLIP CARD AGAIN!");
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            nextPlayer();
            flipCardsBack();
        });
        pause.play();
        System.out.println("After card move: Closest Player - " + closestPlayer.getAnimalToken().getType() + " StepTaken:" + closestPlayer.getAnimalToken().getStepTaken());
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
        System.out.println("FOR GAMEBOARD" + playerList);

        double paneCenterX = boardcards.getWidth() / 2;
        double paneCenterY = boardcards.getHeight() / 2;
        double tokenRadius = radius + 30;

        // Ensure tokenViews are initialized
        if (tokenViews.isEmpty()) {
            System.out.println("Token views are not initialized, initializing now...");
            initializeTokenViews();  // This ensures token views are setup before updating positions
        }

        System.out.println("TOKEN VIEW" + tokenViews);

        // Iterate through all players to update each token's position based on current game state
        for (Player player : playerList) {
            AnimalToken token = player.getAnimalToken();
            ImageView tokenView = tokenViews.get(token.getType());
            int position = player.getPosition();  // Use the actual position
            System.out.println(token.getType() + "is at position: " + position);

            if (tokenView != null) {
                if (token.getStepTaken() == gameMap.getNumberOfStepToWin()) {
                    // Token reaches its final destination
                    System.out.println(token.getType() + " reaches the win position at x: " + token.getInitialLayoutX() + ", y: " + token.getInitialLayoutY());
                    tokenView.setLayoutX(token.getInitialLayoutX());
                    tokenView.setLayoutY(token.getInitialLayoutY());
                } else {
                    if (!token.getIsOut()) {
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
        System.out.println("Setting players with input: " + numberOfPlayer);
        if(numberOfPlayer == 2){
            players.add(new Player(new AnimalToken(AnimalType.FISH), 0, -1, false, 0,true));
            players.add(new Player(new AnimalToken(AnimalType.DRAGON), 1, 11, false,0, true));
        }
        else {
            // create list of players
            players.add(new Player(new AnimalToken(AnimalType.FISH), 0, -1, false, 0, true));
            players.add(new Player(new AnimalToken(AnimalType.PUFFERFISH), 1, 5, false, 0, true));
            players.add(new Player(new AnimalToken(AnimalType.DRAGON), 2, 11, false,0,true));
            players.add(new Player(new AnimalToken(AnimalType.OCTOPUS), 3, 17, false,0,true));
            for (int j = players.size()-1; j >= numberOfPlayer; j--){
                players.remove(j);
            }
            System.out.println("Added 4 players.");
        }
        System.out.println("Total players now: " + players.size());
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

