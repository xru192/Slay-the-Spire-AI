package newaimod.AI.BasicIroncladPlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.AI.AbstractGridAutoPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BasicIroncladGridPlayer extends AbstractGridAutoPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladGridPlayer.class.getName());


    @Override
    protected AbstractCard chooseUpgrade(ArrayList<AbstractCard> options, ArrayList<AbstractCard> deck) {
        return options.get(0);
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

    @Override
    protected AbstractCard chooseHeadbuttCard(ArrayList<AbstractCard> options) {
        return options.get(0);
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
