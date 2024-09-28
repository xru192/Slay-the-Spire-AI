package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

import java.util.Objects;

public class TemplateMonster extends SimpleMonster {

    public TemplateMonster(AbstractMonster monster) { // TODO
        super(monster);
        // TODO
    }

    TemplateMonster(TemplateMonster m, CombatSimulator simulator) {
        super(m, simulator);
        // TODO
    }

    public TemplateMonster(int health, int block, AbstractMonster.Intent intent, int baseDamage, int hits) { // TODO
        super(health, block, intent, baseDamage, hits);
        // TODO
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new TemplateMonster(this, simulator);
    }

    // TODO other methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        // TODO
        TemplateMonster that = (TemplateMonster) o;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()); // TODO
    }

    @Override
    public String toString() {
        // TODO
        return "TemplateMonster{" +
                "health=" + health +
                ", block=" + block +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                '}';
    }
}
