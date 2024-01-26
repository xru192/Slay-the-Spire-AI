package newaimod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DungeonInformationManagerTest {
    @Test
    void testDeckInfo() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        DungeonInformationManager.DeckInfo deckInfo0 = new DungeonInformationManager.DeckInfo(cards);
        cards.add(null);
        assertEquals(0, deckInfo0.deckSize());
    }
}