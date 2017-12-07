package edu.up.cs301.hearts;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.R;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;
//import edu.up.cs301.slapjack.SJComputerPlayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * this is the primary activity for Hearts game
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class HeartsMainActivity extends GameMainActivity {

    public static final int PORT_NUMBER = 7752;


    public void showInfo(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Help");
        builder.setMessage("Starting the game \n" +
                "When the game is launched, the main background layout of a table will appear " +
                "with no cards dealt yet, and a main menu will display in a pop up message. The " +
                "first question the user will be asked is to select the difficulty level of the " +
                "computer player (AI). Choose Easy for an easy game or Normal for a more " +
                "challenging opponent. Next decide if you want to play single round of the game" +
                " (until all cards in the deck have been played) or until one player reaches 50 points. " +
                "Once you have answered these two questions click play game. This will make the pop " +
                "main menu disappear and the cards will be dealt. The text box in the bottom of the" +
                " screen indicate who’s turn it is. \n" +
                " \n \n" +
                "Making the first move \n" +
                "The first card played in the game is always the 2 of clubs. If you have the " +
                "2 of clubs that must be the first card you play. To play a card, you first click " +
                "the card once to select it, then click on it again to play the card. Once you " +
                "play a card you must wait for the remaining players to take their turn. The " +
                "players will take turns in a clockwise order. When it gets to be your turn " +
                "again there will be a message at the bottom of the screen indicating that it " +
                "is your turn. Once everyone has played a card, the person who put down the " +
                "highest card of the suit for that trick (a trick is just a round of cards " +
                "being played) collects the pile of cards played. \n" +
                "  \n\n" +
                "Making a move \n" +
                "After the first trick, the player who collects the pile of cards played is " +
                "the player who starts the next trick.   \n" +
                "If the player is playing a game up to 50 points, then after each trick the " +
                "scores for each player will be updated and displayed at the bottom of the " +
                "screen. If not then the scores will be displayed at the end once all cards " +
                "have been played and collected. \n" +
                "If the player attempts to make an illegal move, there will be an error " +
                "message to indicate to the user that the attempted move is not allowed. \n\n" +
                "Rules/Tips \n" +
                "The goal of the game is to have as few points as possible. You earn points " +
                "by collecting heart cards(+1 per heart card) or the queen of spades(+13). " +
                "After the first card has been played in a trick, the remaining players must " +
                "put down a card of that same suit (if possible), and the one who plays the " +
                "highest card of the suit collects the pile of cards played. If you don’t have " +
                "any cards of the suit of that round you can play any card. This is an ideal " +
                "time to play a heart since you can't collect the pile of cards in that trick." +
                " To refer to the rules of the game while playing, in the bottom of the screen" +
                " click the button \"rules\". \n\n" +
                "Game termination  \n" +
                "Once the game is over, and there are no more cards to play or a player has" +
                " reached 50 points, a pop up message will appear announcing the winner. If " +
                "the game was played until all cards were played then the message will also " +
                "include the scores of each player. There will be a button on this pop up " +
                "asking you if you want to play again or quit. \n" +
                " ");

        AlertDialog alert = builder.create();
        alert.show();
    }

    /** a hearts game for two players. The default is human vs. computer */
    @Override
    public GameConfig createDefaultConfig() {


        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        playerTypes.add(new GamePlayerType("human player (green)") {
            public GamePlayer createPlayer(String name) {
                return new HeartsHumanPlayer(name);
            }});
        playerTypes.add(new GamePlayerType("human player (yellow)") {
            public GamePlayer createPlayer(String name) {
                return new HeartsHumanPlayer(name);
            }
        });
        playerTypes.add(new GamePlayerType("computer player (easy)") {
            public GamePlayer createPlayer(String name) {
                return new EasyAI(name);
            }
        });
        //playerTypes.add(new GamePlayerType("computer player (slow)") {
        // public GamePlayer createPlayer(String name) {
        // return new HardAI(name);
        // }
        //  });

        // Create a game configuration class for Hearts
        GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "Hearts", PORT_NUMBER);

        // Add the default players
        defaultConfig.addPlayer("Human", 0);
        defaultConfig.addPlayer("Computer1", 2);
        defaultConfig.addPlayer("Computer2", 2);
        defaultConfig.addPlayer("Computer3", 2);


        // Set the initial information for the remote player
        defaultConfig.setRemoteData("Guest", "", 1);

        //done!
        return defaultConfig;

    }//createDefaultConfig


    @Override
    public LocalGame createLocalGame() {
        return new HeartsLocalGame();
    }

}