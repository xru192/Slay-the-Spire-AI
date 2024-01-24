package newaimod.util.simulator;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.cards.ironclad.attacks.*;
import newaimod.util.simulator.cards.ironclad.powers.SimpleDemonForm;
import newaimod.util.simulator.cards.ironclad.powers.SimpleInflame;
import newaimod.util.simulator.cards.ironclad.powers.SimpleMetallicize;
import newaimod.util.simulator.cards.ironclad.skills.SimpleBattleTrance;
import newaimod.util.simulator.cards.ironclad.skills.SimpleDefend_Red;
import newaimod.util.simulator.cards.ironclad.skills.SimpleFlameBarrier;
import newaimod.util.simulator.cards.ironclad.skills.SimpleShrugItOff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatSimulatorTest {

    final int MONSTER_START_HEALTH = 50;


    CombatSimulator simulator;
    SimpleMonster monster1;
    SimpleMonster monster2;
    SimplePlayer player;

    @BeforeEach
    void setUp() {
        simulator = new CombatSimulator();
        player = simulator.player;
        monster1 = new SimpleMonster(MONSTER_START_HEALTH, 0, AbstractMonster.Intent.ATTACK, 1, 1);
        monster2 = new SimpleMonster(MONSTER_START_HEALTH, 0, AbstractMonster.Intent.ATTACK, 1, 1);
        simulator.addMonster(monster1);
        simulator.addMonster(monster2);
    }

    @Test
    void testBash() {
        AbstractSimpleCard card = new SimpleBash(simulator, 2, false);
        player.hand.add(card);
        simulator.playCard(card, monster1);
        assertEquals(MONSTER_START_HEALTH - 8, monster1.health);
        assertEquals(1, player.energy);
        assertEquals(2, monster1.vulnerable);
    }

    @Test
    void testBodySlam() {
        AbstractSimpleCard defend = new SimpleDefend_Red(simulator, 1, false);
        AbstractSimpleCard bodySlam = new SimpleBodySlam(simulator, 1, false);
        player.hand.add(defend);
        player.hand.add(bodySlam);

        simulator.playCard(defend, null);
        simulator.playCard(bodySlam, monster1);
        assertEquals(MONSTER_START_HEALTH - 5, monster1.health);
        assertEquals(1, player.energy);
    }

    @Test
    void testCarnage() {
        AbstractSimpleCard card = new SimpleCarnage(simulator, 2, false);
        player.hand.add(card);
        simulator.playCard(card, monster1);
        assertEquals(MONSTER_START_HEALTH - 20, monster1.health);
        assertEquals(1, player.energy);
    }

    @Test
    void testCleave() {
        AbstractSimpleCard card = new SimpleCleave(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(MONSTER_START_HEALTH - 8, monster1.health);
        assertEquals(MONSTER_START_HEALTH - 8, monster2.health);
        assertEquals(2, player.energy);
    }

    @Test
    void testHeadbutt() {
        AbstractSimpleCard card = new SimpleHeadbutt(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, monster2);
        assertEquals(MONSTER_START_HEALTH - 9, monster2.health);
        assertEquals(2, player.energy);
    }

    @Test
    void testIronWave() {
        AbstractSimpleCard card = new SimpleIronWave(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, monster1);
        assertEquals(MONSTER_START_HEALTH - 5, monster1.health);
        assertEquals(5, player.block);
        assertEquals(2, player.energy);
    }

    @Test
    void testPommelStrike() {
        AbstractSimpleCard card = new SimplePommelStrike(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, monster1);
        assertEquals(MONSTER_START_HEALTH - 9, monster1.health);
        assertEquals(1, player.cardsDrawnWith0Energy);
        assertEquals(1, player.cardsDrawnWith1Energy);
        assertEquals(1, player.cardsDrawnWith2Energy);
        assertEquals(2, player.energy);
    }

    @Test
    void testStrike() {
        AbstractSimpleCard card = new SimpleStrike_Red(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, monster1);
        assertEquals(MONSTER_START_HEALTH - 6, monster1.health);
        assertEquals(2, player.energy);
    }

    @Test
    void testTwinStrike() {
        AbstractSimpleCard card = new SimpleTwinStrike(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, monster1);
        assertEquals(MONSTER_START_HEALTH - 10, monster1.health);
        assertEquals(2, player.energy);
    }

    @Test
    void testUppercut() {
        AbstractSimpleCard card = new SimpleUppercut(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, monster1);
        assertEquals(MONSTER_START_HEALTH - 13, monster1.health);
        assertEquals(1, monster1.vulnerable);
        assertEquals(1, monster1.weak);
        assertEquals(2, player.energy);
    }

    @Test
    void testWhirlwind() {
        AbstractSimpleCard card = new SimpleWhirlwind(simulator, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(MONSTER_START_HEALTH - 15, monster1.health);
        assertEquals(MONSTER_START_HEALTH - 15, monster2.health);
        assertEquals(0, player.energy);
    }

    @Test
    void testDemonForm() {
        AbstractSimpleCard card = new SimpleDemonForm(simulator, 3, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(2, player.demonForm);
        assertEquals(0, player.energy);
    }

    @Test
    void testInflame() {
        AbstractSimpleCard card = new SimpleInflame(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(2, player.strength);
        assertEquals(2, player.energy);
    }

    @Test
    void testMetallicize() {
        AbstractSimpleCard card = new SimpleMetallicize(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(3, player.metallicize);
        assertEquals(2, player.energy);
    }

    @Test
    void testBattleTrance() {
        AbstractSimpleCard card = new SimpleBattleTrance(simulator, 0, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(3, player.cardsDrawnWith0Energy);
        assertEquals(3, player.cardsDrawnWith1Energy);
        assertEquals(3, player.cardsDrawnWith2Energy);
        assertEquals(3, player.cardsDrawnWith3Energy);
        assertTrue(player.noDraw);
        assertEquals(3, player.energy);
    }

    @Test
    void testDefend() {
        AbstractSimpleCard card = new SimpleDefend_Red(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(5, player.block);
        assertEquals(2, player.energy);
    }

    @Test
    void testFlameBarrier() {
        AbstractSimpleCard card = new SimpleFlameBarrier(simulator, 2, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(12, player.block);
        assertEquals(1, player.energy);
    }

    @Test
    void testShrugItOff() {
        AbstractSimpleCard card = new SimpleShrugItOff(simulator, 1, false);
        player.hand.add(card);
        simulator.playCard(card, null);
        assertEquals(8, player.block);
        assertEquals(1, player.cardsDrawnWith0Energy);
        assertEquals(1, player.cardsDrawnWith1Energy);
        assertEquals(1, player.cardsDrawnWith2Energy);
        assertEquals(2, player.energy);
    }
}