package com.example.fit3077.cards;

import com.example.fit3077.AnimalType;
import com.example.fit3077.CardType;
import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public class PirateCard extends Card {

    public PirateCard(int stepCount, CardType cardType, int index, boolean isFlipped) {
        super(cardType,stepCount,isFlipped,index);
    }

    @Override
    public void applyMovement(Player currentPlayer, GameMap gameMap) {
        // Apply the effect with a negative count to move backwards
        int backwardSteps = -this.getCount(); // Make the steps negative here
        currentPlayer.moveToken(backwardSteps, gameMap);
    }

    @Override
    public Image getImage() {
        String imageName = "piratecard" + stepCount + ".png";
        String pathName = "/com/example/fit3077/images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }

    @Override
    public boolean matchesType(AnimalType animalType) {
        return false;
    }

    @Override
    public String getImageFileName() {
        return "piratecard" + stepCount + ".png";
    }

    @Override
    public String toString() {
        return "{" +
                "index=" + index +
                ", type=" + cardType +
                ", AnimalType=" + null +
                ", stepCount=" + stepCount +
                ", isFlipped=" + isFlipped +
                '}';
    }
}

