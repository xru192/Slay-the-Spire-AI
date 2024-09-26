package newaimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
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

        AbstractMonster target = combatMove.target == null ? null : combatMove.target.originalMonster;
        switch (combatMove.type) {
            case CARD:
                ArrayList<AbstractCard> cards = AbstractDungeon.player.hand.group;
                if (cards.size() <= combatMove.index) {
                    isDone = true;
                    logger.error("Move failed (index too high): {}", combatMove);
                    return;
                }
                AbstractCard toPlay = cards.get(combatMove.index);
                if (combatMove.target == null && !toPlay.canUse(AbstractDungeon.player, null)) {
                    isDone = true;
                    logger.error("Move failed: {}", combatMove);
                    return;
                }

                if (combatMove.target != null) {
                    if (combatMove.target.originalMonster == null) {
                        isDone = true;
                        logger.error("Move failed because target wasn't linked to the game: {}", combatMove);
                        return;
                    } else if (!toPlay.canUse(AbstractDungeon.player, combatMove.target.originalMonster)) {
                        isDone = true;
                        logger.error("Move failed because card cannot be played on target: {}", combatMove);
                        return;
                    }
                }

                logger.info("Playing card: {}", toPlay.name);

                NewQueueCardAction queueCard = new NewQueueCardAction(cards.get(combatMove.index), target);
                this.addToTop(queueCard);
                isDone = true;
                break;
            case POTION:
                if (combatMove.index < 0 || combatMove.index >= AbstractDungeon.player.potionSlots) {
                    isDone = true;
                    logger.error("Use potion failed because selected potion slot does not exist");
                    return;
                }

                AbstractPotion potion = AbstractDungeon.player.potions.get(combatMove.index);
                if (potion instanceof PotionSlot) {
                    isDone = true;
                    logger.error("Use potion failed because selected potion slot was empty");
                    return;
                }

                if (!potion.canUse()) {
                    isDone = true;
                    logger.error("Move failed because potion cannot be used");
                    return;
                }

                if (potion.targetRequired && (combatMove.target == null || combatMove.target.originalMonster == null)) {
                    isDone = true;
                    logger.error("Move failed because potion requires target but it was missing");
                    return;
                }

                logger.info("Using potion: " + potion.name);
                potion.use(target);
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onUsePotion();
                }
                AbstractDungeon.topPanel.destroyPotion(combatMove.index);
                isDone = true;
                break;
            case PASS:
                isDone = true;
                logger.info("Ending turn");
                AbstractDungeon.overlayMenu.endTurnButton.disable(true);
                break;
            case DISCARD:
                if (combatMove.index < 0 || combatMove.index >= AbstractDungeon.player.potionSlots) {
                    isDone = true;
                    logger.error("Discard potion failed because selected potion slot does not exist");
                    return;
                }

                AbstractPotion potionDiscard = AbstractDungeon.player.potions.get(combatMove.index);
                if (potionDiscard instanceof PotionSlot) {
                    isDone = true;
                    logger.error("Discard potion failed because selected potion slot was empty");
                    return;
                }

                if (!potionDiscard.canDiscard()) {
                    isDone = true;
                    logger.error("Move failed because potion cannot be discarded");
                    return;
                }

                logger.info("Discarding potion: {}", potionDiscard.name);
                AbstractDungeon.topPanel.destroyPotion(potionDiscard.slot);
                isDone = true;
                break;
        }


    }
}
