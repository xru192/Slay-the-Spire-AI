package newaimod.util.simulator.cards.ironclad.attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.TwinStrike;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleTwinStrike extends AbstractSimpleCard {
    public static final String ID = TwinStrike.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    public static final boolean TARGETSONE = true;

    public SimpleTwinStrike(CombatSimulator simulator, TwinStrike card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.damage = 5;
        if (card.upgraded) {
            upgrade();
        }
    }


    public SimpleTwinStrike(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.damage = 5;
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
        int playerModifiedDamage = simulator.player.getModifiedDamage(this.damage);
        target.takeMultiAttack(playerModifiedDamage, 2);
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.damage = 7;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleTwinStrike(simulator, this.cost, this.isUpgraded);
    }
}
