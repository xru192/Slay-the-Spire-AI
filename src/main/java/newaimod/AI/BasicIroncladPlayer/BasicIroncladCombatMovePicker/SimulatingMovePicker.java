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

        List<Pair> endStates = new ArrayList<>();   // States we can reach, along with the first move to reach it
        endStates.add(new Pair(new CombatMove(CombatMove.TYPE.PASS), currentState));

        // Compute states we can reach by playing exactly one card

        List<Pair> firstStates = new ArrayList<>(); // States we can reach by playing one card

        List<AbstractSimpleCard> currentHand = currentState.player.hand;
        for (int i = 0; i < currentHand.size(); ++i) {
            AbstractSimpleCard card = currentHand.get(i);

            if (card.targetsOne) {
                for (int m_index = 0; m_index < currentState.monsterList.size(); ++m_index) {
                    SimpleMonster m = currentState.monsterList.get(m_index);
                    if (card.canPlay(m)) {
                        CombatMove firstMove = new CombatMove(CombatMove.TYPE.CARD, i, m.originalMonster);
                        CombatSimulator state = new CombatSimulator(currentState);
                        state.playCard(state.player.hand.get(i), state.monsterList.get(m_index));
                        firstStates.add(new Pair(firstMove, state));
                    }
                }
            } else {
                CombatMove firstMove = new CombatMove(CombatMove.TYPE.CARD, i, null);
                if (card.canPlay(null)) {
                    CombatSimulator state = new CombatSimulator(currentState);
                    state.playCard(state.player.hand.get(i), null);
                    firstStates.add(new Pair(firstMove, state));
                }
            }
        }

        // Compute states we can reach by playing multiple cards
        Queue<Pair> queue = new ArrayDeque<>(firstStates);
        while (!queue.isEmpty()) {
            Pair pair = queue.poll();
            endStates.add(pair);
            CombatSimulator thisState = pair.state;
            for (int i = 0; i < thisState.player.hand.size(); ++i) {
                AbstractSimpleCard card = thisState.player.hand.get(i);
                if (card.targetsOne) {
                    for (int m_index = 0; m_index < thisState.monsterList.size(); ++m_index) {
                        SimpleMonster m = thisState.monsterList.get(m_index);
                        if (card.canPlay(m)) {
                            CombatSimulator state = new CombatSimulator(thisState);
                            state.playCard(state.player.hand.get(i), state.monsterList.get(m_index));
                            queue.add(new Pair(pair.move, state));
                        }
                    }
                } else {
                    if (card.canPlay(null)) {
                        CombatSimulator state = new CombatSimulator(thisState);
                        state.playCard(state.player.hand.get(i), null);
                        queue.add(new Pair(pair.move, state));
                    }
                }
            }
        }

//        logger.info("Ending states:");
//        for (Pair p : endStates) {
//            logger.info("Pairmove: " + p.move + ". Pairstate: " + p.state);
//        }

        double bestEval = -100000;
        CombatMove bestMove = null;
        CombatSimulator bestState = null;
        for (Pair pair : endStates) {
            double eval = evalState(pair.state);
            if (eval > bestEval) {
                bestEval = eval;
                bestMove = pair.move;
                bestState = pair.state;
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

    class Pair {
        CombatMove move;
        CombatSimulator state;

        Pair(CombatMove move, CombatSimulator state) {
            this.move = move;
            this.state = state;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "move=" + move +
                    ", state=" + state +
                    '}';
        }
    }

}
