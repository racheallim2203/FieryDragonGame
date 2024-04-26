package com.example.fit3077;

import java.util.List;

public class Player {
    private final AnimalToken animalToken; //  represents the which animal token is taken by the player
    private int position; //stores the player's current position on the game board.


    // initializes a new Player object with an AnimalToken and sets the initial position to 0
    public Player(AnimalToken animalToken) {
        this.animalToken = animalToken; //
        this.position = 0; //
    }

    // adjusts the player's position on the game board based on a number of steps to move, which can be positive or negative
    public void moveToken(int steps, GameMap gameMap) {
        List<Habitat> habitats = gameMap.getHabitats();
        int totalAnimals = habitats.stream().mapToInt(h -> h.getCards().size()).sum();
        System.out.println("Current Position: " + this.position);
        System.out.println("Steps to Move: " + steps);

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
        this.position = 0;
    }
}