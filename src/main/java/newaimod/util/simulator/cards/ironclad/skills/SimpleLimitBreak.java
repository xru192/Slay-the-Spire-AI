package newaimod.util.simulator.cards.ironclad.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.LimitBreak;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleLimitBreak extends AbstractSimpleCard {
    public static final String ID = LimitBreak.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final boolean TARGETSONE = false;

    public SimpleLimitBreak(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleLimitBreak(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
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
        int amt = Math.max(0, simulator.player.getStrength());
        simulator.player.gainStrength(amt);
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleLimitBreak(simulator, this.cost, this.isUpgraded);
    }
}
