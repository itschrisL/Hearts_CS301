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
 * HardAI class is a computer player who uses various
 * strategies to plays legal cards with the goal of
 * beating the other players, making it a more challenging
 * opponent to beat.
 *
 * @author Sarah Seydlitz
 * @version November 2017
 */

public class HardAI extends GameComputerPlayer{

    //variables
    protected HeartsGameState state;
    CardDeck currentHand;
    Card chosenCard;
    CardDeck tableDeck = new CardDeck();
    Card[] tableArray;
    CardDeck cardsToPass;
    Rank[] ranks;
    Suit[] suits;
    int[] heartRanks;
    int ind, j, k;
    int clubNum, spadeNum, diamondNum, heartNum;

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
            tableArray = state.cardsOnTable;

            if(ind == state.CurrentPlayerIndex){
                playCard(currentHand);
            }
        }
    }

    public void strategy() {
        for (int i = 0; i < tableArray.length; i++) {
            tableDeck.cards.add(tableArray[i]);
        }

        Card queenOfSpades = new Card(Rank.TEN, Suit.Spade);
        Card kingOfSpades = new Card(Rank.QUEEN, Suit.Spade);
        Card aceOfSpades = new Card(Rank.ACE, Suit.Spade);
        //pick a card at random from EasyAI's card deck
        Random rand = new Random();
        int x = rand.nextInt(13);
        int y = rand.nextInt(4);
        //see what hearts cards are on the table
        heartRanks = new int[4];
        j = 0;
        clubNum = 0;
        spadeNum = 0;
        diamondNum = 0;
        heartNum = 0;
        CardDeck tempClub = new CardDeck();
        CardDeck tempSpade = new CardDeck();
        CardDeck tempDiamond = new CardDeck();
        CardDeck tempHeart = new CardDeck();

        //here we keep track of how many cards we have of each suit
        if (checkIfSuitInHand(Suit.Club)) {
            for (int a = 0; a < 13; a++) {
                if (checkIfCardinHand(currentHand, new Card(ranks[a], Suit.Club))) {
                    tempClub.add(new Card(ranks[a], Suit.Club));
                    clubNum++;
                }
            }
        }
        if (checkIfSuitInHand(Suit.Spade)) {
            for (int b = 0; b < 13; b++) {
                if (checkIfCardinHand(currentHand, new Card(ranks[b], Suit.Spade))) {
                    tempSpade.add(new Card(ranks[b], Suit.Spade));
                    spadeNum++;
                }
            }
        }
        if (checkIfSuitInHand(Suit.Diamond)) {
            for (int c = 0; c < 13; c++) {
                if (checkIfCardinHand(currentHand, new Card(ranks[c], Suit.Diamond))) {
                    tempDiamond.add(new Card(ranks[c], Suit.Diamond));
                    diamondNum++;
                }
            }
        }
        if (checkIfSuitInHand(Suit.Heart)) {
            for (int d = 0; d < 13; d++) {
                if (checkIfCardinHand(currentHand, new Card(ranks[d], Suit.Heart))) {
                    tempHeart.add(new Card(ranks[d], Suit.Heart));
                    heartNum++;
                }
            }
        }
        //we find all the cards in each suit in this hand and add them to some temporary decks
        //we also have the number of cards in each suit that are in our hand

        for (int i = 0; i < 13; i++) {
            Card temp = new Card(ranks[i], Suit.Heart);
            if (checkIfCardOnTable(temp)) {
                heartRanks[j] = i;
                j++;
            }
        }//this gets the ranks of hearts cards on the table

        if (state.baseSuit == Suit.Spade) {
            if (currentHand.containsCard(queenOfSpades)) {
                if (tableDeck.containsCard(kingOfSpades) || tableDeck.containsCard(aceOfSpades)) {
                    chosenCard = queenOfSpades;
                }
            }
        }//this tries to get rid of the queen of spades ASAP

        //this code executes if no other cards have been played yet
        if (state.baseSuit == null) {
            if(clubNum>0&&clubNum<4){
                for (Card a : tempClub.cards) {
                    if (a != null && chosenCard == null) {
                        chosenCard = a;
                        clubNum--;
                    }//we want the first card, which is why the chosenCard == null in the parameters
                }
                tempClub.remove(chosenCard);
            }//get rid of tiny clubs

            else if(diamondNum>0&&diamondNum<4){
                for (Card a : tempDiamond.cards) {
                    if (a != null && chosenCard == null) {
                        chosenCard = a;
                        diamondNum--;
                    }
                }
                tempDiamond.remove(chosenCard);
            }//get rid of tiny diamonds

            else if(spadeNum>0&&spadeNum<4){
                for (Card a : tempSpade.cards) {
                    if (a != null && chosenCard == null) {
                        chosenCard = a;
                        spadeNum--;
                    }
                }
                if(chosenCard==queenOfSpades){
                    tempSpade.remove(queenOfSpades);
                    for (Card a : tempSpade.cards) {
                        if (a != null && chosenCard == null) {
                            chosenCard = a;
                        }
                    }
                }
                tempSpade.remove(chosenCard);
            }//if chosenCard is not queenOfSpades, return the tiny spade

            else{
                if(clubNum>0){
                    for (Card a : tempClub.cards) {
                        if (a != null && chosenCard == null) {
                            chosenCard = a;
                            clubNum--;
                        }
                    }
                    tempClub.remove(chosenCard);
                }//return any club

                else if(diamondNum>0){
                    for (Card a : tempDiamond.cards) {
                        if (a != null && chosenCard == null) {
                            chosenCard = a;
                            diamondNum--;
                        }
                    }
                    tempDiamond.remove(chosenCard);
                }//return any diamond

                else if(clubNum==0&&diamondNum==0&&spadeNum!=0){
                    for (Card a : tempSpade.cards) {
                        if (a != null && chosenCard == null) {
                            chosenCard = a;
                            spadeNum--;
                        }
                    }//find a spade
                    if(chosenCard==queenOfSpades){
                        tempSpade.remove(queenOfSpades);
                        chosenCard = null;
                        for (Card a : tempSpade.cards) {
                            if (a != null && chosenCard == null) {
                                chosenCard = a;
                            }
                        }
                    }//if the card is queen of spades, remove it from the temporary deck and try to find a different spade
                    if(chosenCard!=null) {
                        tempSpade.remove(chosenCard);
                    }//if we found a different spade, then remove chosencard from the deck and keep it as is
                    else if(chosenCard == null && checkIfCardinHand(currentHand, queenOfSpades)){
                        chosenCard = queenOfSpades;
                    }//if we haven't found another spade, return the spade
                }//return a spade, queen of spades last

                else if(heartNum!=0){
                    for (Card a : tempHeart.cards) {
                        if (a != null && chosenCard == null) {
                            chosenCard = a;
                            heartNum--;
                        }
                    }
                    tempHeart.remove(chosenCard);
                }//return any heart
            }
        }//Code if we are first

        //check and see if the AI player has a card of the suit that was originally played
        else if (checkIfSuitInHand(state.baseSuit) && chosenCard == null) {
            if (state.baseSuit == Suit.Club || state.baseSuit == Suit.Spade || state.baseSuit == Suit.Diamond) {
                chosenCard = new Card(ranks[x], state.baseSuit);
            }//if the state.baseSuit is spades, clubs, or diamonds, play a random card of the state.baseSuit
            else if (state.baseSuit == Suit.Heart) {
                //we will be looking at the highest hearts card on the table, heartRanks[j-1], and trying to play a card lower than that (unless j=0, in which case j is 8.
                //if we get through the loop and don't have a card like that, then we start going up instead of down
                if (j == 0) {
                    j = 8;
                }
                for (k = j--; k >= 0; k--) {
                    Card temp2 = new Card(ranks[heartRanks[k]], Suit.Heart);
                    if (currentHand.containsCard(temp2) && chosenCard == null) {
                        chosenCard = temp2;
                    }
                }
                if(chosenCard == null) {
                    for (k = 0; k <= j--; k++) {
                        Card temp3 = new Card(ranks[heartRanks[k]], Suit.Heart);
                        if (currentHand.containsCard(temp3) && chosenCard == null) {
                            chosenCard = temp3;
                        }
                    }
                }
            }//if the state.baseSuit is hearts, play the next lowest card than the highest one on the table, or the next highest card than the highest one on the table
        }//Code if we do have a card of the state.baseSuit

        else if (!checkIfSuitInHand(state.baseSuit) && chosenCard == null){
            if (state.baseSuit == Suit.Heart) {
                if (currentHand.containsCard(queenOfSpades)) {
                    chosenCard = queenOfSpades; //last chance to try to get rid of the queen of spades
                } else {
                    chosenCard = new Card(ranks[x], suits[y]);
                }
            }//if we don't have hearts, play queen of spades or any random card
            else if (state.baseSuit == Suit.Club || state.baseSuit == Suit.Spade || state.baseSuit == Suit.Diamond) {
                if (currentHand.containsCard(queenOfSpades)) {
                    chosenCard = queenOfSpades;
                } else if (checkIfSuitInHand(Suit.Heart)) {
                    if(heartNum!=0){
                        for (Card a : tempHeart.cards) {
                            if (a != null && chosenCard == null) {
                                chosenCard = a;
                                heartNum--;
                            }
                        }
                        tempHeart.remove(chosenCard);
                    }//return the highest heart
                } else {
                    chosenCard = new Card(ranks[x], suits[y]);
                }
            }//if we don't have the state.baseSuit (and it isn't hearts), play queen of spades or highest hearts card or random card
        }//Code if we don't have a card of the state.baseSuit
    }

    public void playCard(CardDeck aisHand) {
        strategy();
        sleep(1000);
        game.sendAction(new HeartsPlayCardAction(this, chosenCard));
    }

    public void CardPass(){
        //pick cards from HardAI's card deck, trying to get rid of high cards
        //Remember, there are three different AI's, so there are three different decks to keep track of
        Card one;
        Card two;
        Card three;

        one = new Card(ranks[getX()],suits[getY()]);
        while(one == null || currentHand.containsCard(one)==false) {
            one = new Card(giveAway());
        }
        cardsToPass.add(one);
        currentHand.remove(one);

        two = new Card(ranks[getX()],suits[getY()]);
        while(two == null || currentHand.containsCard(two)==false) {
            two = new Card(giveAway());
        }
        cardsToPass.add(two);
        currentHand.remove(two);

        three = new Card(ranks[getX()],suits[getY()]);
        while(three == null || currentHand.containsCard(three)==false) {
            three = new Card(giveAway());
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