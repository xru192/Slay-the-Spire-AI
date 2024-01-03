package newaimod.util.Simulator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import newaimod.NewAIMod;
import newaimod.util.CombatUtils;

import java.util.ArrayList;

/**
 * A SimplePlayer represents a simplified version of the state of the player during combat. This includes health, a
 * current hand of cards, and various attributes.
 */
public class SimplePlayer {
    public ArrayList<AbstractSimpleCard> hand;
    public int energy;
    public int health;
    public int block;
    public int strength;
    public int metallicize;
    public int demonForm;
    boolean weakened;
    boolean vulnerable;
    boolean intangible;

    /**
     * SimplePlayer which represents the current state of the player in combat.
     */
    public SimplePlayer() {
        assert NewAIMod.inBattle;
        AbstractPlayer p = AbstractDungeon.player;
        hand = new ArrayList<>();
//        hand.addAll(p.hand.group);
        energy = CombatUtils.usableEnergy();
        health = p.currentHealth;
        block = p.currentBlock;
        strength = CombatUtils.amountOfPower(p, StrengthPower.POWER_ID);
        metallicize = CombatUtils.amountOfPower(p, MetallicizePower.POWER_ID);
        demonForm = CombatUtils.amountOfPower(p, DemonFormPower.POWER_ID);
        weakened = p.hasPower(WeakPower.POWER_ID);
        vulnerable = p.hasPower(VulnerablePower.POWER_ID);
        intangible = p.hasPower(IntangiblePlayerPower.POWER_ID);
    }

}
