/*
* Author: Kyle Gleason
* This class represents the playing card with two variables (blackjack only cares about value) string suit, int value
* the suit holds the suit and the value holds the value of the card (where 2-10 is 2-10, face cards are 10, aces will
* initially be 11 but can change to 1 if adding 11 goes over 21). Each card also has a string for the face. "NA" means
* not a face card. Aces are not considered a face card but oh well, we set it to "ace".
* */

public class Card {
    String suit;
    String face; // NA means not a face card,
    int value;

    Card(String theSuit, int theValue) {
        suit = theSuit;
        value = theValue;
    }

    // Fluent setters
    public Card setFace(String face) {
        this.face = face;
        return this;
    }
}
