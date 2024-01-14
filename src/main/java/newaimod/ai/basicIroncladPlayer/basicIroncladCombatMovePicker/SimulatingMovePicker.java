package newaimod.ai.basicIroncladPlayer.basicIroncladCombatMovePicker;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import newaimod.ai.AbstractCombatMovePicker;
import newaimod.ai.AutoPlayer;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.CombatSimulator.Future;
import newaimod.util.simulator.SimpleMonster;
import newaimod.ai.AutoPlayer.CombatMove;
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


    private int vulnerableBonus(CombatSimulator state) {
        int totalBonus = 0;
        for (SimpleMonster m : state.monsterList) {
            if (m.isAlive() && m.health >= 5 && m.vulnerable > 1) {
                int bonus = m.vulnerable == 2 ? 3 : 8;
                totalBonus += bonus;
            }
        }
        return totalBonus;
    }

    public double evalState(CombatSimulator state) {
        if (state.combatOver()) {
            return 1000;
        }

        state.triggerEndTurnEffects();
        int PH = state.getPlayerHealth();                                    // player health
        int AM = state.countAliveMonsters();                                 // alive monsters
        int TMH = state.getTotalMonsterEffectiveHealth();                    // total monster health
        int PS = state.player.strength;                                      // player strength
        int ES = state.player.getExhaustedSlimed();                          // exhausted Slimed
        int VB = vulnerableBonus(state);                                     // vulnerable bonus
        int CDW0E = state.player.getCardsDrawnWith0Energy();                 // cards drawn with 0+ energy
        int CDW1E = state.player.getCardsDrawnWith1Energy();                 // cards drawn with 1+ energy
        int CDW2E = state.player.getCardsDrawnWith2Energy();                 // cards drawn with 2+ energy
        int CDW3E = state.player.getCardsDrawnWith3Energy();                 // cards drawn with 3+ energy

        double PHw = 1.0;
        double AMw = -6;
        double TMHw = -1.0 / 3;
        double PSw = 5;
        double ESw = 0.001;
        double VBw = 1.0 / 3;
        double CDW0Ew = 0;
        double CDW1Ew = 0.01;
        double CDW2Ew = 0.01;
        double CDW3Ew = 0.01;

        double eval = (PH * PHw) + (AM * AMw) + (TMH * TMHw) + (PS * PSw) + (ES * ESw) + (VB * VBw) + (CDW0E * CDW0Ew) + (CDW1E * CDW1Ew) + (CDW2E * CDW2Ew) + (CDW3E * CDW3Ew);
        if (PH <= 0) {
            eval -= 1000;
        }

        return eval;
    }

}
