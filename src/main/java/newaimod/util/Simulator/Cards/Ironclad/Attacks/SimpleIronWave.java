package newaimod.util.Simulator.Cards.Ironclad.Attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.IronWave;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleIronWave extends AbstractSimpleCard {
    public static final String ID = IronWave.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    public static final boolean TARGETSONE = true;

    public SimpleIronWave(CombatSimulator simulator, IronWave card) {
        super(simulator, ID, TYPE, 1, TARGETSONE);
        this.damage = 5;
        this.block = 5;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleIronWave(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.damage = 5;
        this.block = 5;
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
        int playerModifiedBlock = simulator.player.getModifiedBlock(this.block);
        simulator.player.block += playerModifiedBlock;
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.damage = 7;
        this.block = 7;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleIronWave(simulator, this.cost, this.isUpgraded);
    }
}
