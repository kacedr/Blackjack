/*
* Author: Kyle Gleason
* This class represents the playing card with two variables (blackjack only cares about value) string suit, int value
* the suit holds the suit and the value holds the value of the card (where 2-10 is 2-10, face cards are 10, aces will
* initially be 11 but can change to 1 if adding 11 goes over 21). The actual type of face card does not matter and will
* be randomly generated on the front end.
* */

public class Card {
    String suit;
    int value;
    boolean ace; // set true if the card is an ace can be removed maybe
    Card(String theSuit, int theValue) {
        suit = theSuit;
        value = theValue;
        ace = false;
    }

    // Fluent setter
    public Card setAce(boolean ace) {
        this.ace = ace;
        return this;
    }
}
