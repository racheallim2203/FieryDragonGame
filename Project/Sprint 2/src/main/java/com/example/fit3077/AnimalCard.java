package com.example.fit3077;
import javafx.scene.image.Image;
public class AnimalCard extends Card {
    private final String animalType; // Type of the animal on the card

    public AnimalCard(String animalType, int count) {
        this.animalType = animalType;
        this.count = count;
    }

    @Override
    public boolean applyEffect(Player player, GameMap gameMap) {
        // retrieves the player’s current Habitat from the GameMap using the player's position.
        Habitat currentHabitat = gameMap.getHabitat(player.getPosition());

        // Check if the current habitat contains an animal card that matches the type of this animal card
        if (currentHabitat.containsAnimal(getType())) {
            player.moveToken(this.count, gameMap); // If match, move the player token forward
            return true; // Move was successful, turn continues
        } else {
            // No match, player must wait until the next turn, card effect has been applied but turn ends
            return false; // Move was not successful, turn ends
        }
    }

    @Override
    public Image getImage() {
        String imageName = animalType.toLowerCase() + count + ".png"; // Construct the image file name based on animal type and count
        String pathName = "images/" + imageName;
        return new Image(AnimalCard.class.getResourceAsStream(pathName));
    }

    public String getAnimalType() {
        return animalType;
    }

}

