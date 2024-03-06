/*
* Author: Kyle Gleason
* The logic implemented here for the game is meant to accurately represent a legitimate game played at a casino.
* The reason for this is due to the fact that blackjack is actually not a game of total luck. You can learn
* the blackjack strategy chart and also learn how to count (using Hi-Low method, etc.) to gain an edge against the house.
* The way the cards are dealt, when the dealer shuffles, and the amount of decks is all crucial to gaining this edge.
* todo: The only thing we are missing is the implementation of splitting, doubling down, and insurance (not required)
* */
import java.util.ArrayList;

public class BlackjackGame {
    ArrayList<Card> playerHand, bankerHand;
    BlackjackGameLogic gameLogic;
    BlackjackDealer theDealer;
    double currentBet;
    double totalWinnings;

    // since we can not edit the constructor for Card per requirements, this will say if an ace was added
    boolean containAce = false;

    // amount of decks (defaults to 1)
    private int deckAmount = 1;

    // Lets us know when to shuffle based on percentage of deck used
    // Default is 40% (why 40%?, because 20 cards is the most possible cards used in one hand)
    // todo: When splitting gets implemented, this percentage and logic will change
    private double cutCard = 0.40;

    // constructor for single deck
    BlackjackGame() {
        this.theDealer = new BlackjackDealer();
    }

    // constructor for multiple decks, no cutCard
    BlackjackGame(int deckAmount) {
        this.deckAmount = deckAmount;
        this.theDealer = new BlackjackDealer(deckAmount);
    }

    // constructor for multiple decks, cutCard
    BlackjackGame(int deckAmount, double cutCard) {
        // our limits are 40%-90%
        if(cutCard <= 0.90 && cutCard >= 0.40) {this.cutCard = cutCard;}

        this.deckAmount = deckAmount;
        this.theDealer = new BlackjackDealer(deckAmount);
    }

    // this will determine if we need to shuffle or not. Must be called before every hand
    // returns true if the deck('s) where shuffled, false otherwise
    public boolean newHand() {
        // this is where it will be determined if we should shuffle based on cut card
        boolean shuffle = false;
        double deckRemainingPercentage = (double)theDealer.deckSize() / (deckAmount * 52);
        if (deckRemainingPercentage <= cutCard) {
            // generate a new deck and clear the old one
            theDealer.generateDeck();

            // shuffle the deck
            theDealer.shuffleDeck();

            shuffle = true;
        }

        // Deal new hand for the dealer and the player. blackjack is cards are dealt where
        // one card goes to the player, one to the dealer, one to the player, one to dealer.
        // Since in the write-up we are required to use dealHand which returns a list of two cards
        // we must split each hand dealt thus needing to create temp lists to hold the cards.

        ArrayList<Card> temp1 = theDealer.dealHand();
        playerHand.add(temp1.get(0));
        bankerHand.add(temp1.get(1));
        ArrayList<Card> temp2 = theDealer.dealHand();
        playerHand.add(temp2.get(0));
        bankerHand.add(temp2.get(1));

        return shuffle;
    }

    // this will be called everytime a player decides to "hit" or get dealt another card
    // returns true if the players next dealt card does not go over, false otherwise
    // this is where it will be decided if an 11 should be a 1 or not, we will also call
    // the dealer to hit (if applicable, evaluateBankerDraw figures that out) after the player hits
    public boolean playerHit() {
        // draw card
        playerHand.add(theDealer.drawOne());

        // player did not bust (could have hit 21)
        if (gameLogic.handTotal(playerHand) < 22) {
            // check if there is two or more aces. Only 1 ace can be an 11
            // we are going to set the value on the remaining aces, if there is more than 1, to 1
            // when we are checking for aces, check value and ace flag



            // have the banker hit if 16 or less
            // call banker function

            return true;
        }
        // the player busted but has an ace (or multiple), so we check if we can switch it
        else if (gameLogic.handTotal(playerHand) > 21) {

        }
        // if the player drew a card resulting in a hand over 21, and they do not have an ace
        else {
            return false;
        }

        return false; //remove
    }

    // returns true if the banker hit or stayed, false if the dealer busted
    public boolean bankerHit() {
        return false; //remove
    }

    // if a player chooses to stay or dealt 21, this will be called and the dealer will hit until he can not
    public void playerStay() {

    }

    public double evaluateWinnings() {
        if ((gameLogic.whoWon(playerHand, bankerHand)).compareTo("dealer") == 0 ) {
            // todo Do we minus the bet from the totalWinnings here? for now yes
            totalWinnings -= currentBet;

            // player can not go negative
            if (totalWinnings < 0) {totalWinnings = 0;}

            // returns how much is lost, but as a positive value
            return currentBet;
        }
        else if ((gameLogic.whoWon(playerHand, bankerHand)).compareTo("player") == 0 ) {
            // if the player hit a blackjack
            if (gameLogic.handTotal(playerHand) == 21) {
                totalWinnings += currentBet * 1.5;
                return currentBet * 1.5;
            }

            totalWinnings += currentBet;
            return currentBet;
        }

        // must be a push so update nothing, no winnings
        return 0;
    }
}
