package newaimod.util.simulator;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import newaimod.NewAIMod;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static newaimod.util.CombatUtils.amountOfPower;
import static newaimod.util.CombatUtils.isNotAttack;

/**
 * A SimpleMonster represents a simplified version of the state of a monster during combat. For now, all monsters are
 * represented by SimpleMonster, but in the future specific monsters may be subclasses of SimpleMonster.
 */
public class SimpleMonster {
    public static final Logger logger = LogManager.getLogger(SimpleMonster.class.getName());
    public CombatSimulator simulator;

    public int maxHealth;
    public int health;
    public int block;
    public final AbstractMonster originalMonster; // reference to monster in combat this is representing, null if N/A (testing)
    protected AbstractMonster.Intent intent;
    protected int intentBaseDamage; // base damage of intended attack, -1 if not attacking
    protected int intentHits;     // number of intended hits, -1 if not attacking

    public int vulnerable;
    public int weak;
    protected int strength;

    /**
     * SimpleMonster which represents the current state of the specified AbstractMonster in combat.
     */
    public SimpleMonster(AbstractMonster monster) {
        assert NewAIMod.inBattle;
        originalMonster = monster;
        this.maxHealth = monster.maxHealth;
        this.health = monster.escaped ? 0 : monster.currentHealth;
        this.block = monster.currentBlock;
        EnemyMoveInfo moveInfo = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");
        intent = moveInfo.intent;
        intentBaseDamage = moveInfo.baseDamage;
        intentHits = Math.max(1, moveInfo.multiplier);
        vulnerable = amountOfPower(monster, VulnerablePower.POWER_ID);
        weak = amountOfPower(monster, WeakPower.POWER_ID);
        strength = amountOfPower(monster, StrengthPower.POWER_ID);
    }

    public SimpleMonster(SimpleMonster m, CombatSimulator simulator) {
        this.simulator = simulator;
        this.originalMonster = m.originalMonster;
        this.maxHealth = m.maxHealth;
        this.health = m.health;
        this.block = m.block;
        this.intent = m.intent;
        this.intentBaseDamage = m.intentBaseDamage;
        this.intentHits = m.intentHits;
        this.vulnerable = m.vulnerable;
        this.weak = m.weak;
        this.strength = m.strength;
    }

    public SimpleMonster(int health, int block, AbstractMonster.Intent intent, int baseDamage, int hits) {
        originalMonster = null;
        this.maxHealth = health;
        this.health = health;
        this.block = block;
        this.intent = intent;
        this.intentBaseDamage = isNotAttack(intent) ? -1 : baseDamage;
        this.intentHits = isNotAttack(intent) ? -1 : hits;
    }

    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleMonster(this, simulator);
    }

    /**
     * Returns how much damage this monster will do (per hit) after monster modifications. These include strength and
     * weakened. This does not include when the player is vulnerable.
     *
     * @return the damage after player modifications
     */
    public int getModifiedDamage() {
        if (!isAttacking()) {
            return 0;
        }
        if (weak != 0) {
            logger.info("");
        }

        double weakFactor = weak == 0 ? 1 : 0.75;
        int result = (int) ((intentBaseDamage + strength) * weakFactor);
        return Math.max(0, result);
    }

    public void loseHealth(int amount) {
        if (amount <= 0) {
            return;
        }
        health -= amount;
        health = Math.max(0, health);
        onLoseHealth();
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
        loseHealth(healthLoss);
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
            loseHealth(healthLoss);
        }
    }

    /**
     * This method will be called after the player plays a card. Example usage: Gremlin Nob's anger, Time Eater's time warp, The Guardian's sharp hide.
     */
    protected void onUseCard(AbstractSimpleCard card) {
    }

    /**
     * This method will be called after this monster loses health due to any source. Example usage: Slime splitting, Writhing Mass.
     */
    protected void onLoseHealth() {
    }

    public void takeVulnerable(int amount) {
        assert amount > 0;
        vulnerable += amount;
    }

    public void takeWeak(int amount) {
        assert amount > 0;
        weak += amount;
    }

    /**
     * Returns whether this monster is attacking the player this turn. A monster must be alive to be attacking.
     *
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
                "health=" + health + "/" + maxHealth +
                ", block=" + block +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                '}';
    }
}
