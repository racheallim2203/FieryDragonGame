package com.example.fit3077.cards;

import com.example.fit3077.AnimalType;
import com.example.fit3077.CardType;
import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public class PirateCard extends Card {

    public PirateCard(int stepCount, CardType cardType) {
//        super.cardType = "piratecard";
        super(cardType);
        super.stepCount = stepCount; // Ensure this is within the range [1, 2] for PirateCard
        setFlipped(false);
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

}

