package com.example.fit3077;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main extends Application {
    private int userInput;
    private boolean isNewGame = true;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Setup the initial alert for game choice
            Optional<ButtonType> result = setupGameChoiceDialog();

            if (result.isPresent() && result.get().getText().equals("New Game")) {
                loadGame(stage, true);
            } else if (result.isPresent() && result.get().getText().equals("Continue Game")) {
                loadGame(stage, false);
            } else {
                stage.close();
            }
        } catch (Exception e) {
            System.err.println("Initialization failed: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }

    }

    private Optional<ButtonType> setupGameChoiceDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Fiery Dragon Game");
        alert.setHeaderText("Would you like to start a new game or continue a saved game?");
        ButtonType newGameButton = new ButtonType("New Game");
        ButtonType continueGameButton = new ButtonType("Continue Game");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(newGameButton, continueGameButton, cancelButton);
        return alert.showAndWait();
    }

    private int promptForUserInput() {
        TextInputDialog dialog = new TextInputDialog("4");
        dialog.setTitle("Game Setup");
        dialog.setHeaderText("Set Up Your Game");
        dialog.setContentText("Enter the number of players:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Integer.parseInt(result.get());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Defaulting to 4 players.");
                return 4;  // Default to 4 players if input is invalid
            }
        } else {
            return 4;  // Default to 4 players if no input is given
        }
    }

    private void loadGame(Stage stage, boolean isNewGame) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fiery-dragon-game.fxml"));
        Parent root = loader.load();
        FieryDragonGameController controller = loader.getController();
        controller.setIsNewGame(isNewGame); // Inform the controller whether it's a new game or continuation

        if (isNewGame) {
            userInput = promptForUserInput();  // Get user input right before starting a new game
            controller.setUserInput(userInput);  // Set user input
            System.out.println("Initializing new game with " + userInput + " players.");
            controller.initializeGame(true);
            controller.saveGameState();
        } else {
            System.out.println("Loading existing game...");
            controller.initializeGame(false);
        }
        // Add listener to save game state when the application is closing
        stage.setOnCloseRequest(event -> {
            controller.saveGameState();
        });

        Scene scene = new Scene(root, 620, 620);
        stage.setScene(scene);
        stage.setTitle("Fiery Dragon Game");
        setupStageIcon(stage);
        stage.setResizable(false);
        stage.show();
        System.out.println("Game loaded and displayed.");

    }

    private void setupStageIcon(Stage stage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/BabyDragon.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Icon not found: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
