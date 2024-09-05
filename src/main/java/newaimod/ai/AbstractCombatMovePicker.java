package newaimod.ai;

import newaimod.ai.AutoPlayer.CombatMove;
import newaimod.util.dungeonInfo.DungeonInformationProvider;

/**
 * An AbstractCombatMovePicker implementation picks a move to be executed in any combat state, and against any group of
 * monsters (encounter). A general move-picking algorithm must be implemented. Optionally, specific move-picking
 * algorithms may be implemented for particular encounters.
 */
public abstract class AbstractCombatMovePicker {

    protected final DungeonInformationProvider dungeonInformationProvider;

    protected AbstractCombatMovePicker(DungeonInformationProvider dungeonInformationProvider) {
        this.dungeonInformationProvider = dungeonInformationProvider;
    }

    public final CombatMove pickMove() {
        DungeonInformationProvider.COMBAT_KEY key = dungeonInformationProvider.getCombatKey();
        switch (key) {
            case GREMLIN_NOB_COMBAT:
                return pickMoveGremlinNob();
            case LAGAVULIN_COMBAT:
                return pickMoveLagavulin();
            case THREE_SENTRY_COMBAT:
                return pickMove3Sentries();
            case GUARDIAN_COMBAT:
                return pickMoveTheGuardian();
            case HEXAGHOST_COMBAT:
                return pickMoveHexaghost();
            case SLIME_BOSS_COMBAT:
                return pickMoveSlimeBoss();
            case SPHERE_GUARDIAN_COMBAT:
                return pickMoveSphericGuardian();
            case SENTRY_SPHERE_COMBAT:
                return pickMoveSentrySphere();
            default:
                return pickMoveDefault();
        }
    }

    /**
     * Returns a move to be executed in the current combat state. This method will be called and the move returned will
     * be used for all encounters by default.
     *
     * @return move to be executed in the current combat state.
     */
    abstract protected CombatMove pickMoveDefault();

    /**
     * Returns a move to be executed in the current combat state versus a Gremlin Nob encounter. Can be overridden to
     * use a different move-picking algorithm instead of the default one.
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

    protected CombatMove pickMoveSphericGuardian() {
        return pickMoveDefault();
    }

    protected CombatMove pickMoveSentrySphere() {
        return pickMoveDefault();
    }


}
