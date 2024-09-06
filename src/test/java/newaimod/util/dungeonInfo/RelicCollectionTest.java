package newaimod.util.dungeonInfo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import newaimod.util.dungeonInfo.RelicCollection.RELIC_ID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class RelicCollectionTest {

    @Test
    void testCreation() {
        List<RelicCollection.Relic> relicList = new ArrayList<>();
        relicList.add(new RelicCollection.Relic(RELIC_ID.BURNING_BLOOD));
        relicList.add(new RelicCollection.Relic(RELIC_ID.AKABEKO));
        relicList.add(new RelicCollection.Relic(RELIC_ID.BAG_OF_MARBLES));
        relicList.add(new RelicCollection.Relic(RELIC_ID.CALIPERS));
        relicList.add(new RelicCollection.Relic(RELIC_ID.DEAD_BRANCH));
        relicList.add(new RelicCollection.Relic(RELIC_ID.KUNAI, 2));
        RelicCollection relics = new RelicCollection(relicList);
        relics.add(new RelicCollection.Relic(RELIC_ID.WHETSTONE));

        relicList.forEach(relic -> assertTrue(relics.hasRelic(relic.id)));
        assertTrue(relics.hasRelic(RELIC_ID.WHETSTONE));
        assertEquals(2, relics.getCounter(RELIC_ID.KUNAI));
    }

    @Test
    void testIncrementRelicCounterNormalCase() {
        RelicCollection relics = new RelicCollection().with(RELIC_ID.NUNCHAKU, 7);

        assertFalse(relics.incrementCounterWithCriticalValue(RELIC_ID.NUNCHAKU, 10));
        assertEquals(8, relics.getCounter(RELIC_ID.NUNCHAKU));

        assertFalse(relics.incrementCounterWithCriticalValue(RELIC_ID.NUNCHAKU, 10));
        assertEquals(9, relics.getCounter(RELIC_ID.NUNCHAKU));

        assertTrue(relics.incrementCounterWithCriticalValue(RELIC_ID.NUNCHAKU, 10));
        assertEquals(0, relics.getCounter(RELIC_ID.NUNCHAKU));

        assertFalse(relics.incrementCounterWithCriticalValue(RELIC_ID.NUNCHAKU, 10));
        assertEquals(1, relics.getCounter(RELIC_ID.NUNCHAKU));
    }

    @Test
    void testIncrementRelicNotPresent() {
        RelicCollection relics = new RelicCollection();
        assertFalse(relics.incrementCounterWithCriticalValue(RELIC_ID.SHURIKEN, 3));
        assertFalse(relics.incrementCounterWithCriticalValue(RELIC_ID.SHURIKEN, 3));
        assertFalse(relics.incrementCounterWithCriticalValue(RELIC_ID.SHURIKEN, 3));
        assertFalse(relics.incrementCounterWithCriticalValue(RELIC_ID.SHURIKEN, 3));
    }


    @Test
    void testCaseEquals() {
        RelicCollection relics1 = new RelicCollection()
                .with(RELIC_ID.BURNING_BLOOD)
                .with(RELIC_ID.SHURIKEN, 1);

        RelicCollection relics2 = new RelicCollection()
                .with(RELIC_ID.BURNING_BLOOD, -1)
                .with(RELIC_ID.SHURIKEN, 1);
        assertEquals(relics1, relics2);

        HashSet<RelicCollection> relicCollections = new HashSet<>();
        relicCollections.add(relics1);
        relicCollections.add(relics2);
        assertEquals(1, relicCollections.size());
    }

    @Test
    void testCaseNotEquals() {
        RelicCollection relics1 = new RelicCollection()
                .with(RELIC_ID.BURNING_BLOOD)
                .with(RELIC_ID.SHURIKEN, 1);

        RelicCollection relics2 = new RelicCollection()
                .with(RELIC_ID.BURNING_BLOOD, -1)
                .with(RELIC_ID.SHURIKEN, 2);
        assertNotEquals(relics1, relics2);

        HashSet<RelicCollection> relicCollections = new HashSet<>();
        relicCollections.add(relics1);
        relicCollections.add(relics2);
        assertEquals(2, relicCollections.size());
    }
}