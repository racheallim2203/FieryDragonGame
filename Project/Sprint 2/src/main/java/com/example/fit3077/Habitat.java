package com.example.fit3077;

import javafx.application.Application;
import javafx.scene.image.Image;
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

    public boolean containsAnimal(String animalType) {
        for (AnimalCard card : cards) {
            if (card.getAnimalType().equals(animalType)) {
                return true;
            }
        }
        return false;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

}