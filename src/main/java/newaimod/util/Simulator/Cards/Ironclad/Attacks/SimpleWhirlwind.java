package newaimod.util.Simulator.Cards.Ironclad.Attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Whirlwind;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleWhirlwind extends AbstractSimpleCard {
    public static final String ID = Whirlwind.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    public static final boolean TARGETSONE = false;

    public SimpleWhirlwind(CombatSimulator simulator, AbstractCard card) {
        super(simulator, ID, TYPE, 0, TARGETSONE);
        this.damage = 5;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleWhirlwind(CombatSimulator simulator, boolean upgraded) {
        // we will say Whirlwind costs 0, and on play it uses up the players energy
        super(simulator, ID, TYPE, 0, TARGETSONE);
        this.damage = 5;
        if (upgraded) {
            upgrade();
        }
    }

    @Override
    public boolean canPlay(SimpleMonster target) {
        return meetsNotEntangled();
    }

    @Override
    public void play(SimpleMonster target) {
        int playerModifiedDamage = simulator.player.getModifiedDamage(this.damage);
        int energy = simulator.player.energy;
        for (int i = 0; i < energy; ++i) {
            for (SimpleMonster m : simulator.monsterList) {
                if (m.isAlive()) {
                    m.takeAttack(playerModifiedDamage);
                }
            }
        }
        simulator.player.energy -= energy;
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.damage = 8;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleWhirlwind(simulator, this.isUpgraded);
    }
    
}
