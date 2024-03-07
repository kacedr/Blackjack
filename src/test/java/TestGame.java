import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestGame {
    private BlackjackGame game;

    @Before
    public void setUp() {
        game = new BlackjackGame();
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
}
