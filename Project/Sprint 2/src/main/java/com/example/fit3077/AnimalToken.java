package com.example.fit3077;

import javafx.scene.image.Image;

// Represents the token each player uses to navigate the game map.
public class AnimalToken {

    private final AnimalType animalType;

    private int position;

    private int stepTaken;

    private boolean isOut;

    // attributes to record the location of token at cave
    private double initialLayoutX;
    private double initialLayoutY;

    // attributes to record the location of token at other cave
    private double layoutX;
    private double layoutY;

    // attributes to record the animal type of other cave
    private AnimalType otherCaveType;

    public AnimalToken(AnimalType animalType) {
        this.animalType = animalType;
    }

    public AnimalType getType() {
        return animalType;
    }

    public int getStepTaken() {
        return stepTaken;
    }

    public boolean getIsOut(){
        return isOut;
    }

    public void setIsOut(boolean out){
        isOut = out;
    }

    public void setStepTaken(int stepTaken) {
        this.stepTaken = stepTaken;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getInitialLayoutX() {
        return initialLayoutX;
    }

    public void setInitialLayoutX(double initialLayoutX) {
        this.initialLayoutX = initialLayoutX;
    }

    public double getInitialLayoutY() {
        return initialLayoutY;
    }

    public void setInitialLayoutY(double initialLayoutY) {
        this.initialLayoutY = initialLayoutY;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    public AnimalType getOtherCaveType() {
        return otherCaveType;
    }

    public void setOtherCaveType(AnimalType otherCaveType) {
        this.otherCaveType = otherCaveType;
    }

    public Image getTokenImage() {
        String imageName = animalType.toString().toLowerCase() + "token.png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }

}

