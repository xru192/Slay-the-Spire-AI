package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class SimpleSentry extends SimpleMonster {

    private int artifact;

    public SimpleSentry(Sentry monster) {
        super(monster);
        artifact = CombatUtils.amountOfPower(monster, ArtifactPower.POWER_ID);
    }

    public SimpleSentry(int health, int artifact, boolean attacking) {
        super(health, 0, AbstractMonster.Intent.DEBUFF, -1, -1);
        if (attacking) {
            this.intent = AbstractMonster.Intent.ATTACK;
            this.intentBaseDamage = 9;
            this.intentHits = 1;
        }
        this.artifact = artifact;
    }

    SimpleSentry(SimpleSentry m, CombatSimulator simulator) {
        super(m, simulator);
        artifact = m.artifact;
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleSentry(this, simulator);
    }

    @Override
    public void takeVulnerable(int amount) {
        if (artifact == 0) {
            super.takeVulnerable(amount);
        } else {
            artifact -= 1;
        }
    }

    @Override
    public void takeWeak(int amount) {
        if (artifact == 0) {
            super.takeWeak(amount);
        } else {
            artifact -= 1;
        }
    }

    public int getArtifact() {
        return artifact;
    }

    @Override
    public String toString() {
        return "Sentry{" +
                "health=" + health +
                ", block=" + block +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                '}';
    }
}
