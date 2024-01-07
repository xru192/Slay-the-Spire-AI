package newaimod.AI.BasicIroncladPlayer.BasicIroncladCombatMovePicker;

import newaimod.AI.AbstractCombatMovePicker;
import newaimod.AI.AutoPlayer;
import newaimod.util.Simulator.Cards.AbstractSimpleCard;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.SimpleMonster;
import newaimod.AI.AutoPlayer.CombatMove;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SimulatingMovePicker extends AbstractCombatMovePicker {
    public static final Logger logger = LogManager.getLogger(SimulatingMovePicker.class.getName());

    @Override
    public AutoPlayer.CombatMove pickMoveDefault() {
        logger.info("Picking move (default)");
        CombatSimulator currentState = new CombatSimulator();
        List<Future> endStates = calculateFutures(currentState);

        double bestEval = -100000;
        CombatMove bestMove = null;
        CombatSimulator bestState = null;
        for (Future future : endStates) {
            double eval = evalState(future.state);
            if (eval > bestEval) {
                bestEval = eval;
                bestMove = future.move;
                bestState = future.state;
            }
        }
        logger.info("Best move: " + bestMove);
        logger.info("Best state: " + bestState);
        logger.info("Best eval: " + bestEval);
        assert bestState != null;
        logger.info("Health after attacks" + bestState.getPlayerHealthAfterAttacks());

        return bestMove;
    }


    public double evalState(CombatSimulator state) {
        int aliveMonsters = 0;
        int totalMonsterHealth = 0;
        int lowestAliveHealth = Integer.MAX_VALUE;
        for (SimpleMonster m : state.monsterList) {
            if (m.isAlive()) {
                aliveMonsters += 1;
                totalMonsterHealth += m.health;
                lowestAliveHealth = Math.min(lowestAliveHealth, m.health);
            }
        }

        int playerHealth = state.getPlayerHealthAfterAttacks();
        if (aliveMonsters == 0) {
            return 1000;
        }

        double eval = playerHealth;
        eval -= 6 * aliveMonsters;
        eval -= totalMonsterHealth / 3.0;
        return eval;
    }


    /**
     * Returns a list of all possible futures derived from specified starting state. A future consists of a combat state
     * reachable through a sequence of moves, along with the first move taken to reach that state. For now,
     * potion-related moves are not considered.
     *
     * @param startState the starting state to find futures from
     * @return a list of futures from the starting state
     */
    public List<Future> calculateFutures(CombatSimulator startState) {
        List<Future> futures = new ArrayList<>();
        futures.add(new Future(new CombatMove(CombatMove.TYPE.PASS), startState));

        // Compute states we can reach by playing exactly one card

        List<Future> firstStates = new ArrayList<>(); // States we can reach by playing one card

        List<AbstractSimpleCard> currentHand = startState.player.hand;
        for (int i = 0; i < currentHand.size(); ++i) {
            AbstractSimpleCard card = currentHand.get(i);

            if (card.targetsOne) {
                for (int m_index = 0; m_index < startState.monsterList.size(); ++m_index) {
                    SimpleMonster m = startState.monsterList.get(m_index);
                    if (card.canPlay(m)) {
                        CombatMove firstMove = new CombatMove(CombatMove.TYPE.CARD, i, m.originalMonster);
                        CombatSimulator state = new CombatSimulator(startState);
                        state.playCard(state.player.hand.get(i), state.monsterList.get(m_index));
                        firstStates.add(new Future(firstMove, state));
                    }
                }
            } else {
                CombatMove firstMove = new CombatMove(CombatMove.TYPE.CARD, i, null);
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

    static class Future {
        CombatMove move;    // First move leading to possible future state
        CombatSimulator state; // Possible future state

        Future(CombatMove move, CombatSimulator state) {
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
