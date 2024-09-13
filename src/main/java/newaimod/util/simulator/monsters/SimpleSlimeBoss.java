package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

import java.util.Objects;

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

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleSlimeBoss(this, simulator);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimpleSlimeBoss that = (SimpleSlimeBoss) o;
        return splitting = that.splitting;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), splitting);
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
