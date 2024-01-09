package newaimod.AI.BasicIroncladPlayer;

import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import communicationmod.ChoiceScreenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BasicIroncladCardRewardPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladCardRewardPlayer.class.getName());

    void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.CARD_REWARD) {
            logger.info("Done: choice type not suitable");
            return;
        }

        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList(); // can include "bowl"
        logger.info("Choosing between: " + choices);


        if (AbstractDungeon.actionManager.currentAction instanceof DiscoveryAction) {
            executeDiscoveryAction();
            return;
        }

        executeAddCardToDeck();

    }

    void executeDiscoveryAction() {
        ChoiceScreenUtils.makeCardRewardChoice(0);
    }

    void executeAddCardToDeck() {
        ArrayList<AbstractCard> rewardCards = AbstractDungeon.cardRewardScreen.rewardGroup;

        double maxEval = -1;
        AbstractCard maxCard = null;
        for (AbstractCard card : rewardCards) {
            double eval = evalCard(card);
            if (eval > maxEval) {
                maxEval = eval;
                maxCard = card;
            }
        }

        if (maxCard == null) {
            ChoiceScreenUtils.pressCancelButton();
            return;
        }

        ChoiceScreenUtils.makeCardRewardChoice(rewardCards.indexOf(maxCard));

    }

    /**
     * Returns an evaluation signalling how good it would be to add the specified card to the player's deck. The greater
     * the evaluation the better, and a negative evaluation means adding the card is worse than adding nothing.
     *
     * @param card the card to evaluate adding to the player's deck
     * @return evaluation of the card
     */
    private double evalCard(AbstractCard card) {
        ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;
        switch (card.cardID) {
            case Inflame.ID:
                return 10;
            case Armaments.ID:
                for (AbstractCard c : deck) {
                    if (c.cardID.equals(Armaments.ID)) {
                        return -1;
                    }
                }
                return 8;
            case Cleave.ID:
            case Headbutt.ID:
            case IronWave.ID:
            case PommelStrike.ID:
            case ShrugItOff.ID:
            case TwinStrike.ID:
            case FlameBarrier.ID:
            case Uppercut.ID:
            case Whirlwind.ID:
            case Carnage.ID:
                return 1;
            default:
                return -1;
        }
    }
}
