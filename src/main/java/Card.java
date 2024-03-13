/*
* Author: Kyle Gleason
* This class represents the playing card with two variables (blackjack only cares about value) string suit, int value
* the suit holds the suit and the value holds the value of the card (where 2-10 is 2-10, face cards are 10, aces will
* initially be 11 but can change to 1 if adding 11 goes over 21).
* */

public class Card {
    String suit;
    String face; // useful for streamlining UI adding card pictures to card objects
    int value;

    // Constructor per write up
    Card(String theSuit, int theValue) {
        suit = theSuit;
        value = theValue;
    }

    // Fluent setter (required due to required constructor)
    public Card setFace(String face) {
        this.face = face;
        return this;
    }
}
