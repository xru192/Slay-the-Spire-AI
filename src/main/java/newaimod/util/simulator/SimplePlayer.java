package newaimod.util.simulator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import newaimod.NewAIMod;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.cards.AbstractSimpleCard;

import java.util.ArrayList;

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
    public int dexterity;
    //    public int metallicize;
//    public int demonForm;
    boolean weakened;
    boolean frail;

    boolean entangled;
    boolean vulnerable;
    // boolean intangible;
    boolean noDraw;

    int exhaustedSlimed;

    int cardsDrawnWith0Energy;      // # cards drawn while player has at least 1 energy left (after simulator start)
    int cardsDrawnWith1Energy;      // # cards drawn while player has at least 1 energy left (after simulator start)
    int cardsDrawnWith2Energy;      // # cards drawn while player has at least 2 energy left (after simulator start)
    int cardsDrawnWith3Energy;      // # cards drawn while player has at least 3 energy left (after simulator start)

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
        dexterity = CombatUtils.amountOfPower(p, DexterityPower.POWER_ID);
//        metallicize = CombatUtils.amountOfPower(p, MetallicizePower.POWER_ID);
//        demonForm = CombatUtils.amountOfPower(p, DemonFormPower.POWER_ID);
        weakened = p.hasPower(WeakPower.POWER_ID);
        frail = p.hasPower(FrailPower.POWER_ID);
        entangled = p.hasPower(EntanglePower.POWER_ID);
        vulnerable = p.hasPower(VulnerablePower.POWER_ID);
//        intangible = p.hasPower(IntangiblePlayerPower.POWER_ID);
        noDraw = p.hasPower(NoDrawPower.POWER_ID);
        exhaustedSlimed = 0;
        cardsDrawnWith0Energy = 0;
        cardsDrawnWith1Energy = 0;
        cardsDrawnWith2Energy = 0;
        cardsDrawnWith3Energy = 0;
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
        this.dexterity = player.dexterity;
        this.weakened = player.weakened;
        this.frail = player.frail;
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
