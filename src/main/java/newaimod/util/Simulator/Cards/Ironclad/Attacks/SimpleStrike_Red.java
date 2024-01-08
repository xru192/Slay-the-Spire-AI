package newaimod.util.Simulator.Cards.Ironclad.Attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleStrike_Red extends AbstractSimpleCard {
    public static final String ID = Strike_Red.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    public static final boolean TARGETSONE = true;

    public SimpleStrike_Red(CombatSimulator simulator, Strike_Red card) {
        super(simulator, ID, TYPE, 1, TARGETSONE);
        this.damage = 6;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleStrike_Red(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.damage = 6;
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
    }

    @Override
    public void upgrade() {
        this.damage = 9;
        this.isUpgraded = true;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleStrike_Red(simulator, this.cost, this.isUpgraded);
    }



}
