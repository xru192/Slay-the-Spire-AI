package newaimod.ai;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.*;
import com.megacrit.cardcrawl.events.city.*;
import com.megacrit.cardcrawl.events.exordium.*;
import com.megacrit.cardcrawl.events.shrines.*;
import com.megacrit.cardcrawl.neow.NeowEvent;
import communicationmod.ChoiceScreenUtils;
import newaimod.NewAIMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * An AbstractEventAutoPlayer implementation progresses the game at an event room.
 */
public abstract class AbstractEventAutoPlayer {
    public static final Logger logger = LogManager.getLogger(AbstractEventAutoPlayer.class.getName());

    public void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.EVENT) {
            logger.info("Done: choice type not suitable");
            return;
        }
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        logger.info("Event choices: " + choices);
        if (choices.isEmpty()) {
            logger.info("Done: no valid choices");
            return;
        }

        AbstractEvent event = AbstractDungeon.getCurrRoom().event;

        // Select the only option if there is only one, e.g. [Leave], [Continue]
        if (choices.size() == 1) {
            choose(choices.get(0));
            if (event instanceof NeowEvent) {
                if (choices.get(0).equals("leave")) {
                    NewAIMod.delayAction(100); // various problems arise after neow rewards
                    // e.g. random rare not obtained, upgrade card relics don't work
                }
            }
            logger.info("Done");
            return;
        }

        if (event instanceof NeowEvent) {
            executeNeowEvent();
            return;
        }

        if (event instanceof AccursedBlacksmith) {
            executeAccursedBlacksmith();
        } else if (event instanceof Addict) {
            executeAddict();
        } else if (event instanceof BackToBasics) {
            executeBackToBasics();
        } else if (event instanceof Beggar) {
            executeBeggar();
        } else if (event instanceof BigFish) {
            executeBigFish();
        } else if (event instanceof Colosseum) {
            executeColosseum();
        } else if (event instanceof CursedTome) {
            executeCursedTome();
        } else if (event instanceof DeadAdventurer) {
            executeDeadAdventurer();
        } else if (event instanceof Designer) {
            boolean adjustmentUpgradesOne = ReflectionHacks.getPrivate(event, Designer.class, "adjustmentUpgradesOne");
            boolean cleanUpRemovesCards = ReflectionHacks.getPrivate(event, Designer.class, "cleanUpRemovesCards");
            executeDesigner(adjustmentUpgradesOne, cleanUpRemovesCards);
        } else if (event instanceof DrugDealer) {
            executeDrugDealer();
        } else if (event instanceof Duplicator) {
            executeDuplicator();
        } else if (event instanceof FaceTrader) {
            executeFaceTrader();
        } else if (event instanceof Falling) {
            executeFalling();
        } else if (event instanceof ForgottenAltar) {
            executeForgottenAltar();
        } else if (event instanceof FountainOfCurseRemoval) {
            executeFountainOfCurseRemoval();
        } else if (event instanceof Ghosts) {
            executeGhosts();
        } else if (event instanceof GoldenIdolEvent) { // multi-choice event
            boolean isFirstDecision = choices.contains("take");
            executeGoldenIdolEvent(isFirstDecision);
        } else if (event instanceof GoldShrine) {
            executeGoldShrine();
        } else if (event instanceof GoldenWing) {
            executeGoldenWing();
        } else if (event instanceof KnowingSkull) {
            executeKnowingSkull();
        } else if (event instanceof Sssserpent) {
            executeSssserpent();
        } else if (event instanceof LivingWall) {
            executeLivingWall();
        } else if (event instanceof MaskedBandits) {
            executeMaskedBandits();
        } else if (event instanceof GremlinMatchGame) {
            executeGremlinMatchGame();
        } else if (event instanceof MindBloom) {
            executeMindBloom();
        } else if (event instanceof Mushrooms) {
            executeMushrooms();
        } else if (event instanceof MysteriousSphere) {
            executeMysteriousSphere();
        } else if (event instanceof Nloth) {
            executeNloth();
        } else if (event instanceof Nest) {
            executeNest();
        } else if (event instanceof NoteForYourself) {
            executeNoteForYourself();
        } else if (event instanceof PurificationShrine) {
            executePurificationShrine();
        } else if (event instanceof ScrapOoze) {
            executeScrapOoze();
        } else if (event instanceof SecretPortal) {
            executeSecretPortal();
        } else if (event instanceof SensoryStone) {
            executeSensoryStone();
        } else if (event instanceof ShiningLight) {
            executeShiningLight();
        } else if (event instanceof Cleric) {
            executeCleric();
        } else if (event instanceof TheJoust) {
            executeTheJoust();
        } else if (event instanceof TheLibrary) {
            executeTheLibrary();
        } else if (event instanceof TheMausoleum) {
            executeTheMausoleum();
        } else if (event instanceof MoaiHead) {
            executeMoaiHead();
        } else if (event instanceof WomanInBlue) {
            executeWomanInBlue();
        } else if (event instanceof TombRedMask) {
            executeTombRedMask();
        } else if (event instanceof Transmogrifier) {
            executeTransmogrifier();
        } else if (event instanceof UpgradeShrine) {
            executeUpgradeShrine();
        } else if (event instanceof Vampires) {
            executeVampires();
        } else if (event instanceof WeMeetAgain) {
            executeWeMeetAgain();
        } else if (event instanceof WindingHalls) {
            executeWindingHalls();
        } else if (event instanceof GoopPuddle) {
            executeGoopPuddle();
        } else {
            logger.info("Failed to make a choice: unknown event");
        }
    }

    /**
     * Makes the given event choice if it is valid.
     *
     * @param choice The string inside the brackets in-game of the option to be chosen.
     */
    public void choose(String choice) {
        try {
            ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
            logger.info("Making choice: " + choice);
            ChoiceScreenUtils.makeEventChoice(choices.indexOf(choice));
        } catch (Exception e) {
            logger.info("Failed to make choice: " + choice + ". Error: " + e.getMessage());
        }
    }

    /**
     * If possible, chooses the first option. Otherwise, chooses the second option.
     *
     * @param first  the preferred choice
     * @param second the choice that is only chosen if first is not possible
     */
    public void tryChoose(String first, String second) {
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        if (choices.contains(first)) {
            choose(first);
            return;
        }
        choose(second);
    }

    /**
     * If possible, chooses the first option. Otherwise, if possible chooses the second option.
     * Otherwise, chooses the third option.
     *
     * @param first  the preferred choice
     * @param second the preferred choice if first is not possible
     * @param third  the choice that is only chosen if the first two are not possible
     */
    public void tryChoose(String first, String second, String third) {
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        if (choices.contains(first)) {
            choose(first);
            return;
        }

        if (choices.contains(second)) {
            choose(second);
            return;
        }
        choose(third);
    }

    public void tryChoose(String first, String second, String third, String fourth) {
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        if (choices.contains(first)) {
            choose(first);
            return;
        }

        if (choices.contains(second)) {
            choose(second);
            return;
        }

        if (choices.contains(third)) {
            choose(third);
            return;
        }

        choose(fourth);
    }

    /**
     * Chooses an option at the Neow event. Precondition: user is at the main decision point
     * of choosing between 2 basic options, or 4 enhanced options.
     */
    public abstract void executeNeowEvent();

    public abstract void executeAccursedBlacksmith();

    public abstract void executeAddict();

    public abstract void executeBackToBasics();

    public abstract void executeBeggar();

    public abstract void executeBigFish();

    public abstract void executeColosseum();

    public abstract void executeCursedTome();

    public abstract void executeDeadAdventurer();

    /**
     * Chooses one of the following options at the Designer event:<br>
     * - [Adjustments] - Lose 40 [50] gold. Upgrade a card/Upgrade 2 random cards.<br>
     * - [Clean Up] - Lost 60 [75] gold. Remove a card/Transform 2 cards.<br>
     * - [Full Service] - Lose 90 [110] gold. Remove a card and upgrade a random card.<br>
     * - [Punch] - Lose 3 HP.
     *
     * @param adjustmentUpgradesOne whether [Adjustments] upgrades a card
     * @param cleanUpRemovesCards   whether [Clean Up] removes a card
     */
    public abstract void executeDesigner(boolean adjustmentUpgradesOne, boolean cleanUpRemovesCards);

    public abstract void executeDrugDealer();

    public abstract void executeDuplicator();

    public abstract void executeFaceTrader();

    public abstract void executeFalling();

    public abstract void executeForgottenAltar();

    public abstract void executeFountainOfCurseRemoval();

    public abstract void executeGhosts();

    /**
     * Chooses one of the following options at the Golden Idol event: <br>
     * (First decision point)<br>
     * - [Take] - Obtain Golden Idol. Go to second decision point.<br>
     * - [Leave] - Nothing.<br>
     * (Second decision point)<br>
     * - [Outrun] - Become Cursed - Injury.<br>
     * - [Smash] - Take damage equal to 25% [35] of max HP.<br>
     * - [Hide] - Lose 8% [10] of max HP.<br>
     * Precondition: user is at either the first or second decision point.
     *
     * @param isFirstDecision whether user is at the first decision point.
     */
    public abstract void executeGoldenIdolEvent(boolean isFirstDecision);

    public abstract void executeGoldShrine();

    public abstract void executeGoldenWing();

    public abstract void executeKnowingSkull();

    public abstract void executeSssserpent();

    public abstract void executeLivingWall();

    public abstract void executeMaskedBandits();

    public abstract void executeGremlinMatchGame();

    public abstract void executeMindBloom();

    public abstract void executeMushrooms();

    public abstract void executeMysteriousSphere();

    public abstract void executeNloth();

    public abstract void executeNest();

    public abstract void executeNoteForYourself();

    public abstract void executePurificationShrine();

    public abstract void executeScrapOoze();

    public abstract void executeSecretPortal();

    public abstract void executeSensoryStone();

    public abstract void executeShiningLight();

    public abstract void executeCleric();

    public abstract void executeTheJoust();

    public abstract void executeTheLibrary();

    public abstract void executeTheMausoleum();

    public abstract void executeMoaiHead();

    public abstract void executeWomanInBlue();

    public abstract void executeTombRedMask();

    public abstract void executeTransmogrifier();

    public abstract void executeUpgradeShrine();

    public abstract void executeVampires();

    public abstract void executeWeMeetAgain();

    public abstract void executeWindingHalls();

    public abstract void executeGoopPuddle();


}
