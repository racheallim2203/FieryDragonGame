package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.*;

public class GameMap {
    private final List<Habitat> habitats;
    private final Map<String, AnimalCave> animalCaves; // Maps animal types to their respective caves

    public GameMap() {
        this.habitats = new ArrayList<>();
        this.animalCaves = new HashMap<>();
        initializeHabitats();
        initializeAnimalCaves();
    }

    private void initializeAnimalCaves() {
        animalCaves.put("Fish", new AnimalCave("Fish", 5));
        animalCaves.put("Puffer Fish", new AnimalCave("Puffer Fish", 10));
        animalCaves.put("Baby Dragon", new AnimalCave("Baby Dragon", 15));
        animalCaves.put("Octopus", new AnimalCave("Octopus", 20));
    }

    public AnimalCave getAnimalCave(String type) {
        return animalCaves.get(type);
    }

    // Populates the habitats with sets of AnimalCards, ensuring no repeated animals within a habitat
    // and each animal type appears exactly six times across all habitats.
    private void initializeHabitats() {
        List<String> allAnimals = Arrays.asList("Fish", "Puffer Fish", "Baby Dragon", "Octopus");
        int numHabitats = 8; // Total number of habitats
        int cardsPerHabitat = 3; // Cards per habitat

        // Generate a list to hold all the required AnimalCards
        List<AnimalCard> allCards = new ArrayList<>();
        for (String animal : allAnimals) {
            for (int i = 0; i < 6; i++) { // Each animal appears 6 times in total
                allCards.add(new AnimalCard(animal, 1)); // Add an AnimalCard with a count of 1
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