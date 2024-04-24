package com.example.fit3077;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckOfCards {
    private final ArrayList<Card> deck;

    public DeckOfCards() {
        this.deck = new ArrayList<>();
        List<String> animalTypes = List.of("Dragon", "Fish", "Pufferfish", "Octopus"); // Add more animal types if necessary
        int numberOfEachType = 3;

        // Create AnimalCards
        for (String animalType : animalTypes) {
            for (int count = 1; count <= numberOfEachType; count++) {
                deck.add(new AnimalCard(animalType, count % 4 + 1)); // Assuming count goes from 1 to 3 repeatedly
            }
        }

        // Create PirateCards
        // Assuming we want a similar number of pirate cards as animal cards, adjust numbers as needed
        for (int i = 0; i < numberOfEachType; i++) {
            deck.add(new PirateCard(1)); // Pirate card with 1 pirate
            deck.add(new PirateCard(2)); // Pirate card with 2 pirates
        }

        // Shuffle the deck
        shuffle();
    }

    /**
     * This method will shuffle the card objects
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * This method will return the top card from the deck.
     * If the deck is empty, it will return null.
     */
    public Card dealTopCard() {
        if (deck.size() > 0)
            return deck.remove(0);
        else
            return null;
    }

    /**
     * This returns the number of cards left in the deck
     */
    public int getNumOfCardsInDeck() {
        return deck.size();
    }

    /**
     * This will return the whole deck of cards.
     */
    public List<Card> getCards() {
        return deck;
    }
}
