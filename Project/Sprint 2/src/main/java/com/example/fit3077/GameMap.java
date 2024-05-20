package com.example.fit3077;

import com.example.fit3077.cards.AnimalCard;

import java.util.*;

public class GameMap {
    private final int numberOfStepToWin = 26;
    private final List<Volcano> volcanoList;
    private final List<AnimalCave> animalCaves;
    private List<Habitat> habitats;


    public GameMap(int numberOfPlayers) {
        this.volcanoList = VolcanoList.getInstance();
        this.animalCaves = new ArrayList<AnimalCave>();
        this.habitats = new ArrayList<Habitat>();
        setUpHabitats();
        initializeAnimalCaves(numberOfPlayers);
    }

    public int getNumberOfStepToWin() {
        return numberOfStepToWin;
    }

    private void initializeAnimalCaves(int numberOfCaves) {
        System.out.println("Initialize animal caves");

        int i = 0;

        if (numberOfCaves == 2){
            animalCaves.add(new AnimalCave(AnimalType.FISH, -1));
            animalCaves.add(new AnimalCave(AnimalType.DRAGON, 11));
        }
        else {
            for (AnimalType animalType: AnimalType.values()) {
                if (i < numberOfCaves){
                    int startingLocation = (volcanoList.size() / AnimalType.values().length) * i;
                    animalCaves.add(new AnimalCave(animalType, startingLocation));
                    i++;
                }
                else {
                    break;
                }
            }
        }

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

    public void setUpHabitats(){
        for (Volcano volcanoCard : volcanoList) {
            for (Habitat habitat : volcanoCard.getVolcanoCards()) {
                habitats.add(habitat);
            }
        }
    }

    public List<Habitat> getHabitats() {
        return habitats;
    }


}
