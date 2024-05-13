package com.example.fit3077.cards;

import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public class PirateCard extends Card {

    public PirateCard(int count) {
        super.type = "piratecard";
        super.count = count; // Ensure this is within the range [1, 2] for PirateCard
    }

    @Override
    public void applyMovement(Player currentPlayer, GameMap gameMap, Card card) {
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

