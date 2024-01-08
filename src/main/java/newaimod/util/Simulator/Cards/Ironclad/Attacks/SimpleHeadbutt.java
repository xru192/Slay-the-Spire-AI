package newaimod.util.Simulator.Cards.Ironclad.Attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Headbutt;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleHeadbutt extends AbstractSimpleCard {
    public static final String ID = Headbutt.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    public static final boolean TARGETSONE = true;

    public SimpleHeadbutt(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, card.costForTurn, TARGETSONE);
        this.damage = 9;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleHeadbutt(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.damage = 9;
        if (upgraded) {
            upgrade();
        }
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        return meetsTargetable(target) && meetsEnoughEnergy(cost) && meetsNotEntangled();
    }

    @Override
    public void play(SimpleMonster target) {
        int playerModifiedDamage = simulator.player.getModifiedDamage(this.damage);
        target.takeAttack(playerModifiedDamage);
        simulator.player.payForAndUseCard(this);
        // TODO Headbutt effect
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.damage = 12;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleHeadbutt(simulator, this.cost, this.isUpgraded);
    }

}
