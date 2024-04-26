package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.image.Image;

public class AnimalCave {
    private final String type;
    private final int location; // Location on the game map, could correspond to habitat index

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

    public Image getTokenImage() {
        String imageName = type.toLowerCase() + "token.png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }
}