import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestGame {
    // this test file will serve as a basis for basic testing and extremely rare edge cases
    // the best possible way to test the game is playing it for hours on end

    private BlackjackGame game, gameDSize, gameDSizeShuf;

    @Before
    public void setUp() {
        // no deck amount, shuffle point
        game = new BlackjackGame();

        // deck amount, no shuffle point
        gameDSize = new BlackjackGame(6);

        // deck amount, shuffle point
        gameDSizeShuf = new BlackjackGame(6, 0.60);
    }

    @Test
    public void testDeckGeneration() {
        // test its 52 cards
        Assert.assertEquals(52, game.theDealer.deckSize());

        // test the values are set correctly, direct access of the deck should not be allowed but for
        // the sake of this project and testing, it will be allowed here
        ArrayList<Card> testDeck = game.theDealer.deckOfCards; // direct access to deck
        Assert.assertEquals(380, game.gameLogic.handTotal(testDeck));
    }

    @Test
    public void testHandGeneration() {

    }
}
