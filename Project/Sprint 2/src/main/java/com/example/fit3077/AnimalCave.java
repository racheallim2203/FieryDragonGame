package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.image.Image;

public class AnimalCave {
    private final AnimalType animalType;
    private final int location; // Location on the game map, could correspond to habitat index

    public AnimalCave(AnimalType animalType, int location) {
        this.animalType = animalType;
        this.location = location;
    }

    public AnimalType getType() {
        return animalType;
    }

    public int getLocation() {
        return location;
    }

    public Image getTokenImage() {
        String imageName = animalType.toString().toLowerCase() + "cave.png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }
}