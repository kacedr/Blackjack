import java.util.ArrayList;

public class BlackjackGame {
    ArrayList<Card> playerHand, bankerHand;

    BlackjackDealer theDealer;
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
            int deckAmount = 0;
            for (Card card : playerHand) {
                deckAmount += card.value;
            }

            if (deckAmount == 21) {
                totalWinnings += currentBet * 1.5;
                return currentBet * 1.5;
            }
        }

        // must be a push
        return 0;
    }
}
