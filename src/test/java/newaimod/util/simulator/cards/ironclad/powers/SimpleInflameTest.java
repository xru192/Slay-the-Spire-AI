package newaimod.util.simulator.cards.ironclad.powers;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleInflameTest {
    CombatSimulator state;

    SimpleInflame inflameCard;
    SimpleInflame inflameCardUpgraded;

    @BeforeEach
    void setUp() {
        inflameCard = new SimpleInflame(null, 1, false);
        inflameCardUpgraded = new SimpleInflame(null, 1, true);
        state = new CombatSimulator()
                .withMonster(new SimpleMonster(50, 0, AbstractMonster.Intent.ATTACK, 1, 1))
                .withPlayerEnergy(3)
                .withPlayerCard(inflameCard)
                .withPlayerCard(inflameCardUpgraded);
    }

    @Test
    void cannotPlayWithLowEnergy() {
        state.player.setEnergy(0);
        assertFalse(inflameCard.canPlay(null));
        assertFalse(inflameCardUpgraded.canPlay(null));
    }

    @Test
    void playRegular() {
        state.playCard(inflameCard, null);
        assertEquals(2, state.player.getStrength());
        assertEquals(2, state.player.getEnergy());
    }

    @Test
    void playUpgraded() {
        state.playCard(inflameCardUpgraded, null);
        assertEquals(3, state.player.getStrength());
        assertEquals(2, state.player.getEnergy());
    }
}