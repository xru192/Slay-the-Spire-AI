package newaimod.AI.BasicIroncladPlayer;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import newaimod.AI.AbstractRestAutoPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasicIroncladRestPlayer extends AbstractRestAutoPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladRestPlayer.class.getName());

    @Override
    protected RESTCHOICE choose(boolean canRest, boolean canSmith, boolean canLift, boolean canToke, boolean canDig, boolean canRecall) {
        int needRest = 29;
        if (AbstractDungeon.floorNum == 15) {
            logger.info("At rest site before Act I boss");
            needRest = 39;
        }

        if (AbstractDungeon.player.currentHealth <= needRest) {
            return RESTCHOICE.REST;
        }

        if (canLift) {
            return RESTCHOICE.LIFT;
        } else if (canSmith) {
            return RESTCHOICE.SMITH;
        } else if (canDig) {
            return RESTCHOICE.DIG;
        } else if (canToke) {
            return RESTCHOICE.TOKE;
        } else {
            return RESTCHOICE.RECALL;
        }

    }
}
