package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class TemplateMonster extends SimpleMonster {

    public TemplateMonster(AbstractMonster monster) { // TODO
        super(monster);
        // TODO
    }

    TemplateMonster(TemplateMonster m, CombatSimulator simulator) {
        super(m, simulator);
        // TODO
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new TemplateMonster(this, simulator);
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
