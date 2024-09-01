package newaimod.util.simulator.cards.ironclad.attacks;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SimpleBashTest {
    final int MONSTER_START_HEALTH = 50;

    CombatSimulator state;
    SimpleMonster monster;

    SimpleBash bashCard;
    SimpleBash bashCardUpgraded;

    @BeforeEach
    void setUp() {
        monster = new SimpleMonster(MONSTER_START_HEALTH, 0, AbstractMonster.Intent.ATTACK, 1, 1);
        bashCard = new SimpleBash(null, 2, false);
        bashCardUpgraded = new SimpleBash(null, 2, true);
        state = new CombatSimulator()
                .withMonster(monster)
                .withPlayerEnergy(3)
                .withPlayerCard(bashCard)
                .withPlayerCard(bashCardUpgraded);
    }

    @Test
    void cannotPlayWhenEntangled() {
        state.player.setEntangled(true);
        assertFalse(bashCard.canPlay(monster));
        assertFalse(bashCardUpgraded.canPlay(monster));
    }

    @Test
    void playRegular() {
        state.playCard(bashCard, monster);
        assertEquals(MONSTER_START_HEALTH - 8, monster.health);
        assertEquals(2, monster.vulnerable);
        assertEquals(1, state.player.getEnergy());
    }

    @Test
    void playUpgraded() {
        state.playCard(bashCardUpgraded, monster);
        assertEquals(MONSTER_START_HEALTH - 10, monster.health);
        assertEquals(3, monster.vulnerable);
        assertEquals(1, state.player.getEnergy());
    }
}