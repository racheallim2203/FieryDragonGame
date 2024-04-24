package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

public class PirateCard extends Card {
    public PirateCard(int count) {
        super.count = count; }

    @Override
    public boolean applyEffect() {
        return false; // This should always end the turn
    }
}
