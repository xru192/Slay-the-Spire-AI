package newaimod;

import basemod.BaseMod;

import basemod.ModPanel;
import basemod.devcommands.ConsoleCommand;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.CharacterManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import communicationmod.ChoiceScreenUtils;
import communicationmod.GameStateListener;
import newaimod.ai.AutoPlayer;
import newaimod.ai.basicIroncladPlayer.BasicIroncladPlayer;
import newaimod.properties.PropertyManager;
import newaimod.actions.DoCombatMoveAction;
import newaimod.commands.*;
import newaimod.util.CombatUtils;
import newaimod.util.GeneralUtils;
import newaimod.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SpireInitializer
public class NewAIMod implements
        PostInitializeSubscriber,
        PostDungeonUpdateSubscriber,
        OnStartBattleSubscriber,
        PostBattleSubscriber,
        PostDeathSubscriber,
        StartGameSubscriber,
        PostRenderSubscriber {
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this

    static {
        loadModInfo();
    }

    public static final String RUN_DATA_PATH = "/Users/ryanxu/IdeaProjects/Slay-the-Spire-AI/runData/" + NewAIMod.info.ModVersion.getOriginalValue() + ".txt";
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    private static final String resourcesFolder = "newaimod";

    public static final PropertyManager myPropertyManager = PropertyManager.getInstance();
    public static boolean inBattle;     // whether user is in a combat
    public static boolean inGame;       // whether user is in the game

    private static boolean starting;    // whether game is in process of starting
    private static int waitCounter = 0;
    private static boolean stateChanged = false;

    public static int WAIT = 20;       // wait time (frames) between auto actions
    private AutoPlayer autoPlayer;
    public static AutoPlayer.CombatMove move = null;
    private static boolean creating = false;
    private static final Queue<ImmutablePair<AbstractPlayer.PlayerClass, Integer>> gameQueue = new ArrayDeque<>();

    // This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new NewAIMod();
    }

    public NewAIMod() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");

        myPropertyManager.initializeProperties();

        ConsoleCommand.addCommand("test", TestCommand.class);
        ConsoleCommand.addCommand("move", TestDoCombatMoveActionCommand.class);
        ConsoleCommand.addCommand("cardinfo", CardInfoCommand.class);
        ConsoleCommand.addCommand("monsterinfo", MonsterInfoCommand.class);
        ConsoleCommand.addCommand("settings", SettingsCommand.class);
        ConsoleCommand.addCommand("mapinfo", MapInfoCommand.class);
        ConsoleCommand.addCommand("startgame", StartGameCommand.class);
    }

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);

        ModPanel settingsPanel = myPropertyManager.getSettingsPanel();
        BaseMod.registerModBadge(badgeTexture, null, null, null, settingsPanel);
    }

    public static void delayAction(int amt) {
        waitCounter -= amt;
    }

    // Called once per frame
    @Override
    public void receivePostDungeonUpdate() {
        if (!inGame) {
//            logger.info("Processing gameEnded");
            if (!GameStateListener.checkForDungeonStateChange()) {
                return;
            }
        } else {
            boolean change = GameStateListener.checkForDungeonStateChange();
            if (change && stateChanged) {
                waitCounter -= 2;
            }
            stateChanged = stateChanged || change;
            if (!stateChanged && ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.NONE) {
                return;
            }
            if (waitCounter < 2 + WAIT) { // wait a few update cycles so that actions don't skip over effects
                waitCounter += 1;
                return;
            }
            waitCounter = 0;
            stateChanged = false;
        }

        // At this point, we are in a run and the state has changed since the last time here

        ChoiceScreenUtils.ChoiceType type = ChoiceScreenUtils.getCurrentChoiceType();
        if (!myPropertyManager.enabled) {
            return;
        }

        switch (type) {
            case EVENT:
                autoPlayer.playEvent();
                break;
            case CHEST:
                autoPlayer.playChest();
                break;
            case SHOP_ROOM:
            case SHOP_SCREEN:
                autoPlayer.playShop();
                break;
            case REST:
                autoPlayer.playRest();
                break;
            case CARD_REWARD:
                autoPlayer.playCardReward();
                break;
            case COMBAT_REWARD:
                autoPlayer.playCombatReward();
                break;
            case MAP:
                autoPlayer.playMap();
                break;
            case BOSS_REWARD:
                autoPlayer.playBossReward();
                break;
            case GRID:
                autoPlayer.playGrid();
                break;
            case HAND_SELECT:
                autoPlayer.playHandSelect();
                break;
            case GAME_OVER:
            case COMPLETE:
                if (ChoiceScreenUtils.isConfirmButtonAvailable()) {
                    ChoiceScreenUtils.pressConfirmButton();
                }
                break;
            case NONE:
                // at the start of combat, wait for intents to load in
                // without this, first turn of combat for first card play always has monster intent at 0
                AbstractMonster m = CombatUtils.getWeakestTarget();
                if (m != null && m.intent == AbstractMonster.Intent.DEBUG) {
                    return;
                }

                if (!AbstractDungeon.actionManager.turnHasEnded) {
                    if (inBattle && AbstractDungeon.actionManager.phase.equals(GameActionManager.Phase.WAITING_ON_USER)
                            && AbstractDungeon.actionManager.cardQueue.isEmpty()
                            && AbstractDungeon.actionManager.actions.isEmpty()) {
                        if (creating && move == null) {
                            System.out.println("Thinking");
                        }

                        if (!creating) {
                            logger.info("Thread started for combat action");
                            creating = true;
                            MovePickerRunner<AutoPlayer> run = new MovePickerRunner<>(autoPlayer);
                            new Thread(run).start();
                        }

                        if (move != null) {
                            AbstractDungeon.actionManager.addToBottom(new DoCombatMoveAction(move));
                            creating = false;
                            move = null;
                            logger.info("DoCombatMoveAction added");
                        }

                    }
                }

        }

    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        logger.info("Battle Start received");
        inBattle = true;
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        // triggers only after not losing a combat
        logger.info("Post Battle received");
        inBattle = false;
        move = null;
        creating = false;
    }

    @Override
    public void receivePostDeath() {
        logger.info("Post Death received");
        inBattle = false;
        inGame = false;

        // Save stats for this run
        // TODO handle stats for heart victories
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(RUN_DATA_PATH, true));
            int floor = AbstractDungeon.floorNum;
            writer.write("Died at floor: " + floor + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveStartGame() {
        logger.info("Start Game received");
        inBattle = false;
        inGame = true;
        starting = false;
        autoPlayer = new BasicIroncladPlayer();
    }

    @Override
    public void receivePostRender(SpriteBatch spriteBatch) {
        if (myPropertyManager.enabled && !inGame) {
            tryStartGameInQueue();
        }
    }

    public static void enqueueGame(AbstractPlayer.PlayerClass character, int ascension) {
        gameQueue.add(ImmutablePair.of(character, ascension));
    }

    public static void tryStartGameInQueue() {
        if (isStartCommandAvailable() && !starting && !gameQueue.isEmpty()) {
            logger.info("Starting game from Queue");
            starting = true;
            ImmutablePair<AbstractPlayer.PlayerClass, Integer> pair = gameQueue.remove();
            startGameAtMainMenu(pair.left, pair.right);
        }
    }

    private static void startGameAtMainMenu(AbstractPlayer.PlayerClass character, int ascensionLevel) {
        if (character != AbstractPlayer.PlayerClass.IRONCLAD) {
            throw new RuntimeException("Chose an unsupported character: " + character.toString());
        }

        Settings.seed = SeedHelper.generateUnoffensiveSeed(new Random(System.nanoTime()));
        AbstractDungeon.generateSeeds();
        AbstractDungeon.ascensionLevel = ascensionLevel;
        AbstractDungeon.isAscensionMode = ascensionLevel > 0;
        CardCrawlGame.startOver = true;
        CardCrawlGame.mainMenuScreen.isFadingOut = true;
        CardCrawlGame.mainMenuScreen.fadeOutMusic();
        new CharacterManager().setChosenCharacter(character);
        CardCrawlGame.chosenCharacter = character;
        GameStateListener.resetStateVariables();
    }

    private static boolean isStartCommandAvailable() {
        return !isInDungeon() && CardCrawlGame.mainMenuScreen != null;
    }

    private static boolean isInDungeon() {
        return CardCrawlGame.mode == CardCrawlGame.GameMode.GAMEPLAY && AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.currMapNode != null;
    }

    public static String resourcePath(String file) {
        return resourcesFolder + "/" + file;
    }

    //This determines the mod's ID based on information stored by ModTheSpire.
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(NewAIMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    static class MovePickerRunner<T extends AutoPlayer> implements Runnable {
        private final T autoPlayer;

        public MovePickerRunner(T autoPlayer) {
            this.autoPlayer = autoPlayer;
        }

        @Override
        public void run() {
            NewAIMod.move = autoPlayer.pickCombatMove();
            logger.info("Move computed");
        }
    }

}
