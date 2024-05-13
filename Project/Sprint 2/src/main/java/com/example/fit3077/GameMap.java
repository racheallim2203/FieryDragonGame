package com.example.fit3077;

import com.example.fit3077.cards.AnimalCard;

import java.util.*;

public class GameMap {
    private final List<Habitat> habitats;
    private final List<AnimalCave> animalCaves;


    public GameMap() {
        this.habitats = new ArrayList<>();
        this.animalCaves = new ArrayList<>();
        initializeHabitats();
        initializeAnimalCaves();
    }

    private void initializeHabitats() {
//        List<String> allAnimals = Arrays.asList("fish", "pufferfish", "dragon", "octopus");
        int numHabitats = 8; // Total number of habitats
        int cardsPerHabitat = 3; // Cards per habitat

        // Generate a list to hold all the required AnimalCards
        List<AnimalCard> allCards = new ArrayList<>();

        for (AnimalType animalType: AnimalType.values()) {
            for (int i = 0; i < 6; i++) {
                allCards.add(new AnimalCard(animalType, 0)); // Add an AnimalCard with a count of 1
            }
        }

        Collections.shuffle(allCards); // Shuffle the complete set of cards

        // Distribute the cards into habitats
        for (int i = 0; i < numHabitats; i++) {
            List<AnimalCard> habitatCards = allCards.subList(i * cardsPerHabitat, (i + 1) * cardsPerHabitat);
            this.habitats.add(new Habitat(new ArrayList<>(habitatCards)));
        }
    }

    private void initializeAnimalCaves() {
        int i = 0;
        for (AnimalType animalType: AnimalType.values()) {
            int startingLocation = (habitats.size() / AnimalType.values().length) * i;
            animalCaves.add(new AnimalCave(animalType, startingLocation));
            i++;
        }

//        String[] animalTypes = new String[]{"fish", "pufferfish", "dragon", "octopus"};
//        for (int i = 0; i < animalTypes.length; i++) {
//            // Assuming each type of animal starts at the beginning of each quarter in a circular arrangement
//            int startingLocation = (habitats.size() / animalTypes.length) * i;
//            animalCaves.add(new AnimalCave(animalTypes[i], startingLocation));
//        }
    }

    public Habitat getHabitat(int index) {
        return habitats.get(index % habitats.size());
    }

    public List<Habitat> getHabitats() {
        return habitats;
    }

    public List<AnimalCave> getAnimalCaves() {
        return animalCaves;
    }

    @Override
    public String toString() {
        return "GameMap{" + "habitats=" + habitats + ", animalCaves=" + animalCaves + '}';
    }
}