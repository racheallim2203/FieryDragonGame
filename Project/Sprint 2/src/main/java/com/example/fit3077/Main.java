package com.example.fit3077;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

public class Main extends Application {
    private int userInput;

    @Override
    public void start(Stage stage) throws IOException {

        // Display the input dialog
        InputDialog inputDialog = new InputDialog();
        userInput = inputDialog.display();

        // Load the main layout from FXML
//        Parent root = FXMLLoader.load(getClass().getResource("fiery-dragon-game.fxml"));

        // Load the main layout from FXML

        // Load the main layout from FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fiery-dragon-game.fxml"));
        Parent root = loader.load();

        // Obtain the controller and set the user input
        FieryDragonGameController controller = loader.getController();
        // Pass the user input to the controller
        controller.setUserInput(userInput);

        // Manually call initialization methods that depend on user input
        controller.initializeGame();  // New method to initialize game logic that depends on userInput

        // Define the size of the scene
        Scene scene = new Scene(root, 600, 600);

        // Fix the window size (Prevent resizing)
        stage.setMinWidth(620);
        stage.setMinHeight(670);
        stage.setMaxWidth(620);
        stage.setMaxHeight(670);

        // Try to load the icon
        try {
            Image icon = new Image(getClass().getResourceAsStream("/BabyDragon.png"));
            stage.getIcons().add(icon);
        } catch (NullPointerException e) {
            System.err.println("Icon not found: " + e.getMessage());
        }

        // Set the title of the window (stage)
        stage.setTitle("Fiery Dragon Game");

        // Set the scene and show the stage
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
