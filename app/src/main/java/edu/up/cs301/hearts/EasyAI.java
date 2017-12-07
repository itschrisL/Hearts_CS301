package edu.up.cs301.hearts;

import android.app.Activity;
import android.app.Notification;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
import edu.up.cs301.slapjack.SJSlapAction;

/**
 * Updated by S. Seydlitz on 11/17/17
 */

public class EasyAI extends GameComputerPlayer {

    //variables
    protected HeartsGameState state;
    CardDeck currentHand;
    Card chosenCard;
    int ind;
    CardDeck cardsToPass;
    Rank[] ranks;
    Suit[] suits;


    public EasyAI(String playerName) {
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
    }

    public void strategy() {
        //pick a card at random from EasyAI's card deck
        //Remember, there are three different AI's, so there are three different decks to keep track of
        Random rand = new Random();
        //x is rank, y is suit
        int x = rand.nextInt(13);
        int y = rand.nextInt(4);

        if(state.baseSuit==null){
            chosenCard = new Card(ranks[x],suits[y]);
        }//our card will be first

        //check and see if the AI player has a card of the suit that was originally played. If not, play any card
        else if(checkIfSuitInHand(state.baseSuit)==true){
            chosenCard = new Card(ranks[x], state.baseSuit);
        }
        else{
            chosenCard = new Card(ranks[x],suits[y]);
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

    public boolean checkIfSuitInHand(Suit suit){
        for(Card c: currentHand.cards){
            if(c.getSuit().equals(suit)){
                return true;
            }
        }
        return false;
    }
}