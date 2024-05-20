package com.example.fit3077.cards;

import com.example.fit3077.GameMap;
import com.example.fit3077.Player;

public interface Movement {
    void applyMovement(Player player, GameMap gameMap);
}
