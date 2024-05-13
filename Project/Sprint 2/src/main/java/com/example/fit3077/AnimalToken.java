package com.example.fit3077;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

// Represents the token each player uses to navigate the game map.
public class AnimalToken {
    private final String type;
    private int position;

    private int stepTaken;

    private boolean isOut;

    public AnimalToken(String type) {
        this.type = type; this.isOut = false;
    }

    public String getType() {
        return type;
    }

    public int getStepTaken() {
        return stepTaken;
    }

    public boolean getIsOut(){
        return isOut;
    }

    public void setIsOut(boolean out){
        isOut = out;
    }    public void setStepTaken(int stepTaken) {
        this.stepTaken = stepTaken;
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

