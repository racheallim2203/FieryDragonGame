package com.example.fit3077;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PirateCard extends Card {

    public PirateCard(int count) {
        super.type = "piratecard";
        super.count = count; // Ensure this is within the range [1, 2] for PirateCard
    }

    @Override
    public void applyEffect(Player currentPlayer, GameMap gameMap, Card card) {
        // Apply the effect with a negative count to move backwards
        int backwardSteps = -card.getCount(); // Make the steps negative here
        currentPlayer.moveToken(backwardSteps, gameMap);
    }

    @Override
    public Image getImage() {
        String imageName = "piratecard" + count + ".png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }

    @Override
    public Image getHabitatImage() {
        return null;
    }
}

