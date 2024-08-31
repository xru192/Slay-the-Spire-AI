package newaimod.util.simulator.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.exceptions.CombatlessCardException;

public abstract class AbstractSimpleCard {

    public CombatSimulator simulator;  // the simulator this card can be played in, null if not in combat
    public final String cardID;
    public boolean targetsOne;  // whether this card targets a single monster (not multi/self/random target)
    public final AbstractCard.CardType type;

    public boolean isUpgraded;
    public int cost;
    public int damage = 0;
    public int block = 0;
    public int magicNumber = 0;

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

    public final boolean meetsEnoughEnergy(int cost) {
        if (simulator == null) throw new CombatlessCardException();
        return simulator.player.getEnergy() >= cost;
    }

    public final boolean meetsNotEntangled() {
        if (simulator == null) throw new CombatlessCardException();
        return !simulator.player.isEntangled();
    }

    public final boolean meetsAlive(SimpleMonster m) {
        if (simulator == null) throw new CombatlessCardException();
        return m != null && m.isAlive();
    }

    @Override
    public String toString() {
        return "AbstractSimpleCard{" +
                "cardID='" + cardID + '\'' +
                '}';
    }
}
