package newaimod.util.Simulator;

import newaimod.NewAIMod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A CombatSimulator simulates a simplified version of Slay the Spire combat. An instance represents a state of combat,
 * with information about the player and monsters. It supports the playing of cards, which modifies the state.
 */
public class CombatSimulator {

    @NotNull
    private SimplePlayer player;
    private List<SimpleMonster> monsterList;

    /**
     * CombatSimulator which represents the current state of combat.
     */
    public CombatSimulator() {
        assert NewAIMod.inBattle;
        player = new SimplePlayer();
    }

    public void playCard(AbstractSimpleCard card) {

    }

}
