/* todo change name of class file?
* This is where the bulk of the gameplay decisions will be made. This should be instantiated once at the beginning of
* the program and will handle
* This class will decide:
*   - When to shuffle the deck('s)
*   - If the player or dealer busted after a draw (do not allow them to draw more)
*   - If an ace should be an 11 or 1
* */
public class Player {
    // theGame should not be accessed outside of this class
    private final BlackjackGame theGame;

    // Lets us know the starting number of cards to help determine when to shuffle
    // Default is 1 deck
    private int deckAmount = 1;

    // Lets us know when to shuffle based on percentage of deck used
    // Default is 40% (why 40%?, because 20 cards is the most possible used in one hand)
    private double cutCard = 0.40;

    // default constructor, 1 deck
    public Player() {
        this.theGame = new BlackjackGame();
    }

    // more than one deck, no shuffle point, defaults to 40%
    public Player(int deckAmount) {
        this.theGame = new BlackjackGame(deckAmount);
        this.deckAmount = deckAmount;
    }

    // more than one deck, shuffle point
    public Player(int deckAmount, double cutCard) {
        this.theGame = new BlackjackGame(deckAmount);
        this.deckAmount = deckAmount;

        // minimum amount is 40%, maximum is 90%, anything else defaults to 40%
        if(cutCard < 0.90 || cutCard > 0.40) {this.cutCard = cutCard;}
    }

    // this should be called at the beginning of each hand, returns true if deck is shuffled
    public boolean newHand() {
        // this is where it will be determined if we should shuffle based on cut card
        double deckRemainingPercentage = (double)theGame.theDealer.deckSize() / (deckAmount * 52);

    }

    public boolean drawCard() {

    }
}
