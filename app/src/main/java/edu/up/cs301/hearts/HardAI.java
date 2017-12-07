package edu.up.cs301.hearts;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * Updated by S. Seydlitz on 11/19/17
 */

public class HardAI extends GameComputerPlayer{

    //variables
    protected HeartsGameState state;
    CardDeck currentHand;
    Suit baseSuit;
    Card chosenCard;
    int score = 0;
    String name;
    CardDeck tableDeck = new CardDeck();
    Card[] tableArray;
    int ind;
    CardDeck cardsToPass;
    Rank[] ranks;
    Suit[] suits;

    public HardAI(String playerName) {
        super(playerName);
        ranks = new Rank[13];
        suits = new Suit[4];
        ranks[0] = Rank.TWO;
        ranks[1] = Rank.THREE;
        ranks[2] = Rank.FOUR;
        ranks[3] = Rank.FIVE;
        ranks[4] = Rank.SIX;
        ranks[5] = Rank.SEVEN;
        ranks[6] = Rank.EIGHT;
        ranks[7] = Rank.NINE;
        ranks[8] = Rank.TEN;
        ranks[9] = Rank.JACK;
        ranks[10] = Rank.QUEEN;
        ranks[11] = Rank.KING;
        ranks[12] = Rank.ACE;
        suits[0] = Suit.Club;
        suits[1] = Suit.Spade;
        suits[2] = Suit.Diamond;
        suits[3] = Suit.Heart;
    }

    public void receiveInfo(GameInfo info) {
        if (info instanceof IllegalMoveInfo) {
            playCard(currentHand);
        }
        else if(info instanceof NotYourTurnInfo){
            //TODO might have to add something
        }
        else if (!(info instanceof HeartsGameState)) {
            // otherwise, if it's not a game-state message, ignore
            return;
        }
        else {
            // it's a game-state object: update the state. Since we have an animation
            // going, there is no need to explicitly display anything. That will happen
            // at the next animation-tick, which should occur within 1/20 of a second
            this.state = (HeartsGameState)info;
            Log.i("computer player", "receiving");
        }

        // ignore if we have not yet received the game state
        if (state == null) return;

        ind = super.playerNum;

        if(ind == state.CurrentPlayerIndex){
            currentHand = state.piles[ind];
            playCard(currentHand);
        }

        tableArray = state.cardsOnTable;
    }

    public void strategy() {
        for (int i = 0; i < tableArray.length; i++) {
            tableDeck.cards.add(tableArray[i]);
        }

        Card queenOfSpades = new Card(ranks['Q'], suits['S']);
        Card kingOfSpades = new Card(ranks['K'], suits['S']);
        Card aceOfSpades = new Card(ranks['A'], suits['S']);
        //pick a card at random from EasyAI's card deck
        Random rand = new Random();
        int x = rand.nextInt(13);
        int y = rand.nextInt(4);
        //see what hearts cards are on the table
        int[] nums = new int[4];
        int j = 0;
        int k;
        int clubNum = 0;
        int spadeNum = 0;
        int diamondNum = 0;
        int heartNum = 0;
        CardDeck tempClub = new CardDeck();
        CardDeck tempSpade = new CardDeck();
        CardDeck tempDiamond = new CardDeck();
        CardDeck tempHeart = new CardDeck();

        //here we keep track of how many cards we have of each suit
        if (checkIfSuitInHand(Suit.Club) == true) {
            for (int a = 0; a < 13; a++) {
                if (checkIfCardinHand(currentHand, new Card(ranks[a], suits['C'])) == true) {
                    tempClub.add(new Card(ranks[a], suits['C']));
                }
            }
        }
        if (checkIfSuitInHand(Suit.Spade) == true) {
            for (int b = 0; b < 13; b++) {
                if (checkIfCardinHand(currentHand, new Card(ranks[b], suits['S'])) == true) {
                    tempSpade.add(new Card(ranks[b], suits['S']));
                }
            }
            if (checkIfSuitInHand(Suit.Diamond) == true) {
                for (int c = 0; c < 13; c++) {
                    if (checkIfCardinHand(currentHand, new Card(ranks[c], suits['D'])) == true) {
                        tempDiamond.add(new Card(ranks[c], suits['D']));
                    }
                }
            }
            if (checkIfSuitInHand(Suit.Heart) == true) {
                for (int d = 0; d < 13; d++) {
                    if (checkIfCardinHand(currentHand, new Card(ranks[d], suits['H'])) == true) {
                        tempHeart.add(new Card(ranks[d], suits['H']));
                    }
                }
            }
            for (Card a : tempClub.cards) {
                if (a.equals(a)) {
                    clubNum++;
                }
            }
            for (Card b : tempSpade.cards) {
                if (b.equals(b)) {
                    spadeNum++;
                }
            }
            for (Card c : tempDiamond.cards) {
                if (c.equals(c)) {
                    diamondNum++;
                }
            }
            for (Card d : tempHeart.cards) {
                if (d.equals(d)) {
                    heartNum++;
                }
            }
            //now we have the number of cards in each face that are in our hand

            for (int i = 0; i < 13; i++) {
                Card temp = new Card(ranks[i], suits['H']);
                if (checkIfCardOnTable(temp) == true) {
                    nums[j] = i;
                    j++;
                }
            }//this gets the ranks of hearts cards on the table

            //this code executes if no other cards have been played yet
            if (baseSuit == null) {
                if(clubNum>0&&clubNum<4){
                    clubNum--;
                    for (Card a : tempClub.cards) {
                        if (a != null) {
                            chosenCard = a;
                        }
                    }
                    tempClub.remove(chosenCard);
                }//get rid of tiny clubs
                else if(diamondNum>0&&diamondNum<4){
                    diamondNum--;
                    for (Card a : tempDiamond.cards) {
                        if (a != null) {
                            chosenCard = a;
                        }
                    }
                    tempDiamond.remove(chosenCard);
                }//get rid of tiny diamonds
                else if(spadeNum>0&&spadeNum<4){
                    spadeNum--;
                    for (Card a : tempSpade.cards) {
                        if (a != null) {
                            chosenCard = a;
                        }
                    }
                    if(chosenCard==queenOfSpades){
                        tempSpade.remove(queenOfSpades);
                        for (Card a : tempSpade.cards) {
                            if (a != null) {
                                chosenCard = a;
                            }
                        }
                    }
                    tempSpade.remove(chosenCard);
                }//if chosenCard is not queenOfSpades, return the tiny spade
                else{
                    if(clubNum>0){
                        clubNum--;
                        for (Card a : tempClub.cards) {
                            if (a != null) {
                                chosenCard = a;
                            }
                        }
                        tempClub.remove(chosenCard);
                    }//return any club
                    else if(diamondNum>0){
                        diamondNum--;
                        for (Card a : tempDiamond.cards) {
                            if (a != null) {
                                chosenCard = a;
                            }
                        }
                        tempDiamond.remove(chosenCard);
                    }//return any diamond
                    else if(clubNum==0&&diamondNum==0&&spadeNum!=0){
                        spadeNum--;
                        for (Card a : tempSpade.cards) {
                            if (a != null) {
                                chosenCard = a;
                            }
                        }
                        if(chosenCard==queenOfSpades){
                            tempSpade.remove(queenOfSpades);
                            for (Card a : tempSpade.cards) {
                                if (a != null) {
                                    chosenCard = a;
                                }
                            }
                        }
                        if(chosenCard!=null) {
                            tempSpade.remove(chosenCard);
                        }
                        else if(chosenCard == null && checkIfCardinHand(currentHand, queenOfSpades)){
                            chosenCard = queenOfSpades;
                        }
                    }//return a spade, queen of spades last
                    else if(heartNum!=0){
                        heartNum--;
                        for (Card a : tempHeart.cards) {
                            if (a != null) {
                                chosenCard = a;
                            }
                        }
                        tempHeart.remove(chosenCard);
                    }//return any heart
                }
            }//Code if we are first

            if (baseSuit == Suit.Spade) {
                if (currentHand.containsCard(queenOfSpades)) {
                    if (tableDeck.containsCard(kingOfSpades) == true || tableDeck.containsCard(aceOfSpades) == true) {
                        chosenCard = queenOfSpades;
                    }
                }
            }//this tries to get rid of the queen of spades ASAP

            //check and see if the AI player has a card of the suit that was originally played
            else if (checkIfSuitInHand(baseSuit) == true && chosenCard == null) {
                if (baseSuit == Suit.Club || baseSuit == Suit.Spade || baseSuit == Suit.Diamond) {
                    chosenCard = new Card(ranks[x], baseSuit);
                }//if the basesuit is spades, clubs, or diamonds, play a random card of the baseSuit
                else if (baseSuit == Suit.Heart) {
                    //we will be looking at the highest hearts card on the table, nums[j-1], and trying to play a card lower than that (unless j=0, in which case j is 8.
                    //if we get through the loop and don't have a card like that, then we start going up instead of down
                    if (j == 0) {
                        j = 8;
                    }
                    for (k = j--; k >= 0; k--) {
                        Card temp2 = new Card(ranks[nums[k]], suits['H']);
                        if (currentHand.containsCard(temp2)) {
                            chosenCard = temp2;
                        }
                    }
                    for (k = j--; k <= 12; k++) {
                        Card temp3 = new Card(ranks[nums[k]], suits['H']);
                        if (currentHand.containsCard(temp3)) {
                            chosenCard = temp3;
                        }
                    }
                }//if the basesuit is hearts, play the next lowest card than the highest one on the table, or the next highest card than the highest one on the table
            }//Code if we do have a card of the basesuit

            else if (chosenCard == null){
                if (baseSuit == Suit.Heart) {
                    if (currentHand.containsCard(queenOfSpades)) {
                        chosenCard = queenOfSpades;
                    } else {
                        chosenCard = new Card(ranks[x], suits[y]);
                    }
                }//if we don't have hearts, play queen of spades or any random card
                else if (baseSuit == Suit.Club || baseSuit == Suit.Spade || baseSuit == Suit.Diamond) {
                    if (currentHand.containsCard(queenOfSpades)) {
                        chosenCard = queenOfSpades;
                    } else if (checkIfSuitInHand(Suit.Heart)) {
                        for (int i = 12; i >= 0; i--) {
                            chosenCard = new Card(ranks[i], suits['H']);
                        }
                    } else {
                        chosenCard = new Card(ranks[x], suits[y]);
                    }
                }//if we don't have the basesuit (and it isn't hearts), play queen of spades or highest hearts card or random card
            }//Code if we don't have a card of the basesuit
        }
    }

    public void playCard(CardDeck aisHand) {
        while (chosenCard == null ||  currentHand.containsCard(chosenCard) == false) {
            strategy();
        }
        sleep(1000);
        game.sendAction(new HeartsPlayCardAction(this, chosenCard));
        //TODO want to make sure it's a legal move, so this remove method belongs in localGame
        aisHand.remove(chosenCard);
    }

    public void CardPass(){
        
        //TODO make the strategy better?
        //pick a card at random from EasyAI's card deck
        //Remember, there are three different AI's, so there are three different decks to keep track of
        Random rand = new Random();
        //x is rank, y is suit
        int x = rand.nextInt(13);
        int y = rand.nextInt(4);

        Card one;
        Card two;
        Card three;

        one = new Card(ranks[getX()],suits[getY()]);
        while(one == null || currentHand.containsCard(one)==false) {
            one = new Card(ranks[getX()],suits[getY()]);
        }
        cardsToPass.add(one);
        currentHand.remove(one);

        two = new Card(ranks[getX()],suits[getY()]);
        while(two == null || currentHand.containsCard(two)==false) {
            two = new Card(ranks[getX()],suits[getY()]);
        }
        cardsToPass.add(two);
        currentHand.remove(two);

        three = new Card(ranks[getX()],suits[getY()]);
        while(three == null || currentHand.containsCard(three)==false) {
            three = new Card(ranks[getX()],suits[getY()]);
        }
        cardsToPass.add(three);
        currentHand.remove(three);

    }

    public Card giveAway(){
        Card present;

        if(checkIfSuitInHand(Suit.Spade)==true){
            //return a spade
            present = new Card(ranks[getX()],Suit.Spade);
        }
        else if(checkIfRankInHand(Rank.ACE)==true){
            present = new Card(Rank.ACE, suits[getY()]);
        }
        else if(checkIfRankInHand(Rank.KING)==true){
            present = new Card(Rank.KING, suits[getY()]);
        }
        else if(checkIfRankInHand(Rank.QUEEN)==true){
            present = new Card(Rank.QUEEN, suits[getY()]);
        }
        else{
            present = new Card(ranks[getX()],suits[getY()]);
        }

        return present;
    }

    public int getX(){
        Random rand = new Random();
        int x = rand.nextInt(13);
        return x;
    }

    public int getY(){
        Random rand = new Random();
        int y = rand.nextInt(4);
        return y;
    }

    public boolean checkIfCardOnTable(Card card){
        for(Card b: tableDeck.cards){
            if(b.equals(card)){
                return true;
            }
        }
        return false;
    }

    //TODO convert to containsCard
    public boolean checkIfCardinHand(CardDeck aisHand, Card card){
        boolean value = false;
        if(card == null){
            value = false;
        }
        for(Card c: aisHand.cards){
            if(c.equals(card)){
                value= true;
            }
        }
        return value;
    }

    public boolean checkIfSuitInHand(Suit suit){
        for(Card c: currentHand.cards){
            if(c.getSuit().equals(suit)){
                return true;
            }
        }
        return false;
    }

    public boolean checkIfRankInHand(Rank rank){
        for(Card c: currentHand.cards){
            if(c.getRank().equals(rank)){
                return true;
            }
        }
        return false;
    }
}
