package com.example.fit3077;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputDialog {

    private int userInput;

    public int display() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Input Dialog");

        Label instructionLabel = new Label("How many players (2-4):");
        TextField inputField = new TextField();
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(event -> {
            try {
                int input = Integer.parseInt(inputField.getText());
                if (input >= 2 && input <= 4) {
                    userInput = input;
                    dialogStage.close();
                } else {
                    instructionLabel.setText("Invalid input, please enter an integer between 2 and 4:");
                }
            } catch (NumberFormatException e) {
                instructionLabel.setText("Invalid input, please enter an integer between 2 and 4:");
            }
        });

        VBox vbox = new VBox(10, instructionLabel, inputField, submitButton);
        Scene scene = new Scene(vbox, 300, 150);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return userInput;
    }
}
