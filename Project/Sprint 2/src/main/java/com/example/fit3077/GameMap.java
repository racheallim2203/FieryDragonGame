package com.example.fit3077;

import java.util.*;

public class GameMap {
    private final int numberOfStepToWin = 26;
    private final List<Volcano> volcanoList;
    private final List<AnimalCave> animalCaves;


    public GameMap() {
        this.volcanoList = new ArrayList<Volcano>();
        this.animalCaves = new ArrayList<AnimalCave>();
        initializeHabitats();
        initializeAnimalCaves();
    }

    public int getNumberOfStepToWin() {
        return numberOfStepToWin;
    }

    private void initializeHabitats() {
        int numOfVolcanoCard = 8; // Total number of habitats
        int numOfHabitatsForEachVolcanoCard = 3; // Volcano cards per habitat

        // Generate a list to hold all the required AnimalCards
        List<Habitat> habitatsList = new ArrayList<>();

        for (AnimalType animalType: AnimalType.values()) {
            for (int i = 0; i < 6; i++) {
                Habitat habitat = new Habitat(animalType);
                habitatsList.add(habitat);
            }
        }

        Collections.shuffle(habitatsList); // Shuffle the complete set of habitats list

        // Distribute the cards into habitats
        for (int i = 0; i < numOfVolcanoCard; i++) {
            List<Habitat> volcanoCards = habitatsList.subList(i * numOfHabitatsForEachVolcanoCard, (i + 1) * numOfHabitatsForEachVolcanoCard);
            this.volcanoList.add(new Volcano(volcanoCards));
        }
    }

    private void initializeAnimalCaves() {
        System.out.println("Initialize animal caves");
        int i = 0;

        // WHAT IS STARTING LOCATION? WHAT IS THAT USE FOR? THE OUTPUT: 0 - 2 - 4 - 6
        for (AnimalType animalType: AnimalType.values()) {
            int startingLocation = (volcanoList.size() / AnimalType.values().length) * i;
            animalCaves.add(new AnimalCave(animalType, startingLocation));
            i++;
        }

        // sam's code
//        for (AnimalType animalType: AnimalType.values()) {
//
//            animalCaves.add(new AnimalCave(animalType, startingLocation));
//            i++;
//        }
    }

    public Volcano getVolcano(int index) {
        return volcanoList.get(index % volcanoList.size());
    }

    public List<Volcano> getVolcanoList() {
        return volcanoList;
    }

    public List<AnimalCave> getAnimalCaves() {
        return animalCaves;
    }

    @Override
    public String toString() {
        return "GameMap{" + "volcanoList=" + volcanoList + ", animalCaves=" + animalCaves + '}';
    }
}