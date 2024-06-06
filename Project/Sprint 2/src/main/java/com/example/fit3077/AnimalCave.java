package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.image.Image;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AnimalCave {
    private final AnimalType animalType;
    private final int location; // Location on the game map, could correspond to habitat index

    private boolean hasAnimal;
    private AnimalType currentAnimal;

    public AnimalCave(AnimalType animalType, int location) {
        this.animalType = animalType;
        this.location = location;
        this.hasAnimal = true;
        this.currentAnimal = animalType;
    }

    public AnimalType getType() {
        return animalType;
    }

    public int getLocation() {
        return location;
    }

    public boolean hasAnimal() {
        return hasAnimal;
    }

    public void setHasAnimal(boolean hasAnimal) {
        this.hasAnimal = hasAnimal;
    }

    public AnimalType getCurrentAnimal() {
        return currentAnimal;
    }

    public void setCurrentAnimal(AnimalType currentAnimal) {
        this.currentAnimal = currentAnimal;
    }

    public Image getTokenImage() {
        String imageName = animalType.toString().toLowerCase() + "cave.png";
        String pathName = "images/" + imageName;
        return new Image(getClass().getResourceAsStream(pathName));
    }

    public static void saveCaves(List<AnimalCave> caves, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Count: " + caves.size());
            writer.newLine();

            int caveIndex = 1;
            for (AnimalCave cave : caves) {
                // Serialize the state of players to a string
                String caveData = String.format("Cave%d: %s, Location: %d, Current Animal: %s",
                        caveIndex++,
                        cave.getType(),
                        cave.getLocation(),
                        cave.getCurrentAnimal());
                writer.write(caveData);
                writer.newLine();
            }
            System.out.println("Cave state saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving cave state: " + e.getMessage());
        }
    }
}