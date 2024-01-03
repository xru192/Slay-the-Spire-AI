package newaimod.util.Simulator;

import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractSimpleCard {
    private boolean isUpgraded;
    AbstractCard.CardType type;
    public int cost;
    public int damage;
    public int block;


    abstract public void upgrade();
}
