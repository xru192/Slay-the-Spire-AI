package newaimod.util.dungeonInfo;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.*;
import newaimod.NewAIMod;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.SimplePlayer;
import newaimod.util.simulator.monsters.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

public class RealDungeonInformationProvider implements DungeonInformationProvider {
    public static final Logger logger = LogManager.getLogger(RealDungeonInformationProvider.class.getName());

    private static final RealDungeonInformationProvider instance = new RealDungeonInformationProvider();

    private RealDungeonInformationProvider() {
    }

    public static RealDungeonInformationProvider getInstance() {
        return instance;
    }


    @Override
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

        AbstractDungeon.player.relics.stream()
                .map(this::convertRelic)
                .forEach(currentState.relicCollection::add);

        return currentState;
    }

    @Override
    public RelicCollection getCurrentRelics() {
        return new RelicCollection(
                AbstractDungeon.player.relics.stream()
                        .map(this::convertRelic)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public int getActNumber() {
        return AbstractDungeon.actNum;
    }

    @Override
    public int getFloorNum() {
        return AbstractDungeon.floorNum;
    }

    @Override
    public DungeonInformationProvider.BOSSKEY getActBoss() {
        switch (AbstractDungeon.bossKey) {
            case MonsterHelper.GUARDIAN_ENC:
                return RealDungeonInformationProvider.BOSSKEY.GUARDIAN;
            case MonsterHelper.HEXAGHOST_ENC:
                return RealDungeonInformationProvider.BOSSKEY.HEXAGHOST;
            case MonsterHelper.SLIME_BOSS_ENC:
                return RealDungeonInformationProvider.BOSSKEY.SLIME_BOSS;
            case MonsterHelper.COLLECTOR_ENC:
                return RealDungeonInformationProvider.BOSSKEY.COLLECTOR;
            case MonsterHelper.AUTOMATON_ENC:
                return RealDungeonInformationProvider.BOSSKEY.AUTOMATON;
            case MonsterHelper.CHAMP_ENC:
                return RealDungeonInformationProvider.BOSSKEY.CHAMP;
            case MonsterHelper.AWAKENED_ENC:
                return RealDungeonInformationProvider.BOSSKEY.AWAKENED_ONE;
            case MonsterHelper.TIME_EATER_ENC:
                return RealDungeonInformationProvider.BOSSKEY.TIME_EATER;
            case MonsterHelper.DONU_DECA_ENC:
                return RealDungeonInformationProvider.BOSSKEY.DONU_DECA;
            default:
                throw new RuntimeException("Unexpected boss");
        }
    }

    @Override
    public COMBAT_KEY getCombatKey() {
        switch (AbstractDungeon.lastCombatMetricKey) {
            case MonsterHelper.GREMLIN_NOB_ENC:
                return COMBAT_KEY.GREMLIN_NOB_COMBAT;
            case MonsterHelper.LAGAVULIN_ENC:
                return COMBAT_KEY.LAGAVULIN_COMBAT;
            case MonsterHelper.THREE_SENTRY_ENC:
                return COMBAT_KEY.THREE_SENTRY_COMBAT;
            case MonsterHelper.GUARDIAN_ENC:
                return COMBAT_KEY.GUARDIAN_COMBAT;
            case MonsterHelper.HEXAGHOST_ENC:
                return COMBAT_KEY.HEXAGHOST_COMBAT;
            case MonsterHelper.SLIME_BOSS_ENC:
                return COMBAT_KEY.SLIME_BOSS_COMBAT;
            case MonsterHelper.SPHERE_GUARDIAN_ENC:
                return COMBAT_KEY.SPHERE_GUARDIAN_COMBAT;
            case MonsterHelper.SENTRY_SPHERE_ENC:
                return COMBAT_KEY.SENTRY_SPHERE_COMBAT;
            default:
                return COMBAT_KEY.GENERIC_COMBAT;
        }
    }

    @Override
    public DungeonInformationProvider.DeckInfo getCurrentDeckInfo() {
        return new DeckInfo(AbstractDungeon.player.masterDeck.group);
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

    private RelicCollection.RELIC convertRelic(AbstractRelic relic) {
        // Ironclad relics
        if (relic instanceof BurningBlood) {
            return RelicCollection.RELIC.BURNING_BLOOD;
        } else if (relic instanceof RedSkull) {
            return RelicCollection.RELIC.RED_SKULL;
        } else if (relic instanceof PaperFrog) {
            return RelicCollection.RELIC.PAPER_PHROG;
        } else if (relic instanceof SelfFormingClay) {
            return RelicCollection.RELIC.SELF_FORMING_CLAY;
        } else if (relic instanceof ChampionsBelt) {
            return RelicCollection.RELIC.CHAMPION_BELT;
        } else if (relic instanceof CharonsAshes) {
            return RelicCollection.RELIC.CHARONS_ASHES;
        } else if (relic instanceof MagicFlower) {
            return RelicCollection.RELIC.MAGIC_FLOWER;
        } else if (relic instanceof Brimstone) {
            return RelicCollection.RELIC.BRIMSTONE;
        } else if (relic instanceof BlackBlood) {
            return RelicCollection.RELIC.BLACK_BLOOD;
        } else if (relic instanceof MarkOfPain) {
            return RelicCollection.RELIC.MARK_OF_PAIN;
        } else if (relic instanceof RunicCube) {
            return RelicCollection.RELIC.RUNIC_CUBE;
        // Common relics
        } else if (relic instanceof Akabeko) {
            return RelicCollection.RELIC.AKABEKO;
        } else if (relic instanceof Anchor) {
            return RelicCollection.RELIC.ANCHOR;
        } else if (relic instanceof AncientTeaSet) {
            return RelicCollection.RELIC.ANCIENT_TEA_SET;
        } else if (relic instanceof ArtOfWar) {
            return RelicCollection.RELIC.ART_OF_WAR;
        } else if (relic instanceof BagOfMarbles) {
            return RelicCollection.RELIC.BAG_OF_MARBLES;
        } else if (relic instanceof BagOfPreparation) {
            return RelicCollection.RELIC.BAG_OF_PREPARATION;
        } else if (relic instanceof BloodVial) {
            return RelicCollection.RELIC.BLOOD_VIAL;
        } else if (relic instanceof BronzeScales) {
            return RelicCollection.RELIC.BRONZE_SCALES;
        } else if (relic instanceof CentennialPuzzle) {
            return RelicCollection.RELIC.CENTENNIAL_PUZZLE;
        } else if (relic instanceof CeramicFish) {
            return RelicCollection.RELIC.CERAMIC_FISH;
        } else if (relic instanceof DreamCatcher) {
            return RelicCollection.RELIC.DREAM_CATCHER;
        } else if (relic instanceof HappyFlower) {
            return RelicCollection.RELIC.HAPPY_FLOWER;
        } else if (relic instanceof JuzuBracelet) {
            return RelicCollection.RELIC.JUZU_BRACELET;
        } else if (relic instanceof Lantern) {
            return RelicCollection.RELIC.LANTERN;
        } else if (relic instanceof MawBank) {
            return RelicCollection.RELIC.MAW_BANK;
        } else if (relic instanceof MealTicket) {
            return RelicCollection.RELIC.MEAL_TICKET;
        } else if (relic instanceof Nunchaku) {
            return RelicCollection.RELIC.NUNCHAKU;
        } else if (relic instanceof OddlySmoothStone) {
            return RelicCollection.RELIC.ODDLY_SMOOTH_STONE;
        } else if (relic instanceof Omamori) {
            return RelicCollection.RELIC.OMAMORI;
        } else if (relic instanceof Orichalcum) {
            return RelicCollection.RELIC.ORICHALCUM;
        } else if (relic instanceof PenNib) {
            return RelicCollection.RELIC.PEN_NIB;
        } else if (relic instanceof PotionBelt) {
            return RelicCollection.RELIC.POTION_BELT;
        } else if (relic instanceof PreservedInsect) {
            return RelicCollection.RELIC.PRESERVED_INSECT;
        } else if (relic instanceof RegalPillow) {
            return RelicCollection.RELIC.REGAL_PILLOW;
        } else if (relic instanceof SmilingMask) {
            return RelicCollection.RELIC.SMILING_MASK;
        } else if (relic instanceof Strawberry) {
            return RelicCollection.RELIC.STRAWBERRY;
        } else if (relic instanceof Boot) {
            return RelicCollection.RELIC.THE_BOOT;
        } else if (relic instanceof TinyChest) {
            return RelicCollection.RELIC.TINY_CHEST;
        } else if (relic instanceof ToyOrnithopter) {
            return RelicCollection.RELIC.TOY_ORNITHOPTER;
        } else if (relic instanceof Vajra) {
            return RelicCollection.RELIC.VAJRA;
        } else if (relic instanceof WarPaint) {
            return RelicCollection.RELIC.WAR_PAINT;
        } else if (relic instanceof Whetstone) {
            return RelicCollection.RELIC.WHETSTONE;
        // Uncommon relics
        } else if (relic instanceof BlueCandle) {
            return RelicCollection.RELIC.BLUE_CANDLE;
        } else if (relic instanceof BottledFlame) {
            return RelicCollection.RELIC.BOTTLED_FLAME;
        } else if (relic instanceof BottledLightning) {
            return RelicCollection.RELIC.BOTTLED_LIGHTNING;
        } else if (relic instanceof BottledTornado) {
            return RelicCollection.RELIC.BOTTLED_TORNADO;
        } else if (relic instanceof DarkstonePeriapt) {
            return RelicCollection.RELIC.DARKSTONE_PERIAPT;
        } else if (relic instanceof EternalFeather) {
            return RelicCollection.RELIC.ETERNAL_FEATHER;
        } else if (relic instanceof FrozenEgg2) {
            return RelicCollection.RELIC.FROZEN_EGG;
        } else if (relic instanceof GremlinHorn) {
            return RelicCollection.RELIC.GREMLIN_HORN;
        } else if (relic instanceof HornCleat) {
            return RelicCollection.RELIC.HORN_CLEAT;
        } else if (relic instanceof InkBottle) {
            return RelicCollection.RELIC.INK_BOTTLE;
        } else if (relic instanceof Kunai) {
            return RelicCollection.RELIC.KUNAI;
        } else if (relic instanceof LetterOpener) {
            return RelicCollection.RELIC.LETTER_OPENER;
        } else if (relic instanceof Matryoshka) {
            return RelicCollection.RELIC.MATRYOSHKA;
        } else if (relic instanceof MeatOnTheBone) {
            return RelicCollection.RELIC.MEAT_ON_THE_BONE;
        } else if (relic instanceof MercuryHourglass) {
            return RelicCollection.RELIC.MERCURY_HOURGLASS;
        } else if (relic instanceof MoltenEgg2) {
            return RelicCollection.RELIC.MOLTEN_EGG;
        } else if (relic instanceof MummifiedHand) {
            return RelicCollection.RELIC.MUMMIFIED_HAND;
        } else if (relic instanceof OrnamentalFan) {
            return RelicCollection.RELIC.ORNAMENTAL_FAN;
        } else if (relic instanceof Pantograph) {
            return RelicCollection.RELIC.PANTOGRAPH;
        } else if (relic instanceof Pear) {
            return RelicCollection.RELIC.PEAR;
        } else if (relic instanceof QuestionCard) {
            return RelicCollection.RELIC.QUESTION_CARD;
        } else if (relic instanceof Shuriken) {
            return RelicCollection.RELIC.SHURIKEN;
        } else if (relic instanceof SingingBowl) {
            return RelicCollection.RELIC.SINGING_BOWL;
        } else if (relic instanceof StrikeDummy) {
            return RelicCollection.RELIC.STRIKE_DUMMY;
        } else if (relic instanceof Sundial) {
            return RelicCollection.RELIC.SUNDIAL;
        } else if (relic instanceof Courier) {
            return RelicCollection.RELIC.THE_COURIER;
        } else if (relic instanceof ToxicEgg2) {
            return RelicCollection.RELIC.TOXIC_EGG;
        } else if (relic instanceof WhiteBeast) {
            return RelicCollection.RELIC.WHITE_BEAST_STATUE;
        // Rare relics
        } else if (relic instanceof BirdFacedUrn) {
            return RelicCollection.RELIC.BIRD_FACED_URN;
        } else if (relic instanceof Calipers) {
            return RelicCollection.RELIC.CALIPERS;
        } else if (relic instanceof CaptainsWheel) {
            return RelicCollection.RELIC.CAPTAINS_WHEEL;
        } else if (relic instanceof DeadBranch) {
            return RelicCollection.RELIC.DEAD_BRANCH;
        } else if (relic instanceof DuVuDoll) {
            return RelicCollection.RELIC.DU_VU_DOLL;
        } else if (relic instanceof FossilizedHelix) {
            return RelicCollection.RELIC.FOSSILIZED_HELIX;
        } else if (relic instanceof GamblingChip) {
            return RelicCollection.RELIC.GAMBLING_CHIP;
        } else if (relic instanceof Ginger) {
            return RelicCollection.RELIC.GINGER;
        } else if (relic instanceof Girya) {
            return RelicCollection.RELIC.GIRYA;
        } else if (relic instanceof IceCream) {
            return RelicCollection.RELIC.ICE_CREAM;
        } else if (relic instanceof IncenseBurner) {
            return RelicCollection.RELIC.INCENSE_BURNER;
        } else if (relic instanceof LizardTail) {
            return RelicCollection.RELIC.LIZARD_TAIL;
        } else if (relic instanceof Mango) {
            return RelicCollection.RELIC.MANGO;
        } else if (relic instanceof OldCoin) {
            return RelicCollection.RELIC.OLD_COIN;
        } else if (relic instanceof PeacePipe) {
            return RelicCollection.RELIC.PEACE_PIPE;
        } else if (relic instanceof Pocketwatch) {
            return RelicCollection.RELIC.POCKETWATCH;
        } else if (relic instanceof PrayerWheel) {
            return RelicCollection.RELIC.PRAYER_WHEEL;
        } else if (relic instanceof Shovel) {
            return RelicCollection.RELIC.SHOVEL;
        } else if (relic instanceof StoneCalendar) {
            return RelicCollection.RELIC.STONE_CALENDAR;
        } else if (relic instanceof ThreadAndNeedle) {
            return RelicCollection.RELIC.THREAD_AND_NEEDLE;
        } else if (relic instanceof Torii) {
            return RelicCollection.RELIC.TORII;
        } else if (relic instanceof TungstenRod) {
            return RelicCollection.RELIC.TUNGSTEN_ROD;
        } else if (relic instanceof Turnip) {
            return RelicCollection.RELIC.TURNIP;
        } else if (relic instanceof UnceasingTop) {
            return RelicCollection.RELIC.UNCEASING_TOP;
        } else if (relic instanceof WingBoots) {
            return RelicCollection.RELIC.WING_BOOTS;
        // Boss relics
        } else if (relic instanceof Astrolabe) {
            return RelicCollection.RELIC.ASTROLABE;
        } else if (relic instanceof BlackStar) {
            return RelicCollection.RELIC.BLACK_STAR;
        } else if (relic instanceof BustedCrown) {
            return RelicCollection.RELIC.BUSTED_CROWN;
        } else if (relic instanceof CallingBell) {
            return RelicCollection.RELIC.CALLING_BELL;
        } else if (relic instanceof CoffeeDripper) {
            return RelicCollection.RELIC.COFFEE_DRIPPER;
        } else if (relic instanceof CursedKey) {
            return RelicCollection.RELIC.CURSED_KEY;
        } else if (relic instanceof Ectoplasm) {
            return RelicCollection.RELIC.ECTOPLASM;
        } else if (relic instanceof EmptyCage) {
            return RelicCollection.RELIC.EMPTY_CAGE;
        } else if (relic instanceof FusionHammer) {
            return RelicCollection.RELIC.FUSION_HAMMER;
        } else if (relic instanceof PandorasBox) {
            return RelicCollection.RELIC.PANDORAS_BOX;
        } else if (relic instanceof PhilosopherStone) {
            return RelicCollection.RELIC.PHILOSOPHERS_STONE;
        } else if (relic instanceof RunicDome) {
            return RelicCollection.RELIC.RUNIC_DOME;
        } else if (relic instanceof RunicPyramid) {
            return RelicCollection.RELIC.RUNIC_PYRAMID;
        } else if (relic instanceof SacredBark) {
            return RelicCollection.RELIC.SACRED_BARK;
        } else if (relic instanceof SlaversCollar) {
            return RelicCollection.RELIC.SLAVERS_COLLAR;
        } else if (relic instanceof SneckoEye) {
            return RelicCollection.RELIC.SNECKO_EYE;
        } else if (relic instanceof Sozu) {
            return RelicCollection.RELIC.SOZU;
        } else if (relic instanceof TinyHouse) {
            return RelicCollection.RELIC.TINY_HOUSE;
        } else if (relic instanceof VelvetChoker) {
            return RelicCollection.RELIC.VELVET_CHOKER;
        // Event relics
        } else if (relic instanceof BloodyIdol) {
            return RelicCollection.RELIC.BLOODY_IDOL;
        } else if (relic instanceof CultistMask) {
            return RelicCollection.RELIC.CULTIST_HEADPIECE;
        } else if (relic instanceof Enchiridion) {
            return RelicCollection.RELIC.ENCHIRIDION;
        } else if (relic instanceof FaceOfCleric) {
            return RelicCollection.RELIC.FACE_OF_CLERIC;
        } else if (relic instanceof GoldenIdol) {
            return RelicCollection.RELIC.GOLDEN_IDOL;
        } else if (relic instanceof GremlinMask) {
            return RelicCollection.RELIC.GREMLIN_VISAGE;
        } else if (relic instanceof MarkOfTheBloom) {
            return RelicCollection.RELIC.MARK_OF_THE_BLOOM;
        } else if (relic instanceof MutagenicStrength) {
            return RelicCollection.RELIC.MUTAGENIC_STRENGTH;
        } else if (relic instanceof NlothsGift) {
            return RelicCollection.RELIC.NLOTHS_GIFT;
        } else if (relic instanceof NlothsMask) {
            return RelicCollection.RELIC.NLOTHS_HUNGRY_FACE;
        } else if (relic instanceof Necronomicon) {
            return RelicCollection.RELIC.NECRONOMICON;
        } else if (relic instanceof NeowsLament) {
            return RelicCollection.RELIC.NEOWS_LAMENT;
        } else if (relic instanceof NilrysCodex) {
            return RelicCollection.RELIC.NILRYS_CODEX;
        } else if (relic instanceof OddMushroom) {
            return RelicCollection.RELIC.ODD_MUSHROOM;
        } else if (relic instanceof RedMask) {
            return RelicCollection.RELIC.RED_MASK;
        } else if (relic instanceof SpiritPoop) {
            return RelicCollection.RELIC.SPIRIT_POOP;
        } else if (relic instanceof SsserpentHead) {
            return RelicCollection.RELIC.SSSERPENT_HEAD;
        } else if (relic instanceof WarpedTongs) {
            return RelicCollection.RELIC.WARPED_TONGS;
        // Shop relics
        } else if (relic instanceof Cauldron) {
            return RelicCollection.RELIC.CAULDRON;
        } else if (relic instanceof ChemicalX) {
            return RelicCollection.RELIC.CHEMICAL_X;
        } else if (relic instanceof ClockworkSouvenir) {
            return RelicCollection.RELIC.CLOCKWORK_SOUVENIR;
        } else if (relic instanceof DollysMirror) {
            return RelicCollection.RELIC.DOLLYS_MIRROR;
        } else if (relic instanceof FrozenEye) {
            return RelicCollection.RELIC.FROZEN_EYE;
        } else if (relic instanceof HandDrill) {
            return RelicCollection.RELIC.HAND_DRILL;
        } else if (relic instanceof Waffle) {
            return RelicCollection.RELIC.LEES_WAFFLE;
        } else if (relic instanceof MedicalKit) {
            return RelicCollection.RELIC.MEDICAL_KIT;
        } else if (relic instanceof MembershipCard) {
            return RelicCollection.RELIC.MEMBERSHIP_CARD;
        } else if (relic instanceof OrangePellets) {
            return RelicCollection.RELIC.ORANGE_PELLETS;
        } else if (relic instanceof Orrery) {
            return RelicCollection.RELIC.ORRERY;
        } else if (relic instanceof PrismaticShard) {
            return RelicCollection.RELIC.PRISMATIC_SHARD;
        } else if (relic instanceof Sling) {
            return RelicCollection.RELIC.SLING_OF_COURAGE;
        } else if (relic instanceof StrangeSpoon) {
            return RelicCollection.RELIC.STRANGE_SPOON;
        } else if (relic instanceof Abacus) {
            return RelicCollection.RELIC.THE_ABACUS;
        } else if (relic instanceof Toolbox) {
            return RelicCollection.RELIC.TOOLBOX;
        } else {
            return RelicCollection.RELIC.FILLER;
        }
    }
}
