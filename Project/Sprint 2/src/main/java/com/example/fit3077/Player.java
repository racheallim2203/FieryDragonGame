package com.example.fit3077;

import java.util.List;

public class Player {
    private final AnimalToken animalToken; //  represents the which animal token is taken by the player
    private int position; //stores the player's current position on the game board.
    private int playerID; // to track player turns
    
    // initializes a new Player object with an AnimalToken and sets the initial position to
    public Player(AnimalToken animalToken, int playerID, int initialPosition, boolean isStatic) {
        this.animalToken = animalToken;
        this.playerID = playerID;
        if (isStatic) {
            this.position = initialPosition;
            this.animalToken.setIsOut(true);
            this.animalToken.setStepTaken(1);
        } else {
            setInitialPosition();
        }
    }


    private void setInitialPosition() {
        switch (animalToken.getType()) {
            case FISH:
                this.position = -1;
                break;
            case PUFFERFISH:
                this.position = 5;
                break;
            case DRAGON:
                this.position = 11;
                break;
            case OCTOPUS:
                this.position = 17;
                break;
            default:
                this.position = -1;
                break;
        }
    }


    // adjusts the player's position on the game board based on a number of steps to move, which can be positive or negative
    public void moveToken(int steps, GameMap gameMap) {
        List<Volcano> habitats = gameMap.getVolcanoList();
        int totalAnimals = habitats.stream().mapToInt(h -> h.getVolcanoCards().size()).sum();
        System.out.println("Current Position: " + this.position);
        System.out.println("Steps to Move: " + steps);

        if(this.getAnimalToken().getIsOut()){
            gameMap.getHabitats().get(this.position).setContainAnimalToken(false);
        }

        int stepTaken = this.getAnimalToken().getStepTaken();
        if (stepTaken == 0 && !this.getAnimalToken().getIsOut()) {
            System.out.println("it is out!!!");
            this.getAnimalToken().setIsOut(true);
        }

        System.out.println("player step taken before move: " + stepTaken);
        this.getAnimalToken().setStepTaken(stepTaken + steps);
        System.out.println("player step taken after move: " + this.getAnimalToken().getStepTaken());

        // Adding totalHabitats ensures that the index remains positive, also works perfectly for positive steps
        // Set the new position
        this.position = (this.position + steps + totalAnimals) % totalAnimals;
        System.out.println("New Position: " + this.position);

        if (this.getAnimalToken().getIsOut()){
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
}