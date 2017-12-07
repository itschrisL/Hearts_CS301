package edu.up.cs301.hearts;



import java.io.Serializable;
import java.util.ArrayList;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.Suit;
import edu.up.cs301.card.Card;


/**
 *
 *
 * Created by emmasoriano on 10/19/17.
 */

public class CardDeck implements Serializable {

    int currCard=0;
    // to satisfy Serializable interface
    private static final long serialVersionUID = 7216223171210121485L;

    // the cards in our deck; the last card in the ArrayList is the top card
    // in the deck
    public ArrayList<Card> cards;

    /**
     * constructor, creating an empty deck
     */
    public CardDeck() {
        cards = new ArrayList<Card>();
    }

    /**
     * copy constructor, making an exact copy of a deck
     *
     * @param orig the deck from which the copy should be made
     */
    public CardDeck(CardDeck orig) {
        // synchronize to ensure that original is not being modified as we
        // iterate over it
        synchronized (orig.cards) {
            // create a new arrayList for our new deck; add each card in it
            cards = new ArrayList<Card>();
            for (Card c : orig.cards) {
                cards.add(c);
            }
        }
    }

    public Card get(int i) {
        // synchronize so that the underlying ArrayList is not accessed
        // inconsistently
        synchronized(this.cards) {
            return cards.get(i);
        }
    }
    /**
     * adds one of each card, increasing the size of the deck by 52. Cards are added
     * spades first (King to Ace), then similarly with hearts, diamonds and clubs.
     *
     * @return the deck
     */
    public CardDeck add52() {
        // add the cards
        for (char s : "SHDC".toCharArray()) {
            for (char r : "KQJT98765432A".toCharArray()) {
                this.add(Card.fromString("" + r + s));
            }
        }

        // return the deck
        return this;
    }

    /**
     *
     */
    public CardDeck shuffle() {
        // synchronize so that we don't have someone trying to modify the
        // deck as we're modifying it
        synchronized (this.cards) {
            // go through a loop that randomly rearranges the cards
            for (int i = cards.size(); i > 1; i--) {
                int spot = (int) (i * Math.random());
                Card temp = cards.get(spot);
                cards.set(spot, cards.get(i - 1));
                cards.set(i - 1, temp);
            }
        }

        // return the deck
        return this;
    }

    public boolean containsCard(Card c) {
        return cards.contains(c);
    }

    /**
     * Moves the top card the current deck to the top of another; does nothing if
     * the first deck is empty
     *
     * @param targetDeck the deck to which the card should be moved
     */
    public void moveTopCardTo(CardDeck targetDeck) {

        // will hold the card
        Card c = null;

        // size of the first deck
        int size;

        // indivisibly check the deck for empty, and remove the card, to
        // avoid a race condition
        synchronized (this.cards) {
            size = this.size();
            if (size > 0) {
                c = cards.remove(cards.size() - 1);
            }
        }

        // if the original size was non-zero, add the card to the top of the
        // target deck
        if (size > 0) {
            targetDeck.add(c);
        }
    }

    /**
     * move all cards in the current deck to a another deck by repeated moving
     * a single card from top to top
     *
     * @param target the deck that will get the cards
     */
    public void moveAllCardsTo(CardDeck target) {
        // if the source and target are the same, ignore
        if (this == target) {
            return;
        }

        // keep moving cards until the current deck is empty
        while (size() > 0) {
            moveTopCardTo(target);
        }
    }

    /**
     * add a card to the top of a deck
     *
     * @param c the card to add
     */
    public void add(Card c) {
        // synchronize so that the underlying ArrayList is not accessed
        // inconsistently
        synchronized (this.cards) {
            cards.add(c);
        }
    }

    //SARAH ADDED THIS!!!!!!!!!!!!!!!!!!
    //
    //
    //
    //BC I NEED IT!!
    public void remove(Card c) {
        // synchronize so that the underlying ArrayList is not accessed
        // inconsistently
        synchronized (this.cards) {
            cards.remove(c);
        }
    }

    /**
     * @return the number of cards in the deck
     */
    public int size() {
        return cards.size();
    }

    /**
     * replace each element in the deck with a null card; does not change
     * the size of the deck, but rather causes the deck to yield null for
     * when accessed with a valid index. A null card might represent that
     * there is a card in that position, but that it is face-down.
     */
    public void nullifyDeck() {
        // synchronize so that we don't get any race conditions (e.g., with
        // shuffle()
        synchronized (this.cards) {
            // null out each card
            for (int i = 0; i < cards.size(); i++) {
                cards.set(i, null);
            }
        }
    }

    /**
     * remove the top card from the deck
     *
     * @return the top card in the deck, which is also removed,
     * or null if the deck was empty
     */
    public Card removeTopCard() {
        synchronized (this.cards) {
            if (cards.isEmpty()) return null;
            return cards.remove(cards.size() - 1);
        }
    }

    /**
     * @return the top card in the deck, without removing it; null
     * if the deck was empty
     */
    public Card peekAtTopCard() {
        synchronized (this.cards) {
            if (cards.isEmpty()) return null;
            return cards.get(cards.size() - 1);
        }
    }

    public Card cardAt(int n) {
        return cards.get(n);
    }

    public Card peekAtPlayerCard() {
        //System.out.println("in peekCard ");
        synchronized (this.cards) {
            //System.out.println("in synchronized card ");
            if (cards.isEmpty()) {
                //System.out.println("deck is empty");
                return null;
            }

            else {
                //System.out.println("values in card array:  " + cards.get(currCard));
                currCard = 0;
                for (int i = 0; i <= cards.size(); i++) {
                    //System.out.println("card size:   " + cards.size());
                    //System.out.println("curr card value:  " + cards.get(currCard));
                    if (i<0) currCard=0;
                    else if(i==0||i==1) currCard=0;
                    else {
                        Card curr = cards.get(0);
                        currCard++;
                        return curr;
                    }
                }
            }
        }
        return cards.get(0);
    }

    /**
     * creates a printable version of the object, a list
     * of two-character names for each card in the deck
     * (starting at the bottom of the deck), surrounded by
     * brackets
     *
     * @return a printable version of the deck
     */
    @Override
    public String toString() {
        // the eventual return value
        String rtnVal = "";

        // synchronize to avoid iterating while the
        // deck is being modified
        synchronized (this.cards) {
            // loop through, printing the short name of each
            // card (using '--' for null)
            for (Card c : cards) {
                if (c == null) {
                    rtnVal += " --";
                } else {
                    rtnVal += " " + c.shortName();
                }
            }
        }

        // surround by brackets and retuirn
        rtnVal = "[" + rtnVal + " ]";
        return rtnVal;
    }

    /**
     * //     * sorts a given set of cards by suit and value
     * //     * @param cards
     * //     * @return
     * //
     */
    public void sortCards(CardDeck hand) {
        // sorts cards based on suit
        //TODO:
        // needs to be array, NOT ArrayList to have an actual order

        ArrayList<Card> sortedCards= new ArrayList<Card>();
        Card[] sortedCardsArray;

        if(hand.cards != null) {
            for (int i = 0; i < 4; i++) {
                for (Card c: hand.cards) {
                    if(c != null) {
                        if (i==0 && c.getSuit().equals(Suit.Diamond)) {
                            sortedCards.add(c);
                        }
                        if (i==1 && c.getSuit().equals(Suit.Spade)) {
                            sortedCards.add(c);
                        }
                        if (i==2 && c.getSuit().equals(Suit.Club)) {
                            sortedCards.add(c);
                        }
                        if (i==3 && c.getSuit().equals(Suit.Heart)) {
                            sortedCards.add(c);
                        }
                    }
                }
            }
        }
        sortedCardsArray = sortedCards.toArray(new Card[hand.size()]);
        hand.cards = sortedCards;

    }

}

//public class CardDeck {
//
//
//    HeartsCard[] deck = new HeartsCard[52];
//    public String[] suitValues = {"Clubs", "Spades", "Diamonds", "Hearts"};
//
//    public CardDeck(){
//        //initialize the deck so the cards are "in order"
//        int i, j;
//        int index = 0;
//        for (i = 0; i < 4; i++){
//            for(j = 2; j < 13; j++){
//                deck[index] = new HeartsCard(j, i);
//                index++;
//            }
//        }
//        shuffle();
//    }
//
//    /**
//     * Shuffles the deck
//     */
//    public void shuffle(){
//        int i, ranNum;
//        ArrayList<HeartsCard> tempList = new ArrayList<HeartsCard>(Arrays.asList(deck));
//        HeartsCard[] tempDeck = new HeartsCard[52];
//
//        for (i = 0; i < 52; i++){
//            ranNum =(int)(Math.random()*(tempList.size()-1));
//            tempDeck[i] = tempList.get(ranNum);
//            tempList.remove(ranNum);
//        }
//        deck = tempDeck;
//    }
//
//    /**
//     *
//     * @return HeartsCard[4][13]
//     * The four players then their hands of 13 cards
//     */
//    public HeartsCard[][] dealHand() {
//        shuffle();
//        HeartsCard[][] hand = new HeartsCard[4][13];
//
//        int counter = 0;
//        int i, j;
//
//        for (i = 0; i < 4; i++) {
//            for (j = 0; j < hand[0].length; j++) {
//                hand[i][j] = deck[counter];
//                counter++;
//            }
//            sortCards(hand[i]);
//        }
//        //takes 13 cards from top of deck, removes them from deck and adds them to players hand
//
//
//        return hand;
//    }
//
//    /**
//     * sorts a given set of cards by suit and value
//     * @param cards
//     * @return
//     */
//    public HeartsCard[] sortCards(HeartsCard[] cards){
////        // sorts cards based on suit and
////        int i, j;
////        int indexCounter = 0;
////        int count = 0;
////        HeartsCard[] rtrnHand = new HeartsCard[cards.length];
////        ArrayList<HeartsCard> cardsToSort = new ArrayList<>();
////
////        if(cards != null) {
////            for (i = 0; i < 4; i++) {
////                cardsToSort = new ArrayList<>();
////                for (j = 0; j < cards.length - 1; j++) {
////                    if(cards[i] != null) {
////                        if (cards[j].suitValueIndex == i) {
////                            cardsToSort.add(cards[j]);
////                            count++;
////                        }
////                    }
////                }
////                insertionSort(cardsToSort);
////                for(HeartsCard C : cardsToSort){
////                    rtrnHand[indexCounter] = C;
////                    indexCounter++;
////                }
////                if(indexCounter >= cards.length){
////                    break;
////                }
////            }
////        }
////        return rtrnHand;
//        return null;
//    }
//
//    /**
//     * Uses the insertion sort algorithm to sort cards based on face value
//     * @param cards
//     * @return
//     */
//    public ArrayList<HeartsCard> insertionSort(ArrayList<HeartsCard> cards){
//
//        int i, j;
//        HeartsCard tempCard;
//
//        for(i = 1; i < cards.size(); i++){
//            tempCard = cards.get(i);
//            j = i - 1;
//            while ((j >= 0) && (cards.get(j).faceValue > tempCard.faceValue)){
//                cards.set(j+1, cards.get(j));
//                j = j-1;
//            }
//            cards.set(j+1, tempCard);
//        }
//
//        return cards;
//    }
//
//    /**
//     * Uses a quick sort algorithm to sort cards
//     * based on face value, not suit.
//     * @param cards
//     * @return
//     */
//    public ArrayList<HeartsCard> quickSort(ArrayList<HeartsCard> cards){
//
////        // Declare Variables
////        ArrayList<HeartsCard> rtnCards = new ArrayList<>();
////        ArrayList<HeartsCard> greater = new ArrayList<>();
////        ArrayList<HeartsCard> less = new ArrayList<>();
////        HeartsCard pivotCard;
////        int pIndex;
////
////        if(cards != null){
////            if(cards.size() == 1){
////                return cards;
////            }
////            else{
////
////                pIndex = cards.size()/2;
////                pivotCard = cards.get(pIndex);
////                for(HeartsCard c : cards){
////                    if(!(c.equals(pivotCard))){
////                        if(c.faceValue > pivotCard.faceValue){
////                            greater.add(c);
////                        }
////                        else{
////                            less.add(c);
////                        }
////                    }
////                }
////
////                rtnCards.addAll(quickSort(greater));
////                rtnCards.add(pivotCard);
////                rtnCards.addAll(quickSort(less));
////            }
////        }
////        return rtnCards;
//        return null;
//    }
//}
