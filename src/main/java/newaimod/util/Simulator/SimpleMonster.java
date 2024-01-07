package newaimod.util.Simulator;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import newaimod.util.CombatUtils;

import static newaimod.util.CombatUtils.amountOfPower;

/**
 * A SimpleMonster represents a simplified version of the state of a monster during combat. For now, all monsters are
 * represented by SimpleMonster, but in the future specific monsters may be subclasses of SimpleMonster.
 */
public class SimpleMonster {
    public CombatSimulator simulator;

    public int health;
    int block;
    public AbstractMonster originalMonster = null; // reference to monster in combat this is representing
    AbstractMonster.Intent intent;
    int intentDamage;   // damage of intended attack, -1 if not attacking
    int intentHits;     // number of intended hits, -1 if not attacking

    int vulnerable;

    /**
     * SimpleMonster which represents the current state of the specified AbstractMonster in combat.
     */
    public SimpleMonster(AbstractMonster monster, CombatSimulator simulator) {
        originalMonster = monster;
        this.health = monster.escaped ? 0 : monster.currentHealth;
        this.block = monster.currentBlock;
        EnemyMoveInfo moveInfo = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");
        intent = moveInfo.intent;
        intentDamage = monster.getIntentDmg();
        intentHits = Math.max(1, moveInfo.multiplier);
        vulnerable = amountOfPower(monster, VulnerablePower.POWER_ID);
//        strength = amountOfPower(monster, StrengthPower.POWER_ID);
//        weakened = monster.hasPower(WeakPower.POWER_ID);
    }

    public SimpleMonster(SimpleMonster m, CombatSimulator simulator) {
        this.simulator = simulator;
        this.originalMonster = m.originalMonster;
        this.health = m.health;
        this.block = m.block;
        this.intent = m.intent;
        this.intentDamage = m.intentDamage;
        this.intentHits = m.intentHits;
        this.vulnerable = m.vulnerable;
    }

    /**
     * Have this monster take an attack from the player.
     *
     * @param damage the damage of the attack (after player-side modifications)
     */
    public void takeAttack(int damage) {
        assert damage >= 0;
        if (vulnerable > 0) {
            // TODO phrog
            damage = damage + damage / 2;
        }

        int blockLoss = Math.min(block, damage);
        int healthLoss = Math.min(health, Math.max(0, damage - block));
        block -= blockLoss;
        health -= healthLoss;
    }

    public void takeMultiAttack(int damage, int multiAmt) {
        assert damage >= 0;
        assert multiAmt >= 2;
        if (vulnerable > 0) {
            // TODO phrog
            damage = damage + damage / 2;
        }

        for (int i = 0; i < multiAmt; ++i) {
            int blockLoss = Math.min(block, damage);
            int healthLoss = Math.min(health, Math.max(0, damage - block));
            block -= blockLoss;
            health -= healthLoss;
        }
    }

    public void takeVulnerable(int amount) {
        assert amount > 0;
        vulnerable += amount;

    }

    /**
     * Returns whether this monster is attacking the player this turn. A monster must be alive to be attacking.
     * @return whether this monster will attack the player this turn
     */
    public boolean isAttacking() {
        return isAlive() && !CombatUtils.isNotAttack(intent);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isTargetable() {
        return health > 0;
    }

    @Override
    public String toString() {
        return "SimpleMonster{" +
                "health=" + health +
                ", block=" + block +
                ", intent=" + intent +
                ", intentDamage=" + intentDamage +
                ", intentHits=" + intentHits +
                '}';
    }
}
