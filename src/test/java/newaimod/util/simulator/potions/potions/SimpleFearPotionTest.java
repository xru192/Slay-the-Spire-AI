package newaimod.util.simulator.potions.potions;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleFearPotionTest {
    @Test
    void testFearPotion() {
        SimpleMonster monster = new SimpleMonster(10, 0, AbstractMonster.Intent.DEFEND, 0, 0);
        SimpleFearPotion potion = new SimpleFearPotion();
        CombatSimulator state = new CombatSimulator()
                .withMonster(monster)
                .withPotion(potion, 0);
        state.usePotion(0, monster);
        assertEquals(3, monster.vulnerable);
    }
}