package newaimod.util.simulator;

import newaimod.util.simulator.cards.AbstractSimpleCard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A SimplePlayer represents a simplified version of the state of the player during combat. This includes health, a
 * current hand of cards, and various attributes.
 */
public class SimplePlayer {
    @NotNull
    public final CombatSimulator simulator;
    public ArrayList<AbstractSimpleCard> hand;
    public int energy;
    public int health;
    public int block;
    public int strength;
    public int dexterity;
    public boolean weakened;
    public boolean frail;
    public int metallicize;
    public int demonForm;
    public boolean entangled;
    public boolean vulnerable;
    public boolean noDraw;
    public int exhaustedSlimed;
    public int cardsDrawnWith0Energy;      // # cards drawn while player has at least 1 energy left (after simulator start)
    public int cardsDrawnWith1Energy;      // # cards drawn while player has at least 1 energy left (after simulator start)
    public int cardsDrawnWith2Energy;      // # cards drawn while player has at least 2 energy left (after simulator start)
    public int cardsDrawnWith3Energy;      // # cards drawn while player has at least 3 energy left (after simulator start)

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

    public SimplePlayer(SimplePlayer player, CombatSimulator simulator) {
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
     * Returns the number of Slimed cards this player has exhausted.
     *
     * @return the number of Slimed cards this player has exhausted
     */
    public int getExhaustedSlimed() {
        return exhaustedSlimed;
    }

    public void gainNoDraw() {
        noDraw = true;
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

    public int getCardsDrawnWith0Energy() {
        return cardsDrawnWith0Energy;
    }

    public int getCardsDrawnWith1Energy() {
        return cardsDrawnWith1Energy;
    }

    public int getCardsDrawnWith2Energy() {
        return cardsDrawnWith2Energy;
    }

    public int getCardsDrawnWith3Energy() {
        return cardsDrawnWith3Energy;
    }

    public boolean isEntangled() {
        return entangled;
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
