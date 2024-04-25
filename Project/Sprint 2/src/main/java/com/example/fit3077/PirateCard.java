package com.example.fit3077;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PirateCard extends Card {

    public PirateCard(int count) {
        super.type = "Pirate";
        super.count = count;
    }

    @Override
    public boolean applyEffect(Player player, GameMap gameMap) {
        player.moveToken(-this.count, gameMap); // Move backward
        return false; // This should always end the turn
    }

    @Override
    public Image getImage() {
        String imageName = "piratecard" + count + ".png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }
}

