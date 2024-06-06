package com.example.fit3077;

import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Player {
    private final AnimalToken animalToken; //  represents the which animal token is taken by the player
    private int position; //stores the player's current position on the game board.
    private int playerID; // to track player turns
    private int otherID; // to track cave that current player stays
    private boolean isOut;
    private int stepsTaken;
    boolean isNewGame;

    // Constructor to initialize player with loaded attributes
    public Player(AnimalToken animalToken, int playerID, int position, boolean isOut, int stepsTaken, boolean isNewGame, boolean isStatic) {
        this.animalToken = animalToken;
        this.playerID = playerID;
        this.position = position;
        this.stepsTaken=stepsTaken;
        this.isOut = isOut;
        this.isNewGame = isNewGame;
        if (!isNewGame || isStatic) {
            animalToken.setIsOut(isOut);
            System.out.println("Set Is Out");
            animalToken.setStepTaken(stepsTaken);
            System.out.println("Set Steps Taken");
        }

    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public boolean isOut() {
        return isOut;
    }

    private void setInitialPosition() {
        this.position = getInitialPositionForType(animalToken.getType());
    }

    private int getInitialPositionForType(AnimalType type) {
        return switch (type) {
            case FISH -> -1;
            case PUFFERFISH -> 5;
            case DRAGON -> 11;
            case OCTOPUS -> 17;
            default -> -1;
        };
    }

    public int getInitialPosition() {
        return getInitialPositionForType(this.animalToken.getType());
//        return this.animalToken.getType();

    }

    // adjusts the player's position on the game board based on a number of steps to move, which can be positive or negative
    public void moveToken(int steps, GameMap gameMap) {
        List<Volcano> habitats = gameMap.getVolcanoList();
        int totalAnimals = habitats.stream().mapToInt(h -> h.getVolcanoCards().size()).sum();
        System.out.println( this.getAnimalToken().getType() + " Current Position: " + this.position);
        System.out.println("Steps to Move: " + steps);

        if(this.getAnimalToken().getIsOut()){
            System.out.println("animal is out and set current position to no animal before move");
            gameMap.getHabitats().get(this.position).setContainAnimalToken(false);
        }

        int stepTaken = this.getAnimalToken().getStepTaken();
        if (stepTaken == 0 && !this.getAnimalToken().getIsOut()) {
            System.out.println("it is out from own cave!!!");
            this.getAnimalToken().setIsOut(true);
            gameMap.getAnimalCaves().get(this.playerID).setHasAnimal(false);
            gameMap.getAnimalCaves().get(this.playerID).setCurrentAnimal(null);
        }
        else if (stepTaken != 0 && !this.getAnimalToken().getIsOut()) {
            System.out.println("it is out from other cave!!!");
            this.getAnimalToken().setIsOut(true);
            gameMap.getAnimalCaves().get(this.otherID).setHasAnimal(false);
            gameMap.getAnimalCaves().get(this.otherID).setCurrentAnimal(null);
        }

        System.out.println("player step taken before move: " + stepTaken);
        this.getAnimalToken().setStepTaken(stepTaken + steps);
        System.out.println("player step taken after move: " + this.getAnimalToken().getStepTaken());

        if (this.getAnimalToken().getStepTaken() > 26){
            System.out.println("Need to go one more round!");
            this.getAnimalToken().setStepTaken((this.getAnimalToken().getStepTaken() % 25) + 1);
        }

        // Adding totalHabitats ensures that the index remains positive, also works perfectly for positive steps
        // Set the new position
        this.position = (this.position + steps + totalAnimals) % totalAnimals;
        System.out.println("New Position: " + this.position);

        if (this.getAnimalToken().getIsOut()){
            System.out.println("set new position has animal");
            gameMap.getHabitats().get(this.position).setContainAnimalToken(true);
        }

    }


    public int getPosition() {
        return position;
    }

    public AnimalToken getAnimalToken() {
        return animalToken;
    }

    public int getPlayerID() { return playerID; }


    public void resetPosition() {
        setInitialPosition();;
        this.getAnimalToken().setStepTaken(0);
        this.getAnimalToken().setIsOut(false);
    }

    public void setPosition(int position) {
        this.position = position;

    }

    public int getOtherID() {
        return otherID;
    }

    public void setOtherID(int otherID) {
        this.otherID = otherID;
    }

    // Static method to save all players' states to a file
    public static void savePlayers(List<Player> players, String filePath, Player inPlayPlayer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Count: " + players.size());
            writer.newLine();

            int playerIndex = 1;
            for (Player player : players) {
                // Serialize the state of players to a string
                String playerData = String.format("Player%d: %s, Position: %d, Out: %b, StepsTaken: %d, InitialX: %.2f, InitialY: %.2f, X: %.2f, Y: %.2f",
                        playerIndex++,
                        player.getAnimalToken().getType(),
                        player.getPosition(),
                        player.getAnimalToken().getIsOut(),
                        player.getAnimalToken().getStepTaken(),
                        player.getAnimalToken().getInitialLayoutX(),
                        player.getAnimalToken().getInitialLayoutY(),
                        player.getAnimalToken().getLayoutX(),
                        player.getAnimalToken().getLayoutY());
                writer.write(playerData);
                writer.newLine();
            }
            // save current player data
            String inPlayPlayerData = String.format("Current Player ID: %d", inPlayPlayer.getPlayerID());
            writer.write(inPlayPlayerData);
            writer.newLine();

            System.out.println("Game state saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving game state: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "{Type=" + animalToken.getType() +
                ", Position=" + position +
                ", IsOut=" + this.getAnimalToken().getIsOut() +
                ", StepsTaken=" + this.getAnimalToken().getStepTaken() +
                '}';
    }
}


