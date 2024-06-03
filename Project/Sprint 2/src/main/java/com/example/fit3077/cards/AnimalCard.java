package com.example.fit3077.cards;

import com.example.fit3077.AnimalType;
import com.example.fit3077.CardType;
import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public class AnimalCard extends Card {
//    private final String animalType; // Type of the animal on the card

    private final AnimalType animalType;

    public AnimalCard(AnimalType animalType, int stepCount, CardType cardType, int index, boolean isFlipped) {
        super(cardType,stepCount,isFlipped,index);
        this.animalType = animalType;
    }


    @Override
    public void applyMovement(Player currentPlayer, GameMap gameMap) {
        int forwardSteps = this.getCount();

        // Jeh Guan - check if the token's stepTaken will exceed the total step that should be taken to win the game if it is moved
        int currentStepTaken = currentPlayer.getAnimalToken().getStepTaken();
        if ((currentStepTaken + forwardSteps) <= gameMap.getNumberOfStepToWin()){
            System.out.println("Token can be moved");
            currentPlayer.moveToken(forwardSteps, gameMap);
        }
        else {
            System.out.println("Token can't be moved as the stepTaken will exceed " + gameMap.getNumberOfStepToWin());
        }

    }
    @Override
    public Image getImage() {

        String imageName = animalType.toString().toLowerCase() + stepCount + ".png";
        String pathName = "/com/example/fit3077/images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));

    }

    @Override
    public boolean matchesType(AnimalType animalType) {
        return this.animalType.equals(animalType);
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    @Override
    public String getImageFileName() {
        return animalType.toString().toLowerCase() + stepCount + ".png";
    }


    @Override
    public String toString() {
        return "{" +
                "index=" + index +
                ", type=" + cardType +
                ", animalType=" + animalType +
                ", stepCount=" + stepCount +
                ", isFlipped=" + isFlipped +
                '}';
    }


}

