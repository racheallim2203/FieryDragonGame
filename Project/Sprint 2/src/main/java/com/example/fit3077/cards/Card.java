package com.example.fit3077.cards;

import com.example.fit3077.AnimalType;
import com.example.fit3077.CardType;
import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public abstract class Card implements Movement {
//    protected String cardType; // This can be "animal" or "pirate"
    protected int stepCount;

    public CardType cardType;

    protected boolean isFlipped;
    protected int index;  // Position in the deck or display

    public Card(CardType cardType, int stepCount, boolean isFlipped, int index) {
        this.cardType = cardType;
        this.stepCount = stepCount;
        this.isFlipped = isFlipped;
        this.index = index;  // Initialize index
    }

    public abstract String getImageFileName(); // Abstract method to get image file name

    public boolean isFlipped() {
        return isFlipped;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    @Override
    public String toString() {
        return "{" +
                "index=" + index +
                ", type=" + cardType +
                ", stepCount=" + stepCount +
                ", isFlipped=" + isFlipped +
                '}';
    }

}

