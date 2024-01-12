package newaimod.AI;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;
import communicationmod.ChoiceScreenUtils;
import newaimod.NewAIMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * An AbstractRestAutoPlayer implementation progresses the game at a rest site.
 */
public abstract class AbstractRestAutoPlayer {
    public static final Logger logger = LogManager.getLogger(AbstractRestAutoPlayer.class.getName());

    protected enum RESTCHOICE {
        REST, SMITH, LIFT, TOKE, DIG, RECALL;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.REST) {
            logger.info("Done: choice type not suitable");
            return;
        }
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        logger.info("Choosing between: " + choices);
        if (ChoiceScreenUtils.isConfirmButtonAvailable()) {
            logger.info("Pressing confirm");
            ChoiceScreenUtils.pressConfirmButton();
            logger.info("Done");
            return;
        }
        if (((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI.somethingSelected) {
            logger.info("Done: something was already selected");
            return;
        }
        if (choices.size() == 1) {
            logger.info("Done: making only choice");
            ChoiceScreenUtils.makeRestRoomChoice(0);
            return;
        }

        boolean canRest = choices.contains("rest");
        boolean canSmith = choices.contains("smith");
        boolean canLift = choices.contains("lift");
        boolean canToke = choices.contains("toke");
        boolean canDig = choices.contains("dig");
        boolean canRecall = choices.contains("recall");

        RESTCHOICE choice = choose(canRest, canSmith, canLift, canToke, canDig, canRecall);
        boolean chosen = choose(choice);
        if (chosen) {
            logger.info("Chose " + choice);
            logger.info("Done");
        } else {
            logger.error("AbstractRestAutoPlayer implementation chose an invalid rest site option.");
        }

    }

    /**
     * Returns a choice to perform at the current rest site. Only returns an available option. Precondition: at least 2
     * options are available.
     *
     * @param canRest   whether "Rest" is an available option
     * @param canSmith  whether "Smith" is an available option
     * @param canLift   whether "Lift" is an available option
     * @param canToke   whether "Toke" is an available option
     * @param canDig    whether "Dig" is an available option
     * @param canRecall whether "Recall" is an available option
     * @return choice to perform at the current rest site
     */
    protected abstract RESTCHOICE choose(boolean canRest, boolean canSmith, boolean canLift, boolean canToke, boolean canDig, boolean canRecall);

    private static boolean choose(RESTCHOICE choice) {
        try {
            ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
            if (!choices.contains(choice.toString())) {
                return false;
            }
            ChoiceScreenUtils.makeRestRoomChoice(choices.indexOf(choice.toString()));
            return true;
        } catch (Exception e) {
            logger.info("Failed to make choice: " + choice + ". Error: " + e.getMessage());
            return false;
        }
    }

}
