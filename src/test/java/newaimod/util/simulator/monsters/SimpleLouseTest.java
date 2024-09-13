package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleStrike_Red;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleTwinStrike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleLouseTest {

    CombatSimulator state;
    SimpleLouse louseWithCurlUp;
    SimpleLouse louseWithoutCurlUp;
    SimpleStrike_Red strike;
    SimpleTwinStrike twinStrike;

    @BeforeEach
    void setUp() {
        louseWithCurlUp = new SimpleLouse(10, 0, AbstractMonster.Intent.ATTACK, 5, 1, 5);
        louseWithoutCurlUp = new SimpleLouse(10, 2, AbstractMonster.Intent.ATTACK, 5, 1, 0);
        strike = new SimpleStrike_Red(null, 1, false);
        twinStrike = new SimpleTwinStrike(null, 1, false);
        state = new CombatSimulator()
                .withMonster(louseWithCurlUp)
                .withMonster(louseWithoutCurlUp)
                .withPlayerEnergy(3)
                .withPlayerCard(strike)
                .withPlayerCard(twinStrike);
    }

    @Test
    void curlUpShouldTrigger() {
        state.playCard(strike, louseWithCurlUp);
        assertEquals(4, louseWithCurlUp.health);
        assertEquals(5, louseWithCurlUp.block);
        assertEquals(0, louseWithCurlUp.getCurlUp());

        state.playCard(twinStrike, louseWithCurlUp);
        assertFalse(louseWithCurlUp.isAlive());
    }

    @Test
    void curlUpDoesNotStopKill() {
        state.playCard(twinStrike, louseWithCurlUp);
        assertFalse(louseWithCurlUp.isAlive());
    }

    @Test
    void noCurlUpHasNoEffect() {
        state.playCard(strike, louseWithoutCurlUp);
        assertEquals(6, louseWithoutCurlUp.health);
        assertEquals(0, louseWithoutCurlUp.block);
    }

    @Test
    void noCurlUpHasNoEffect2() {
        state.playCard(twinStrike, louseWithoutCurlUp);
        assertEquals(2, louseWithoutCurlUp.health);
        assertEquals(0, louseWithoutCurlUp.block);
    }
}