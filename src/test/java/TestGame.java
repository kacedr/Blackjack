import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.*;

public class TestGame {
    // this test file will serve as a basis for basic testing and extremely rare edge cases
    // the best possible way to test the game is playing it for hours on end
    // testing actual gameplay is hard since shuffle is random and seeding would require changing the class files

    private BlackjackGame game, gameDSize;
    private BlackjackGameLogic gameLogic;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;

    @Before
    public void setUp() {
        // no deck amount, shuffle point
        game = new BlackjackGame();

        // deck amount, no shuffle point
        gameDSize = new BlackjackGame(6);

        gameLogic = new BlackjackGameLogic();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
    }

    @Test
    public void testDeckGeneration() {
        // test its 52 cards
        assertEquals(52, game.theDealer.deckSize());

        // test the values are set correctly, direct access of the deck should not be allowed but for
        // the sake of this project and testing, it will be allowed here
        ArrayList<Card> testDeck = game.theDealer.deckOfCards; // direct access to deck
        assertEquals(380, game.gameLogic.handTotal(testDeck));

        assertEquals(312, gameDSize.theDealer.deckSize());
        ArrayList<Card> testDDeck = gameDSize.theDealer.deckOfCards;
        assertEquals(2280, gameDSize.gameLogic.handTotal(testDDeck));
    }

    @Test
    public void testDeckContents() {
        int deckAmount = 6;
        assertEquals(52 * deckAmount, gameDSize.theDealer.deckSize());

        // test there's exactly one card for each suit (except for 10s, there should be 4)
        ArrayList<Card> testDeck = gameDSize.theDealer.deckOfCards; // direct access to deck
        ArrayList<Card> spade = new ArrayList<>();
        ArrayList<Card> heart = new ArrayList<>();
        ArrayList<Card> diamond = new ArrayList<>();
        ArrayList<Card> club = new ArrayList<>();
        for (Card card : testDeck) {
            switch (card.suit) {
                case "Spades":
                    spade.add(card);
                    break;
                case "Hearts":
                    heart.add(card);
                    break;
                case "Diamond":
                    diamond.add(card);
                    break;
                case "Clubs":
                    club.add(card);
                    break;
            }
        }

        assertEquals(spade.size(), 13 * deckAmount);
        assertEquals(heart.size(), 13 * deckAmount);
        assertEquals(diamond.size(), 13 * deckAmount);
        assertEquals(club.size(), 13 * deckAmount);

        assertEquals(game.gameLogic.handTotal(spade), 95 * deckAmount);
        assertEquals(game.gameLogic.handTotal(heart), 95 * deckAmount);
        assertEquals(game.gameLogic.handTotal(diamond), 95 * deckAmount);
        assertEquals(game.gameLogic.handTotal(club), 95* deckAmount);
    }

    @Test
    public void testHandGeneration() {
        // tests its 52 cards
        assertEquals(52, game.theDealer.deckSize());

        // deal two hands, should return false as we do not need to shuffle (initially it shuffled)
        Assert.assertFalse(game.newHand());

        System.out.println(game.playerHand.get(0).value);
        System.out.println(game.playerHand.get(1).value);
    }

    @Test
    public void testDrawOne() {
        assertNotNull("Drawing a card should not return null", game.theDealer.drawOne());
        assertEquals("Deck should have 51 cards after drawing one card", 51, game.theDealer.deckSize());
    }

    @Test
    public void testShuffleDeck() {
        ArrayList<Card> beforeShuffle = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            beforeShuffle.add(game.theDealer.drawOne());
        }
        game.theDealer.generateDeck();
        game.theDealer.shuffleDeck();
        ArrayList<Card> afterShuffle = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            afterShuffle.add(game.theDealer.drawOne());
        }
        assertNotEquals(beforeShuffle, afterShuffle);
        assertEquals("Deck should be 0 cards after shuffle and empty", 0, game.theDealer.deckSize());
    }

    @Test
    public void testDeckSize() {
        assertEquals("Deck should be 52 cards initially", 52, game.theDealer.deckSize());
        game.theDealer.dealHand();
        assertEquals("Deck should have 50 cards after dealing a hand", 50, game.theDealer.deckSize());
        game.theDealer.drawOne();
        assertEquals("Deck should have 49 cards after drawing one card", 49, game.theDealer.deckSize());
    }

    @Test
    public void testWhoWon() {
        // Player wins
        playerHand.add(new Card("Hearts", 10));
        playerHand.add(new Card("Diamonds", 8));
        dealerHand.add(new Card("Spades", 10));
        dealerHand.add(new Card("Clubs", 7));
        assertEquals("Player should win", "player", gameLogic.whoWon(playerHand, dealerHand));

        // Dealer wins
        playerHand.clear();
        dealerHand.clear();
        playerHand.add(new Card("Hearts", 7));
        playerHand.add(new Card("Diamonds", 8));
        dealerHand.add(new Card("Spades", 9));
        dealerHand.add(new Card("Clubs", 9));
        assertEquals("Dealer should win", "dealer", gameLogic.whoWon(playerHand, dealerHand));

        // Push
        playerHand.clear();
        dealerHand.clear();
        playerHand.add(new Card("Hearts", 10));
        playerHand.add(new Card("Diamonds", 7));
        dealerHand.add(new Card("Spades", 10));
        dealerHand.add(new Card("Clubs", 7));
        assertEquals("Should be a push", "push", gameLogic.whoWon(playerHand, dealerHand));
    }

    @Test
    public void testHandTotal() {
        // Regular cards
        playerHand.add(new Card("Hearts", 2));
        playerHand.add(new Card("Diamond", 3));
        assertEquals("Hand total should be 5", 5, gameLogic.handTotal(playerHand));

        // Face cards
        playerHand.clear();
        playerHand.add(new Card("Hearts", 10));
        playerHand.add(new Card("Diamond", 10));
        playerHand.add(new Card("Spades", 10));
        assertEquals("Hand total should be 30", 30, gameLogic.handTotal(playerHand));
    }

    @Test
    public void testEvaluateBankerDraw() {
        // Should draw
        dealerHand.add(new Card("Hearts", 2));
        dealerHand.add(new Card("Diamonds", 3));
        assertTrue("Dealer should draw another card", gameLogic.evaluateBankerDraw(dealerHand));

        // Should not draw
        dealerHand.clear();
        dealerHand.add(new Card("Hearts", 10));
        dealerHand.add(new Card("Diamonds", 7));
        assertFalse("Dealer should not draw another card", gameLogic.evaluateBankerDraw(dealerHand));
    }

    @Test
    public void testDealerHitsUntilSeventeen() {
        ArrayList<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card("Hearts", 2));
        dealerHand.add(new Card("Diamonds", 3));
        game.bankerHand = dealerHand;

        while (gameLogic.evaluateBankerDraw(game.bankerHand)) {
            game.bankerHand.add(game.theDealer.drawOne());
        }

        assertTrue("Dealer should stop drawing at 17 or more, not before", gameLogic.handTotal(game.bankerHand) >= 17);
    }
    @Test
    public void testBlackjackForPlayerWin() {
        ArrayList<Card> playerHand = new ArrayList<>();
        ArrayList<Card> dealerHand = new ArrayList<>();
        playerHand.add(new Card("Hearts", 11)); // 11 (Ace)
        playerHand.add(new Card("Diamonds", 10)); // 21
        dealerHand.add(new Card("Spades", 9)); // dealer 9
        dealerHand.add(new Card("Clubs", 9)); // dealer 18
        assertEquals("Player should win with Blackjack", "player", gameLogic.whoWon(playerHand, dealerHand));
    }
    @Test
    public void testWinningsWithBlackJack() {
        // Given
        BlackjackGame game = new BlackjackGame();
        game.currentBet = 100;
        game.totalWinnings = 500;

        // Player has blackjack
        game.playerHand.add(new Card("Hearts", 11)); // 11 (Ace)
        game.playerHand.add(new Card("Diamonds", 10)); // 21

        // Dealer does not have blackjack and should stay on 17
        game.bankerHand.add(new Card("Spades", 10)); // Dealer 10
        game.bankerHand.add(new Card("Clubs", 7)); // Dealer 17


        double winnings = game.evaluateWinnings();


        assertEquals("Player wins with blackjack should increase winnings", 150, winnings, 0.0); // test that blackjack pays 150%
        assertEquals("Total winnings should be updated correctly", 650, game.totalWinnings, 0.0);
    }
    @Test
    public void testPushDoesNotAffectWinnings() {
        game.totalWinnings = 500;
        game.currentBet = 100;

        game.playerHand.add(new Card("Hearts", 10));
        game.playerHand.add(new Card("Diamonds", 7)); // 17
        game.bankerHand.add(new Card("Clubs", 10));
        game.bankerHand.add(new Card("Spades", 7)); // also 17

        double winnings = game.evaluateWinnings(); // check winnings on push
        // use .01 pad since working with double
        assertEquals("Winnings should be 0 on a push", 0, winnings, 0.01);
        assertEquals("Total winnings should remain unchanged on a push", 500, game.totalWinnings, 0.01);
    }

}
