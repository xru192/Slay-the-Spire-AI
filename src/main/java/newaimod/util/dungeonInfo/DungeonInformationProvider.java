package newaimod.util.dungeonInfo;

import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.util.simulator.CombatSimulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface DungeonInformationProvider {
    CombatSimulator getCurrentState();

    /**
     * Returns the current act number (1-4).
     *
     * @return the current act number
     */
    int getActNumber();

    /**
     * Return the current floor number. At Neow, the floor number is 0.
     *
     * @return current floor number
     */
    int getFloorNum();

    enum BOSSKEY {
        GUARDIAN,
        HEXAGHOST,
        SLIME_BOSS,
        COLLECTOR,
        AUTOMATON,
        CHAMP,
        AWAKENED_ONE,
        TIME_EATER,
        DONU_DECA
    }

    /**
     * Returns the boss which the player will face at the end of the current act.
     *
     * @return the boss at the end of this act
     */
    DungeonInformationProvider.BOSSKEY getActBoss();

    enum COMBAT_KEY {
        GREMLIN_NOB_COMBAT,
        LAGAVULIN_COMBAT,
        THREE_SENTRY_COMBAT,
        GUARDIAN_COMBAT,
        HEXAGHOST_COMBAT,
        SLIME_BOSS_COMBAT,
        SPHERE_GUARDIAN_COMBAT,
        SENTRY_SPHERE_COMBAT,
        GENERIC_COMBAT, // catch-all
    }

    /**
     * Returns the key for the current combat
     *
     * @return key for current combat
     */
    COMBAT_KEY getCombatKey();

    /**
     * Returns an object which describes the content of the player's deck (the one the player begins a combat with).
     * This object represents the player's deck at present; it will not reflect temporary changes to the deck.
     *
     * @return object describing player's deck
     */
    DungeonInformationProvider.DeckInfo getCurrentDeckInfo();

    /**
     * A DeckInfo represents and provides information about a deck of cards.
     */
    class DeckInfo {

        private final ArrayList<AbstractCard> deck;
        private final HashMap<String, Integer> unupgraded;
        private final HashMap<String, Integer> upgraded;

        public DeckInfo(ArrayList<AbstractCard> deck) {
            this.deck = new ArrayList<>(deck);
            unupgraded = new HashMap<>();
            upgraded = new HashMap<>();
            this.deck.forEach(card -> {
                Map<String, Integer> map = card.upgraded ? upgraded : unupgraded;
                map.compute(card.cardID, (key, value) -> value == null ? 1 : value + 1);
            });
        }

        public boolean containsCard(String cardID) {
            return upgraded.containsKey(cardID) || unupgraded.containsKey(cardID);
        }

        /**
         * Returns the frequency of a card in the deck.
         *
         * @param cardID the ID of the card to count copies of
         * @return the number of copies of the card in the deck
         */
        public int countTotalCopies(String cardID) {
            return upgraded.getOrDefault(cardID, 0) + unupgraded.getOrDefault(cardID, 0);
        }

        /**
         * Returns the number of cards in the deck which match any of the given cards.
         *
         * @param cardIDs IDs of the cards to count copies of
         * @return the number of total copies of the cards in the deck
         */
        public int countTotalCopies(String[] cardIDs) {
            return Arrays.stream(cardIDs)
                    .mapToInt(this::countTotalCopies)
                    .sum();
        }

        /**
         * Returns the number of unupgraded copies of a card that are in the deck.
         *
         * @param cardID the ID of the card to count unupgraded copies of
         * @return the number of unupgraded copies of the card in the deck
         */
        public int countUnupgradedCopies(String cardID) {
            return unupgraded.getOrDefault(cardID, 0);
        }

        /**
         * Returns the number of upgraded copies of a card that are in the deck.
         *
         * @param cardID the ID of the card to count upgraded copies of
         * @return the number of upgraded copies of the card in the deck
         */
        public int countUpgradedCopies(String cardID) {
            return upgraded.getOrDefault(cardID, 0);
        }

        /**
         * Returns the number of cards in the deck.
         *
         * @return the size of the deck
         */
        public int deckSize() {
            return deck.size();
        }

        /**
         * Returns the number of starter strikes in this deck.
         *
         * @return number of starter strikes
         */
        public int countStarterStrike() {
            return (int) deck.stream().filter(AbstractCard::isStarterStrike).count();
        }

        /**
         * Returns the number of starter defends in this deck.
         *
         * @return number of starter defends
         */
        public int countStarterDefend() {
            return (int) deck.stream().filter(AbstractCard::isStarterDefend).count();
        }

        /**
         * Returns the number of cards in this deck which contain the word "Strike".
         *
         * @return the number of cards containing "Strike"
         */
        public int countStrikeCards() {
            return (int) deck.stream()
                    .filter(card -> card.hasTag(AbstractCard.CardTags.STRIKE))
                    .count();
        }

    }

}
