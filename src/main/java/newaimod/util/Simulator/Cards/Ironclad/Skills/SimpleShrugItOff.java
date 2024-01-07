package newaimod.util.Simulator.Cards.Ironclad.Skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.ShrugItOff;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleShrugItOff extends AbstractSimpleCard {
    public static final String ID = ShrugItOff.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final boolean TARGETSONE = false;

    public SimpleShrugItOff(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.block = 8;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleShrugItOff(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.block = 8;
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
        // TODO draw a card
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.block = 11;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleShrugItOff(simulator, this.cost, this.isUpgraded);
    }
}
