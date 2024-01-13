package newaimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import newaimod.ai.AutoPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class DoCombatMoveAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(DoCombatMoveAction.class.getName());

    private final AutoPlayer.CombatMove combatMove;

    public DoCombatMoveAction(AutoPlayer.CombatMove combatMove) {
        this.combatMove = combatMove;
    }


    @Override
    public void update() {
        // Ensure Time Warp power is not violated
        if (AbstractDungeon.player.endTurnQueued) {
            isDone = true;
            return;
        }

        switch (combatMove.type) {
            case CARD:
                ArrayList<AbstractCard> cards = AbstractDungeon.player.hand.group;

                AbstractCard toPlay = cards.get(combatMove.index);
                if (toPlay == null || !toPlay.canUse(AbstractDungeon.player, combatMove.target)) {
                    isDone = true;
                    logger.info("ERROR Illegal card on target played: " + combatMove);
                    return;
                }
                logger.info("Playing card: " + toPlay.name);
                NewQueueCardAction queueCard = new NewQueueCardAction(cards.get(combatMove.index), combatMove.target);
                this.addToTop(queueCard);
                isDone = true;
                break;
            case POTION:
                isDone = true;
                break;
            case PASS:
                isDone = true;
                logger.info("Ending turn");
                AbstractDungeon.overlayMenu.endTurnButton.disable(true);
                break;
            case DISCARD:
                isDone = true;
                break;
        }


    }
}
