package newaimod.util.simulator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import newaimod.ai.AutoPlayer;
import newaimod.NewAIMod;
import newaimod.util.simulator.cards.AbstractSimpleCard;
import newaimod.util.simulator.cards.Filler;
import newaimod.util.simulator.cards.ironclad.attacks.*;
import newaimod.util.simulator.cards.ironclad.powers.SimpleDemonForm;
import newaimod.util.simulator.cards.ironclad.powers.SimpleInflame;
import newaimod.util.simulator.cards.ironclad.powers.SimpleMetallicize;
import newaimod.util.simulator.cards.ironclad.skills.*;
import newaimod.util.simulator.cards.Neutral.status.SimpleSlimed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
        for (SimpleMonster m : monsterList) {
            m.onUseCard(card);
        }
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
            case TwinStrike.ID:
                return new SimpleTwinStrike(this, (TwinStrike) card);
            case IronWave.ID:
                return new SimpleIronWave(this, (IronWave) card);
            case ShrugItOff.ID:
                return new SimpleShrugItOff(this, card);
            case Headbutt.ID:
                return new SimpleHeadbutt(this, card);
            case DemonForm.ID:
                return new SimpleDemonForm(this, card);
            case Inflame.ID:
                return new SimpleInflame(this, card);
            case Metallicize.ID:
                return new SimpleMetallicize(this, card);
            case Cleave.ID:
                return new SimpleCleave(this, card);
            case PommelStrike.ID:
                return new SimplePommelStrike(this, card);
            case FlameBarrier.ID:
                return new SimpleFlameBarrier(this, card);
            case Carnage.ID:
                return new SimpleCarnage(this, card);
            case Uppercut.ID:
                return new SimpleUppercut(this, card);
            case Whirlwind.ID:
                return new SimpleWhirlwind(this, card);
            case Armaments.ID:
                return new SimpleArmaments(this, card);
            case BattleTrance.ID:
                return new SimpleBattleTrance(this, card);
            case Slimed.ID:
                return new SimpleSlimed(this, card);
        }
        return new Filler(this, card.type, card.cost);
    }

    /**
     * Have the player be attacked by the alive monsters, as if the monsters take their turn.
     */
    private void aliveMonstersAttackPlayer() {
        for (SimpleMonster m : monsterList) {
            if (m.isAttacking()) {
                assert m.intentHits >= 1 && m.intentDamage >= 0;
                for (int i = 0; i < m.intentHits; ++i) {
                    player.takeAttack(m.getModifiedDamage());
                }
            }
        }
    }

    /**
     * Simulates some of the effects of the player ending their turn. In particular, powers and relics which activate at
     * the end of turn will trigger, and then any alive monsters will attack the player.
     */
    public void triggerEndTurnEffects() {
        player.triggerEndTurnPowers();
        aliveMonstersAttackPlayer();
    }

    /**
     * Returns whether the player is allowed to play cards in this state. False is only returned if there is something
     * explicitly preventing the player from playing cards in general, so this does not include a lack of energy.
     *
     * @return whether the player is allowed to play cards
     */
    public boolean playerCanPlayCards() {
        return countAliveMonsters() > 0;
    }

    /**
     * Returns whether the combat is over. Note that when the Awakened One killed the first time the combat is not yet
     * over.
     *
     * @return whether the combat is over
     */
    public boolean combatOver() {
        assert !AbstractDungeon.lastCombatMetricKey.equals(MonsterHelper.AWAKENED_ENC);
        return countAliveMonsters() == 0;
    }

    /**
     * Returns the amount of health the player has. Returns 0 if the player has lost all health. Note that Fairy in a
     * Bottle/Lizard Tail revivals are not accounted for.
     *
     * @return the health of the player
     */
    public int getPlayerHealth() {
        assert player.health >= 0;
        return player.health;
    }

    /**
     * Returns the total effective health of all monsters. A monster's effective health is the same as its health,
     * except for the Spheric Guardian. Since it has barricade, a living Spheric Guardian's block is counted as health.
     *
     * @return total effective health of monsters in this state
     */
    public int getTotalMonsterEffectiveHealth() {
        int totalHealth = 0;
        for (SimpleMonster m : monsterList) {
            if (m.isAlive()) {
                totalHealth += m.health;
                if (m.originalMonster instanceof SphericGuardian) {
                    totalHealth += m.block;
                }
            }
        }
        return totalHealth;
    }

    /**
     * @return the number of alive monsters in this state
     */
    public int countAliveMonsters() {
        int aliveMonsters = 0;
        for (SimpleMonster m : monsterList) {
            if (m.isAlive()) {
                ++aliveMonsters;
            }
        }
        return aliveMonsters;
    }

    @Override
    public String toString() {
        return "CombatSimulator{" +
                "player=" + player +
                ", monsterList=" + monsterList +
                '}';
    }

    /**
     * Returns a list of all possible futures derived from this state. A future consists of a combat state reachable
     * through a sequence of moves (this turn only), along with the first move taken to reach that state. Note that the
     * starting state will be a state reachable by the move PASS. For now, potion-related moves are not considered.
     *
     * @param startState the starting state to find futures from
     * @return a list of futures from the starting state
     */
    public static List<Future> calculateFutures(CombatSimulator startState) {
        List<Future> futures = new ArrayList<>();
        futures.add(new Future(new AutoPlayer.CombatMove(AutoPlayer.CombatMove.TYPE.PASS), startState));

        // Compute states we can reach by playing exactly one card

        List<Future> firstStates = new ArrayList<>(); // States we can reach by playing one card

        List<AbstractSimpleCard> currentHand = startState.player.hand;
        for (int i = 0; i < currentHand.size(); ++i) {
            AbstractSimpleCard card = currentHand.get(i);

            if (card.targetsOne) {
                for (int m_index = 0; m_index < startState.monsterList.size(); ++m_index) {
                    SimpleMonster m = startState.monsterList.get(m_index);
                    if (card.canPlay(m)) {
                        AutoPlayer.CombatMove firstMove = new AutoPlayer.CombatMove(AutoPlayer.CombatMove.TYPE.CARD, i, m.originalMonster);
                        CombatSimulator state = new CombatSimulator(startState);
                        state.playCard(state.player.hand.get(i), state.monsterList.get(m_index));
                        firstStates.add(new Future(firstMove, state));
                    }
                }
            } else {
                AutoPlayer.CombatMove firstMove = new AutoPlayer.CombatMove(AutoPlayer.CombatMove.TYPE.CARD, i, null);
                if (card.canPlay(null)) {
                    CombatSimulator state = new CombatSimulator(startState);
                    state.playCard(state.player.hand.get(i), null);
                    firstStates.add(new Future(firstMove, state));
                }
            }
        }

        // Compute states we can reach by playing multiple cards
        Queue<Future> queue = new ArrayDeque<>(firstStates);
        while (!queue.isEmpty()) {
            Future future = queue.poll();
            futures.add(future);
            CombatSimulator thisState = future.state;
            if (!thisState.playerCanPlayCards()) {
                continue;
            }
            for (int i = 0; i < thisState.player.hand.size(); ++i) {
                AbstractSimpleCard card = thisState.player.hand.get(i);
                if (card.targetsOne) {
                    for (int m_index = 0; m_index < thisState.monsterList.size(); ++m_index) {
                        SimpleMonster m = thisState.monsterList.get(m_index);
                        if (card.canPlay(m)) {
                            CombatSimulator state = new CombatSimulator(thisState);
                            state.playCard(state.player.hand.get(i), state.monsterList.get(m_index));
                            queue.add(new Future(future.move, state));
                        }
                    }
                } else {
                    if (card.canPlay(null)) {
                        CombatSimulator state = new CombatSimulator(thisState);
                        state.playCard(state.player.hand.get(i), null);
                        queue.add(new Future(future.move, state));
                    }
                }
            }
        }

        return futures;
    }

    public static class Future {
        public final AutoPlayer.CombatMove move;    // First move leading to possible future state
        public final CombatSimulator state; // Possible future state

        Future(AutoPlayer.CombatMove move, CombatSimulator state) {
            this.move = move;
            this.state = state;
        }

        @Override
        public String toString() {
            return "Future{" +
                    "move=" + move +
                    ", state=" + state +
                    '}';
        }
    }
}
