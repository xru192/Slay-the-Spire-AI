package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleSlimeBoss extends SimpleMonster {

    private boolean splitting;

    public SimpleSlimeBoss(SlimeBoss monster) {
        super(monster);
        splitting = health <= maxHealth / 2.0;
    }

    SimpleSlimeBoss(SimpleSlimeBoss m, CombatSimulator simulator) {
        super(m, simulator);
        this.splitting = m.splitting;
    }

    public boolean isSplitting() {
        return splitting;
    }

    @Override
    protected void onLoseHealth() {
        if (health <= maxHealth / 2.0) {
            splitting = true;
            intent = AbstractMonster.Intent.UNKNOWN;
            intentBaseDamage = -1;
            intentHits = -1;
        }
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleSlimeBoss(this, simulator);
    }

    @Override
    public String toString() {
        return "SlimeBoss{" +
                "health=" + health +
                ", block=" + block +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                '}';
    }
}
