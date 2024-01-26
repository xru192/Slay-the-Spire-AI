package newaimod.util.simulator.cards.ironclad.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.DemonForm;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleDemonForm extends AbstractSimpleCard {
    public static final String ID = DemonForm.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.POWER;
    public static final boolean TARGETSONE = false;

    public SimpleDemonForm(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.magicNumber = 2;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleDemonForm(CombatSimulator simulator, int cost, boolean upgraded) {
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
        simulator.player.demonForm += this.magicNumber;
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.magicNumber = 3;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleDemonForm(simulator, this.cost, this.isUpgraded);
    }
}
