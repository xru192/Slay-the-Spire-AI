package newaimod.ai.basicIroncladPlayer.basicIroncladCombatMovePicker;

import newaimod.ai.AbstractCombatMovePicker;
import newaimod.ai.AutoPlayer;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.CombatSimulator.Future;
import newaimod.ai.AutoPlayer.CombatMove;
import newaimod.util.simulator.monsters.SimpleGremlinNob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SimulatingMovePicker extends AbstractCombatMovePicker {
    public static final Logger logger = LogManager.getLogger(SimulatingMovePicker.class.getName());

    @Override
    public AutoPlayer.CombatMove pickMoveDefault() {
        logger.info("Picking move (default)");
        return pickMoveUsingEval(new BasicStateEvaluator());
    }


    @Override
    protected CombatMove pickMoveGremlinNob() {
        logger.info("Picking move (Gremlin Nob)");
        return pickMoveUsingEval(this::evalStateGremlinNob);
    }

    @Override
    protected CombatMove pickMoveLagavulin() {
        BasicStateEvaluator evaluator = new BasicStateEvaluator();
        evaluator.TMHw = -1.0 / 2;
        return pickMoveUsingEval(evaluator);
    }

    @Override
    protected CombatMove pickMove3Sentries() {
        BasicStateEvaluator evaluator = new BasicStateEvaluator();
        CombatSimulator current = new CombatSimulator();
        if (current.countAliveMonsters() == 3) {
            evaluator.TMHw = -2.0 / 3;
        }
        return pickMoveUsingEval(evaluator);
    }

    private AutoPlayer.CombatMove pickMoveUsingEval(StateEvaluator evaluator) {
        CombatSimulator currentState = new CombatSimulator();
        List<Future> endStates = CombatSimulator.calculateFutures(currentState);

        double bestEval = -100000;
        CombatMove bestMove = null;
        CombatSimulator bestState = null;
        for (Future future : endStates) {
            double eval = evaluator.evaluate(future.state);
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

        return bestMove;
    }

    private double evalStateGremlinNob(CombatSimulator state) {
        StateEvaluator evaluator = new BasicStateEvaluator();
        double basicEval = evaluator.evaluate(state);
        if (state.combatOver()) {
            return basicEval;
        }

        assert state.monsterList.size() == 1 && state.monsterList.get(0) instanceof SimpleGremlinNob;
        int GNS = ((SimpleGremlinNob) state.monsterList.get(0)).getStrength(); // Gremlin Nob strength
        double GNSw = -3;
        return basicEval + GNS * GNSw;
    }
}
