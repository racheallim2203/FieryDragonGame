package com.example.fit3077;

public abstract class Card implements Effect {
    protected  int count; // number of animals or pirates on the card

    public abstract boolean applyEffect(Player player, GameMap gameMap);
}

