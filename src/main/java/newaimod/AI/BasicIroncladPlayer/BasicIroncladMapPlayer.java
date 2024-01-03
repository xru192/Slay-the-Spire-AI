package newaimod.AI.BasicIroncladPlayer;

import communicationmod.ChoiceScreenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BasicIroncladMapPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladMapPlayer.class.getName());

    void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.MAP) return;
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        logger.info("Choosing between: " + choices);
        if (choices.isEmpty()) {
            logger.info("Done: no choices");
            return;
        }

        if (choices.size() == 1) {
            // will handle when there is only one child, like final rest -> boss
            choose(choices.get(0));
            logger.info("Done");
            return;
        }

        choose(choices.get(0));
    }

    public static void choose(String choice) {
        try {
            ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
            logger.info("Making choice: " + choice);
            ChoiceScreenUtils.makeMapChoice(choices.indexOf(choice));
        } catch (Exception e) {
            logger.info("Failed to make choice: " + choice + ". Error: " + e.getMessage());
        }
    }
}
