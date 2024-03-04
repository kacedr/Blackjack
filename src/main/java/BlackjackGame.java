import java.util.ArrayList;

public class BlackjackGame {
    ArrayList<Card> playerHand, bankerHand;

    BlackjackDealer theDealer;
    BlackjackGameLogic gameLogic;
    double currentBet;
    double totalWinnings;
    public double evaluateWinnings() {
        if ((gameLogic.whoWon(playerHand, bankerHand)).compareTo("dealer") == 0 ) {
            // todo Do we minus the bet from the totalWinnings here?
        }
    }
}
