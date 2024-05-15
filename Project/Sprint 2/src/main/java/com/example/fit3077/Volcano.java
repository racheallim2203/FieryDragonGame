package com.example.fit3077;

import com.example.fit3077.cards.AnimalCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Volcano {

    private final List<Habitat> volcanoCard;

    public Volcano(List<Habitat> cards) {
        this.volcanoCard = cards;
    }

    public List<Habitat> getVolcanoCards() {
        return volcanoCard;
    }

    public boolean containsAnimal(AnimalType animalType) {
        for (Habitat card : volcanoCard) {
            if (card.getAnimalType().equals(animalType)) {
                return true;
            }
        }
        return false;
    }

    public void shuffle() {
        Collections.shuffle(volcanoCard);
    }


}
