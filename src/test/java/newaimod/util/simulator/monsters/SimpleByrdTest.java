package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleStrike_Red;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleTwinStrike;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleWhirlwind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleByrdTest {
    final int INIT_HEALTH = 20;
    CombatSimulator state;
    SimpleByrd byrdWithFlight;
    SimpleByrd byrdWithoutFlight;

    SimpleStrike_Red strike;
    SimpleTwinStrike twinStrike;
    SimpleWhirlwind whirlwind;

    @BeforeEach
    void setUp() {
        byrdWithFlight = new SimpleByrd(INIT_HEALTH, 0, AbstractMonster.Intent.ATTACK, 1, 5, 3);
        byrdWithoutFlight = new SimpleByrd(INIT_HEALTH, 0, AbstractMonster.Intent.ATTACK, 1, 5, 0);
        strike = new SimpleStrike_Red(null, 1, false);
        twinStrike = new SimpleTwinStrike(null, 1, false);
        whirlwind = new SimpleWhirlwind(null, false);
        state = new CombatSimulator()
                .withMonster(byrdWithFlight)
                .withMonster(byrdWithoutFlight)
                .withPlayerEnergy(4)
                .withPlayerCard(strike)
                .withPlayerCard(twinStrike)
                .withPlayerCard(whirlwind);
    }

    @Test
    void flightTriggers() {
        state.playCard(strike, byrdWithFlight);
        assertEquals(INIT_HEALTH - 3, byrdWithFlight.health);
        assertEquals(2, byrdWithFlight.getFlight());
    }

    @Test
    void flightTriggersMultiAttack() {
        state.playCard(twinStrike, byrdWithFlight);
        assertEquals(INIT_HEALTH - 4, byrdWithFlight.health);
        assertEquals(1, byrdWithFlight.getFlight());
    }

    @Test
    void flightRemoved() {
        state.playCard(strike, byrdWithFlight);
        state.playCard(twinStrike, byrdWithFlight);
        state.playCard(whirlwind, null);
        assertEquals(INIT_HEALTH - 3 - 2 - 2 - 5 - 5, byrdWithFlight.health);
        assertEquals(0, byrdWithFlight.getFlight());
        assertFalse(byrdWithFlight.isAttacking());
    }

    @Test
    void flightRemovedAfterMultiAttack() {
        state.playCard(strike, byrdWithFlight);
        state.playCard(whirlwind, null);
        assertEquals(INIT_HEALTH - 3 - 2 - 2 - 2, byrdWithFlight.health);
        assertEquals(INIT_HEALTH - 5 - 5 - 5, byrdWithoutFlight.health);
        assertEquals(0, byrdWithFlight.getFlight());
        assertFalse(byrdWithFlight.isAttacking());
    }

    @Test
    void noFlightHasNoEffect() {
        state.playCard(strike, byrdWithoutFlight);
        assertEquals(INIT_HEALTH - 6, byrdWithoutFlight.health);
        assertEquals(0, byrdWithoutFlight.getFlight());
    }

    @Test
    void noFlightHasNoEffect2() {
        state.playCard(twinStrike, byrdWithoutFlight);
        assertEquals(INIT_HEALTH - 10, byrdWithoutFlight.health);
        assertEquals(0, byrdWithoutFlight.getFlight());
        assertTrue(byrdWithoutFlight.isAttacking());
    }
}