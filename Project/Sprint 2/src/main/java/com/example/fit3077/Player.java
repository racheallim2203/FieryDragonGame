package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;
import java.util.*;
import java.util.List;
public class Player {
    private final AnimalToken animalToken; //  represents the which animal token is taken by the player
    private int position; //stores the player's current position on the game board.

    private boolean winningStatus = false;

    // initializes a new Player object with an AnimalToken and sets the initial position to 0
    public Player(AnimalToken animalToken) {
        this.animalToken = animalToken; //
        this.position = 0; //
    }

    // adjusts the player's position on the game board based on a number of steps to move, which can be positive or negative
    public void moveToken(int steps, GameMap gameMap) {
        this.position = (this.position + steps) % gameMap.getHabitats().size();
        /* modulo operator to wrap the position around if it exceeds the number of habitats on the board
        (ensuring the position stays within valid bounds). If the resulting position is negative
        (possible with backward moves), it corrects this by adding the total number of habitats*/
        if (this.position < 0) {
            this.position += gameMap.getHabitats().size(); // Correction for negative positions
        }
    }


    public int getPosition() {
        return position;
    }

    public AnimalToken getAnimalToken() {
        return animalToken;
    }


}