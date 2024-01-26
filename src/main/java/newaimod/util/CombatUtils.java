package newaimod.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class CombatUtils {
    /**
     * Returns the alive monster with the lowest health left.
     *
     * @return the alive monster with the lowest health left, or null if none exists
     */
    public static AbstractMonster getWeakestTarget() {
        int minHealth = 1000;
        AbstractMonster weakest = null;
        try {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m.currentHealth > 0 && m.currentHealth < minHealth) {
                    minHealth = m.currentHealth;
                    weakest = m;
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
        return weakest;
    }

    public static SimpleMonster getWeakestTarget(CombatSimulator state) {
        int minHealth = 1000;
        SimpleMonster weakest = null;
        try {
            for (SimpleMonster m : state.monsterList) {
                if (m.health > 0 && m.health < minHealth) {
                    minHealth = m.health;
                    weakest = m;
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
        return weakest;
    }

    /**
     * Returns whether the specified intent does not include an attack.
     *
     * @param in the intent to check the type of
     * @return whether the intent does not include an attack
     */
    public static boolean isNotAttack(AbstractMonster.Intent in) {
        return in != AbstractMonster.Intent.ATTACK && in != AbstractMonster.Intent.ATTACK_BUFF
                && in != AbstractMonster.Intent.ATTACK_DEBUFF && in != AbstractMonster.Intent.ATTACK_DEFEND;
    }

    /**
     * Returns the amount of energy the player has left to use this turn.
     *
     * @return the amount of energy the player has left to use this turn.
     */
    public static int usableEnergy() {
        return EnergyPanel.totalCount;
    }

    /**
     * Returns the current amount of the specified power the specified creature has. Returns 0 if the creature does not
     * own the power.
     *
     * @param c  the creature to check the power of
     * @param id the ID of the power to check the amount of
     * @return the current amount of the specified power the specified creature
     */
    public static int amountOfPower(AbstractCreature c, String id) {
        return c.hasPower(id) ? c.getPower(id).amount : 0;
    }
}
