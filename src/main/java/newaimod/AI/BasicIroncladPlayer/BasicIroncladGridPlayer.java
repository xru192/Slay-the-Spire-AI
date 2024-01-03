package newaimod.AI.BasicIroncladPlayer;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import communicationmod.ChoiceScreenUtils;
import communicationmod.patches.GridCardSelectScreenPatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BasicIroncladGridPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladGridPlayer.class.getName());

    void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.GRID) {
            logger.info("Done: choice type not suitable");
            return;
        }

        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        logger.info("Choosing between: " + choices);
        GridCardSelectScreen screen = AbstractDungeon.gridSelectScreen;
        if (screen.confirmScreenUp) {
            logger.info("Pressing confirm");
            ChoiceScreenUtils.pressConfirmButton();
            logger.info("Done");
            return;
        }

        int numCards = ReflectionHacks.getPrivate(AbstractDungeon.gridSelectScreen, GridCardSelectScreen.class, "numCards");
        String tipMsg = ReflectionHacks.getPrivate(AbstractDungeon.gridSelectScreen, GridCardSelectScreen.class, "tipMsg");
        logger.info("Number of Cards to Select:" + numCards);
        logger.info("Tip Message: " + tipMsg);

        ArrayList<AbstractCard> gridScreenCards = ChoiceScreenUtils.getGridScreenCards();
        for (int i = 0; i < numCards; ++i) {
            choose(gridScreenCards.get(i));
        }

        ChoiceScreenUtils.pressConfirmButton();
    }

    public static void choose(AbstractCard choice) {
        try {
            ArrayList<AbstractCard> gridScreenCards = ChoiceScreenUtils.getGridScreenCards();
            GridCardSelectScreenPatch.hoverCard = gridScreenCards.get(gridScreenCards.indexOf(choice));
            GridCardSelectScreenPatch.replaceHoverCard = true;
            logger.info("Making choice: " + choice);
            AbstractDungeon.gridSelectScreen.update();
        } catch (Exception e) {
            logger.info("Failed to make choice: " + choice + ". Error: " + e.getMessage());
        }
    }

}
