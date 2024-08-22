package newaimod.util.dungeonInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A RelicCollection represents the list of relics that the player possesses during a run.
 */
public class RelicCollection {

    private final List<RELIC> orderedRelics;

    public RelicCollection() {
        orderedRelics = new ArrayList<>();
    }

    public RelicCollection(List<RELIC> relics) {
        orderedRelics = relics;
    }

    public RelicCollection(RelicCollection other) {
        orderedRelics = new ArrayList<>(other.orderedRelics);
    }

    public void add(RELIC relic) {
        orderedRelics.add(relic);
    }

    public RelicCollection with(RELIC relic) {
        orderedRelics.add(relic);
        return this;
    }

    public boolean hasRelic(RELIC relic) {
        return orderedRelics.contains(relic);
    }

    public enum RELIC {
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
