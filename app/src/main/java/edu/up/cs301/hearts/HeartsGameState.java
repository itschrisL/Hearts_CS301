package edu.up.cs301.hearts;


import android.graphics.RectF;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GamePlayer;
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

    /*
    /**
     * HeartsGameState Constructor
     * @param d
     * @param user
     */
    public GamePlayer[] heartsPlayers = new GamePlayer[4];
    public Card[] cardsOnTable=new Card[4];
    public Suit baseSuit;
    public int firstCardIndex;
    //TODO maybe take this out? See if it's taken care of in localGame
    public boolean GameOver = false;

    // variables that keep track table
    public Boolean[] cardPlayedBool = new Boolean[4];
    public int turn;
    public int round;
    public GamePlayer CurrentPlayer;
    public int CurrentPlayerIndex;

    public Suit suit;


    // Chosen difficulty
    //  - 0: EasyAIxxx
    //  - 1: HardAI
    protected int Difficulty;

    // the three piles of cards:
    //  - 0: pile for player 0
    //  - 1: pile for player 1
    //  - 2: pile for player 2
    //  - 3: pile for player 3
    public CardDeck[] piles;

    //TODO create scoring method and keep track/update trick
    public int[] Scores = new int[4];
    public int Trick = 0;


    /**
     *
     * Constructor for objects of class HeartsGameState. Initializes for the beginning of the
     * game, with a random player as the first to turn card
     */
    public HeartsGameState() {
        deal();
        CurrentPlayerIndex= hasTwoOfClubs();


        //initialize CurrentPlayerIndex
        // starting player
    }

    /**
     * Copy constructor for objects of class HeartsGameState. Makes a copy of the given state
     *
     * @param orig the state to be copied
     */
    public HeartsGameState(HeartsGameState orig) {
        // set index of player whose turn it is
        CurrentPlayerIndex = orig.CurrentPlayerIndex;
        baseSuit = orig.baseSuit;
        firstCardIndex = orig.firstCardIndex;
        GameOver = orig.GameOver;

        piles = new CardDeck[4];
        cardPlayedBool = new Boolean[4];
        cardsOnTable = new Card[4];

        // variables that keep track table
        for(int i=0; i<4;i++) {
            piles[i] = orig.piles[i];
            cardPlayedBool[i] = orig.cardPlayedBool[i];
            cardsOnTable[i] = orig.cardsOnTable[i];
        }
        suit= orig.suit;
        turn= orig.turn;
        round= orig.round;

        CurrentPlayer = orig.CurrentPlayer;
        CurrentPlayerIndex = orig.CurrentPlayerIndex;

    }

    //TODO this gives us nullPointerException when we call it in constructor of LocalGame so we commented it out. Not sure if this method needs fixing.
    public void setPlayers(GamePlayer[] players){
        for(int i =0; i<heartsPlayers.length;i++){
            heartsPlayers[i] = players[i];
        }
    }

    /**
     * Set AI difficulty
     * @param difficulty
     *  - 0 : EasyAIxxx
     *  - 1 : HardAI
     */
    public void setDifficulty(int difficulty){
        //TODO fix main menu so that it asks user to select difficulty level
        if((difficulty == 0)||(difficulty == 1)){
            this.Difficulty = difficulty;
        }
        else{
            return;
        }
    }

    /**
     * Set Current Player
     * @param index
     */
    public void setCurrentPlayer(int index){
        if((index >= 0)&&(index <= 3)){
            CurrentPlayer = heartsPlayers[index];
            CurrentPlayerIndex = index;
        }
    }

    /**
     * Set Game Over
     * @param b
     */
    public void setGameOver(boolean b){
        GameOver = b;
    }

    /**
     * Set Trick
     */
    public void setTrick(int trick){
        Trick = trick;
    }

    public int getDifficulty(){return Difficulty;}

    /**
     *
     * @return
     */
    public Suit getSuitIndex(){
        return suit;
    }

    public int[] getScores(){
        return Scores;
    }

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

    public int getPlayerIndex(GamePlayer p){
        int returnVal = 0;
        for(int i =0; i<heartsPlayers.length; i++){
            if(heartsPlayers[i].equals(p)){
                returnVal= i;
            }
        }
        return returnVal;
    }

    /**
     * Tells which player's turn it is.
     *09
     * @return the index (0 , 1, 2, 3) of the player whose turn it is.
     */
    public int getCurrentPlayerIndex() {
        return CurrentPlayerIndex;
    }

    public void clearTable(){
        for(int i=0; i<4; i++){
            cardsOnTable[i]=null;
        }

    }

    // TODO take away if already addressed in local game
    public void addCardToTable(Card card, int playerIndex){
        //TODO might have to update cardPlayedBool/cardsPlayed in localGame
//        if (!(cardPlayedBool[playerIndex])){
//            cardsPlayed[playerIndex] = card;
//            turn++;
//        }
    }

    /**
     * Changes the Current Player to the next player.
     */
    public void NextTurn(){
        //current player now is being updated in local game
        if(CurrentPlayerIndex == 3){
            CurrentPlayer = heartsPlayers[0];
            CurrentPlayerIndex = 0;
        }
        else {
            CurrentPlayerIndex++;
            CurrentPlayer = heartsPlayers[CurrentPlayerIndex];
        }

        //check if it's time to update the base suit for a new trick
//        int count = 0;
//        int index =0;
//        for(int i=0; i<cardsOnTable.length; i++){
//            //check number of cards on table
//            if(cardsOnTable[i]!=null){
//                count++;
//                index = i;
//            }
//        }
//        //if there is only one card on the table, make that the suit for the trick
//        if(count==1){
//            baseSuit = cardsOnTable[index].getSuit();
//            firstCardIndex = index;
//            return;
//        }
    }

    public void deal(){
        CardDeck defaultDeck=new CardDeck();
        defaultDeck.add52();
        defaultDeck.shuffle();

        // initialize the decks as follows:
        piles = new CardDeck[4];
        piles[0] = new CardDeck(); // create empty deck
        piles[1] = new CardDeck(); // create empty deck
        piles[2] = new CardDeck(); // create empty deck
        piles[3] = new CardDeck(); // create empty deck

        int counter = 0;
        for(int i=0; i<defaultDeck.cards.size();i++){
            piles[counter].add(defaultDeck.get(i));
            if((i+1)%13==0&&counter!=3){counter++;}
        }

        CurrentPlayerIndex = hasTwoOfClubs();
    }

    /**
     * Returns the id of the player with the two of clubs
     * @return
     */
    public int hasTwoOfClubs(){
        Card twoClubs = new Card(Rank.TWO, Suit.Club);
        for(int i = 0; i<heartsPlayers.length;i++){

            if(piles[i].containsCard(twoClubs)) {
                return i;
            }

        }
        return -1;
    }

    /**
     * scales a rectangle, moving all edges with respect to its center
     *
     * @param rect
     *        the original rectangle
     * @param factor
     *        the scaling factor
     * @return
     *        the scaled rectangle
     */
    private static RectF scaledBy(RectF rect, float factor) {
        // compute the edge locations of the original rectangle, but with
        // the middle of the rectangle moved to the origin
        float midX = (rect.left+rect.right)/2;
        float midY = (rect.top+rect.bottom)/2;
        float left = rect.left-midX;
        float right = rect.right-midX;
        float top = rect.top-midY;
        float bottom = rect.bottom-midY;

        // scale each side; move back so that center is in original location
        left = left*factor + midX;
        right = right*factor + midX;
        top = top*factor + midY;
        bottom = bottom*factor + midY;

        // create/return the new rectangle
        return new RectF(left, top, right, bottom);
    }


//    /**
//     * Finds Highest Card and returns it
//     * @return
//     */
//    public Card getHighestCard() {
//        Card rtrnCard = cardsPlayed[0];
//        int i;
//        int tempInt;
//        int currentHighestRank = cardsPlayed[0].getIntValue();
//        for(i = 0; i < cardsPlayed.length; i++){
//            if(cardsPlayed[i].getSuit().equals(suit)){
//                tempInt = cardsPlayed[i].getIntValue();
//                if((currentHighestRank < tempInt)&&(!(cardsPlayed[i].equals(rtrnCard)))){
//                    rtrnCard = cardsPlayed[i];
//                    currentHighestRank = cardsPlayed[i].getIntValue();
//                }
//            }
//        }
//        return rtrnCard;
//    }
}
