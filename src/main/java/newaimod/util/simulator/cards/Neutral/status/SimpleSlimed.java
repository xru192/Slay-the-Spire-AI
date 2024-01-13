package newaimod.util.simulator.cards.Neutral.status;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Slimed;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleSlimed extends AbstractSimpleCard {
    public static final String ID = Slimed.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.STATUS;
    public static final boolean TARGETSONE = false;

    public SimpleSlimed(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
    }

    public SimpleSlimed(CombatSimulator simulator, int cost) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        return meetsEnoughEnergy(cost);
    }

    @Override
    public void play(SimpleMonster target) {
        simulator.player.exhaustSlimed();
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleSlimed(simulator, this.cost);
    }
}
