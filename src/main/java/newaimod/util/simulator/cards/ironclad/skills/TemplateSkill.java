package newaimod.util.simulator.cards.ironclad.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class TemplateSkill extends AbstractSimpleCard {
    public static final String ID = ""; // TODO
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final boolean TARGETSONE = false; // TODO

    public TemplateSkill(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.block = 0; // TODO
        if (card.upgraded) {
            upgrade();
        }
    }

    public TemplateSkill(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.block = 0; // TODO
        if (upgraded) {
            upgrade();
        }
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        // TODO depends on TARGETSONE
//        return meetsTargetable(target) && meetsEnoughEnergy(this.cost);
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
        return new TemplateSkill(simulator, this.cost, this.isUpgraded);
    }
}
