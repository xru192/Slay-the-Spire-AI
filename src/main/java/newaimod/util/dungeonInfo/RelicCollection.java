package newaimod.util.dungeonInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A RelicCollection represents the list of relics that the player possesses during a run.
 */
public class RelicCollection {

    private final List<Relic> orderedRelics;

    public RelicCollection() {
        orderedRelics = new ArrayList<>();
    }

    public RelicCollection(List<Relic> relics) {
        orderedRelics = relics;
    }

    public RelicCollection(RelicCollection other) {
        orderedRelics = new ArrayList<>(other.orderedRelics);
    }

    public void add(Relic relic) {
        orderedRelics.add(relic);
    }

    public RelicCollection with(RELIC_ID id) {
        orderedRelics.add(new Relic(id));
        return this;
    }

    public RelicCollection with(RELIC_ID id, int counter) {
        orderedRelics.add(new Relic(id, counter));
        return this;
    }

    public boolean hasRelic(RELIC_ID id) {
        return orderedRelics.stream().anyMatch(relic -> relic.id.equals(id));
    }

    /**
     * Returns the value of the counter on the specified relic.
     * Returns -1 if the relic is not present, or has no counter value.
     *
     * @param id the relic to get the counter value for
     * @return the counter on the specified relic
     */
    public int getCounter(RELIC_ID id) {
        return orderedRelics.stream()
                .filter(r -> r.id.equals(id))
                .map(Relic::getCounter)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Increments the counter on the specified relic, if present, and resets it to 0 if the critical value was reached.
     * Returns true if the counter was reset, an false if relic is not present, or the critical value is not reached.
     *
     * @param relicId       the relic to increment counter value
     * @param critical the value which the counter should reset back to 0 on (e.g. 3 for Kunai)
     * @return whether the counter hit the critical value, resetting back to 0
     */
    public boolean incrementCounterWithCriticalValue(RELIC_ID relicId, int critical) {
        Relic match = orderedRelics.stream()
                .filter(relic -> relic.id.equals(relicId))
                .findFirst()
                .orElse(null);
        if (match != null) {
            if (++match.counter == critical) {
                match.counter = 0;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelicCollection)) return false;
        RelicCollection that = (RelicCollection) o;
        return Objects.equals(orderedRelics, that.orderedRelics);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderedRelics);
    }

    public static class Relic {
        public final RELIC_ID id;
        private int counter;

        public Relic(RELIC_ID id) {
            this.id = id;
            counter = -1;
        }

        public Relic(RELIC_ID id, int counter) {
            this.id = id;
            this.counter = counter;
        }

        public int getCounter() {
            return counter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Relic)) return false;
            Relic relic = (Relic) o;
            return counter == relic.counter && id == relic.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, counter);
        }
    }

    public enum RELIC_ID {
        // Ironclad
        BURNING_BLOOD,
        RED_SKULL,
        PAPER_PHROG,
        SELF_FORMING_CLAY,
        CHAMPION_BELT,
        CHARONS_ASHES,
        MAGIC_FLOWER,
        BRIMSTONE,
        BLACK_BLOOD,
        MARK_OF_PAIN,
        RUNIC_CUBE,
        // Common
        AKABEKO,
        ANCHOR,
        ANCIENT_TEA_SET,
        ART_OF_WAR,
        BAG_OF_MARBLES,
        BAG_OF_PREPARATION,
        BLOOD_VIAL,
        BRONZE_SCALES,
        CENTENNIAL_PUZZLE,
        CERAMIC_FISH,
        DREAM_CATCHER,
        HAPPY_FLOWER,
        JUZU_BRACELET,
        LANTERN,
        MAW_BANK,
        MEAL_TICKET,
        NUNCHAKU,
        ODDLY_SMOOTH_STONE,
        OMAMORI,
        ORICHALCUM,
        PEN_NIB,
        POTION_BELT,
        PRESERVED_INSECT,
        REGAL_PILLOW,
        SMILING_MASK,
        STRAWBERRY,
        THE_BOOT,
        TINY_CHEST,
        TOY_ORNITHOPTER,
        VAJRA,
        WAR_PAINT,
        WHETSTONE,
        // Uncommon
        BLUE_CANDLE,
        BOTTLED_FLAME,
        BOTTLED_LIGHTNING,
        BOTTLED_TORNADO,
        DARKSTONE_PERIAPT,
        ETERNAL_FEATHER,
        FROZEN_EGG,
        GREMLIN_HORN,
        HORN_CLEAT,
        INK_BOTTLE,
        KUNAI,
        LETTER_OPENER,
        MATRYOSHKA,
        MEAT_ON_THE_BONE,
        MERCURY_HOURGLASS,
        MOLTEN_EGG,
        MUMMIFIED_HAND,
        ORNAMENTAL_FAN,
        PANTOGRAPH,
        PEAR,
        QUESTION_CARD,
        SHURIKEN,
        SINGING_BOWL,
        STRIKE_DUMMY,
        SUNDIAL,
        THE_COURIER,
        TOXIC_EGG,
        WHITE_BEAST_STATUE,
        // Rare
        BIRD_FACED_URN,
        CALIPERS,
        CAPTAINS_WHEEL,
        DEAD_BRANCH,
        DU_VU_DOLL,
        FOSSILIZED_HELIX,
        GAMBLING_CHIP,
        GINGER,
        GIRYA,
        ICE_CREAM,
        INCENSE_BURNER,
        LIZARD_TAIL,
        MANGO,
        OLD_COIN,
        PEACE_PIPE,
        POCKETWATCH,
        PRAYER_WHEEL,
        SHOVEL,
        STONE_CALENDAR,
        THREAD_AND_NEEDLE,
        TORII,
        TUNGSTEN_ROD,
        TURNIP,
        UNCEASING_TOP,
        WING_BOOTS,
        // Boss
        ASTROLABE,
        BLACK_STAR,
        BUSTED_CROWN,
        CALLING_BELL,
        COFFEE_DRIPPER,
        CURSED_KEY,
        ECTOPLASM,
        EMPTY_CAGE,
        FUSION_HAMMER,
        PANDORAS_BOX,
        PHILOSOPHERS_STONE,
        RUNIC_DOME,
        RUNIC_PYRAMID,
        SACRED_BARK,
        SLAVERS_COLLAR,
        SNECKO_EYE,
        SOZU,
        TINY_HOUSE,
        VELVET_CHOKER,
        // Event
        BLOODY_IDOL,
        CULTIST_HEADPIECE,
        ENCHIRIDION,
        FACE_OF_CLERIC,
        GOLDEN_IDOL,
        GREMLIN_VISAGE,
        MARK_OF_THE_BLOOM,
        MUTAGENIC_STRENGTH,
        NLOTHS_GIFT,
        NLOTHS_HUNGRY_FACE,
        NECRONOMICON,
        NEOWS_LAMENT,
        NILRYS_CODEX,
        ODD_MUSHROOM,
        RED_MASK,
        SPIRIT_POOP,
        SSSERPENT_HEAD,
        WARPED_TONGS,
        // Shop
        CAULDRON,
        CHEMICAL_X,
        CLOCKWORK_SOUVENIR,
        DOLLYS_MIRROR,
        FROZEN_EYE,
        HAND_DRILL,
        LEES_WAFFLE,
        MEDICAL_KIT,
        MEMBERSHIP_CARD,
        ORANGE_PELLETS,
        ORRERY,
        PRISMATIC_SHARD,
        SLING_OF_COURAGE,
        STRANGE_SPOON,
        THE_ABACUS,
        TOOLBOX,
        // Fallback
        FILLER
    }
}
