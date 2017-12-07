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
    private final static int WIN_TARGET = 100;

    /**
     * Constructor for the HeartsLocalGame.
     */
    public HeartsLocalGame() {
        super();
        // create the state for the beginning of the game
        currentGame = new HeartsGameState();
        currentGame.heartsPlayers = this.players;
           //currentGame.setPlayers(players);


    }

    /**
     * @param card
     * @return true if right suit
     */
    public Boolean validSuit(Card card, int playerIndex) {
        //check if player has a card of the same suit as baseSuit
        boolean hasSuit=false;
        for(Card c: currentGame.piles[playerIndex].cards){
            if(card.getSuit().equals(currentGame.baseSuit)){
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
     * @param card
     * @return
     */
    public boolean cardInCurrentPlayerHand(Card card) {
        return currentGame.piles[currentGame.getCurrentPlayerIndex()].containsCard(card);}


    /**
     * validCard method checks if the card played by currentPlayer is legal
     */
    public boolean validCard(Card card, int index) {
        boolean valid = false;

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
            currentGame.cardsOnTable[index] = card;}

        //check if it's the end of a trick
        int count =0;
        for(int i =0; i<currentGame.cardsOnTable.length;i++){
            if(currentGame.cardsOnTable[i] ==null){
                count++;
            }
        }

        //if it's the end of a trick
        if(count==0){
            //should set currentPlayer to the collector of cards of the trick
            updateScore();
            clearTable();
        }
        //rotate turns in clockwise order
        else{
            currentGame.NextTurn();
        }
        //if it's a humanPlayer, update the GUI by changing the doubleTap and singlTap variables.
        GamePlayer p = currentGame.heartsPlayers[index];
        if(p instanceof HeartsHumanPlayer){
            HeartsHumanPlayer hp = (HeartsHumanPlayer) p;
            hp.doubleTap = true;
            hp.singleTap = false;
            // TODO hp.cardLocationBool[n] = true;
            currentGame.heartsPlayers[index] = hp;
        }
        //TODO might have to do something similar for AI players

        //it's a valid card so return true
        return true;
    }

    /**
     * @param player
     * @return
     */
    public boolean playersTurn(GamePlayer player) {
        if (currentGame.CurrentPlayer.equals(player)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * The winTrick method determines which player won the round
     * and sets their isWinner boolean to true.
     */
    public int winTrick() {
        //find suit of first card played
        int winnerIndex=0;
        int highestFace = currentGame.cardsOnTable[currentGame.firstCardIndex].getRankIndex();


        //find highest card of suit played
        for (int i = 0; i < currentGame.cardsOnTable.length; i++) {
            if (currentGame.cardsOnTable[i].getRankIndex() > highestFace) {
                highestFace = currentGame.cardsOnTable[i].getRankIndex();
                winnerIndex=i;
                //rank = currentGame.cardsOnTable[i].getRank();
            }
        }
        //set the winner of this trick to be the starting player for the next trick
        currentGame.setCurrentPlayer(winnerIndex);

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

    public void updateScore() {
        int p = winTrick();
        int points = calculatePoints();
        currentGame.setScores(points, p);
    }

    public void threeCardPass(CardDeck[] cards) {
        int i;
        int j;
        // Pass to left
        if (currentGame.round % 4 == 0) {
            for (i = 0; i < currentGame.heartsPlayers.length; i++) {
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
            for (i = 0; i < currentGame.heartsPlayers.length; i++) {
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

    public void setNewRound() {
        currentGame.deal();
        currentGame.hasTwoOfClubs();
        clearTable();
        currentGame.round++;
    }

    public void addCardToTable(Card card, int playerIndex) {

        if (card == null) {
            return;
        }

        if (currentGame.turn == 0) {
            currentGame.baseSuit = card.getSuit();
        }

        if (!(currentGame.cardPlayedBool[playerIndex])) {
            currentGame.cardsOnTable[playerIndex] = card;
            //TODO remove that card from player's deck
            currentGame.turn++;
        }
    }

    public void clearTable() {
        for (int i = 0; i < 4; i++) {
            currentGame.cardsOnTable[i] = null;
        }
    }

    protected void sendUpdatedStateTo(GamePlayer p) {
        int thisPlayerNum = this.getPlayerIdx(p);
        HeartsGameState tempState = new HeartsGameState(currentGame);
        // need to null out some stuff in state
        p.sendInfo(tempState);
    }

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

    protected String checkIfGameOver() {
        //have they reached 100points
        int[] scores = currentGame.getScores();
        int highestScore = currentGame.Scores[0];
        int lowestScore = currentGame.Scores[0];
        String tie = "";
        int loser = 0;
        int winner = 0;

        //check if the game is over
        for (int i = 0; i < currentGame.Scores.length; i++) {
            if (currentGame.Scores[i] > highestScore) {
                highestScore = currentGame.Scores[i];
                loser = i;
            }
        }

        //find who won
        for (int i = 0; i < currentGame.Scores.length; i++) {
            if (currentGame.Scores[i] < lowestScore) {
                lowestScore = currentGame.Scores[i];
                winner = i;
            }
        }

        //check if it's a tie
        for (int i = 0; i < currentGame.Scores.length; i++) {
            if (currentGame.Scores[i] == lowestScore) {
               // tie = tie.concat("" + currentGame.names[i] + ", ");

            }
        }

        //TODO correctly display which player(s) won
        if (highestScore >= 100) {
            //TODO call method announceWinner for heart human player in array to make popup message of winner display
            //hp.announceWinner();
            return "Game over. " + tie + " is the winner!!";
        }

        return "game is still going";
    }

    protected boolean makeMove(GameAction action) {

        // check that we have hearts action; if so cast it
        if (!(action instanceof HeartsMoveAction)) {
            return false;
        }
        HeartsMoveAction h_move_action = (HeartsMoveAction) action;

        // get the index of the player making the move; return false
        int thisPlayerIdx = getPlayerIdx(h_move_action.getPlayer());

        //TODO change this method?
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
                //TODO fix this method
                //threeCardPass(cardPassAction.getCardPass());
            } else {
                return false;
            }
        }
        //TODO incorporate pass only to hard game
        //TODO make sure players have to pass before they can play a card

        else if (h_move_action.isPlayCard()) { // we have a "play card " action
            HeartsPlayCardAction playCardAction = (HeartsPlayCardAction) h_move_action;

            if (!canMove(thisPlayerIdx)) {
                // attempt to play when it's another player's turn
                return false;
            } else {
                //check valid card
                if (validCard(playCardAction.getCard(), thisPlayerIdx)) {
                    // play that valid card
                    currentGame.addCardToTable(playCardAction.getCard(), thisPlayerIdx);
                    //TODO nextTurn() doesn't work. We change currentPlayer index in local game
                    //currentGame.NextTurn();
                    //if end of trick, update scores
                    if (currentGame.turn == 3) {
                        updateScore();
                        clearTable();
                    }
                } else {
                    return false;
                }

            }
        } else { // some unexpected action
            return false;
        }

        // return true, because the move was successful if we get here
        sendUpdatedStateTo(currentGame.heartsPlayers[thisPlayerIdx]);
        return true;

    }
}