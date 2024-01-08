package newaimod.util.Simulator.Cards.Ironclad.Skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.FlameBarrier;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleFlameBarrier extends AbstractSimpleCard {
    public static final String ID = FlameBarrier.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final boolean TARGETSONE = false;

    public SimpleFlameBarrier(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.block = 12;
        this.magicNumber = 4;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleFlameBarrier(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.block = 12;
        this.magicNumber = 4;
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
        int modifiedBlock = simulator.player.getModifiedBlock(this.block);
        simulator.player.block += modifiedBlock;
        simulator.player.payForAndUseCard(this);
        // TODO secondary effect
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.block = 16;
        this.magicNumber = 6;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleFlameBarrier(simulator, this.cost, this.isUpgraded);
    }
}
