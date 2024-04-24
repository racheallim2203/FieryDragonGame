package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

public class AnimalCard extends Card {
    //  The final keyword suggests that once animalType is assigned a value
    //  indicates the type of animal represented by the card.
    private final String animalType;

    // Constructor initializes the AnimalCard with a specific animal type and count.
    public AnimalCard(String animalType, int count) {
        this.animalType = animalType;
        this.count = count; // The 'count' should be set here based on the animal type or passed as a parameter.

        // count is  an attribute of the superclass Card
    }

    public String getAnimal() {
        return animalType;
    }


    /*The method applyEffect is overridden from the Card superclass.
    It takes a Player and GameMap as parameters and returns a boolean indicating whether the
    move was successful.
        */


    @Override
    public boolean applyEffect() {
        return false;
    }
}
