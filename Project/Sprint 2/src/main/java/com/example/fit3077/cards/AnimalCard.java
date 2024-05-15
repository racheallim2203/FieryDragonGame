package com.example.fit3077.cards;

import com.example.fit3077.AnimalType;
import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public class AnimalCard extends Card {
//    private final String animalType; // Type of the animal on the card

    private final AnimalType animalType;

    public AnimalCard(AnimalType animalType, int stepCount) {
//        super.type = animalType;      // sam - doesn't make sense to override the Card's type as it only can be "animal" or "pirate"
        super.stepCount = stepCount;
        this.animalType = animalType;
    }


    @Override
    public void applyMovement(Player currentPlayer, GameMap gameMap, Card card) {
        int forwardSteps = card.getCount();
        currentPlayer.moveToken(forwardSteps, gameMap);
    }

    @Override
    public Image getImage() {

        String imageName = animalType.toString().toLowerCase() + stepCount + ".png";
        String pathName = "/com/example/fit3077/images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));

    }

    public AnimalType getAnimalType() {
        return animalType;
    }

}

