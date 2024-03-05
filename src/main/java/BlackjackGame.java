import java.util.ArrayList;

public class BlackjackGame {
    ArrayList<Card> playerHand, bankerHand;
    BlackjackGameLogic gameLogic;
    BlackjackDealer theDealer;
    double currentBet;
    double totalWinnings;

    // amount of decks (defaults to 1)
    private int deckAmount = 1;

    // Lets us know when to shuffle based on percentage of deck used
    // Default is 40% (why 40%?, because 20 cards is the most possible cards used in one hand)
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
        double deckRemainingPercentage = (double)theDealer.deckSize() / (deckAmount * 52);
        if (deckRemainingPercentage <= cutCard) {
            theDealer.shuffleDeck();
            return true;
        }
        return false;
    }

    // this will be called everytime a player decides to "hit" or get dealt another card
    // returns true if the players next dealt card does not go over, false otherwise
    public boolean hitCard() {

    }

    // if a player chooses to stay, this will be called and the dealer will hit until he can not
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
