package newaimod.util.simulator.cards.ironclad.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.BattleTrance;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleBattleTrance extends AbstractSimpleCard {
    public static final String ID = BattleTrance.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final boolean TARGETSONE = false;

    public SimpleBattleTrance(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.magicNumber = 3;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleBattleTrance(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.magicNumber = 3;
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
        simulator.player.payForAndUseCard(this);
        simulator.player.drawCards(this.magicNumber);
        simulator.player.gainNoDraw();
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.magicNumber = 4;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleBattleTrance(simulator, this.cost, this.isUpgraded);
    }
}
