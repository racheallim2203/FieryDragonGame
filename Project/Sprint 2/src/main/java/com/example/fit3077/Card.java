package com.example.fit3077;

import javafx.scene.image.Image;

public abstract class Card implements Effect {
    protected String type; // This can be "animal" or "pirate"
    protected int count; // Already exists, to represent the number of animals or pirates on the card

    // The constructors, getters, and setters would need to be updated to reflect the type of card

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
        String pathName = "images/" + type + count + ".png"; // Construct the path name based on card type and count
        return new Image(Card.class.getResourceAsStream(pathName));
    }

    public Image getBackOfCardImage() {
        return new Image(Card.class.getResourceAsStream("images/back_of_card.png"));
    }
}

