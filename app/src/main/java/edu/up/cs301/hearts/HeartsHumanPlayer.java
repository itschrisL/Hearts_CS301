package edu.up.cs301.hearts;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.*;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;


/**
 * HeartsHumanPlayer class allows for users to play a game
 * of Hearts and interact with the GUI.
 *
 * @author Lindsey Lavee
 * @version December 2017
 */
public class HeartsHumanPlayer extends GameHumanPlayer implements Animator {

    TextView player1Score;
    TextView player2Score;
    TextView player3Score;
    TextView humanScore;
    String[] playerNames= new String[4];
    int count=52;
    int index;
    int drawCardCount=0;
    boolean doubleTap=false;
    boolean singleTap=false;
    private Card selectedCard;
    private Card cardToPlay;
    private  RectF [] yellowCardLocation= new RectF[14];
    private  RectF [] cardLocation = new RectF[14];

    //card spaces on the 'table'
    RectF Player0CardSpace;
    RectF Player1CardSpace;
    RectF Player2CardSpace;
    RectF Player3CardSpace;

    //three card pass variables
    Card[] cardsToPass=new Card[3];
    boolean doubleTapCard1=false;
    boolean singleTapCard1=false;
    boolean doubleTapCard2=false;
    boolean singleTapCard2=false;
    boolean doubleTapCard3=false;
    boolean singleTapCard3=false;
    Card selectedCard1;
    Card selectedCard2;
    Card selectedCard3;
    int card1Location;
    int card2Location;
    int card3Location;

    //var to see where human touched
    float x;
    float y;

    // sizes and locations of card decks and cards, expressed as percentages
    // of the screen height and width
    private final static float CARD_HEIGHT_PERCENT = 40; // height of a card
    private final static float CARD_WIDTH_PERCENT = 17; // width of a card
    private final static float LEFT_BORDER_PERCENT = 4; // width of left border
    private final static float RIGHT_BORDER_PERCENT = 20; // width of right border
    private final static float VERTICAL_BORDER_PERCENT = 4; // width of top/bottom borders

    // our game state
    protected HeartsGameState state;

    // our activity
    private Activity myActivity;

    // the amination surface
    private AnimationSurface surface;

    // the background color
    private int backgroundColor;

    /**
     * constructor
     *
     * @param name
     *        the player's name
     */
    public HeartsHumanPlayer(String name){
        super(name);
        playerNames = super.allPlayerNames;
        index = super.playerNum;
        setCardLocations();
        drawCardTable();
    }

    /**
     * callback method: we have received a message from the game
     *
     * @param info
     *        the message we have received from the game
     */
    @Override
    public void receiveInfo(GameInfo info) {
        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {

            // if we had an out-of-turn or illegal move, flash the screen
            surface.flash(Color.RED, 50);
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
        }
    }

    /**
     * call-back method: called whenever the GUI has changed (e.g., at the beginning
     * of the game, or when the screen orientation changes).
     *
     * @param activity
     *        the current activity
     */
    public void setAsGui(GameMainActivity activity) {

        // remember the activity
        myActivity = activity;

        // Load the layout resource for the new configuration
        activity.setContentView(R.layout.sj_human_player);// TODO change to hearts

        // link the animator (this object) to the animation surface
        surface = (AnimationSurface) myActivity
                .findViewById(R.id.animation_surface);
        surface.setAnimator(this);

        // read in the card images
        edu.up.cs301.card.Card.initImages(activity);

        // if the state is not null, simulate having just received the state so that
        // any state-related processing is done
        if (state != null) {receiveInfo(state);}
    }

    /**
     letting the human player select 3 cards to pass to one of the
     computer players.  Replacing these empty slots with the cards they
     receive in return.
     */
    public void CardPass(){
        /*
        Paint p = new Paint();
        p.setColor(Color.YELLOW);

        for (int n=0; n<=13; n++) {
            if (cardLocation[n].contains(x, y)) {

                selectedCard1 = humanCards[n];
                selectedCard2 = humanCards[n];
                selectedCard3 = humanCards[n];
                //if(isMyTurn) {

                if (singleTapCard1 == false) {
                    selectedCard1 = humanCards[n];
                    singleTapCard1 = true;
                    doubleTapCard1 = false;
                } else if (cardsToPass[1] == selectedCard1) {
                    //draw card to play in human table spot
                    doubleTapCard1 = true;
                    cardLocationBool[n] = true;
                    singleTapCard1 = false;
                } else {

                    singleTapCard1 = true;
                    selectedCard1 = humanCards[n];
                    doubleTapCard1 = false;
                }
                if (singleTapCard2 == false) {
                    selectedCard2 = humanCards[n];
                    singleTapCard2 = true;
                    doubleTapCard2 = false;
                } else if (cardsToPass[2] == selectedCard2) {
                    //draw card to play in human table spot
                    doubleTapCard2 = true;
                    cardLocationBool[n] = true;
                    singleTapCard2 = false;
                } else {

                    singleTapCard2 = true;
                    selectedCard2 = humanCards[n];
                    doubleTapCard2 = false;
                }
                if (singleTapCard3 == false) {
                    selectedCard3 = humanCards[n];
                    singleTapCard3 = true;
                    doubleTapCard3 = false;
                } else if (cardsToPass[0] == selectedCard3) {
                    //draw card to play in human table spot
                    doubleTapCard3 = true;
                    cardLocationBool[n] = true;
                    singleTapCard3 = false;
                } else {

                    singleTapCard3 = true;
                    selectedCard3 = humanCards[n];
                    doubleTapCard3 = false;
                }
            }
        }

        if (doubleTapCard1==true){
            //draw selected card in played human spot

            game.sendAction(new HeartsMoveAction(this));
            //drawCard(g, HcardPile, selectedCard);

            RectF temp = yellowCardLocation[count];

            g.drawRect(yellowCardLocation[count], p);

            yellowCardLocation[count] = temp;
            cardLocationBool[count] = true;

            drawMe = true;
            humanCards[count] = null;

            cardsToPass[0] = selectedCard1; //needs to make its way to state
            card1Location=count;
        }
        if (doubleTapCard2==true){
            //draw selected card in played human spot
            game.sendAction(new HeartsMoveAction(this));
            //drawCard(g, HcardPile, selectedCard);

            RectF temp = yellowCardLocation[count];

            g.drawRect(yellowCardLocation[count], p);

            yellowCardLocation[count] = temp;
            cardLocationBool[count] = true;

            drawMe = true;
            humanCards[count] = null;

            cardsToPass[1] = selectedCard2; //needs to make its way to state
            card2Location=count;
        }
        if (doubleTapCard3==true){
            //draw selected card in played human spot
            game.sendAction(new HeartsMoveAction(this));
            //drawCard(g, HcardPile, selectedCard);

            RectF temp = yellowCardLocation[count];

            g.drawRect(yellowCardLocation[count], p);

            yellowCardLocation[count] = temp;
            cardLocationBool[count] = true;

            drawMe = true;
            humanCards[count] = null;

            cardsToPass[2] = selectedCard3; //needs to make its way to state
            card3Location=count;
        }
        */
    }

    /**
     * @return the top GUI view
     */
    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * @return
     *        the amimation interval, in milliseconds
     */
    public int interval() {
        // 1/20 of a second
        return 50;
    }

    /**
     * @return
     *        the background color
     */
    public int backgroundColor() {
        return Color.GREEN;
    }

    /**
     * @return
     *        whether the animation should be paused
     */
    public boolean doPause() {
        return false;
    }

    /**
     * @return
     *        whether the animation should be terminated
     */
    public boolean doQuit() {
        return false;
    }

    public void setCardLocations() {
        // intialize values for the locations of human player's hand

        for (int row = 1; row < 8; row++) {
            for (int col = 1; col < 3; col++) {

                float rectRight = 210;
                float rectTop = 1000;
                float rectBottom = 1300;
                float rectLeft = 60;

                rectLeft = rectLeft + ((row - 1) * 200);
                rectRight = rectRight + ((row - 1) * 200);
                rectTop = rectTop + ((col - 1) * 325);
                rectBottom = rectBottom + ((col - 1) * 325);

                cardLocation[drawCardCount] = new RectF(rectLeft, rectTop, rectRight, rectBottom);

                float rectRightY = 230;
                float rectTopY = 990;
                float rectBottomY = 1320;
                float rectLeftY = 50;

                rectLeftY = rectLeftY + ((row - 1) * 200);
                rectRightY = rectRightY + ((row - 1) * 200);
                rectTopY = rectTopY + ((col - 1) * 325);
                rectBottomY = rectBottomY + ((col - 1) * 325);

                yellowCardLocation[drawCardCount] = new RectF(rectLeftY, rectTopY, rectRightY, rectBottomY);
                drawCardCount++;


            }
        }

    }

    public void drawCardTable(){
        float HrectLeft = 650;
        float HrectRight = 800;
        float HrectTop = 500;
        float HrectBottom = 700;

        Player0CardSpace = new RectF(HrectLeft, HrectTop, HrectRight, HrectBottom);

        float AI1rectLeft = 250;
        float AI1rectRight = 400;
        float AI1rectTop = 300;
        float AI1rectBottom = 500;

        Player1CardSpace = new RectF(AI1rectLeft, AI1rectTop, AI1rectRight, AI1rectBottom);

        float AI2rectLeft = 650;
        float AI2rectRight = 800;
        float AI2rectTop = 100;
        float AI2rectBottom = 300;

        Player2CardSpace = new RectF(AI2rectLeft, AI2rectTop, AI2rectRight, AI2rectBottom);

        float AI3rectLeft = 1050;
        float AI3rectRight = 1200;
        float AI3rectTop = 300;
        float AI3rectBottom = 500;


        Player3CardSpace = new RectF(AI3rectLeft, AI3rectTop, AI3rectRight, AI3rectBottom);

    }


    /**
     * callback-method: we have gotten an animation "tick"; redraw the screen image:
     * - the middle deck, with the top card face-up, others face-down
     * - the two players' decks, with all cards face-down
     * - a red bar to indicate whose turn it is
     *
     * @param g
     *        the canvas on which we are to draw
     */

    public void tick(Canvas g) {
        // ignore if we have not yet received the game state
        if (state == null) return;

        //Draw the table card slot for player 0
        drawCard(g, Player0CardSpace, state.cardsOnTable[0]);
        //Draw the table card slot for player 1
        drawCard(g, Player1CardSpace, state.cardsOnTable[1]);
        //Draw the table card slot for player 2
        drawCard(g, Player2CardSpace, state.cardsOnTable[2]);
        //Draw the table card slot for player 3
        drawCard(g, Player3CardSpace, state.cardsOnTable[3]);

        //draw a dot by the current players card space
        for (int i = 0; i < 4; i++) {drawDot(g, state.CurrentPlayerIndex);}
        //draws cards for human to see
        for (int i = 0; i < Math.min(13, state.piles[0].size()); i++) {drawCard(g, cardLocation[i], state.piles[0].cards.get(i));}

        //update the displayed scores
        updateText();
    }


    public void updateText(){
        humanScore= myActivity.findViewById(R.id.HumanScore);
        player1Score= myActivity.findViewById(R.id.AI1Score);
        player2Score= myActivity.findViewById(R.id.AI2Score);
        player3Score= myActivity.findViewById(R.id.AI3Score);

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int[] currentScores=state.getScores();
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                humanScore.setText(HeartsHumanPlayer.super.allPlayerNames[0]+" score: " + currentScores[0]);
                player1Score.setText(HeartsHumanPlayer.super.allPlayerNames[1]+" score: " + currentScores[1]);
                player2Score.setText(HeartsHumanPlayer.super.allPlayerNames[2]+"  score: " + currentScores[2]);
                player3Score.setText(HeartsHumanPlayer.super.allPlayerNames[3]+" score: " + currentScores[3]);
            }
        });
    }

    public void drawDot(Canvas g, int currentPlayerIndex){
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        int x=0;
        int y=0;

        if (currentPlayerIndex==0){
            x=700;
            y=750;
        }else if (currentPlayerIndex==1){
            x=200;
            y=550;
        }else if (currentPlayerIndex==2){
            x=700;
            y=350;
        }else if (currentPlayerIndex==3){
            x=1100;
            y=550;
        }

        g.drawCircle(x, y, 50, white);
    }


    public void announceWinner(){
        AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);

        builder.setTitle("Winner!");
        //builder.setMessage(""+state.winTrick()+" is the winner!");

        AlertDialog alert = builder.create();
        alert.show();

    }

    /**
     * callback method: we have received a touch on the animation surface
     *
     * @param event
     *        the motion-event
     */
    public void onTouch(MotionEvent event) {
        boolean dontFlash= false;
        // ignore everything except down-touch events
        if (event.getAction() != MotionEvent.ACTION_DOWN) return;

        // get the location of the touch on the surface
        x = (int) event.getX();
        y = (int) event.getY();

        if(cardLocation != null){
            for (int n=0; n<=13; n++) {
                if (cardLocation[n].contains(x, y)) {
                    dontFlash=true;
                    selectedCard=state.piles[0].get(n);
                    if (singleTap == false) {
                        selectedCard = state.piles[0].get(n);
                        singleTap = true;
                        doubleTap = false;
                    } else if (cardToPlay == selectedCard) {
                        // call action
                        game.sendAction(new HeartsPlayCardAction(this, selectedCard));
                    } else {
                        singleTap = true;
                        selectedCard = state.piles[0].get(n);
                        doubleTap = false;
                    }
                    //moves selected card to table slot
                    cardToPlay = state.piles[0].get(n);
                    count = n;
                }
            }
        }
        if (dontFlash==false){ surface.flash(Color.RED, 50);}
    }

    /**
     * draws a card on the canvas; if the card is null, draw a card-back
     *
     * @param g
     *        the canvas object
     * @param rect
     *        a rectangle defining the location to draw the card
     * @param c
     *        the card to draw; if null, a card-back is drawn
     */
    private static void drawCard(Canvas g, RectF rect, edu.up.cs301.card.Card c) {
        if (c == null) {
            // null: draw a card-back, consisting of a blue card
            // with a white line near the border. We implement this
            // by drawing 3 concentric rectangles:
            // - blue, full-size
            // - white, slightly smaller
            // - blue, even slightly smaller
            Paint white = new Paint();
            white.setColor(Color.WHITE);
            Paint blue = new Paint();
            blue.setColor(Color.BLUE);
            RectF inner1 = scaledBy(rect, 0.96f); // scaled by 96%
            RectF inner2 = scaledBy(rect, 0.98f); // scaled by 98%
            g.drawRect(rect, blue); // outer rectangle: blue
            g.drawRect(inner2, white); // middle rectangle: white
            g.drawRect(inner1, blue); // inner rectangle: blue
        }
        else {
            // just draw the card
            c.drawOn(g, rect);
        }
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

}