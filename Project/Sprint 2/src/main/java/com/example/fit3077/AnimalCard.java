package com.example.fit3077;

public class AnimalCard extends Card {
    //  The final keyword suggests that once animalType is assigned a value
    //  indicates the type of animal represented by the card.
    private final String animalType;

    // Constructor initializes the AnimalCard with a specific animal type and count.
    public AnimalCard(String animalType, int count) {
        this.animalType = animalType;
        this.count = count; // The 'count' should be set here based on the animal type or passed as a parameter.

        // count is  an attribute of the superclass Card
    }

    public String getAnimal() {
        return animalType;
    }


    /*The method applyEffect is overridden from the Card superclass.
        It takes a Player and GameMap as parameters and returns a boolean indicating whether the
        move was successful.
            */
    @Override
    public boolean applyEffect(Player player, GameMap gameMap) {
        // retrieves the player’s current Habitat from the GameMap using the player's position.
        Habitat currentHabitat = gameMap.getHabitat(player.getPosition());

        // Check if the current habitat contains an animal card that matches the type of this animal card
        if (currentHabitat.containsAnimal(animalType)) {
            player.moveToken(this.count, gameMap); // If match, move the player token forward
            return true; // Move was successful, turn continues
        } else {
            // No match, player must wait until the next turn, card effect has been applied but turn ends
            return false; // Move was not successful, turn ends
        }
    }
}

