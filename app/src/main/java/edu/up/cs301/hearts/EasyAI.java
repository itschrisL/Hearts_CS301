package edu.up.cs301.hearts;


import java.util.Random;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;


/**
 * EasyAI class is a computer player who plays random
 * legal cards, making it a very easy opponent to beat.
 *
 * @author Sarah Seydlitz
 * @version November 2017
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


        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            return;
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
            ind = super.playerNum;
            currentHand = state.piles[ind];

            if(ind == state.CurrentPlayerIndex){
                strategy();
                sleep(2000);
                game.sendAction(new HeartsPlayCardAction(this, chosenCard));
                //playCard(currentHand);
            }
        }


    }

    public void strategy() {
        Card twoClubs= new Card(Rank.TWO, Suit.Club);
        if(currentHand.containsCard(twoClubs)){
            chosenCard= twoClubs;
        }
        else {
            //pick a card at random from EasyAI's card deck
            for (Card c : currentHand.cards) {
                if (c.getSuit().equals(state.baseSuit)) {
                    chosenCard = c;
                    break;
                }
            }
        }

    }

    public void playCard(CardDeck aisHand) {

        while (chosenCard == null ||  currentHand.containsCard(chosenCard) == false) {
            strategy();
        }
        sleep(1000);
        game.sendAction(new HeartsPlayCardAction(this, chosenCard));
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
