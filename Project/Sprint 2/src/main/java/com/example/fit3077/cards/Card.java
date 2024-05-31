package com.example.fit3077.cards;

import com.example.fit3077.AnimalType;
import com.example.fit3077.CardType;
import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public abstract class Card implements Movement {
//    protected String cardType; // This can be "animal" or "pirate"
    protected int stepCount;

    private final CardType cardType;
    private boolean isFlipped;

    protected Card(CardType cardType) {
        this.cardType = cardType;
    }

    public abstract String getImageFileName(); // Abstract method to get image file name

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public int getCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    // This method needs to be implemented in subclasses
//    @Override
//    public abstract void applyMovement(Player player, GameMap gameMap, Card card);

//    public abstract void applyMovement(Player player, GameMap gameMap);

    // an abstract method
    // This method will return an Image that represents the Card
    public abstract Image getImage();

    public Image getBackOfCardImage() {
        return new Image(Card.class.getResourceAsStream("/images/uncoveredcards.png"));
    }

//    public abstract Image getHabitatImage();
    public CardType getCardType() {
    return cardType;
    }

    public abstract boolean matchesType(AnimalType animalType);




}

