package newaimod.util.Simulator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import newaimod.NewAIMod;
import newaimod.util.CombatUtils;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A SimplePlayer represents a simplified version of the state of the player during combat. This includes health, a
 * current hand of cards, and various attributes.
 */
public class SimplePlayer {
    public final CombatSimulator simulator;
    public ArrayList<AbstractSimpleCard> hand;
    public int energy;
    public int health;
    public int block;
    public int strength;
//    public int metallicize;
//    public int demonForm;
//    boolean weakened;
//    boolean vulnerable;
//    boolean intangible;

    /**
     * SimplePlayer which represents the current state of the player in combat.
     */
    public SimplePlayer(CombatSimulator simulator) {
        assert NewAIMod.inBattle;
        this.simulator = simulator;
        AbstractPlayer p = AbstractDungeon.player;
        hand = new ArrayList<>();
        for (AbstractCard card : p.hand.group) {
            hand.add(simulator.convertCard(card));
        }
        energy = CombatUtils.usableEnergy();
        health = p.currentHealth;
        block = p.currentBlock;
        strength = CombatUtils.amountOfPower(p, StrengthPower.POWER_ID);
//        metallicize = CombatUtils.amountOfPower(p, MetallicizePower.POWER_ID);
//        demonForm = CombatUtils.amountOfPower(p, DemonFormPower.POWER_ID);
//        weakened = p.hasPower(WeakPower.POWER_ID);
//        vulnerable = p.hasPower(VulnerablePower.POWER_ID);
//        intangible = p.hasPower(IntangiblePlayerPower.POWER_ID);
    }

    public SimplePlayer(SimplePlayer player, CombatSimulator simulator) {
        assert NewAIMod.inBattle;
        this.simulator = simulator;
        this.hand = new ArrayList<>();
        for (AbstractSimpleCard card : player.hand) {
            this.hand.add(card.copy(simulator));
        }
        this.energy = player.energy;
        this.health = player.health;
        this.block = player.block;
        this.strength = player.strength;
    }

    public void payForAndUseCard(AbstractSimpleCard card) {
        assert hand.contains(card);
        hand.remove(card);
        energy -= card.cost;
    }


    /**
     * Returns how much damage an attack will do (per hit) after player modifications. These include strength and
     * weakened. TODO weakened/relics
     *
     * @param base the base damage of the attack (per hit)
     * @return the damage after player modifications
     */
    public int getModifiedDamage(int base) {
        return base + strength;
    }


    /**
     * Returns how much block will be gained by a card after player modifications. These include dexterity and frail.
     * TODO dex/frail/relics
     *
     * @param base the base block gain of the card
     * @return the block gain after player modifications
     */
    public int getModifiedBlock(int base) {
        return base;
    }

    // TODO update (make sure simulator doesn't have to be equal)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePlayer that = (SimplePlayer) o;
        return energy == that.energy && health == that.health && block == that.block && strength == that.strength && Objects.equals(hand, that.hand);
    }

    // TODO update
    @Override
    public int hashCode() {
        return Objects.hash(hand, energy, health, block);
    }

    @Override
    public String toString() {
        return "SimplePlayer{" +
                "hand=" + hand +
                ", energy=" + energy +
                ", health=" + health +
                ", block=" + block +
                ", strength=" + strength +
                '}';
    }
}
