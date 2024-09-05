package newaimod.util.dungeonInfo;

import com.megacrit.cardcrawl.cards.AbstractCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RealDungeonInformationProviderTest {
    @Test
    void testDeckInfo() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        RealDungeonInformationProvider.DeckInfo deckInfo0 = new DungeonInformationProvider.DeckInfo(cards);
        cards.add(null);
        assertEquals(0, deckInfo0.deckSize());
    }
}