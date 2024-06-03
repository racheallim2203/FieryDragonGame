package com.example.fit3077;

import com.example.fit3077.cards.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class DeckOfCards { ;
    private ArrayList<Card> deck;


    public DeckOfCards(ArrayList<Card> cardsInGame) {
        this.deck = new ArrayList<>();
        int numberOfEachType = 3;

        // Create AnimalCards - loop enumeration (Animal Type)
        for (AnimalType animalType: AnimalType.values()) {
            for (int count = 1; count <= numberOfEachType; count++) {
                deck.add(new AnimalCard(animalType,count, CardType.animalCard, deck.size(),false));
            }
        }


        // Create PirateCards
        // Only 2 of each pirate card needed
        for (int i = 0; i < 2; i++) {
            deck.add(new PirateCard(1, CardType.pirateCard, deck.size(), false));
            deck.add(new PirateCard(2, CardType.pirateCard, deck.size(), false));
        }


        // Add SwapCards
        deck.add(new SwapCard(CardType.swapCard, deck.size(), false));// Add 4 SwapCards
        deck.add(new SwapCard(CardType.swapCard, deck.size(), false));
        deck.add(new SwapCard(CardType.swapCard, deck.size(), false));
        deck.add(new SwapCard(CardType.swapCard, deck.size(), false));


        // Debug: Print the number of cards in the deck after creation
        System.out.println("Deck of Cards initialized with " + getNumOfCardsInDeck() + " cards.");

    }

    public DeckOfCards(List<String> savedState) throws Exception {
        this.deck = new ArrayList<>();
        loadCards(savedState);
    }

    public void loadCards(List<String> savedState) throws Exception {
        this.deck = new ArrayList<>();
        for (String entry : savedState) {
            String[] parts = entry.split(",");
            int index = Integer.parseInt(parts[0]);
            boolean isFlipped = Boolean.parseBoolean(parts[1]);
            CardType cardType = CardType.valueOf(parts[2]);
            int count = Integer.parseInt(parts[3]);
            String animalTypeStr = parts[4];

            Card card;
            if ("ANIMALCARD".equalsIgnoreCase(cardType.toString())) {
                AnimalType animalType = AnimalType.valueOf(animalTypeStr);
                card = new AnimalCard(animalType, count, cardType, index, isFlipped);
            } else if ("PIRATECARD".equalsIgnoreCase(cardType.toString())) {
                card = new PirateCard(count, cardType, index, isFlipped);
            } else if ("SWAPCARD".equalsIgnoreCase(cardType.toString())) {
                card = new SwapCard(cardType, index, isFlipped);
            } else {
                throw new Exception("Unknown card type: " + cardType);
            }

            card.setIndex(index);
            this.deck.add(card);
            System.out.println("DECK OF CARDS");
            System.out.println(deck);
        }
    }



    /**
     * This method will shuffle the card objects
     */
    public void shuffle() {
        Collections.shuffle(deck);
        for (int i = 0; i < deck.size(); i++) {
            deck.get(i).setIndex(i);  // Update the index after shuffling
        }
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
        return new ArrayList<>(deck);
    }




}
