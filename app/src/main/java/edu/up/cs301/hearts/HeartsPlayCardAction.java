package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;

/**
 * Created by emmasoriano on 12/4/17.
 */

public class HeartsPlayCardAction extends HeartsMoveAction {
    private static final long serialVersionUID = 4250639793499599047L;
    private Card card;
    /**
     * Constructor for the HeartsPlayCardAction class.
     *
     * @param player  the player making the move
     */
    public HeartsPlayCardAction(GamePlayer player, Card playCard)
    {
        // initialize the source with the superclass constructor
        super(player);
        card = playCard;
    }

    /**
     * @return
     *        whether this action is a "play card" move
     */
    public boolean isPlayCard() {
        return true;
    }

    public Card getCard(){return card;}
}
