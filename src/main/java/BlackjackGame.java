/*
* Author: Kyle Gleason
* The logic implemented here for the game is meant to accurately represent a legitimate game played at a casino.
* The reason for this is due to the fact that blackjack is actually not a game of total luck. You can learn
* the blackjack strategy chart and also learn how to count (using Hi-Low method, etc.) to gain an edge against the house.
* The way the cards are dealt, when the dealer shuffles, and the amount of decks is all crucial to gaining this edge.
* todo: The only thing we are missing is the implementation of splitting, doubling down, and insurance (not required)
*
* Protocol for running the game:
*   1.) Initialize this class (BlackjackGame.java) ONCE, everything pertaining to the game will be in this instance
*   2.) To start a new hand, call newHand(), this will create two hands, one for the banker and one for the player.
*       newHand() will also run logic to determine if the deck should be shuffled or not. newHand() should be only
*       called once per hand at the beginning
*   3.) Once the cards are dealt, the player will be up to first choose if he wants to hit or stay.
*       a.) If he chooses to hit, playerHit() should be called.
*       b.) If the player chooses to stay, playerStay() will be called. This will initiate the banker to hit until
*           he either busts or stays.
*       c.) If the player is dealt an 21, there is no need to call anything, dealHand() will know this and make a call
*           to the banker to play through.
*   4.) No matter the outcome of the game, evaluateWinnings() should be called at the end as this handles the winnings.
*
*   What does each function return and what does it tells us (for front end)?
*   newHand(): Returns a boolean. True: the deck was shuffled False: the deck was not shuffled
*   playerHit(): Returns a boolean. True: the draw card did not go over 21 False: player busted
*   playerStay(): Returns a boolean. True: the banker did not bust False: the banker busted
*   evaluateWinnings(): Returns a double. ==bet: player lost the hand x2bet+: player won hand bet==0: hand pushed
*   Front end can also access playerHand and bankerHand to see current hands cards
*
*   What the front end has set up before calling anything: It must set the totalWinnings to the initial amount of money
*   What the front end as to set up before each call to newHand(): It must set the currentBet to the requested bet
*
*   EXTRA: If there is time and our game is thoroughly tested, implement choosing deck amount and cut card. This should
*   not take long as the methods are already here and just need to be attached to the UI.
*   EXTRA: Methods pertaining to splitting, and doubling down are NOT built yet and will only be built if we really
*   have time.
* */
import java.util.ArrayList;
import java.util.Objects;

public class BlackjackGame {
    ArrayList<Card> playerHand, bankerHand; // these are the true hands
    BlackjackGameLogic gameLogic;
    BlackjackDealer theDealer;
    double currentBet;
    double totalWinnings;

    // amount of decks (defaults to 1)
    private int deckAmount = 1;

    // Lets us know when to shuffle based on percentage of deck used
    // Default is 40% (why 40%?, because 20 cards is the most possible cards used in one hand)
    // todo: When splitting gets implemented, this percentage and logic will change
    private double cutCard = 0.40;

    // constructor for single deck
    BlackjackGame() {
        this.theDealer = new BlackjackDealer();
        this.gameLogic = new BlackjackGameLogic();
        this.playerHand = new ArrayList<>();
        this.bankerHand = new ArrayList<>();
        theDealer.generateDeck();
        theDealer.shuffleDeck(); // we want to initially shuffle
    }

    // constructor for multiple decks, no cutCard
    BlackjackGame(int deckAmount) {
        this.deckAmount = deckAmount;
        this.theDealer = new BlackjackDealer(deckAmount);
        this.gameLogic = new BlackjackGameLogic();
        this.playerHand = new ArrayList<>();
        this.bankerHand = new ArrayList<>();
        theDealer.generateDeck();
        theDealer.shuffleDeck(); // we want to initially shuffle
    }

    // constructor for multiple decks, cutCard
    BlackjackGame(int deckAmount, double cutCard) {
        // our upper bound is 90%, lower is 40%, anything else might cause errors
        if (cutCard <= 0.90 && cutCard >= 0.40) {this.cutCard = cutCard;}

        this.deckAmount = deckAmount;
        this.theDealer = new BlackjackDealer(deckAmount);
        this.gameLogic = new BlackjackGameLogic();
        this.playerHand = new ArrayList<>();
        this.bankerHand = new ArrayList<>();
        theDealer.generateDeck();
        theDealer.shuffleDeck(); // we want to initially shuffle
    }

    // this will determine if we need to shuffle or not. Must be called before every hand
    // returns true if the deck('s) where shuffled, false otherwise. Other than that, this deals the hands
    public boolean newHand() {
        // clear hands todo might need to change location
        this.playerHand = new ArrayList<>();
        this.bankerHand = new ArrayList<>();

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

        // Deal new hand for the dealer and the player. Blackjack cards are dealt where
        // one card goes to the player, one to the dealer, one to the player, one to dealer.
        // Since in the write-up we are required to use dealHand which returns a list of two cards
        // we must split each hand dealt thus needing to create temp lists to hold the cards.

        ArrayList<Card> temp1 = theDealer.dealHand();
        playerHand.add(temp1.get(0));
        bankerHand.add(temp1.get(1));
        ArrayList<Card> temp2 = theDealer.dealHand();
        playerHand.add(temp2.get(0));
        bankerHand.add(temp2.get(1));

        // we only check if the player got dealt 21 as the bankers hand is abstracted from user until they stay
        if (gameLogic.handTotal(playerHand) == 21) {
            playerStay();
        }

        return shuffle;
    }

    // this will be called everytime a player decides to "hit" or get dealt another card
    // returns true if the players next dealt card does not go over, false otherwise
    // this is where it will be decided if an 11 should be a 1 or not
    public boolean playerHit() {
        // draw card
        playerHand.add(theDealer.drawOne());

        // checks if that card results in a 21
        if (gameLogic.handTotal(playerHand) == 21) {
            playerStay(); // player automatically stays, can be changed
            return true;
        }

        // player did not bust but did not hit 21
        if (gameLogic.handTotal(playerHand) < 22) {
            return true;
        }

        // if it does not pass the first few checks, the player either busted or has aces that can be converted
        if (checkAces(playerHand)) {
            // check if it resulted in a 21
            if (gameLogic.handTotal(playerHand) == 21) {
                playerStay();
            }
            return true;
        }

        // after checking all aces, player must have busted
        return false;
    }

    // takes in a hand and checks if any of the aces can be converted to 1 to keep the total 21 and under
    private boolean checkAces(ArrayList<Card> hand) {
        boolean converted = false;
        // This will continue to attempt to convert Aces from 11 to 1 as long as the hand is over 21.
        while (gameLogic.handTotal(hand) > 21) {
            boolean foundAceToConvert = false;
            for (Card card : hand) {
                if ("1".equals(card.face) && card.value == 11) {
                    card.value = 1; // Convert the Ace from 11 to 1
                    foundAceToConvert = true;
                    converted = true;
                    break; // Break after converting one Ace to recheck the total hand value
                }
            }
            // if no Aces were converted in this pass, break out of the while loop to avoid infinite loop
            if (!foundAceToConvert) {
                break;
            }
        }
        return converted; // Return true if at least one Ace was converted
    }



    // if a player chooses to stay or dealt 21, this will be called and the dealer will hit until he can not
    // return true if the banker did not bust, false if he did. This function serves to aid in readability
    // and serves no purpose other than readability. We could just use bankerHit but that would be confusing.
    public boolean playerStay() {
        return bankerHit();
    }

    // returns true if the banker stayed, false if busted
    private boolean bankerHit() {
        while (gameLogic.evaluateBankerDraw(bankerHand)) {
            bankerHand.add(theDealer.drawOne());

            // check to see if an ace can be converted to 1
            checkAces(bankerHand);
        }

        return gameLogic.handTotal(bankerHand) <= 21;
    }

    // evaluates who won the hand. This should be called no matter what after each hand finishes
    public double evaluateWinnings() {
        String whoWonRes = (gameLogic.whoWon(playerHand, bankerHand));
        System.out.println(whoWonRes);
        if (whoWonRes.compareTo("dealer") == 0 ) {
            // todo Do we minus the bet from the totalWinnings here? for now yes
            totalWinnings = totalWinnings - currentBet;

            // player can not go negative
            if (totalWinnings < 0) {totalWinnings = 0;}

            // returns how much is lost, but as a negative value
            return -currentBet;
        }
        else if (whoWonRes.compareTo("player") == 0 ) {
            // if the player hit a blackjack
            if (gameLogic.handTotal(playerHand) == 21) {
                totalWinnings += currentBet * 1.5;
                return currentBet * 1.5;
            }

            totalWinnings += currentBet;
            return currentBet; // this is just used to signify to the ui the player
        }

        // must be a push so update nothing, no winnings
        return 0;
    }
}
