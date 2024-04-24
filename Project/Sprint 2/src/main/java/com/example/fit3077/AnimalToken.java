package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

// Represents the token each player uses to navigate the game map.
public class AnimalToken {
    private final String type; // The type of the animal.
    private int position; // The position of the token on the game map.

    // Constructor for the AnimalToken class.
    public AnimalToken(String type, int startPosition) {
        this.type = type;
        this.position = startPosition; // Set the token's starting position to its respective cave location.
    }

    // Returns the type of the animal token.
    public String getType() {
        return type;
    }

    // Returns the current position of the animal token.
    public int getPosition() {
        return position;
    }

    // Sets the position of the animal token.
    public void setPosition(int position) {
        this.position = position;
    }
}

