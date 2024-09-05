package newaimod.util.simulator.cards.ironclad.skills;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleDefendRedTest {
    CombatSimulator state;

    SimpleDefend_Red defendCard;
    SimpleDefend_Red defendCardUpgraded;

    @BeforeEach
    void setUp() {
        defendCard = new SimpleDefend_Red(null, 1, false);
        defendCardUpgraded = new SimpleDefend_Red(null, 1, true);
        state = new CombatSimulator()
                .withMonster(new SimpleMonster(50, 0, AbstractMonster.Intent.ATTACK, 1, 1))
                .withPlayerEnergy(3)
                .withPlayerCard(defendCard)
                .withPlayerCard(defendCardUpgraded);
    }

    @Test
    void cannotPlayWithLowEnergy() {
        state.player.setEnergy(0);
        assertFalse(defendCard.canPlay(null));
        assertFalse(defendCardUpgraded.canPlay(null));
    }

    @Test
    void playRegular() {
        state.playCard(defendCard, null);
        assertEquals(5, state.player.getBlock());
        assertEquals(2, state.player.getEnergy());
    }

    @Test
    void playUpgraded() {
        state.playCard(defendCardUpgraded, null);
        assertEquals(8, state.player.getBlock());
        assertEquals(2, state.player.getEnergy());
    }
}