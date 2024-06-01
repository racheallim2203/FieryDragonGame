package com.example.fit3077;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolcanoList {
    private static List<Volcano> volcanoList;

    private VolcanoList(){
        volcanoList = new ArrayList<Volcano>();
        initializeHabitats();
    }

    public static void setVolcanoes(List<Volcano> newVolcanoes) {
        volcanoList = newVolcanoes;  // Updates the list of volcanoes
    }

    public static List<Volcano> getInstance() {
        if (volcanoList == null) {
            return new VolcanoList().volcanoList;
        } else {
            return volcanoList;
        }
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
}
