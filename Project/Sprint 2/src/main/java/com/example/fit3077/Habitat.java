package com.example.fit3077;

import com.example.fit3077.cards.AnimalCard;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.*;

public class Habitat {
    private AnimalType animalType;
    private ArrayList<AnimalCard> animalCards;
    private boolean containAnimalToken;

    public Habitat(AnimalType animalType){
        this.animalType = animalType;
        this.animalCards = new ArrayList<>();
        setContainAnimalToken(false);
    }

    public AnimalType getAnimalType(){
        return animalType;
    }

    public ArrayList<AnimalCard> getAnimalCards() {
        return animalCards;
    }

    public boolean isContainAnimalToken() {
        return containAnimalToken;
    }

    public void setContainAnimalToken(boolean containAnimalToken) {
        this.containAnimalToken = containAnimalToken;
    }


    public Image getHabitatImage() {
        String imageName = animalType.toString().toLowerCase() + ".png";
        String pathName = "images/" + imageName;

        return new Image(getClass().getResourceAsStream(pathName));
    }

    public void shuffle() {
        Collections.shuffle(animalCards);
    }
}
