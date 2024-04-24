package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;
public class Player {
    private int position; //stores the player's current position on the game board.

    private boolean winningStatus = false;


    // adjusts the player's position on the game board based on a number of steps to move, which can be positive or negative
    public void moveToken(int steps) {
        this.position = (this.position + steps);

    }

    // assuming cards are drawn from the top of the deck, and pop() removes the top card from the stack and returns it.
    public Card drawCard(Stack<Card> deck) {
        return deck.pop();
    }

    public boolean hasWon() {
        return winningStatus;
    }

    public int getPosition() {
        return position;
    }

    public AnimalToken getAnimalToken() {
        return animalToken;
    }

    public void setWinningStatus(boolean status) {
        this.winningStatus = status;
    }

}
