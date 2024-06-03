package com.example.fit3077.cards;

import com.example.fit3077.AnimalType;
import com.example.fit3077.CardType;
import com.example.fit3077.GameMap;
import com.example.fit3077.Player;
import javafx.scene.image.Image;

public class SwapCard extends Card {

    public SwapCard(CardType cardType, int index, boolean isFlipped) {
        super(cardType,0,isFlipped, index);
    }

    @Override
    public void applyMovement(Player currentPlayer, GameMap gameMap) {

    }

    @Override
    public Image getImage() {
        String imageName = "swapCard.png";
        String pathName = "/com/example/fit3077/images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));

    }

    @Override
    public boolean matchesType(AnimalType animalType) {
        return false;
    }

    @Override
    public String getImageFileName() {
        return "swapCard.png";
    }

    @Override
    public String toString() {
        return "{" +
                "index=" + index +
                ", type=" + cardType +
                ", AnimalType=" + null +
                ", isFlipped=" + isFlipped +
                ", stepCount=" + stepCount +
                '}';
    }
}
