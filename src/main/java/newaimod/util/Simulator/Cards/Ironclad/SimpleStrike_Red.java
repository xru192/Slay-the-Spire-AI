package newaimod.util.Simulator.Cards.Ironclad;

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

    public SimpleStrike_Red(CombatSimulator simulator, int cost) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.damage = 6;
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        if (target == null) {
            return false;
        }

        // TODO if player is entangled

        return target.isTargetable() && simulator.player.energy >= cost;
    }

    @Override
    public void play(SimpleMonster target) {
        int playerModifiedDamage = simulator.player.getModifiedDamage(this.damage);
        target.takeAttack(playerModifiedDamage);
        simulator.player.payForAndUseCard(this);
//        simulator.player.energy -= this.cost;
    }

    @Override
    public void upgrade() {
        this.damage = 9;
        this.isUpgraded = true;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleStrike_Red(simulator, this.cost);
    }



}
