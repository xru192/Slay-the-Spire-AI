package newaimod.util.simulator.cards.ironclad.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Inflame;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleInflame extends AbstractSimpleCard {
    public static final String ID = Inflame.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.POWER;
    public static final boolean TARGETSONE = false;

    public SimpleInflame(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.magicNumber = 2;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleInflame(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.magicNumber = 2;
        if (upgraded) {
            upgrade();
        }
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        return meetsEnoughEnergy(this.cost);
    }

    @Override
    public void play(SimpleMonster target) {
        simulator.player.strength += this.magicNumber;
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.magicNumber = 3;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleInflame(simulator, this.cost, this.isUpgraded);
    }
}
