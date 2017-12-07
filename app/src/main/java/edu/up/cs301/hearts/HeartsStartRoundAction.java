package edu.up.cs301.hearts;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by lytle on 12/6/2017.
 */

public class HeartsStartRoundAction extends GameAction {
    private static final long serialVersionUID = 3534234521434L;

    public HeartsStartRoundAction(GamePlayer player){
        super(player);
    }

    /**
     * @return
     *        whether this action is a "play card" move
     */
    public boolean isPlayCard() {
        return false;
    }

    public boolean isRoundStart(){return true;}

}

