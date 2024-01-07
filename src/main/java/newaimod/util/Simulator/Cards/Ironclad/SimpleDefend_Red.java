package newaimod.util.Simulator.Cards.Ironclad;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleDefend_Red extends AbstractSimpleCard {
    public static final String ID = Defend_Red.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final boolean TARGETSONE = false;

    public SimpleDefend_Red(CombatSimulator simulator, Defend_Red card) {
        super(simulator, ID, TYPE, 1, TARGETSONE);
        this.block = 5;
    }

    public SimpleDefend_Red(CombatSimulator simulator, int cost) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.block = 5;
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        return simulator.player.energy >= this.cost;
    }

    @Override
    public void play(SimpleMonster target) {
        // TODO Frail, dexterity
        int modifiedBlock = simulator.player.getModifiedBlock(this.block);
        simulator.player.block += modifiedBlock;
//        simulator.player.energy -= this.cost;
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.block = 8;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleDefend_Red(simulator, this.cost);
    }
}
