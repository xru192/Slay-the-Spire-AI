package newaimod.util.Simulator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.AI.AutoPlayer;
import newaimod.NewAIMod;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.Cards.Filler;
import newaimod.util.Simulator.Cards.Ironclad.Attacks.*;
import newaimod.util.Simulator.Cards.Ironclad.Powers.SimpleInflame;
import newaimod.util.Simulator.Cards.Ironclad.Skills.SimpleDefend_Red;
import newaimod.util.Simulator.Cards.Ironclad.Skills.SimpleFlameBarrier;
import newaimod.util.Simulator.Cards.Ironclad.Skills.SimpleShrugItOff;
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
            case Inflame.ID:
                return new SimpleInflame(this, card);
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
        }
        return new Filler(this, card.type, card.cost);
    }

    /**
     * Have the player be attacked by the alive monsters, as if the monsters take their turn.
     */
    public void aliveMonstersAttackPlayer() {
        for (SimpleMonster m : monsterList) {
            if (m.isAttacking()) {
                assert m.intentHits >= 1 && m.intentDamage >= 0;
                for (int i = 0; i < m.intentHits; ++i) {
                    player.takeAttack(m.getModifiedDamage());
                }
            }
        }
    }

    @Override
    public String toString() {
        return "CombatSimulator{" +
                "player=" + player +
                ", monsterList=" + monsterList +
                '}';
    }

    /**
     * Returns a list of all possible futures derived from this state. A future consists of a combat state
     * reachable through a sequence of moves, along with the first move taken to reach that state. For now,
     * potion-related moves are not considered.
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
        public AutoPlayer.CombatMove move;    // First move leading to possible future state
        public CombatSimulator state; // Possible future state

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
