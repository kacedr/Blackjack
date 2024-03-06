/*
* Author: Kyle Gleason
* TODO: If time, implement splitting and insurance (not a requirement but is crucial to blackjack)
*  Splitting will introduce more logic pertaining to shuffle point
* This class pertains some of the basic logic for the game. It includes counting of the hand total, handling who won
* and determining if the banker needs to draw another card.
* */
import java.util.ArrayList;

public class BlackjackGameLogic {

    /*
    * Counts the value of all the cards in a hand, 2-10 (keep value), face cards (10) ace (1 or 11)
    * Currently has no logic pertaining if an ace should be a 1 or an 11 but will possibly be updated
    * Think the logic should be in the event handlers? maybe seperated classes idk
    * */
    public int handTotal(ArrayList<Card> hand) {
        int size = 0;
        for (Card card : hand) {
            size += card.value;
        }
        return size;
    }

    /*
    * Most of the logic for determining if drawing a card results in a win/lose for dealer or player will be in
    * BlackjackGame.java. Here we are just assuming that this function is called when either
    *       A.) The player or dealer hit 21 (not both)
    *       B.) The player or dealer both choose to stand
    *       C.) The player or the dealer hands sum up to over 21
    * While there are many more niche ways to win, loose, or push (not really just not typing out every possible)
    * The gist is this function should only be called once per hand as you can only win a hand once
    * */
    public String whoWon(ArrayList <Card> playerHand1, ArrayList<Card> dealerHand) {
        int playerHand1Total = handTotal(playerHand1);
        int dealerHandTotal = handTotal(dealerHand);

        if ((playerHand1Total > dealerHandTotal) && (playerHand1Total < 22)) {
            return "player";
        } else if ((dealerHandTotal > playerHand1Total) && (dealerHandTotal < 22)) {
            return "dealer";
        }
        else if (dealerHandTotal == playerHand1Total) {
            return "push";
        }
        // *** REMOVE, FOR TESTING ***
        else {
            return "ERROR IN LOGIC";
        }
    }


    // Returns true if the dealer must draw another card (dealer must hit on 16 or less)
    public boolean evaluateBankerDraw (ArrayList<Card> hand) {
        return handTotal(hand) <= 16;
    }
}
