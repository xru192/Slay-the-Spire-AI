package newaimod.util.simulator.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class Filler extends AbstractSimpleCard {
    public static final String ID = "Filler";

    public Filler(CombatSimulator simulator, AbstractCard.CardType type, int cost) {
        super(simulator, ID, type, cost, true);
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        return false;
    }

    @Override
    public void play(SimpleMonster target) {

    }

    @Override
    public void upgrade() {

    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new Filler(simulator, this.type, this.cost);
    }
}
