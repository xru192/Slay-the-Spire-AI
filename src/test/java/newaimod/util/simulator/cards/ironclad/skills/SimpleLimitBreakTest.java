package newaimod.util.simulator.cards.ironclad.skills;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleLimitBreakTest {
    CombatSimulator state;

    SimpleLimitBreak limitBreak;
    SimpleLimitBreak limitBreakUpgraded;

    @BeforeEach
    void setUp() {
        limitBreak = new SimpleLimitBreak(null, 1, false);
        limitBreakUpgraded = new SimpleLimitBreak(null, 1, true);
        state = new CombatSimulator()
                .withMonster(new SimpleMonster(50, 0, AbstractMonster.Intent.ATTACK, 1, 1))
                .withPlayerEnergy(3)
                .withPlayerCard(limitBreak)
                .withPlayerCard(limitBreakUpgraded);
    }

    @Test
    void playRegular() {
        state = state.withPlayerStrength(5);
        state.playCard(limitBreak, null);
        assertEquals(10, state.player.getStrength());
        assertEquals(2, state.player.getEnergy());
    }

    @Test
    void playUpgraded() {
        state = state.withPlayerStrength(5);
        state.playCard(limitBreakUpgraded, null);
        assertEquals(10, state.player.getStrength());
        assertEquals(2, state.player.getEnergy());
    }
}