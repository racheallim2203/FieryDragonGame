package com.example.fit3077;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

    // Serialize the state of Volcano cards to a string
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (Habitat habitat : volcanoCard) {
            sb.append(habitat.getAnimalType()).append(",");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1); // Remove last comma
        return sb.toString();
    }

    public static void saveGameState(List<Volcano> volcanoes, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Volcano volcano : volcanoes) {
                writer.write(volcano.serialize());
                writer.newLine();
            }
        }
    }


}
