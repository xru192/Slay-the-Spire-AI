package newaimod.AI;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import communicationmod.ChoiceScreenUtils;
import communicationmod.patches.GridCardSelectScreenPatch;
import newaimod.NewAIMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An AbstractGridAutoPlayer implementation progresses the game at a grid selection screen.
 */
public abstract class AbstractGridAutoPlayer {
    public static Logger logger = LogManager.getLogger(AbstractGridAutoPlayer.class.getName());

    public final void execute() {
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
        if (gridScreenCards.isEmpty()) {
            logger.info("Done: grid contains no cards");
            return;
        }
        ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;


//        AbstractCard defaultChoice = gridScreenCards.get(0);

        if (screen.forUpgrade) {
            logger.info("Processing Upgrade");
            AbstractCard upgradeChoice = chooseUpgrade(gridScreenCards, deck);
            choose(upgradeChoice);
        } else if (screen.forTransform) {
            logger.info("Processing Transform");
            if (numCards == 1) {
                AbstractCard transformChoice = chooseTransformOne(gridScreenCards, deck);
                choose(transformChoice);
            } else if (numCards == 2) {
                CardPair transformChoice = chooseTransformTwo(gridScreenCards, deck);
                choose(transformChoice.first);
                choose(transformChoice.second);
            } else {
                assert false;
            }
        } else if (screen.forPurge) {
            logger.info("Processing Purge");
            if (numCards == 1) {
                AbstractCard removeChoice = chooseRemoveOne(gridScreenCards, deck);
                choose(removeChoice);
            } else if (numCards == 2) {
                CardPair removeChoice = chooseRemoveTwo(gridScreenCards, deck);
                choose(removeChoice.first);
                choose(removeChoice.second);
            } else {
                assert false;
            }
        } else if (AbstractDungeon.actionManager.currentAction instanceof DiscardPileToTopOfDeckAction) {
            logger.info("Processing DiscardPileToTopOfDeck");
            AbstractCard choice = chooseHeadbuttCard(gridScreenCards);
            choose(choice);
        } else if (tipMsg.equals("Choose a card for Bottled Flame.")) {
            logger.info("Processing Bottled Flame");
            AbstractCard choice = chooseBottledFlameCard(gridScreenCards, deck);
            choose(choice);
        } else if (tipMsg.equals("Choose a card for Bottled Lightning.")) {
            logger.info("Processing Bottled Lightning");
            AbstractCard choice = chooseBottledLightningCard(gridScreenCards, deck);
            choose(choice);
        } else if (tipMsg.equals("Choose a card for Bottled Tornado.")) {
            logger.info("Processing Bottled Tornado");
            AbstractCard choice = chooseBottledTornadoCard(gridScreenCards, deck);
            choose(choice);
        } else if (tipMsg.equals("Choose 3 cards for Astrolabe.")) {
            logger.info("Processing Astrolabe");
            CardTriplet choice = chooseAstrolabeCards(gridScreenCards, deck);
            choose(choice.first);
            choose(choice.second);
            choose(choice.third);
            NewAIMod.delayAction(10);
        } else if (tipMsg.equals("The Bell Tolls...")) {
            logger.info("Processing Toll of the Bell");
            logger.info("Pressing confirm");
            ChoiceScreenUtils.pressConfirmButton();
        } else {
            // possibilities: duplicator, others
            logger.info("Processing unknown grid selection");
            assert false;
        }

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

    protected abstract AbstractCard chooseUpgrade(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected abstract AbstractCard chooseTransformOne(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected abstract CardPair chooseTransformTwo(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected abstract AbstractCard chooseRemoveOne(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected abstract CardPair chooseRemoveTwo(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected abstract AbstractCard chooseHeadbuttCard(ArrayList<AbstractCard> options);

    protected abstract AbstractCard chooseBottledFlameCard(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected abstract AbstractCard chooseBottledLightningCard(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected abstract AbstractCard chooseBottledTornadoCard(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected abstract CardTriplet chooseAstrolabeCards(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck);

    protected static class CardPair {
        @NotNull
        AbstractCard first;
        @NotNull
        AbstractCard second;
        public CardPair(@NotNull AbstractCard first, @NotNull AbstractCard second) {
            this.first = first;
            this.second = second;
        }
    }

    protected static class CardTriplet {
        @NotNull
        AbstractCard first;
        @NotNull
        AbstractCard second;
        @NotNull
        AbstractCard third;
        public CardTriplet(@NotNull AbstractCard first, @NotNull AbstractCard second, @NotNull AbstractCard third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

}
