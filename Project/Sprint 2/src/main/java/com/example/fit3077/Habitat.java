package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class Habitat {
    private final List<AnimalCard> cards;

    public Habitat(List<AnimalCard> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public List<AnimalCard> getCards() {
        return cards;
    }

    // Method to check if the habitat contains an animal card of a specific type
    public boolean containsAnimal(String animalType) {
        for (AnimalCard card : getCards()) {
            if (card.getAnimal().equals(animalType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Habitat{" +
                "cards=" + cards +
                '}';
    }

}


