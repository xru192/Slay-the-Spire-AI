package newaimod.util.Simulator.Cards.Ironclad.Attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Bash;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public class SimpleBash extends AbstractSimpleCard {
    public static final String ID = Bash.ID;
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    public static final boolean TARGETSONE = true;

    public SimpleBash(CombatSimulator simulator, Bash card) {
        super(simulator, ID, TYPE, 2, TARGETSONE);
        this.damage = 8;
        if (card.upgraded) {
            upgrade();
        }
    }

    public SimpleBash(CombatSimulator simulator, int cost, boolean upgraded) {
        super(simulator, ID, TYPE, cost, TARGETSONE);
        this.damage = 8;
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
        int vulnAmount = isUpgraded ? 3 : 2;
        target.takeAttack(playerModifiedDamage);
        target.takeVulnerable(vulnAmount);
        simulator.player.payForAndUseCard(this);
    }

    @Override
    public void upgrade() {
        this.isUpgraded = true;
        this.damage = 10;
    }

    @Override
    public AbstractSimpleCard copy(CombatSimulator simulator) {
        return new SimpleBash(simulator, this.cost, this.isUpgraded);
    }
}
