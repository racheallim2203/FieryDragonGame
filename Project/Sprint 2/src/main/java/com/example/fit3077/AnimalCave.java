package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

public class AnimalCave {
    private final String type;
    private final int location; // store the cave's location on the game map

    public AnimalCave(String type, int location) {
        this.type = type;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public int getLocation() {
        return location;
    }
}
