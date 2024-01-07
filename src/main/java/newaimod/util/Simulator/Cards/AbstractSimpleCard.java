package newaimod.util.Simulator.Cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;

public abstract class AbstractSimpleCard {

    public String cardID;
    public CombatSimulator simulator;  // the simulator this card belongs to
    public boolean targetsOne;  // whether this card targets a single monster (not multi/self/random target)

    public boolean isUpgraded;
    public AbstractCard.CardType type;
    public int cost;
    public int damage = 0;
    public int block = 0;

    public AbstractSimpleCard(CombatSimulator simulator, String id, AbstractCard.CardType type, int cost, boolean targetsOne) {
        this.simulator = simulator;
        cardID = id;
        this.isUpgraded = false;
        this.type = type;
        this.cost = cost;
        this.targetsOne = targetsOne;
    }

    /**
     * Returns whether this card can be played on the specified target.
     *
     * @param target the target to play this card on
     * @return whether this card can be played
     */
    abstract public boolean canPlay(SimpleMonster target);

    abstract public void play(SimpleMonster target);

    abstract public void upgrade();

    abstract public AbstractSimpleCard copy(CombatSimulator simulator);

    @Override
    public String toString() {
        return "AbstractSimpleCard{" +
                "cardID='" + cardID + '\'' +
                '}';
    }
}
