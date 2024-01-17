package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.powers.AngerPower;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.cards.AbstractSimpleCard;

public class SimpleGremlinNob extends SimpleMonster {
    private final int anger;

    public SimpleGremlinNob(GremlinNob monster, CombatSimulator simulator) {
        super(monster, simulator);
        this.simulator = simulator;
        this.anger = CombatUtils.amountOfPower(monster, AngerPower.POWER_ID);
    }

    SimpleGremlinNob(SimpleGremlinNob m, CombatSimulator simulator) {
        super(m, simulator);
        this.anger = m.anger;
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleGremlinNob(this, simulator);
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public void onUseCard(AbstractSimpleCard card) {
        super.onUseCard(card);
        if (card.type == AbstractCard.CardType.SKILL) {
            this.strength += anger;
        }
    }

    @Override
    public String toString() {
        return "GremlinNob{" +
                "health=" + health +
                ", block=" + block +
                ", anger=" + anger +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentDamage=" + intentDamage +
                ", intentHits=" + intentHits +
                '}';
    }
}
