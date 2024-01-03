package newaimod.AI.BasicIroncladPlayer;

import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import communicationmod.ChoiceScreenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BasicIroncladHandSelectPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladHandSelectPlayer.class.getName());


    void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.HAND_SELECT) {
            logger.info("Done: choice type not suitable");
            return;
        }

        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        logger.info("Choosing between: " + choices);
        if (choices.isEmpty()) {
            logger.info("Done: no choices");
            return;
        }

        if (AbstractDungeon.actionManager.currentAction instanceof GamblingChipAction) {
            logger.info("Processing Gambling Chip");
            if (ChoiceScreenUtils.isConfirmButtonAvailable()) {
                logger.info("Pressing confirm");
                ChoiceScreenUtils.pressConfirmButton();
            }
            logger.info("Done");
            return;
        }

        int toSelect = AbstractDungeon.handCardSelectScreen.numCardsToSelect;
        for (int i = 0; i < toSelect; i++) {
            ChoiceScreenUtils.makeHandSelectScreenChoice(0);
        }
        ChoiceScreenUtils.pressConfirmButton();
        logger.info("Done");
    }
}
