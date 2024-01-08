package newaimod.AI;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import newaimod.AI.AutoPlayer.CombatMove;

/**
 * An AbstractCombatMovePicker implementation picks a move to be executed in any combat state, and against any group of
 * monsters (encounter). A general move-picking algorithm must be implemented. Optionally, specific move-picking
 * algorithms may be implemented for particular encounters.
 */
public abstract class AbstractCombatMovePicker {

    public final CombatMove pickMove() {
        String key = AbstractDungeon.lastCombatMetricKey;
        switch (key) {
            case MonsterHelper.GREMLIN_NOB_ENC:
                return pickMoveGremlinNob();
            case MonsterHelper.LAGAVULIN_ENC:
                return pickMoveLagavulin();
            case MonsterHelper.THREE_SENTRY_ENC:
                return pickMove3Sentries();
            case MonsterHelper.GUARDIAN_ENC:
                return pickMoveTheGuardian();
            case MonsterHelper.HEXAGHOST_ENC:
                return pickMoveHexaghost();
            case MonsterHelper.SLIME_BOSS_ENC:
                return pickMoveSlimeBoss();
            default:
                return pickMoveDefault();
        }
    }

    /**
     * Returns a move to be executed in the current combat state.
     * This method will be called and the move returned will be used for all encounters by default.
     *
     * @return move to be executed in the current combat state.
     */
    abstract public CombatMove pickMoveDefault();

    /**
     * Returns a move to be executed in the current combat state versus a Gremlin Nob encounter.
     * Can be overridden to use a different move-picking algorithm instead of the default one.
     */
    protected CombatMove pickMoveGremlinNob() {
        return pickMoveDefault();
    }

    protected CombatMove pickMoveLagavulin() {
        return pickMoveDefault();
    }

    protected CombatMove pickMove3Sentries() {
        return pickMoveDefault();
    }

    protected CombatMove pickMoveTheGuardian() {
        return pickMoveDefault();
    }

    protected CombatMove pickMoveHexaghost() {
        return pickMoveDefault();
    }

    protected CombatMove pickMoveSlimeBoss() {
        return pickMoveDefault();
    }


}
