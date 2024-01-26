package newaimod.ai.basicIroncladPlayer.basicIroncladCombatMovePicker;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import newaimod.ai.AbstractCombatMovePicker;
import newaimod.ai.AutoPlayer.CombatMove;
import newaimod.util.CombatUtils;
import basemod.ReflectionHacks;
import newaimod.util.DungeonInformationManager;
import newaimod.util.simulator.CombatSimulator;

import java.util.ArrayList;


public class BasicIroncladCombatMovePicker extends AbstractCombatMovePicker {

    @Override
    public CombatMove pickMoveDefault() {
        return basicMove();
    }

    private int getIncomingDamage(AbstractMonster monster) {
        EnemyMoveInfo moveInfo = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");
        if (CombatUtils.isNotAttack(moveInfo.intent)) {
            return 0;
        }

        int hits = Math.max(1, moveInfo.multiplier);
        int hitDamage = Math.max(0, monster.getIntentDmg());

        return hits * hitDamage;
    }

    /**
     * Returns a move according to a basic evaluation.
     */
    private CombatMove basicMove() {
        int currentEnergy = CombatUtils.usableEnergy();
        ArrayList<AbstractCard> hand = AbstractDungeon.player.hand.group;

        int totalIncomingDamage = 0;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            totalIncomingDamage += getIncomingDamage(m);
        }
        totalIncomingDamage = Math.max(0, totalIncomingDamage - AbstractDungeon.player.currentBlock);

        AbstractCard toPlay = null;

        for (AbstractCard card : hand) {
            if (!card.canUse(AbstractDungeon.player, CombatUtils.getWeakestTarget())) {
                continue;
            }

            if (card.costForTurn > currentEnergy) {
                continue;
            }

            if (toPlay != null && totalIncomingDamage <= 3 && card.damage == 0) {
                continue;
            }

            toPlay = card;
        }

        if (toPlay == null) {
            return new CombatMove(CombatMove.TYPE.PASS);
        }

        assert hand.contains(toPlay);
        CombatSimulator currentState = DungeonInformationManager.getInstance().getCurrentState();
        return new CombatMove(CombatMove.TYPE.CARD, hand.indexOf(toPlay), CombatUtils.getWeakestTarget(currentState));
    }
}
