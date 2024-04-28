package com.example.fit3077;
import javafx.scene.image.Image;
public class AnimalCard extends Card {
    private final String animalType; // Type of the animal on the card

    public AnimalCard(String animalType, int count) {
        super.type = animalType;
        super.count = count;
        this.animalType = animalType;
    }


    @Override
    public void applyEffect(Player currentPlayer, GameMap gameMap, Card card) {
        int forwardSteps = card.getCount();
        currentPlayer.moveToken(forwardSteps, gameMap);
    }

    @Override
    public Image getImage() {
        String imageName = animalType.toLowerCase() + count + ".png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }
    @Override
    public Image getHabitatImage() {
        String imageName = animalType.toLowerCase() + ".png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }

    public String getAnimalType() {
        return animalType;
    }

}

