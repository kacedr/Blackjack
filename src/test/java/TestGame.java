import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Objects;

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

        Assert.assertEquals(312, gameDSize.theDealer.deckSize());
        ArrayList<Card> testDDeck = gameDSize.theDealer.deckOfCards;
        Assert.assertEquals(2280, gameDSize.gameLogic.handTotal(testDDeck));
    }

    @Test
    public void testDeckContents() {
        int deckAmount = 6;
        Assert.assertEquals(52 * deckAmount, gameDSize.theDealer.deckSize());

        // test there's exactly one card for each suit (except for 10s, there should be 4)
        ArrayList<Card> testDeck = gameDSize.theDealer.deckOfCards; // direct access to deck
        ArrayList<Card> spade = new ArrayList<>();
        ArrayList<Card> heart = new ArrayList<>();
        ArrayList<Card> diamond = new ArrayList<>();
        ArrayList<Card> club = new ArrayList<>();
        for (Card card : testDeck) {
            switch (card.suit) {
                case "spade":
                    spade.add(card);
                    break;
                case "heart":
                    heart.add(card);
                    break;
                case "diamond":
                    diamond.add(card);
                    break;
                case "club":
                    club.add(card);
                    break;
            }
        }

        Assert.assertEquals(spade.size(), 13 * deckAmount);
        Assert.assertEquals(heart.size(), 13 * deckAmount);
        Assert.assertEquals(diamond.size(), 13 * deckAmount);
        Assert.assertEquals(club.size(), 13 * deckAmount);

        Assert.assertEquals(game.gameLogic.handTotal(spade), 95 * deckAmount);
        Assert.assertEquals(game.gameLogic.handTotal(heart), 95 * deckAmount);
        Assert.assertEquals(game.gameLogic.handTotal(diamond), 95 * deckAmount);
        Assert.assertEquals(game.gameLogic.handTotal(club), 95* deckAmount);
    }

    @Test
    public void testHandGeneration() {
        // tests its 52 cards
        Assert.assertEquals(52, game.theDealer.deckSize());

        // deal two hands, should return false as we do not need to shuffle (initially it shuffled)
        Assert.assertFalse(game.newHand());

        System.out.println(game.playerHand.get(0).value);
        System.out.println(game.playerHand.get(1).value);
    }

    @Test
    public void testShufflePoint() {

    }
}
