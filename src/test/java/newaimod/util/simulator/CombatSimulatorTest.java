package newaimod.util.simulator;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleCarnage;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleStrike_Red;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombatSimulatorTest {

    final int MONSTER_START_HEALTH = 50;

    CombatSimulator simulator;
    SimpleMonster monster;
    SimplePlayer player;

    @BeforeEach
    void setUp() {
        simulator = new CombatSimulator();
        player = simulator.player;
        monster = new SimpleMonster(MONSTER_START_HEALTH, 0, AbstractMonster.Intent.ATTACK, 1, 1);
        simulator.addMonster(monster);
    }

    @Test
    void testStrike() {
        AbstractSimpleCard card = new SimpleStrike_Red(simulator, 1, false);
        simulator.player.hand.add(card);
        simulator.playCard(card, monster);
        assertEquals(monster.health, MONSTER_START_HEALTH - 6);
        assertEquals(simulator.player.energy, 2);
    }

    @Test
    void testCarnage() {
        AbstractSimpleCard card = new SimpleCarnage(simulator, 2, false);
        simulator.player.hand.add(card);
        simulator.playCard(card, monster);
        assertEquals(monster.health, MONSTER_START_HEALTH - 20);
        assertEquals(simulator.player.energy, 1);
    }




}