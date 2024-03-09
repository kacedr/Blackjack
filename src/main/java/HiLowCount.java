/*
* This is the counter for Hi Low strategy, This is going to automatically count the cards drawn and divide them by the
* Amount of decks. The user has the option to enable or disable this counter. The logic goes as follows:
*   2-6 (+1)
*   7-9 (+0)
*   10-A (-1)
* At the end of each hand, a True Count will be computed, which is the count divided by the amount of decks
* DISCLAIMER: The HiLow method should be used in unison with the blackjack strategy chart to gain a maximum edge.
* Casinos also are aware of card counting, and I am not responsible for getting anyone banned from casinos etc.
* Card counting is NOT illegal in all 50 states as long as players don't use an external device to aid in the counting.
* */
public class HiLowCount {
    int count = 0;
    double trueCount = 0.0;
    int deckAmount = 1;


    HiLowCount () {

    }

    // updates the deck of cards


}
