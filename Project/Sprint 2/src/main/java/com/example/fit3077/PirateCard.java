package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

public class PirateCard extends Card {

    public PirateCard(int count) {
        setType("piratecard");
        setCount(count);
    }

    @Override
    public boolean applyEffect(Player player, GameMap gameMap) {
        player.moveToken(-this.count, gameMap); // Move backward
        return false; // This should always end the turn
    }
}

