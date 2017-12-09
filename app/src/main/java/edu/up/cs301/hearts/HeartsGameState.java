package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.infoMsg.GameState;


/**
 * Contains the state of a Hearts game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 *
 * @author Steven R. Vegdahl, Emma Soriano, Chris Lytle
 * @version October 2017
 */
public class HeartsGameState extends GameState {

    ///////////////////////////////////////////////////
    // ************** instance variables ************
    ///////////////////////////////////////////////////

    private static final long serialVersionUID = -8269749892027578792L;

    public Card[] cardsOnTable = new Card[4];
    public Suit baseSuit;
    public int firstCardIndex;


    // variables that keep track table
    public Boolean[] cardPlayedBool = new Boolean[4];
    public int turn;
    public int round;
    public int CurrentPlayerIndex;
    public Suit suit;

    public int[] Scores = new int[4];
    public int Trick = 0;

    // the three piles of cards:
    //  - 0: pile for player 0
    //  - 1: pile for player 1
    //  - 2: pile for player 2
    //  - 3: pile for player 3
    public CardDeck[] piles;

    /**
     *
     * Constructor for objects of class HeartsGameState. Initializes for the beginning of the
     * game, with a random player as the first to turn card
     */
    public HeartsGameState() {
        super();
        //shuffles and deals cards to each card pile
        deal();

        //set the current player to have be the one with Two of Clubs
        CurrentPlayerIndex = hasTwoOfClubs();

        //set all player's booleans to false since none have played yet
        for(int i=0; i<cardPlayedBool.length;i++){
            cardPlayedBool[i] = false;
        }
    }

    /**
     * Copy constructor for objects of class HeartsGameState. Makes a copy of the given state
     *
     * @param orig the state to be copied
     */
    public HeartsGameState(HeartsGameState orig) {//copy constructor
        CurrentPlayerIndex = orig.CurrentPlayerIndex;
        baseSuit = orig.baseSuit;
        firstCardIndex = orig.firstCardIndex;
        piles = orig.piles;
        cardPlayedBool = orig.cardPlayedBool;
        cardsOnTable = orig.cardsOnTable;
        suit= orig.suit;
        turn= orig.turn;
        round= orig.round;
        CurrentPlayerIndex = orig.CurrentPlayerIndex;
    }

    /**
     * Set Trick
     */
    public void setTrick(int trick){
        Trick = trick;
    }

    /**
     *
     * @return
     */
    public Suit getSuitIndex(){
        return suit;
    }

    /**
     * Returns scores for players
     */
    public int[] getScores(){
        return Scores;
    }

    /**
     * Set scores for the player who won points
     */
    public void setScores(int score, int idx){
        Scores[idx]= Scores[idx]+score;
    }

    /**
     *
     */
    public Card getCardOnTable(int playerIndex){
        return cardsOnTable[playerIndex];
    }

    /**
     * Gives the given deck.
     *
     * @return the deck for the given player, or the middle deck if the
     * index is 2
     */
    public CardDeck getDeck(int num) {
        if (num < 0 || num >= 4) return null;
        return piles[num];
    }

    /**
     * Set currentPlayerIndex
     */
    public void setCurrentPlayerIndex(int index){
        CurrentPlayerIndex = index;
    }

    /**
     * Tells which player's turn it is.
     *09
     * @return the index (0 , 1, 2, 3) of the player whose turn it is.
     */
    public int getCurrentPlayerIndex() {
        return CurrentPlayerIndex;
    }

    /**
     * Set baseSuit
     */
    public void setBaseSuit(Suit suit){baseSuit=suit;}

    /**
     * Returns baseSuit
     */
    public Suit getBaseSuit(){return baseSuit;}

    public void clearTable(){
        //clears the table by setting cardsOnTable equal to null
        for(int i=0; i<4; i++){
            cardsOnTable[i]=null;
        }

    }

    /**
     * Changes the Current Player to the next player.
     */
    public void NextTurn(){
        //updates currentPlayer and makes them take turns in a clockwise direction on the GUI
        if(CurrentPlayerIndex == 3){
            CurrentPlayerIndex = 0;
        }
        else {
            CurrentPlayerIndex++;
        }
    }

    /**
     * Deal cards and initializes card piles
     */
    public void deal(){
        //create a standard 52 deck of cards
        CardDeck defaultDeck=new CardDeck();
        defaultDeck.add52();
        defaultDeck.shuffle();

        // initialize the decks as follows:
        piles = new CardDeck[4];
        piles[0] = new CardDeck(); // create empty deck
        piles[1] = new CardDeck(); // create empty deck
        piles[2] = new CardDeck(); // create empty deck
        piles[3] = new CardDeck(); // create empty deck

        //deal out 13 cards to each player's corresponding pile
        int counter = 0;
        for(int i=0; i<defaultDeck.cards.size();i++){
            piles[counter].add(defaultDeck.get(i));
            if((i+1)%13==0&&counter!=3){counter++;}
        }
    }

    /*
     * Returns the index of the player with the two of clubs
     * and the baseSuit for the first trick
     * @return
     */
    public int hasTwoOfClubs(){
        Card twoClubs = new Card(Rank.TWO, Suit.Club);
        baseSuit=Suit.Club;
        for(int i = 0; i<piles.length;i++){
            if(piles[i].containsCard(twoClubs)) {
                return i;
            }

        }
        return -1;
    }


}
