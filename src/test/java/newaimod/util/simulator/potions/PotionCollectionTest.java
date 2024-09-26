package newaimod.util.simulator.potions;

import newaimod.util.simulator.potions.potions.SimpleFearPotion;
import newaimod.util.simulator.potions.potions.SimpleWeakPotion;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class PotionCollectionTest {

    @Test
    void testEqualPotionCollections() {
        PotionCollection pc1 = new PotionCollection(null, false, false);
        PotionCollection pc2 = new PotionCollection(null, false, false);
        pc1.addPotion(new SimpleFearPotion(), 0);
        pc1.addPotion(new SimpleWeakPotion(), 1);
        pc2.addPotion(new SimpleFearPotion(), 1);
        pc2.addPotion(new SimpleWeakPotion(), 0);
        assertEquals(pc1, pc2);
    }

    @Test
    void testNotEqualPotionCollections() {
        PotionCollection pc1 = new PotionCollection(null, false, false);
        PotionCollection pc2 = new PotionCollection(null, false, false);
        pc1.addPotion(new SimpleFearPotion(), 0);
        pc1.addPotion(new SimpleWeakPotion(), 1);
        pc1.addPotion(new SimpleWeakPotion(), 2);
        pc2.addPotion(new SimpleWeakPotion(), 0);
        pc2.addPotion(new SimpleFearPotion(), 1);
        assertNotEquals(pc1, pc2);
    }
}