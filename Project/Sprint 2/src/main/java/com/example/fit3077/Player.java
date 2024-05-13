package com.example.fit3077;

import java.util.List;

public class Player {
    private final AnimalToken animalToken; //  represents the which animal token is taken by the player
    private int position; //stores the player's current position on the game board.


    // initializes a new Player object with an AnimalToken and sets the initial position to
    public Player(AnimalToken animalToken) {
        this.animalToken = animalToken; //
        setInitialPosition();
    }

    private void setInitialPosition() {
        switch (animalToken.getType().toLowerCase()) {
            case "fish":
                this.position = -1;
                break;
            case "pufferfish":
                this.position = 5;
                break;
            case "octopus":
                this.position = 17;
                break;
            case "dragon":
                this.position = 11;
                break;
            default:
                this.position = -1;
                break;
        }
    }


    // adjusts the player's position on the game board based on a number of steps to move, which can be positive or negative
    public void moveToken(int steps, GameMap gameMap) {
        List<Habitat> habitats = gameMap.getHabitats();
        int totalAnimals = habitats.stream().mapToInt(h -> h.getCards().size()).sum();
        System.out.println("Current Position: " + this.position);
        System.out.println("Steps to Move: " + steps);
        int stepTaken = this.getAnimalToken().getStepTaken();
        this.getAnimalToken().setStepTaken(stepTaken + steps);
        // Adding totalHabitats ensures that the index remains positive, also works perfectly for positive steps
        // Set the new position
        this.position = (this.position + steps + totalAnimals) % totalAnimals;
        System.out.println("New Position: " + this.position);
    }


    public int getPosition() {
        return position;
    }

    public AnimalToken getAnimalToken() {
        return animalToken;
    }


    public void resetPosition() {
        setInitialPosition();;
        this.getAnimalToken().setStepTaken(0);
    }
}