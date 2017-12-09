package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;

/**
 * Created by emmasoriano on 12/4/17.
 */

public class HeartsCardPassAction extends HeartsMoveAction {
    private static final long serialVersionUID = 4250639793499599047L;
    private CardDeck[] cardPass;
    /**
     * Constructor for the HeartsCardPassAction class.
     *
     * @param player  the player making the move
     */
    public HeartsCardPassAction(GamePlayer player, CardDeck[] pass)
    {
        // initialize the source with the superclass constructor
        super(player);
        cardPass = pass;
    }

    /**
     * @return
     *        whether this action is a "card pass" move
     */
    public boolean isCardPass() {
        return true;
    }

    public CardDeck[] getCardPass(){ return cardPass; }


}