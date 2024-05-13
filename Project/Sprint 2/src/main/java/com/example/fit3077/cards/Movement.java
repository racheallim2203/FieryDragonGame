package com.example.fit3077.cards;

import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import com.example.fit3077.cards.Card;

public interface Movement {
    void applyMovement(Player player, GameMap gameMap, Card card);
}
