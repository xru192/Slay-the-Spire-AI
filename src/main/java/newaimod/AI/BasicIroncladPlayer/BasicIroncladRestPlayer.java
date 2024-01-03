package newaimod.AI.BasicIroncladPlayer;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;
import communicationmod.ChoiceScreenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BasicIroncladRestPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladRestPlayer.class.getName());

    /**
     * Rest if health is below 60, upgrade otherwise.
     */
    public void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.REST) {
            logger.info("Done: choice type not suitable");
            return;
        }
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        logger.info("Choosing between: " + choices.toString());
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

        // pick the first option
        if (!choices.isEmpty()) {
            choose(choices.get(0));
        }
        logger.info("Done");

    }

    public static void choose(String choice) {
        try {
            ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
            logger.info("Making choice: " + choice);
            ChoiceScreenUtils.makeRestRoomChoice(choices.indexOf(choice));
        } catch (Exception e) {
            logger.info("Failed to make choice: " + choice + ". Error: " + e.getMessage());
        }
    }
}
