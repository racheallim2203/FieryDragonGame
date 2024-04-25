package com.example.fit3077;

import javafx.scene.image.Image;

public abstract class Card implements Effect {
    protected String type; // This can be "animal" or "pirate"
    protected int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // This method needs to be implemented in subclasses
    @Override
    public abstract boolean applyEffect(Player player, GameMap gameMap);

    // This method will return an Image that represents the Card
    public Image getImage() {
        String fileName = type + count + ".png";
        String pathName = "images/" + fileName;
        Image image = new Image(Card.class.getResourceAsStream(pathName));
        if (image.isError()) {
            throw new RuntimeException("Failed to load image: " + pathName);
        }
        return image;
    }

    public Image getBackOfCardImage() {
        return new Image(Card.class.getResourceAsStream("/images/uncoveredcards.png"));
    }

    public abstract Image getHabitatImage();
}

