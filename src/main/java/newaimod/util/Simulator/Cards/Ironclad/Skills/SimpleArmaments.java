package newaimod.util.Simulator.Cards.Ironclad.Skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Armaments;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleArmaments extends AbstractSimpleCard {
    public static final String ID = Armaments.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final boolean TARGETSONE = false;

    public SimpleArmaments(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.block = 5;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleArmaments(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.block = 5;
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
        // TODO effect if Armaments is not upgraded
        if (isUpgraded) {
            for (AbstractSimpleCard card : simulator.player.hand) {
                card.upgrade();
            }
        }
        int modifiedBlock = simulator.player.getModifiedBlock(this.block);
        simulator.player.block += modifiedBlock;
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleArmaments(simulator, this.cost, this.isUpgraded);
    }
}
