package newaimod.util.Simulator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Bash;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.NewAIMod;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.Cards.Filler;
import newaimod.util.Simulator.Cards.Ironclad.SimpleBash;
import newaimod.util.Simulator.Cards.Ironclad.SimpleDefend_Red;
import newaimod.util.Simulator.Cards.Ironclad.SimpleStrike_Red;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A CombatSimulator simulates a simplified version of Slay the Spire combat. An instance represents a state of combat,
 * with information about the player and monsters. It supports the playing of cards, which modifies the state.
 */
public class CombatSimulator {
    public static final Logger logger = LogManager.getLogger(CombatSimulator.class.getName());

    @NotNull
    public SimplePlayer player;
    public List<SimpleMonster> monsterList;

    /**
     * CombatSimulator which represents the current state of combat.
     */
    public CombatSimulator() {
        assert NewAIMod.inBattle;
        player = new SimplePlayer(this);
        monsterList = new ArrayList<>();

        List<AbstractMonster> roomMonsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        for (AbstractMonster m : roomMonsters) {
            if (!m.isDeadOrEscaped()) {
                monsterList.add(new SimpleMonster(m, this));
            }
        }
    }

    /**
     * CombatSimulator which copies the state represented by another simulator.
     *
     * @param simulator the simulator to copy
     */
    public CombatSimulator(CombatSimulator simulator) {
        player = new SimplePlayer(simulator.player, this);
        monsterList = new ArrayList<>();
        for (SimpleMonster m : simulator.monsterList) {
            monsterList.add(new SimpleMonster(m, this));
        }
    }

    /**
     * Tries to modify the state of combat by playing the specified card with a specified target. Returns whether the
     * card was successfully played.
     *
     * @param card   the card to play
     * @param target the target of the card to play
     * @return whether the card was successfully played
     */
    public boolean playCard(AbstractSimpleCard card, SimpleMonster target) {
        if (!card.canPlay(target)) {
            return false;
        }

        card.play(target);
        return true;
    }

    /**
     * Returns a simulated card corresponding to the specified AbstractCard.
     *
     * @param card the card to convert
     * @return a simulated card corresponding to the specified AbstractCard
     */
    public AbstractSimpleCard convertCard(AbstractCard card) {
        switch (card.cardID) {
            case Strike_Red.ID:
                return new SimpleStrike_Red(this, (Strike_Red) card);
            case Defend_Red.ID:
                return new SimpleDefend_Red(this, (Defend_Red) card);
            case Bash.ID:
                return new SimpleBash(this, (Bash) card);
        }
        return new Filler(this, card.type, card.cost);
    }

    /**
     * Returns the amount of health the player would have after alive monsters perform their attacks
     */
    public int getPlayerHealthAfterAttacks() {
        int health = player.health;
        int totalIncomingDmg = 0;
        for (SimpleMonster m : monsterList) {
            if (m.isAttacking()) {
                assert m.intentHits >= 1 && m.intentDamage >= 0;
                totalIncomingDmg += m.intentDamage * m.intentHits;
            }
        }

        int blockedDmg = Math.min(player.block, totalIncomingDmg);
        health -= totalIncomingDmg - blockedDmg;
        return health;
    }

    @Override
    public String toString() {
        return "CombatSimulator{" +
                "player=" + player +
                ", monsterList=" + monsterList +
                '}';
    }
}
