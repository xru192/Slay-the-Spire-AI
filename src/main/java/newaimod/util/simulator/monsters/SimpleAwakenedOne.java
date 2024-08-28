package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.powers.UnawakenedPower;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleAwakenedOne extends SimpleMonster {

    private final boolean unawakened;
    private boolean trulyDead;

    public SimpleAwakenedOne(AwakenedOne monster) {
        super(monster);
        unawakened = monster.hasPower(UnawakenedPower.POWER_ID);
        trulyDead = !unawakened && health <= 0;
    }

    SimpleAwakenedOne(SimpleAwakenedOne m, CombatSimulator simulator) {
        super(m, simulator);
        unawakened = m.unawakened;
        trulyDead = m.trulyDead;
    }

    public boolean isTrulyDead() {
        return trulyDead;
    }

    @Override
    protected void onLoseHealth() {
        super.onLoseHealth();
        trulyDead = !unawakened && health <= 0;
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleAwakenedOne(this, simulator);
    }

    @Override
    public String toString() {
        return "AwakenedOne{" +
                "health=" + health +
                ", block=" + block +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                ", unawakened=" + unawakened +
                '}';
    }
}
