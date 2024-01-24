package newaimod.ai.basicIroncladPlayer;

import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import communicationmod.ChoiceScreenUtils;
import newaimod.util.DungeonInformationManager;
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
        final int ACT = DungeonInformationManager.getInstance().getActNumber();
        switch (ACT) {
            case 1:
                return evalCardActI(card, DungeonInformationManager.getInstance().getActBoss());
            case 2:
                return evalCardActII(card);
            case 3:
                return evalCardActIII(card);
            default:
                throw new RuntimeException("Unexpected Act");
        }
    }

    private double evalCardActI(AbstractCard card, DungeonInformationManager.BOSSKEY boss) {
        DungeonInformationManager.DeckInfo deck = DungeonInformationManager.getInstance().getCurrentDeckInfo();
        int floor = DungeonInformationManager.getInstance().getFloorNum();
        String[] goodDamage = {
                TwinStrike.ID, Whirlwind.ID, Headbutt.ID, PommelStrike.ID
        };

        String[] attacks = {
                TwinStrike.ID, Whirlwind.ID, Headbutt.ID, BodySlam.ID, Carnage.ID, Cleave.ID, IronWave.ID, PommelStrike.ID, Uppercut.ID
        };

        int numGoodDamage = deck.countTotalCopies(goodDamage);
        int numAttacksAdded = deck.countTotalCopies(attacks);

        switch (card.cardID) {
            case Inflame.ID:
                return 100;
            case Metallicize.ID:
            case DemonForm.ID:
                if (numGoodDamage >= 2 || numAttacksAdded >= 4) {
                    return 50;
                }
                return 10;
            case Armaments.ID:
                if (deck.containsCard(Armaments.ID)) {
                    return -1;
                }
                if (card.upgraded) {
                    return 48.5;
                }
                return 20;
            case BattleTrance.ID:
                if (deck.containsCard(BattleTrance.ID)) {
                    return -1;
                }
                if (numGoodDamage >= 2 || numAttacksAdded >= 4) {
                    return 50;
                }
                return 25;
            case Whirlwind.ID:
                if (!deck.containsCard(Whirlwind.ID) && !deck.containsCard(Cleave.ID)) {
                    return 100;
                }
                if (deck.countTotalCopies(Whirlwind.ID) <= 1 && numAttacksAdded <= 4) {
                    return 50;
                }
                return -1;
            case TwinStrike.ID:
                if (numAttacksAdded <= 4) {
                    return 49;
                }
                return -1;
            case Headbutt.ID:
                if (numAttacksAdded <= 4) {
                    return 48;
                }
                return -1;
            case PommelStrike.ID:
                if (numAttacksAdded <= 4) {
                    return 47;
                }
                return -1;
            case BodySlam.ID:
                if (deck.containsCard(BodySlam.ID)) {
                    return -1;
                }
                if (card.upgraded) {
                    return 48.5;
                }
                if (numAttacksAdded <= 4) {
                    return 46.5;
                }
                return -1;
            case Uppercut.ID:
                if (numAttacksAdded <= 3) {
                    return 46;
                }
                return -1;
            case Cleave.ID:
                if (!deck.containsCard(Whirlwind.ID) && !deck.containsCard(Cleave.ID)) {
                    return 95;
                }
                if (numAttacksAdded <= 3) {
                    return 45;
                }
                return -1;
            case Carnage.ID:
                if (numAttacksAdded <= 3) {
                    return 44;
                }
                return -1;
            case IronWave.ID:
                if (numAttacksAdded <= 2) {
                    return 15;
                }
                return -1;
            case ShrugItOff.ID:
            case FlameBarrier.ID:
                if (numAttacksAdded >= 3) {
                    return 46.5;
                }
                return 10;
            default:
                return -1;
        }
    }

    private double evalCardActII(AbstractCard card) {
        ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;
        switch (card.cardID) {
            case Inflame.ID:
                return 10;
            case Metallicize.ID:
                return 1;
            case DemonForm.ID:
                return 5;
            case Armaments.ID:
                for (AbstractCard c : deck) {
                    if (c.cardID.equals(Armaments.ID)) {
                        return -1;
                    }
                }
                return 8;
            case BattleTrance.ID:
                for (AbstractCard c : deck) {
                    if (c.cardID.equals(BattleTrance.ID)) {
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

    private double evalCardActIII(AbstractCard card) {
        ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;
        switch (card.cardID) {
            case Inflame.ID:
                return 10;
            case Metallicize.ID:
                return 1;
            case DemonForm.ID:
                return 5;
            case Armaments.ID:
                for (AbstractCard c : deck) {
                    if (c.cardID.equals(Armaments.ID)) {
                        return -1;
                    }
                }
                return 8;
            case BattleTrance.ID:
                for (AbstractCard c : deck) {
                    if (c.cardID.equals(BattleTrance.ID)) {
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
