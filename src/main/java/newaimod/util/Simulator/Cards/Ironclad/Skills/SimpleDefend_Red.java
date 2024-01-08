package newaimod.util.Simulator.Cards.Ironclad.Skills;

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
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.block = 5;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleDefend_Red(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.block = 5;
        if (upgraded) {
            upgrade();
        }
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        return meetsEnoughEnergy(cost);
    }

    @Override
    public void play(SimpleMonster target) {
        int modifiedBlock = simulator.player.getModifiedBlock(this.block);
        simulator.player.block += modifiedBlock;
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.block = 8;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleDefend_Red(simulator, this.cost, this.isUpgraded);
    }
}
