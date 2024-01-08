package newaimod.AI.BasicIroncladPlayer.BasicIroncladCombatMovePicker;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.AI.AbstractCombatMovePicker;
import newaimod.AI.AutoPlayer;
import newaimod.util.Simulator.CombatSimulator;
import newaimod.util.Simulator.CombatSimulator.Future;
import newaimod.util.Simulator.SimpleMonster;
import newaimod.AI.AutoPlayer.CombatMove;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SimulatingMovePicker extends AbstractCombatMovePicker {
    public static final Logger logger = LogManager.getLogger(SimulatingMovePicker.class.getName());

    @Override
    public AutoPlayer.CombatMove pickMoveDefault() {
        logger.info("Picking move (default)");
        CombatSimulator currentState = new CombatSimulator();
        List<Future> endStates = CombatSimulator.calculateFutures(currentState);

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


    @Override
    protected CombatMove pickMoveGremlinNob() {
        logger.info("Picking move (Gremlin Nob)");
        CombatSimulator currentState = new CombatSimulator();
        List<Future> endStates = CombatSimulator.calculateFutures(currentState);

        double bestEval = -100000;
        CombatMove bestMove = null;
        CombatSimulator bestState = null;
        for (Future future : endStates) {
            // Don't consider playing skills past turn 1
            if (GameActionManager.turn > 1 && future.move.type == CombatMove.TYPE.CARD) {
                if (currentState.player.hand.get(future.move.index).type == AbstractCard.CardType.SKILL) {
                    continue;
                }
            }

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

        eval += state.player.strength * 5;
        return eval;
    }


}
