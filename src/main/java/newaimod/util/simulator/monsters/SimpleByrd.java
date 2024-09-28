package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlightPower;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

import java.util.Objects;

public class SimpleByrd extends SimpleMonster {
    private int flight;

    public SimpleByrd(AbstractMonster monster) {
        super(monster);
        flight = CombatUtils.amountOfPower(monster, FlightPower.POWER_ID);
    }

    SimpleByrd(SimpleByrd m, CombatSimulator simulator) {
        super(m, simulator);
        flight = m.flight;
    }

    public SimpleByrd(int health, int block, AbstractMonster.Intent intent, int baseDamage, int hits, int flight) {
        super(health, block, intent, baseDamage, hits);
        this.flight = flight;
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleByrd(this, simulator);
    }

    public int getFlight() {
        return flight;
    }

    @Override
    public void takeAttack(int damage) {
        int afterFlightDamage = flight > 0 ? damage / 2 : damage;
        super.takeAttack(afterFlightDamage);
        if (flight == 1) {
            intent = AbstractMonster.Intent.STUN;
            intentHits = -1;
            intentBaseDamage = -1;
        }
        flight = Math.max(0, flight - 1);
    }

    @Override
    public void takeMultiAttack(int damage, int multiAmt) {
        int afterFlightDamage = flight > 0 ? damage / 2 : damage;
        super.takeMultiAttack(afterFlightDamage, multiAmt);
        if (0 < flight && flight <= multiAmt) {
            intent = AbstractMonster.Intent.STUN;
            intentHits = -1;
            intentBaseDamage = -1;
        }
        flight = Math.max(0, flight - multiAmt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimpleByrd that = (SimpleByrd) o;
        return flight == that.flight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flight);
    }

    @Override
    public String toString() {
        return "Byrd{" +
                "health=" + health +
                ", block=" + block +
                ", flight=" + flight +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                '}';
    }
}
