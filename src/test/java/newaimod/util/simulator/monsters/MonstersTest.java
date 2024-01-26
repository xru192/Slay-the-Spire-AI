package newaimod.util.simulator.monsters;

import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimplePlayer;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleBash;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleStrike_Red;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonstersTest {

    CombatSimulator simulator;
    SimplePlayer player;

    @BeforeEach
    void setUp() {
        simulator = new CombatSimulator();
        player = simulator.player;
    }

    @Test
    void testSentryArtifact() {
        SimpleSentry sentry = new SimpleSentry(40, 1, true);
        simulator.addMonster(sentry);
        SimpleBash bash = new SimpleBash(simulator, 2, false);
        SimpleStrike_Red strike = new SimpleStrike_Red(simulator, 1, false);
        player.hand.add(bash);
        player.hand.add(strike);
        simulator.playCard(bash, sentry);
        simulator.playCard(strike, sentry);
        assertEquals(0, sentry.getArtifact());
        assertEquals(0, sentry.vulnerable);
        assertEquals(40 - 8 - 6, sentry.health);
    }
}