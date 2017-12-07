package edu.up.cs301.hearts;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by emmasoriano on 12/4/17.
 */

public class HeartsMoveAction extends GameAction {
    private static final long serialVersionUID = -4107100271012188849L;

    /**
     * Constructor for SJMoveAction
     *
     * @param player the player making the move
     */
    public HeartsMoveAction(GamePlayer player)
    {
        // invoke superclass constructor to set source
        super(player);
    }

    /**
     * @return
     *        whether the move was a "play card"
     */
    public boolean isPlayCard() {
        return false;
    }

    /**
     * @return
     *        whether the move was a "card pass"
     */
    public boolean isCardPass() {
        return false;
    }


}
