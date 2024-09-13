package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.CurlUpPower;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

import java.util.Objects;

public class SimpleLouse extends SimpleMonster {
    private int curlUp;

    public SimpleLouse(AbstractMonster monster) {
        super(monster);
        curlUp = CombatUtils.amountOfPower(monster, CurlUpPower.POWER_ID);
    }

    SimpleLouse(SimpleLouse m, CombatSimulator simulator) {
        super(m, simulator);
        curlUp = m.curlUp;
    }

    public SimpleLouse(int health, int block, AbstractMonster.Intent intent, int baseDamage, int hits, int curlUp) {
        super(health, block, intent, baseDamage, hits);
        this.curlUp = curlUp;
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleLouse(this, simulator);
    }

    @Override
    public void takeAttack(int damage) {
        super.takeAttack(damage);
        if (this.isAlive() && curlUp > 0) {
            block += curlUp;
            curlUp = 0;
        }
    }

    public int getCurlUp() {
        return curlUp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimpleLouse that = (SimpleLouse) o;
        return curlUp == that.curlUp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), curlUp);
    }

    @Override
    public String toString() {
        return "Louse{" +
                "health=" + health +
                ", block=" + block +
                ", curlUp=" + curlUp +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                '}';
    }
}
