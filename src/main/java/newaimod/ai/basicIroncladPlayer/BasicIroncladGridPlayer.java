package newaimod.ai.basicIroncladPlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.*;
import newaimod.ai.AbstractGridAutoPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BasicIroncladGridPlayer extends AbstractGridAutoPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladGridPlayer.class.getName());

    private double evalUpgrade(AbstractCard card) {
        switch (card.cardID) {
            case Armaments.ID:
                return 100;
            case Whirlwind.ID:
                return 99;
            case BodySlam.ID:
                return 98;
            case Uppercut.ID:
                return 97;
            case Bash.ID:
                return 50;
            case TwinStrike.ID:
                return 49;
            case Strike_Red.ID:
            case Defend_Red.ID:
                return -1;
            default:
                return 0;
        }
    }

    @Override
    protected AbstractCard chooseUpgrade(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        double bestEval = -1_000_000;
        AbstractCard bestCard = null;
        for (AbstractCard card : options) {
            if (!card.upgraded || card.cardID.equals(SearingBlow.ID)) {
                double eval = evalUpgrade(card);
                if (eval > bestEval) {
                    bestEval = eval;
                    bestCard = card;
                }
            }
        }

        return bestCard;
    }

    @Override
    protected AbstractCard chooseTransformOne(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return options.get(0);
    }

    @Override
    protected CardPair chooseTransformTwo(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return new CardPair(options.get(0), options.get(1));
    }

    @Override
    protected AbstractCard chooseRemoveOne(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return options.get(0);
    }

    @Override
    protected CardPair chooseRemoveTwo(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return new CardPair(options.get(0), options.get(1));
    }

    private double evalHeadbuttCard(AbstractCard card) {
        switch (card.cardID) {
            case Inflame.ID:
            case Metallicize.ID:
            case DemonForm.ID:
                return card.upgraded ? 10 : 9;
            case Armaments.ID:
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
                return card.upgraded ? 2 : 1;
            case Strike_Red.ID:
            case Defend_Red.ID:
                return card.upgraded ? 0.5 : 0;
            default:
                return -1;
        }
    }

    @Override
    protected AbstractCard chooseHeadbuttCard(ArrayList<AbstractCard> options) {
        double bestEval = -1_000_000;
        AbstractCard bestCard = null;
        for (AbstractCard card : options) {
            double eval = evalHeadbuttCard(card);
            if (eval > bestEval) {
                bestEval = eval;
                bestCard = card;
            }
        }

        return bestCard;
    }

    @Override
    protected AbstractCard chooseBottledFlameCard(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return options.get(0);
    }

    @Override
    protected AbstractCard chooseBottledLightningCard(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return options.get(0);
    }

    @Override
    protected AbstractCard chooseBottledTornadoCard(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return options.get(0);
    }

    @Override
    protected CardTriplet chooseAstrolabeCards(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return new CardTriplet(options.get(0), options.get(1), options.get(2));
    }
}
