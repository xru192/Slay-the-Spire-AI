package newaimod.util.simulator.cards.ironclad.attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.BodySlam;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleBodySlam extends AbstractSimpleCard {
    public static final String ID = BodySlam.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    public static final boolean TARGETSONE = true;

    public SimpleBodySlam(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleBodySlam(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        if (upgraded) {
            upgrade();
        }
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        return meetsTargetable(target) && meetsEnoughEnergy(cost) && meetsNotEntangled();
    }

    @Override
    public void play(SimpleMonster target) {
        int playerModifiedDamage = simulator.player.getModifiedDamage(simulator.player.block);
        target.takeAttack(playerModifiedDamage);
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.cost = 0;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleBodySlam(simulator, this.cost, this.isUpgraded);
    }

}
