import java.util.ArrayList;

public class BlackjackGame {
    ArrayList<Card> playerHand, bankerHand;

    // constructor for single deck
    BlackjackGame() {
        BlackjackDealer theDealer;
    }

    // constructor for multiple decks, not implemented atm
    BlackjackGame(int deckAmount) {
        BlackjackDealer theDealer = new BlackjackDealer(deckAmount);
    }

    BlackjackGameLogic gameLogic;
    double currentBet;
    double totalWinnings;
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
