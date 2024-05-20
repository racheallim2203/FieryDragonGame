package com.example.fit3077;

import com.example.fit3077.cards.AnimalCard;
import com.example.fit3077.cards.Card;
import com.example.fit3077.cards.PirateCard;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class DeckOfCards {
    private final ArrayList<Card> deck;

    public DeckOfCards() {
        this.deck = new ArrayList<>();
        int numberOfEachType = 3;

        // Create AnimalCards - loop enumeration (Animal Type)
        for (AnimalType animalType: AnimalType.values()) {
            for (int count = 1; count <= numberOfEachType; count++) {
                deck.add(new AnimalCard(animalType,count, CardType.animalCard));
            }
        }


        // Create PirateCards
        // Only 2 of each pirate card needed
        for (int i = 0; i < 2; i++) {
            deck.add(new PirateCard(1, CardType.pirateCard)); // Pirate card with 1 pirate
            deck.add(new PirateCard(2, CardType.pirateCard)); // Pirate card with 2 pirates
        }

        // Debug: Print the number of cards in the deck after creation
        System.out.println("Deck of Cards initialized with " + getNumOfCardsInDeck() + " cards.");

    }

    /**
     * This method will shuffle the card objects
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

//    /**
//     * This method will return the top card from the deck.
//     * If the deck is empty, it will return null.
//     */
//    public Card dealTopCard() {
//        if (deck.size()>0)
//            return deck.remove(0);
//        else
//            return null;
//    }

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
        return new ArrayList<>(deck);
    }


}
