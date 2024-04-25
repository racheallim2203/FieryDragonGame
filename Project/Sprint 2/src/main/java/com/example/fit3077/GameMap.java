package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.*;

public class GameMap {
    private final List<Habitat> habitats;

    public GameMap() {
        this.habitats = new ArrayList<>();
        initializeHabitats();
    }

    // Populates the habitats with sets of AnimalCards, ensuring no repeated animals within a habitat
    // and each animal type appears exactly six times across all habitats.
    private void initializeHabitats() {
        List<String> allAnimals = Arrays.asList("fish", "pufferfish", "dragon", "octopus");
        int numHabitats = 8; // Total number of habitats
        int cardsPerHabitat = 3; // Cards per habitat

        // Generate a list to hold all the required AnimalCards
        List<AnimalCard> allCards = new ArrayList<>();
        for (String animal : allAnimals) {
            for (int i = 0; i < 6; i++) { // Each animal appears 6 times in total
                allCards.add(new AnimalCard(animal,0)); // Add an AnimalCard with a count of 1
            }
        }

        Collections.shuffle(allCards); // Shuffle the complete set of cards

        // Distribute the cards into habitats
        for (int i = 0; i < numHabitats; i++) {
            // Extract a sublist for each habitat
            List<AnimalCard> habitatCards = allCards.subList(i * cardsPerHabitat, (i + 1) * cardsPerHabitat);
            this.habitats.add(new Habitat(new ArrayList<>(habitatCards)));
        }
    }

    public Habitat getHabitat(int index) {
        return habitats.get(index % habitats.size());
    }

    public List<Habitat> getHabitats() {
        return habitats;
    }

    @Override
    public String toString() {
        return "GameMap{" + "habitats=" + habitats + '}';
    }
}