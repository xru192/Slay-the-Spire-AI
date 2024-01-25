package newaimod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.*;
import newaimod.NewAIMod;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.SimplePlayer;
import newaimod.util.simulator.monsters.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * The DungeonInformationManager handles requests for information about the state of the dungeon.
 */
public class DungeonInformationManager {
    public static final Logger logger = LogManager.getLogger(DungeonInformationManager.class.getName());

    private static final DungeonInformationManager instance = new DungeonInformationManager();

    private DungeonInformationManager() {
    }

    public static DungeonInformationManager getInstance() {
        return instance;
    }

    /**
     * Returns a CombatSimulator representing the current state of combat. Requires that the player is in combat.
     *
     * @return the current state of combat
     */
    public CombatSimulator getCurrentState() {
        assert NewAIMod.inBattle;
        CombatSimulator currentState = new CombatSimulator();
        SimplePlayer currentPlayer = currentState.player;

        AbstractPlayer p = AbstractDungeon.player;
        currentPlayer.health = p.currentHealth;
        currentPlayer.block = p.currentBlock;
        for (AbstractCard card : p.hand.group) {
            currentPlayer.hand.add(currentState.convertCard(card));
        }
        currentPlayer.energy = CombatUtils.usableEnergy();
        currentPlayer.strength = CombatUtils.amountOfPower(p, StrengthPower.POWER_ID);
        currentPlayer.dexterity = CombatUtils.amountOfPower(p, DexterityPower.POWER_ID);
        currentPlayer.weakened = p.hasPower(WeakPower.POWER_ID);
        currentPlayer.frail = p.hasPower(FrailPower.POWER_ID);
        currentPlayer.metallicize = CombatUtils.amountOfPower(p, MetallicizePower.POWER_ID);
        currentPlayer.demonForm = CombatUtils.amountOfPower(p, DemonFormPower.POWER_ID);
        currentPlayer.entangled = p.hasPower(EntanglePower.POWER_ID);
        currentPlayer.vulnerable = p.hasPower(VulnerablePower.POWER_ID);
        currentPlayer.noDraw = p.hasPower(NoDrawPower.POWER_ID);
        currentPlayer.exhaustedSlimed = 0;
        currentPlayer.cardsDrawnWith0Energy = 0;
        currentPlayer.cardsDrawnWith1Energy = 0;
        currentPlayer.cardsDrawnWith2Energy = 0;
        currentPlayer.cardsDrawnWith3Energy = 0;

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDeadOrEscaped()) {
                currentState.addMonster(convertMonster(m));
            }
        }

        return currentState;
    }

    private SimpleMonster convertMonster(AbstractMonster m) {
        switch (m.id) {
            case GremlinNob.ID:
                return new SimpleGremlinNob((GremlinNob) m);
            case Lagavulin.ID:
                return new SimpleLagavulin((Lagavulin) m);
            case Sentry.ID:
                return new SimpleSentry((Sentry) m);
            case SlimeBoss.ID:
                return new SimpleSlimeBoss((SlimeBoss) m);
            case TheGuardian.ID:
                return new SimpleTheGuardian((TheGuardian) m);
            default:
                return new SimpleMonster(m);
        }
    }

    /**
     * Returns the current act number (1-4).
     *
     * @return the current act number
     */
    public int getActNumber() {
        return AbstractDungeon.actNum;
    }

    public enum BOSSKEY {
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
    public BOSSKEY getActBoss() {
        switch (AbstractDungeon.bossKey) {
            case MonsterHelper.GUARDIAN_ENC:
                return BOSSKEY.GUARDIAN;
            case MonsterHelper.HEXAGHOST_ENC:
                return BOSSKEY.HEXAGHOST;
            case MonsterHelper.SLIME_BOSS_ENC:
                return BOSSKEY.SLIME_BOSS;
            case MonsterHelper.COLLECTOR_ENC:
                return BOSSKEY.COLLECTOR;
            case MonsterHelper.AUTOMATON_ENC:
                return BOSSKEY.AUTOMATON;
            case MonsterHelper.CHAMP_ENC:
                return BOSSKEY.CHAMP;
            case MonsterHelper.AWAKENED_ENC:
                return BOSSKEY.AWAKENED_ONE;
            case MonsterHelper.TIME_EATER_ENC:
                return BOSSKEY.TIME_EATER;
            case MonsterHelper.DONU_DECA_ENC:
                return BOSSKEY.DONU_DECA;
            default:
                throw new RuntimeException("Unexpected boss");
        }

    }

    /**
     * Return the current floor number. At Neow, the floor number is 0.
     *
     * @return current floor number
     */
    public int getFloorNum() {
        return AbstractDungeon.floorNum;
    }


    /**
     * Returns an object which describes the content of the player's deck (the one the player begins a combat with).
     * This object represents the player's deck at present; it will not reflect changes to the deck.
     *
     * @return object describing player's deck
     */
    public DeckInfo getCurrentDeckInfo() {
        return new DeckInfo(AbstractDungeon.player.masterDeck.group);
    }

    /**
     * A DeckInfo represents and provides information about a deck of cards.
     */
    public static class DeckInfo {

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
            return unupgraded.getOrDefault(unupgraded, 0);
        }

        /**
         * Returns the number of upgraded copies of a card that are in the deck.
         *
         * @param cardID the ID of the card to count upgraded copies of
         * @return the number of upgraded copies of the card in the deck
         */
        public int countUpgradedCopies(String cardID) {
            return upgraded.getOrDefault(upgraded, 0);
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
