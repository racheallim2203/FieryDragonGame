package com.example.fit3077.cards;

import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public abstract class Card implements Movement {
//    protected String cardType; // This can be "animal" or "pirate"
    protected int stepCount;

//    public String getType() {
//        return cardType;
//    }
//
//    public void setType(String type) {
//        this.cardType = type;
//    }

    public int getCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    // This method needs to be implemented in subclasses
    @Override
    public abstract void applyMovement(Player player, GameMap gameMap, Card card);

    // an abstract method
    // This method will return an Image that represents the Card
    public abstract Image getImage();

    // This method will return an Image that represents the Card
//    public Image getImage() {
//        String fileName = cardType + stepCount + ".png";
//        String pathName = "images/" + fileName;
//        Image image = new Image(Card.class.getResourceAsStream(pathName));
//        if (image.isError()) {
//            throw new RuntimeException("Failed to load image: " + pathName);
//        }
//        return image;
//    }

    public Image getBackOfCardImage() {
        return new Image(Card.class.getResourceAsStream("/images/uncoveredcards.png"));
    }

    public abstract Image getHabitatImage();
}

