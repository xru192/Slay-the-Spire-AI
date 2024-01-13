package newaimod.util.simulator.cards.ironclad.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class TemplatePower extends AbstractSimpleCard {
    public static final String ID = ""; // TODO
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.POWER;
    public static final boolean TARGETSONE = false;

    public TemplatePower(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.magicNumber = 0; // TODO
        if (card.upgraded) {
            upgrade();
        }
    }

    public TemplatePower(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.magicNumber = 0; // TODO
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
        // TODO
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        // TODO
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new TemplatePower(simulator, this.cost, this.isUpgraded);
    }
}
