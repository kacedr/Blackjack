/*
 * Author: Kyle Gleason
 * This class represents the dealer at the table (who is usually the banker) and deals with the deck. This class is
 * responsible for creating the deck('s), shuffling the decks, and dealing cards. It also keeps track of the current
 * deck size.
 * */
import java.util.ArrayList;
import java.util.Collections;

public class BlackjackDealer {
    // the deck of cards
    ArrayList<Card> deckOfCards;

    // amount of decks
    int deckAmount;

    // the hiLowCount class will always be counting no matter if the user hides it or not
    HiLowCount cardCounter; // todo: Not implemented right now

    // constructor, a new deck is created everytime we construct a class instance
    public BlackjackDealer() {
        this.deckOfCards = new ArrayList<>();
        this.deckAmount = 1;
    }

    // Does not have to be implemented for project, lets user specify amount of decks
    // for this to be implemented, we would need to edit BlackjackGame to call this constructor
    public BlackjackDealer(int decks) {
        this.deckOfCards = new ArrayList<>();
        this.deckAmount = Math.max(decks, 0); // no negative or zero amount of decks
    }

    /*
    * Logic for generating a deck of 52 cards (no jokers), 4 suits of 13 cards (2-10, K, Q, J, A)
    * Face cards will be implemented as a 10 (thus 4 10's per suit will be generated) and aces will be initially 11
    * Determining the face card will also be done here but not through the constructor due to requirements in write up.
    * for multiple decks, when assigning face cards, a track needs to be kept on which ones have been assigned for
    * each deck. (ie. 4 decks must only have 4 kings of each suit)
    * */
    public void generateDeck() {
        // make sure we get a new deck of cards everytime this is called
        deckOfCards.clear();

        for (int i = 0; i < deckAmount; i++) {
            deckOfCards.addAll(generate13Cards("Spades"));
            deckOfCards.addAll(generate13Cards("Clubs"));
            deckOfCards.addAll(generate13Cards("Hearts"));
            deckOfCards.addAll(generate13Cards("Diamond"));
        }
    }

    // Creates a list of 13 cards for a specific suit
    private ArrayList<Card> generate13Cards(String suit) {
        ArrayList<Card> thirteenCards = new ArrayList<>();

        // generate cards 2-10
        for (int i = 2; i <= 10; i++) {
            thirteenCards.add(new Card(suit, i).setFace(String.valueOf(i)));
        }

        // generate face cards (will just be 3 10's)
        thirteenCards.add(new Card(suit, 10).setFace("11")); // CONTRADICTION, THIS IS A JACK NOT A ACE
        thirteenCards.add(new Card(suit, 10).setFace("12")); // THIS IS A QUEEN
        thirteenCards.add(new Card(suit, 10).setFace("13")); // THIS IS A KING

        // add the ace (value starts with 11, determining if it should be a 1 will be later)
        thirteenCards.add(new Card(suit, 11).setFace("1")); // 1 == ACE FOR setFace

        return thirteenCards;
    }

    /*
    * This will deal two cards in the form of a list. Since in blackjack cards are dealt from the top of the deck
    * there will be no randomness to where the card is picked from, just the front of the array. The deck must be
    * shuffled before this is called.
    * */
    public ArrayList<Card> dealHand() {
        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(drawOne());
        newHand.add(drawOne());
        return newHand;
    }

    // Just draws a card off the top of the deck, sends in card value for hi-low counter
    public Card drawOne() {
        Card drawnCard = deckOfCards.removeLast();

        return drawnCard;
    }

    // Uses collections to shuffle deck of cards
    public void shuffleDeck() {Collections.shuffle(deckOfCards);}

    // Returns size of card list (deck of cards)
    public int deckSize() {return deckOfCards.size();}
}
