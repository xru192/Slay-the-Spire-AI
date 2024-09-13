package newaimod.util.simulator.monsters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

import java.util.Objects;

public class SimpleLagavulin extends SimpleMonster {
    public enum MODE {
        SLEEP_ONE,  // in first turn of sleep, not damaged this turn
        SLEEP_TWO, // in second turn of sleep, not damaged this turn
        SLEEP_THREE, // in third turn of sleep, not damaged this turn (will wake up next turn)
        AWAKE // damaged this turn, or not in sleep (will be targeting player next turn)
    }

    private MODE mode;
    private final boolean wasAsleep;

    public SimpleLagavulin(Lagavulin monster) {
        super(monster);
        wasAsleep = ReflectionHacks.getPrivate(monster, Lagavulin.class, "asleep");
        boolean isOut = ReflectionHacks.getPrivate(monster, Lagavulin.class, "isOutTriggered");
        if (!wasAsleep || isOut) {
            mode = MODE.AWAKE;
        } else {
            int idleCount = ReflectionHacks.getPrivate(monster, Lagavulin.class, "idleCount");
            switch (idleCount) {
                case 0:
                    mode = MODE.SLEEP_ONE;
                    break;
                case 1:
                    mode = MODE.SLEEP_TWO;
                    break;
                case 2:
                    mode = MODE.SLEEP_THREE;
                    break;
                default:
                    mode = MODE.AWAKE;
                    logger.error("Unexpected");
            }
        }
    }

    SimpleLagavulin(SimpleLagavulin m, CombatSimulator simulator) {
        super(m, simulator);
        this.mode = m.mode;
        this.wasAsleep = m.wasAsleep;
    }

    public MODE getMode() {
        return mode;
    }

    @Override
    protected void onLoseHealth() {
        mode = MODE.AWAKE;
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleLagavulin(this, simulator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimpleLagavulin that = (SimpleLagavulin) o;
        return mode == that.mode && wasAsleep == that.wasAsleep;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mode, wasAsleep);
    }

    @Override
    public String toString() {
        return "Lagavulin{" +
                "State: " + mode +
                "health=" + health +
                ", block=" + block +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                '}';
    }

}
