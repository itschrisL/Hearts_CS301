package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;


/**
 * This class checks legality of players moves and finds the winner of the round.
 * @author Emma Soriano, Chris Lytle
 * @version 10/19/17
 */

public class HeartsLocalGame extends LocalGame {
    HeartsGameState currentGame;
    public boolean startOfTrick;
    private final static int WIN_TARGET = 100;

    /**
     * Constructor for the HeartsLocalGame.
     */
    public HeartsLocalGame() {
        super();
        // create the state for the beginning of the game
        currentGame = new HeartsGameState();
    }

    /**
     * validSuit takes in a card and playerIndex to determine if the
     * suit of the card is valid for this trick.
     *
     * @param card
     * @return true if right suit
     */
    public Boolean validSuit(Card card, int playerIndex) {
        if(startOfTrick){ //first card played for the trick
            currentGame.baseSuit=card.getSuit(); // set baseSuit for this trick
            return true;
        }
        //check if player has a card of the same suit as baseSuit
        boolean hasSuit=false;
        for(Card c: currentGame.piles[playerIndex].cards){
            if(c.getSuit().equals(currentGame.baseSuit)){
                hasSuit = true;
            }
        }
        //if the card is the same suit as the baseSuit, return true.
        if (card.getSuit().equals(currentGame.baseSuit)) {
            return true;
        }
        //if they don't have a card of the same suit as baseSuit, return true.
        else if(!hasSuit){
            return true;
        }
        //illegal card, return false.
        else{
            return false;
        }
    }


    /**
     * validCard method checks if the card played by currentPlayer is legal
     */
    public boolean validCard(Card card, int index) {
        //player must play two of clubs if they have it, and it must be the first card played each round
        Card twoClubs = new Card(Rank.TWO, Suit.Club);
        for(int i=0; i<currentGame.piles.length;i++){
            if(currentGame.piles[i].containsCard(twoClubs) && !card.equals(twoClubs)){
                return false;
            }
        }

        // check if valid suit
        if (!(validSuit(card, index))) {
            return false;
        }
        // check if card is in hand
        if (!(cardInCurrentPlayerHand(card))) {
            return false;
        }
        //Add valid card to the table if no other card has been played already by that player for this trick
        if(currentGame.cardsOnTable[index] == null){
            currentGame.cardsOnTable[index] = card;
            currentGame.piles[index].remove(card);
            currentGame.turn++;
            }

        //check if it's the end of a trick
        int emptySpacesCount =0;
        for(int i =0; i<currentGame.cardsOnTable.length;i++){
            if(currentGame.cardsOnTable[i] ==null){
                emptySpacesCount++;
            }
        }

        //if it's the end of a trick
        if(emptySpacesCount==0){
            //send to humanPlayer first to update GUI
            sendUpdatedStateTo(players[0]);
            //should set currentPlayer to the collector of cards of the trick
            updateScore();
            clearTable();
            //now it's the beginning of a new trick so let baseCard be set in ValidCard()
            //with respect to the first card played in that trick;
            startOfTrick = true;
        }
        //rotate turns in clockwise order
        else{
            currentGame.NextTurn();
        }


        //if baseSuit is already set for that trick
        if(emptySpacesCount==3){
            //baseSuit is already defined
            startOfTrick=false;
        }



        //if it's a humanPlayer, update the GUI by changing the doubleTap and singlTap variables.
        //GamePlayer p = players[currentGame.CurrentPlayerIndex];
/*
        if(p instanceof HeartsHumanPlayer){
            HeartsHumanPlayer hp = (HeartsHumanPlayer) p;
            hp.doubleTap = true;
            hp.singleTap = false;
            players[index] = hp;
        }*/
        //TODO might have to do something similar for AI players

        //it's a valid card so return true
        return true;
    }

    /**
     * checks if the card is in the current player's hand
     * @param card
     * @return
     */
    public boolean cardInCurrentPlayerHand(Card card) {
        return currentGame.piles[currentGame.getCurrentPlayerIndex()].containsCard(card);}

    /**
     * The winTrick method determines which player won the trick
     *
     */
    public int winTrick() {
        //find suit of first card played
        int winnerIndex=0;
        int highestFace = currentGame.cardsOnTable[0].getRankIndex(currentGame.cardsOnTable[0].getRank());


        //find highest card of suit played
        for (int i = 0; i < currentGame.cardsOnTable.length; i++) {
            if (currentGame.cardsOnTable[i].getRankIndex(currentGame.cardsOnTable[i].getRank()) > highestFace) {
                highestFace = currentGame.cardsOnTable[i].getRankIndex(currentGame.cardsOnTable[i].getRank());
                winnerIndex=i;
            }
        }
        startOfTrick = true;
        //set the winner of this trick to be the starting player for the next trick
        currentGame.setCurrentPlayerIndex(winnerIndex);

        //do players still have cards to play?
        boolean newRound = true;
        for (int i = 0; i < currentGame.piles.length; i++) {
            if (!currentGame.piles[i].equals(null)) {
                newRound = false;
            }
        }
        //if everyone has no cards left, shuffle and deal cards again
        if (newRound) {
            currentGame.deal();
        }

        return winnerIndex;

    }

    /**
     * The calculatePoints method calculates the number
     * of points that should be added to the player who
     * collects the cards after a trick.
     *
     */
    public int calculatePoints() {
        int points = 0;
        for (Card c : currentGame.cardsOnTable) {
            //add one point each time a heart is on the table
            if (c.getSuit().equals(Suit.Heart)) {
                points++;
            }
            //add 13 points if the queen of spades is on the table
            else if (c.getSuit().equals(Suit.Spade) && c.getRank().equals(Rank.QUEEN)) {
                points += 13;
            }
        }
        return points;
    }

    /**
     * The updateScore method sets the score of player who
     * won the cards from a trick.
     *
     */
    public void updateScore() {
        int p = winTrick();
        int points = calculatePoints();
        currentGame.setScores(points, p);
    }

    /**
     * The threeCardPass method passes a CardDeck containing
     * three cards from one player to another. The direction
     * of passing cards is determined by the round.
     *
     */
    public void threeCardPass(CardDeck[] cards) {
        int i;
        int j;
        // Pass to left
        if (currentGame.round % 4 == 0) {
            for (i = 0; i < players.length; i++) {
                // removes from hand
                for (j = 0; j < cards[i].cards.size(); j++) {
                    currentGame.piles[i].remove(cards[i].get(j));
                }
                // adds to left player
                for (j = 0; j < cards[i].cards.size(); j++) {
                    if (i == 3) {
                        currentGame.piles[0].add(cards[i].get(j));
                    } else {
                        currentGame.piles[i + 1].add(cards[i].get(j));
                    }

                }
            }
        } else if (currentGame.round % 4 == 1) { // Pass Across
            for (i = 0; i < players.length; i++) {
                // removes from hand
                for (j = 0; j < cards[i].cards.size(); j++) {
                    currentGame.piles[i].remove(cards[i].get(j));
                }
                // adds to left player
                for (j = 0; j < cards[i].cards.size(); j++) {
                    if (i == 1 || i == 0) {
                        currentGame.piles[i + 2].add(cards[i].get(j));
                    } else if (i == 2) {
                        currentGame.piles[0].add(cards[i].get(j));
                    }

                }
            }
        } else if (currentGame.round % 4 == 2) { // Pass Right

        } else { // No Pass

        }
    }

    /**
     * The setNewRound method deals cards to all players,
     * finds the starting player, and clears the table.
     *
     */
    public void setNewRound() {
        currentGame.deal();
        currentGame.hasTwoOfClubs();
        clearTable();
        currentGame.round++;
    }

    /**
     * The addCardToTable method adds a card to the
     * cardsOnTable array, removes it from the player's
     * card pile and increments the index of which turn
     * it is.
     *
     */
    public void addCardToTable(Card card, int playerIndex) {

        if (card == null) {
            return;
        }

        if (currentGame.turn == 0) {
            currentGame.baseSuit = card.getSuit();
        }
        currentGame.cardsOnTable[playerIndex] = card;
        currentGame.piles[playerIndex].remove(card);
        currentGame.turn++;
        if (!(currentGame.cardPlayedBool[playerIndex]))
        {
            currentGame.cardsOnTable[playerIndex] = card;
            currentGame.turn++;
        }
    }

    /**
     * The clearTable method clears the cards on the table
     *
     */
    public void clearTable() {
        for (int i = 0; i < 4; i++) {
            currentGame.cardsOnTable[i] = null;
        }
    }

    /**
     * The sendUpdatedStateTo() method sends the updated version
     * of our HeartsGameState to a specific player.
     *
     */
    protected void sendUpdatedStateTo(GamePlayer p) {
        int thisPlayerNum = this.getPlayerIdx(p);
        HeartsGameState tempState = new HeartsGameState(currentGame);
        // need to null out some stuff in state
        p.sendInfo(tempState);
    }

    /**
     * The canMove method determines if a player attempting
     * to make a move is legally allowed to.
     *
     */
    protected boolean canMove(int idx) {
        boolean myTurn;
        int index = idx;

        if (index == currentGame.CurrentPlayerIndex) {
            myTurn = true;
        } else {
            myTurn = false;
        }

        return myTurn;
    }

    /**
     * The checkIfGameOver method checks if the game has ended
     * and it displays the winner(s) if it has.
     *
     */
    protected String checkIfGameOver() {
        //have they reached 100points
        int[] scores = currentGame.getScores();
        int highestScore = currentGame.Scores[0];
        int lowestScore = currentGame.Scores[0];
        String tie = "";

        //check if the game is over
        for (int i = 0; i < currentGame.Scores.length; i++) {
            if (currentGame.Scores[i] > highestScore) {
                highestScore = currentGame.Scores[i];
            }
        }

        //find who won
        for (int i = 0; i < currentGame.Scores.length; i++) {
            if (currentGame.Scores[i] < lowestScore) {
                lowestScore = currentGame.Scores[i];
            }
        }

        //check if it's a tie
        for (int i = 0; i < currentGame.Scores.length; i++) {
            if (currentGame.Scores[i] == lowestScore) {
                // tie = tie.concat("" + currentGame.names[i] + ", ");

            }
        }

        //displays which player(s) won
        if (highestScore >= WIN_TARGET) {
            return "Game over. " + tie + " is the winner!!";
        }

        return null;
    }

    /**
     * The makeMove method receives actions sent by players
     * and determines if it is a legal move or not
     *
     */
    protected boolean makeMove(GameAction action) {

        // check that we have hearts action; if so cast it
        if (!(action instanceof HeartsMoveAction)) {
            return false;
        }
        HeartsMoveAction h_move_action = (HeartsMoveAction) action;

        // get the index of the player making the move; return false
        int thisPlayerIdx = getPlayerIdx(h_move_action.getPlayer());

        if (!canMove(thisPlayerIdx)) { // illegal player
            return false;
        }

        if (h_move_action.isCardPass()) { // we have a "card pass " action
            boolean legalPass = true;
            HeartsCardPassAction cardPassAction = (HeartsCardPassAction) h_move_action;

            // check we are passing exactly 3 cards
            for (int i = 0; i < currentGame.piles.length; i++) { //checks that it is the beginning of a round
                if (currentGame.piles[i].size() < 13) {
                    legalPass = false;
                }
            }
            if (legalPass) {
                threeCardPass(cardPassAction.getCardPass());
            } else {
                return false;
            }
        }

        else if (h_move_action.isPlayCard()) { // we have a "play card " action

            HeartsPlayCardAction playCardAction = (HeartsPlayCardAction) h_move_action;

            if (!canMove(thisPlayerIdx)) {
                // attempt to play when it's another player's turn
                return false;
            } else {
                //check valid card
                if (validCard(playCardAction.getCard(), thisPlayerIdx)) {
                    currentGame.cardsOnTable[thisPlayerIdx]=playCardAction.getCard();
                    // play that valid card

                } else {
                    return false;
                }
            }
        } else { // some unexpected action
            return false;
        }

        // return true, because the move was successful if we get here
        return true;
    }
}
