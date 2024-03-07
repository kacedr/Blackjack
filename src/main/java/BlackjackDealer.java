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
    * Determining the face card (ie. if it's a K, Q or J) will be done on front end. It does not matter backend
    * for multiple decks, when assigning face cards, a track needs to be kept on which ones have been assigned for
    * each deck. (ie. 4 decks must only have 4 kings of each suit)
    * */
    public void generateDeck() {
        // make sure we get a new deck of cards everytime this is called
        deckOfCards.clear();

        for (int i = 0; i < deckAmount; i++) {
            deckOfCards.addAll(generate13Cards("spade"));
            deckOfCards.addAll(generate13Cards("club"));
            deckOfCards.addAll(generate13Cards("heart"));
            deckOfCards.addAll(generate13Cards("diamond"));
        }
    }

    // Creates a list of 13 cards for a specific suit
    private ArrayList<Card> generate13Cards(String suit) {
        ArrayList<Card> thirteenCards = new ArrayList<>();

        // generate cards 2-10
        for (int i = 2; i <= 10; i++) {
            thirteenCards.add(new Card(suit, i));
        }

        // generate face cards (will just be 3 10's)
        for (int i = 0; i < 3; i++) {
            thirteenCards.add(new Card(suit, 10));
        }

        // add the ace (value starts with 11, determining if it should be a 1 will be later)
        thirteenCards.add(new Card(suit, 11).setAce(true));

        return thirteenCards;
    }

    /*
    * This will deal two cards in the form of a list. Since in blackjack cards are dealt from the top of the deck
    * there will be no randomness to where the card is picked from, just the front of the array. The deck must be
    * shuffled before this is called.
    * */
    public ArrayList<Card> dealHand() {
        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(deckOfCards.removeLast());
        newHand.add(deckOfCards.removeLast());
        return newHand;
    }

    // Just draws a card off the top of the deck
    public Card drawOne() {return deckOfCards.removeLast();}

    // Uses collections to shuffle deck of cards
    public void shuffleDeck() {Collections.shuffle(deckOfCards);}

    // Returns size of card list (deck of cards)
    public int deckSize() {return deckOfCards.size();}
}
