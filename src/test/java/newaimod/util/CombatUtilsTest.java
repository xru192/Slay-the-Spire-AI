package newaimod.util;

import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleBash;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleStrike_Red;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleTwinStrike;
import newaimod.util.simulator.cards.ironclad.skills.SimpleDefend_Red;
import org.junit.jupiter.api.Test;

import static newaimod.util.CombatUtils.handsEqual;
import static org.junit.jupiter.api.Assertions.*;

class CombatUtilsTest {

    @Test
    void handsEqualWithNoDuplicates() {
        CombatSimulator state1 = new CombatSimulator()
                .withPlayerCard(new SimpleDefend_Red(null, 1, false))
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleBash(null, 1, false))
                .withPlayerCard(new SimpleTwinStrike(null, 1, false));
        CombatSimulator state2 = new CombatSimulator()
                .withPlayerCard(new SimpleBash(null, 1, false))
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleDefend_Red(null, 1, false))
                .withPlayerCard(new SimpleTwinStrike(null, 1, false));

        assertTrue(handsEqual(state1.player.hand, state2.player.hand));
    }

    @Test
    void handsEqualWithDuplicates() {
        CombatSimulator state1 = new CombatSimulator()
                .withPlayerCard(new SimpleDefend_Red(null, 1, true))
                .withPlayerCard(new SimpleDefend_Red(null, 1, false))
                .withPlayerCard(new SimpleDefend_Red(null, 1, false))
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleStrike_Red(null, 1, false));
        CombatSimulator state2 = new CombatSimulator()
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleDefend_Red(null, 1, false))
                .withPlayerCard(new SimpleDefend_Red(null, 1, false))
                .withPlayerCard(new SimpleDefend_Red(null, 1, true));

        assertTrue(handsEqual(state1.player.hand, state2.player.hand));
    }

    @Test
    void handsNotEqualWithCostDifferences() {
        CombatSimulator state1 = new CombatSimulator()
                .withPlayerCard(new SimpleDefend_Red(null, 0, false))
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleBash(null, 1, false))
                .withPlayerCard(new SimpleTwinStrike(null, 1, false));
        CombatSimulator state2 = new CombatSimulator()
                .withPlayerCard(new SimpleBash(null, 1, false))
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleDefend_Red(null, 1, false))
                .withPlayerCard(new SimpleTwinStrike(null, 1, false));

        assertFalse(handsEqual(state1.player.hand, state2.player.hand));
    }
}