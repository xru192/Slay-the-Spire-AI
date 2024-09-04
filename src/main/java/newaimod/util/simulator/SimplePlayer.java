package newaimod.util.simulator;

import newaimod.util.simulator.cards.AbstractSimpleCard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static newaimod.util.CombatUtils.handsEqual;

/**
 * A SimplePlayer represents a simplified version of the state of the player during combat. This includes health, a
 * current hand of cards, and various attributes.
 */
public class SimplePlayer {
    @NotNull
    public final CombatSimulator simulator;
    public ArrayList<AbstractSimpleCard> hand;
    private int energy;
    private int health;
    private int block;
    private int strength;
    private int dexterity;
    private boolean weakened;
    private boolean frail;
    private int metallicize;
    private int demonForm;
    private boolean entangled;
    private boolean vulnerable;
    private boolean noDraw;
    private int exhaustedSlimed;
    private int cardsDrawnWith0Energy;      // # cards drawn while player has at least 1 energy left (after simulator start)
    private int cardsDrawnWith1Energy;      // # cards drawn while player has at least 1 energy left (after simulator start)
    private int cardsDrawnWith2Energy;      // # cards drawn while player has at least 2 energy left (after simulator start)
    private int cardsDrawnWith3Energy;      // # cards drawn while player has at least 3 energy left (after simulator start)

    /**
     * A "default" SimplePlayer. The default player has 80 health, 0 block, 3 energy, an empty hand, and no powers.
     *
     * @param simulator the simulator the player belongs to
     */
    public SimplePlayer(@NotNull CombatSimulator simulator) {
        this.simulator = simulator;
        hand = new ArrayList<>();
        energy = 3;
        health = 80;
        block = 0;
        strength = 0;
        dexterity = 0;
        weakened = false;
        frail = false;
        metallicize = 0;
        demonForm = 0;
        entangled = false;
        vulnerable = false;
        noDraw = false;
        exhaustedSlimed = 0;
        cardsDrawnWith0Energy = 0;
        cardsDrawnWith1Energy = 0;
        cardsDrawnWith2Energy = 0;
        cardsDrawnWith3Energy = 0;
    }

    public SimplePlayer(@NotNull SimplePlayer player, @NotNull CombatSimulator simulator) {
        this.simulator = simulator;
        this.hand = new ArrayList<>();
        for (AbstractSimpleCard card : player.hand) {
            this.hand.add(card.copy(simulator));
        }
        this.energy = player.energy;
        this.health = player.health;
        this.block = player.block;
        this.strength = player.strength;
        this.dexterity = player.dexterity;
        this.weakened = player.weakened;
        this.frail = player.frail;
        this.metallicize = player.metallicize;
        this.demonForm = player.demonForm;
        this.entangled = player.entangled;
        this.vulnerable = player.vulnerable;
        this.noDraw = player.noDraw;
        this.exhaustedSlimed = player.exhaustedSlimed;
        this.cardsDrawnWith0Energy = player.cardsDrawnWith0Energy;
        this.cardsDrawnWith1Energy = player.cardsDrawnWith1Energy;
        this.cardsDrawnWith2Energy = player.cardsDrawnWith2Energy;
        this.cardsDrawnWith3Energy = player.cardsDrawnWith3Energy;
    }

    public void payForAndUseCard(AbstractSimpleCard card) {
        assert hand.contains(card);
        hand.remove(card);
        energy -= card.cost;
    }

    /**
     * Trigger any powers or relics which occur on the player plays a card.
     */
    public void onUseCard(AbstractSimpleCard card) {
        // TODO add relic effects
    }

    /**
     * Have this player trigger all effects which occur due to powers when the player ends their turn. Examples:
     * Metallicize, Combust.
     */
    public void triggerEndTurnPowers() {
        assert metallicize >= 0;
        block += metallicize;
    }

    /**
     * Returns how much damage an attack will do (per hit) after player modifications. These include strength and
     * weakened. TODO relics
     *
     * @param base the base damage of the attack (per hit)
     * @return the damage after player modifications
     */
    public int getModifiedDamage(int base) {
        double weakFactor = weakened ? 0.75 : 1.0;
        int result = (int) ((base + strength) * weakFactor);
        return Math.max(0, result);
    }


    /**
     * Returns how much block will be gained by a card after player modifications. These include dexterity and frail.
     * TODO relics
     *
     * @param base the base block gain of the card
     * @return the block gain after player modifications
     */
    public int getModifiedBlock(int base) {
        double frailFactor = frail ? 0.75 : 1.0;
        int result = (int) ((base + dexterity) * frailFactor);
        return Math.max(0, result);
    }

    /**
     * Have this player take damage from a source which is not a monster's attack. Examples: sharp hide, thorns, decay.
     *
     * @param damage the amount of damage the player takes
     */
    public void takeDamage(int damage) {
        assert damage >= 0;
        int blockLoss = Math.min(block, damage);
        int healthLoss = Math.min(health, Math.max(0, damage - block));
        block -= blockLoss;
        health -= healthLoss;
    }

    /**
     * Have this player take an attack from a monster.
     *
     * @param damage the damage of the attack (after monster-side modifications)
     */
    public void takeAttack(int damage) {
        assert damage >= 0;
        if (vulnerable) {
            damage = damage + damage / 2;
        }

        int blockLoss = Math.min(block, damage);
        int healthLoss = Math.min(health, Math.max(0, damage - block));
        block -= blockLoss;
        health -= healthLoss;
    }

    /**
     * Have this player exhaust a Slimed card.
     */
    public void exhaustSlimed() {
        ++exhaustedSlimed;
    }

    /**
     * Have this player draw cards from the draw pile. If the source of draw is from a card, this method should be
     * called after the card is paid for.
     *
     * @param amount the number of cards to draw
     */
    public void drawCards(int amount) {
        if (noDraw) {
            return;
        }

        switch (energy) {
            case 3:
                cardsDrawnWith3Energy += amount;
            case 2:
                cardsDrawnWith2Energy += amount;
            case 1:
                cardsDrawnWith1Energy += amount;
            case 0:
                cardsDrawnWith0Energy += amount;
        }

    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    /**
     * Have this player gain block from a card. The amount of block will be affected by player powers.
     *
     * @param base the amount of block the card grants without any modifiers
     */
    public void gainBlockFromCard(int base) {
        this.block += getModifiedBlock(base);
    }

    public void gainRawBlock(int block) {
        this.block += block;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void gainStrength(int strength) {
        this.strength += strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public boolean isWeakened() {
        return weakened;
    }

    public void setWeakened(boolean weakened) {
        this.weakened = weakened;
    }

    public boolean isFrail() {
        return frail;
    }

    public void setFrail(boolean frail) {
        this.frail = frail;
    }

    public int getMetallicize() {
        return metallicize;
    }

    public void setMetallicize(int metallicize) {
        this.metallicize = metallicize;
    }

    public void gainMetallicize(int metallicize) {
        this.metallicize += metallicize;
    }

    public int getDemonForm() {
        return demonForm;
    }

    public void setDemonForm(int demonForm) {
        this.demonForm = demonForm;
    }

    public void gainDemonForm(int demonForm) {
        this.demonForm += demonForm;
    }

    public boolean isEntangled() {
        return entangled;
    }

    public void setEntangled(boolean entangled) {
        this.entangled = entangled;
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public boolean hasNoDraw() {
        return noDraw;
    }

    public void setNoDraw(boolean noDraw) {
        this.noDraw = noDraw;
    }

    /**
     * Returns the number of Slimed cards this player has exhausted.
     *
     * @return the number of Slimed cards this player has exhausted
     */
    public int getExhaustedSlimed() {
        return exhaustedSlimed;
    }

    public void setExhaustedSlimed(int exhaustedSlimed) {
        this.exhaustedSlimed = exhaustedSlimed;
    }

    public int getCardsDrawnWith0Energy() {
        return cardsDrawnWith0Energy;
    }

    public void setCardsDrawnWith0Energy(int cardsDrawnWith0Energy) {
        this.cardsDrawnWith0Energy = cardsDrawnWith0Energy;
    }

    public int getCardsDrawnWith1Energy() {
        return cardsDrawnWith1Energy;
    }

    public void setCardsDrawnWith1Energy(int cardsDrawnWith1Energy) {
        this.cardsDrawnWith1Energy = cardsDrawnWith1Energy;
    }

    public int getCardsDrawnWith2Energy() {
        return cardsDrawnWith2Energy;
    }

    public void setCardsDrawnWith2Energy(int cardsDrawnWith2Energy) {
        this.cardsDrawnWith2Energy = cardsDrawnWith2Energy;
    }

    public int getCardsDrawnWith3Energy() {
        return cardsDrawnWith3Energy;
    }

    public void setCardsDrawnWith3Energy(int cardsDrawnWith3Energy) {
        this.cardsDrawnWith3Energy = cardsDrawnWith3Energy;
    }

    @Override
    public boolean equals(Object o) {
        // auto-generated, but omits comparison of CombatSimulator and uses custom comparison for hands
        if (this == o) return true;
        if (!(o instanceof SimplePlayer)) return false;
        SimplePlayer that = (SimplePlayer) o;
        return energy == that.energy && health == that.health && block == that.block && strength == that.strength && dexterity == that.dexterity && weakened == that.weakened && frail == that.frail && metallicize == that.metallicize && demonForm == that.demonForm && entangled == that.entangled && vulnerable == that.vulnerable && noDraw == that.noDraw && exhaustedSlimed == that.exhaustedSlimed && cardsDrawnWith0Energy == that.cardsDrawnWith0Energy && cardsDrawnWith1Energy == that.cardsDrawnWith1Energy && cardsDrawnWith2Energy == that.cardsDrawnWith2Energy && cardsDrawnWith3Energy == that.cardsDrawnWith3Energy
                && handsEqual(hand, that.hand);
    }

    @Override
    public int hashCode() {
        // auto-generated, but omits CombatSimulator
        return Objects.hash(hand, energy, health, block, strength, dexterity, weakened, frail, metallicize, demonForm, entangled, vulnerable, noDraw, exhaustedSlimed, cardsDrawnWith0Energy, cardsDrawnWith1Energy, cardsDrawnWith2Energy, cardsDrawnWith3Energy);
    }

    @Override
    public String toString() {
        return "SimplePlayer{" +
                "hand=" + hand +
                ", energy=" + energy +
                ", health=" + health +
                ", block=" + block +
                ", strength=" + strength +
                ", weakened=" + weakened +
                '}';
    }
}
