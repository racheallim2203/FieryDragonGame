package com.example.fit3077;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

// Represents the token each player uses to navigate the game map.
public class AnimalToken {
    private final String type;
    private int position;

    public AnimalToken(String type, int position) {
        this.type = type;
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Image getTokenImage() {
        String imageName = type.toLowerCase() + "token.png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }

}

