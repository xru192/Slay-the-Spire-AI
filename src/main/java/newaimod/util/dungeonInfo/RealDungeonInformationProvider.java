package newaimod.util.dungeonInfo;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
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
        currentPlayer.setHealth(p.currentHealth);
        currentPlayer.setBlock(p.currentBlock);
        for (AbstractCard card : p.hand.group) {
            currentPlayer.hand.add(currentState.convertCard(card));
        }
        currentPlayer.setEnergy(CombatUtils.usableEnergy());
        currentPlayer.setStrength(CombatUtils.amountOfPower(p, StrengthPower.POWER_ID));
        currentPlayer.setDexterity(CombatUtils.amountOfPower(p, DexterityPower.POWER_ID));
        currentPlayer.setWeakened(p.hasPower(WeakPower.POWER_ID));
        currentPlayer.setFrail(p.hasPower(FrailPower.POWER_ID));
        currentPlayer.setMetallicize(CombatUtils.amountOfPower(p, MetallicizePower.POWER_ID));
        currentPlayer.setDemonForm(CombatUtils.amountOfPower(p, DemonFormPower.POWER_ID));
        currentPlayer.setEntangled(p.hasPower(EntanglePower.POWER_ID));
        currentPlayer.setVulnerable(p.hasPower(VulnerablePower.POWER_ID));
        currentPlayer.setNoDraw(p.hasPower(NoDrawPower.POWER_ID));
        currentPlayer.setExhaustedSlimed(0);
        currentPlayer.setCardsDrawnWith0Energy(0);
        currentPlayer.setCardsDrawnWith1Energy(0);
        currentPlayer.setCardsDrawnWith2Energy(0);
        currentPlayer.setCardsDrawnWith3Energy(0);

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
            case AwakenedOne.ID:
                return new SimpleAwakenedOne((AwakenedOne) m);
            case LouseNormal.ID:
            case LouseDefensive.ID:
                return new SimpleLouse(m);
            default:
                return new SimpleMonster(m);
        }
    }

    private RelicCollection.Relic convertRelic(AbstractRelic relic) {
        int counter = relic.counter;
        // Ironclad relics
        if (relic instanceof BurningBlood) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BURNING_BLOOD, counter);
        } else if (relic instanceof RedSkull) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.RED_SKULL, counter);
        } else if (relic instanceof PaperFrog) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PAPER_PHROG, counter);
        } else if (relic instanceof SelfFormingClay) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SELF_FORMING_CLAY, counter);
        } else if (relic instanceof ChampionsBelt) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CHAMPION_BELT, counter);
        } else if (relic instanceof CharonsAshes) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CHARONS_ASHES, counter);
        } else if (relic instanceof MagicFlower) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MAGIC_FLOWER, counter);
        } else if (relic instanceof Brimstone) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BRIMSTONE, counter);
        } else if (relic instanceof BlackBlood) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BLACK_BLOOD, counter);
        } else if (relic instanceof MarkOfPain) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MARK_OF_PAIN, counter);
        } else if (relic instanceof RunicCube) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.RUNIC_CUBE, counter);
        // Common relics
        } else if (relic instanceof Akabeko) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.AKABEKO, counter);
        } else if (relic instanceof Anchor) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ANCHOR, counter);
        } else if (relic instanceof AncientTeaSet) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ANCIENT_TEA_SET, counter);
        } else if (relic instanceof ArtOfWar) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ART_OF_WAR, counter);
        } else if (relic instanceof BagOfMarbles) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BAG_OF_MARBLES, counter);
        } else if (relic instanceof BagOfPreparation) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BAG_OF_PREPARATION, counter);
        } else if (relic instanceof BloodVial) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BLOOD_VIAL, counter);
        } else if (relic instanceof BronzeScales) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BRONZE_SCALES, counter);
        } else if (relic instanceof CentennialPuzzle) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CENTENNIAL_PUZZLE, counter);
        } else if (relic instanceof CeramicFish) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CERAMIC_FISH, counter);
        } else if (relic instanceof DreamCatcher) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.DREAM_CATCHER, counter);
        } else if (relic instanceof HappyFlower) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.HAPPY_FLOWER, counter);
        } else if (relic instanceof JuzuBracelet) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.JUZU_BRACELET, counter);
        } else if (relic instanceof Lantern) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.LANTERN, counter);
        } else if (relic instanceof MawBank) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MAW_BANK, counter);
        } else if (relic instanceof MealTicket) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MEAL_TICKET, counter);
        } else if (relic instanceof Nunchaku) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.NUNCHAKU, counter);
        } else if (relic instanceof OddlySmoothStone) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ODDLY_SMOOTH_STONE, counter);
        } else if (relic instanceof Omamori) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.OMAMORI, counter);
        } else if (relic instanceof Orichalcum) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ORICHALCUM, counter);
        } else if (relic instanceof PenNib) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PEN_NIB, counter);
        } else if (relic instanceof PotionBelt) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.POTION_BELT, counter);
        } else if (relic instanceof PreservedInsect) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PRESERVED_INSECT, counter);
        } else if (relic instanceof RegalPillow) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.REGAL_PILLOW, counter);
        } else if (relic instanceof SmilingMask) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SMILING_MASK, counter);
        } else if (relic instanceof Strawberry) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.STRAWBERRY, counter);
        } else if (relic instanceof Boot) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.THE_BOOT, counter);
        } else if (relic instanceof TinyChest) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.TINY_CHEST, counter);
        } else if (relic instanceof ToyOrnithopter) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.TOY_ORNITHOPTER, counter);
        } else if (relic instanceof Vajra) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.VAJRA, counter);
        } else if (relic instanceof WarPaint) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.WAR_PAINT, counter);
        } else if (relic instanceof Whetstone) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.WHETSTONE, counter);
        // Uncommon relics
        } else if (relic instanceof BlueCandle) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BLUE_CANDLE, counter);
        } else if (relic instanceof BottledFlame) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BOTTLED_FLAME, counter);
        } else if (relic instanceof BottledLightning) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BOTTLED_LIGHTNING, counter);
        } else if (relic instanceof BottledTornado) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BOTTLED_TORNADO, counter);
        } else if (relic instanceof DarkstonePeriapt) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.DARKSTONE_PERIAPT, counter);
        } else if (relic instanceof EternalFeather) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ETERNAL_FEATHER, counter);
        } else if (relic instanceof FrozenEgg2) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.FROZEN_EGG, counter);
        } else if (relic instanceof GremlinHorn) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.GREMLIN_HORN, counter);
        } else if (relic instanceof HornCleat) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.HORN_CLEAT, counter);
        } else if (relic instanceof InkBottle) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.INK_BOTTLE, counter);
        } else if (relic instanceof Kunai) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.KUNAI, counter);
        } else if (relic instanceof LetterOpener) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.LETTER_OPENER, counter);
        } else if (relic instanceof Matryoshka) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MATRYOSHKA, counter);
        } else if (relic instanceof MeatOnTheBone) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MEAT_ON_THE_BONE, counter);
        } else if (relic instanceof MercuryHourglass) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MERCURY_HOURGLASS, counter);
        } else if (relic instanceof MoltenEgg2) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MOLTEN_EGG, counter);
        } else if (relic instanceof MummifiedHand) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MUMMIFIED_HAND, counter);
        } else if (relic instanceof OrnamentalFan) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ORNAMENTAL_FAN, counter);
        } else if (relic instanceof Pantograph) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PANTOGRAPH, counter);
        } else if (relic instanceof Pear) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PEAR, counter);
        } else if (relic instanceof QuestionCard) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.QUESTION_CARD, counter);
        } else if (relic instanceof Shuriken) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SHURIKEN, counter);
        } else if (relic instanceof SingingBowl) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SINGING_BOWL, counter);
        } else if (relic instanceof StrikeDummy) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.STRIKE_DUMMY, counter);
        } else if (relic instanceof Sundial) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SUNDIAL, counter);
        } else if (relic instanceof Courier) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.THE_COURIER, counter);
        } else if (relic instanceof ToxicEgg2) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.TOXIC_EGG, counter);
        } else if (relic instanceof WhiteBeast) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.WHITE_BEAST_STATUE, counter);
        // Rare relics
        } else if (relic instanceof BirdFacedUrn) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BIRD_FACED_URN, counter);
        } else if (relic instanceof Calipers) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CALIPERS, counter);
        } else if (relic instanceof CaptainsWheel) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CAPTAINS_WHEEL, counter);
        } else if (relic instanceof DeadBranch) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.DEAD_BRANCH, counter);
        } else if (relic instanceof DuVuDoll) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.DU_VU_DOLL, counter);
        } else if (relic instanceof FossilizedHelix) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.FOSSILIZED_HELIX, counter);
        } else if (relic instanceof GamblingChip) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.GAMBLING_CHIP, counter);
        } else if (relic instanceof Ginger) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.GINGER, counter);
        } else if (relic instanceof Girya) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.GIRYA, counter);
        } else if (relic instanceof IceCream) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ICE_CREAM, counter);
        } else if (relic instanceof IncenseBurner) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.INCENSE_BURNER, counter);
        } else if (relic instanceof LizardTail) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.LIZARD_TAIL, counter);
        } else if (relic instanceof Mango) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MANGO, counter);
        } else if (relic instanceof OldCoin) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.OLD_COIN, counter);
        } else if (relic instanceof PeacePipe) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PEACE_PIPE, counter);
        } else if (relic instanceof Pocketwatch) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.POCKETWATCH, counter);
        } else if (relic instanceof PrayerWheel) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PRAYER_WHEEL, counter);
        } else if (relic instanceof Shovel) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SHOVEL, counter);
        } else if (relic instanceof StoneCalendar) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.STONE_CALENDAR, counter);
        } else if (relic instanceof ThreadAndNeedle) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.THREAD_AND_NEEDLE, counter);
        } else if (relic instanceof Torii) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.TORII, counter);
        } else if (relic instanceof TungstenRod) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.TUNGSTEN_ROD, counter);
        } else if (relic instanceof Turnip) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.TURNIP, counter);
        } else if (relic instanceof UnceasingTop) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.UNCEASING_TOP, counter);
        } else if (relic instanceof WingBoots) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.WING_BOOTS, counter);
        // Boss relics
        } else if (relic instanceof Astrolabe) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ASTROLABE, counter);
        } else if (relic instanceof BlackStar) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BLACK_STAR, counter);
        } else if (relic instanceof BustedCrown) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BUSTED_CROWN, counter);
        } else if (relic instanceof CallingBell) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CALLING_BELL, counter);
        } else if (relic instanceof CoffeeDripper) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.COFFEE_DRIPPER, counter);
        } else if (relic instanceof CursedKey) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CURSED_KEY, counter);
        } else if (relic instanceof Ectoplasm) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ECTOPLASM, counter);
        } else if (relic instanceof EmptyCage) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.EMPTY_CAGE, counter);
        } else if (relic instanceof FusionHammer) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.FUSION_HAMMER, counter);
        } else if (relic instanceof PandorasBox) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PANDORAS_BOX, counter);
        } else if (relic instanceof PhilosopherStone) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PHILOSOPHERS_STONE, counter);
        } else if (relic instanceof RunicDome) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.RUNIC_DOME, counter);
        } else if (relic instanceof RunicPyramid) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.RUNIC_PYRAMID, counter);
        } else if (relic instanceof SacredBark) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SACRED_BARK, counter);
        } else if (relic instanceof SlaversCollar) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SLAVERS_COLLAR, counter);
        } else if (relic instanceof SneckoEye) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SNECKO_EYE, counter);
        } else if (relic instanceof Sozu) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SOZU, counter);
        } else if (relic instanceof TinyHouse) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.TINY_HOUSE, counter);
        } else if (relic instanceof VelvetChoker) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.VELVET_CHOKER, counter);
        // Event relics
        } else if (relic instanceof BloodyIdol) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.BLOODY_IDOL, counter);
        } else if (relic instanceof CultistMask) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CULTIST_HEADPIECE, counter);
        } else if (relic instanceof Enchiridion) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ENCHIRIDION, counter);
        } else if (relic instanceof FaceOfCleric) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.FACE_OF_CLERIC, counter);
        } else if (relic instanceof GoldenIdol) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.GOLDEN_IDOL, counter);
        } else if (relic instanceof GremlinMask) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.GREMLIN_VISAGE, counter);
        } else if (relic instanceof MarkOfTheBloom) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MARK_OF_THE_BLOOM, counter);
        } else if (relic instanceof MutagenicStrength) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MUTAGENIC_STRENGTH, counter);
        } else if (relic instanceof NlothsGift) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.NLOTHS_GIFT, counter);
        } else if (relic instanceof NlothsMask) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.NLOTHS_HUNGRY_FACE, counter);
        } else if (relic instanceof Necronomicon) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.NECRONOMICON, counter);
        } else if (relic instanceof NeowsLament) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.NEOWS_LAMENT, counter);
        } else if (relic instanceof NilrysCodex) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.NILRYS_CODEX, counter);
        } else if (relic instanceof OddMushroom) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ODD_MUSHROOM, counter);
        } else if (relic instanceof RedMask) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.RED_MASK, counter);
        } else if (relic instanceof SpiritPoop) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SPIRIT_POOP, counter);
        } else if (relic instanceof SsserpentHead) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SSSERPENT_HEAD, counter);
        } else if (relic instanceof WarpedTongs) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.WARPED_TONGS, counter);
        // Shop relics
        } else if (relic instanceof Cauldron) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CAULDRON, counter);
        } else if (relic instanceof ChemicalX) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CHEMICAL_X, counter);
        } else if (relic instanceof ClockworkSouvenir) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.CLOCKWORK_SOUVENIR, counter);
        } else if (relic instanceof DollysMirror) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.DOLLYS_MIRROR, counter);
        } else if (relic instanceof FrozenEye) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.FROZEN_EYE, counter);
        } else if (relic instanceof HandDrill) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.HAND_DRILL, counter);
        } else if (relic instanceof Waffle) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.LEES_WAFFLE, counter);
        } else if (relic instanceof MedicalKit) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MEDICAL_KIT, counter);
        } else if (relic instanceof MembershipCard) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.MEMBERSHIP_CARD, counter);
        } else if (relic instanceof OrangePellets) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ORANGE_PELLETS, counter);
        } else if (relic instanceof Orrery) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.ORRERY, counter);
        } else if (relic instanceof PrismaticShard) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.PRISMATIC_SHARD, counter);
        } else if (relic instanceof Sling) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.SLING_OF_COURAGE, counter);
        } else if (relic instanceof StrangeSpoon) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.STRANGE_SPOON, counter);
        } else if (relic instanceof Abacus) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.THE_ABACUS, counter);
        } else if (relic instanceof Toolbox) {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.TOOLBOX, counter);
        } else {
            return new RelicCollection.Relic(RelicCollection.RELIC_ID.FILLER, counter);
        }
    }
}
