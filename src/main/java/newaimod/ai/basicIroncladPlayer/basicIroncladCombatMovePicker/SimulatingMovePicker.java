package newaimod.ai.basicIroncladPlayer.basicIroncladCombatMovePicker;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.DemonForm;
import com.megacrit.cardcrawl.cards.red.Inflame;
import com.megacrit.cardcrawl.cards.red.Metallicize;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import newaimod.ai.AbstractCombatMovePicker;
import newaimod.ai.AutoPlayer;
import newaimod.util.DungeonInformationManager;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.CombatSimulator.Future;
import newaimod.ai.AutoPlayer.CombatMove;
import newaimod.util.simulator.monsters.SimpleGremlinNob;
import newaimod.util.simulator.monsters.SimpleLagavulin;
import newaimod.util.simulator.monsters.SimpleSlimeBoss;
import newaimod.util.simulator.monsters.SimpleTheGuardian;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
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
        logger.info("Picking move (Lagavulin)");
        return pickMoveUsingEval(this::evalStateLagavulin);
    }

    @Override
    protected CombatMove pickMove3Sentries() {
        BasicStateEvaluator evaluator = new BasicStateEvaluator();
        CombatSimulator current = DungeonInformationManager.getInstance().getCurrentState();
        if (current.countAliveMonsters() == 3) {
            evaluator.TMHw = -2.0 / 3;
        }
        return pickMoveUsingEval(evaluator);
    }

    @Override
    protected CombatMove pickMoveSlimeBoss() {
        logger.info("Picking move (Slime Boss)");
        return pickMoveUsingEval(this::evalStateSlimeBoss);
    }

    @Override
    protected CombatMove pickMoveTheGuardian() {
        logger.info("Picking move (The Guardian)");
        return pickMoveUsingEval(this::evalStateTheGuardian);
    }

    private AutoPlayer.CombatMove pickMoveUsingEval(StateEvaluator evaluator) {
        CombatSimulator currentState = DungeonInformationManager.getInstance().getCurrentState();
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

    private double evalStateLagavulin(CombatSimulator state) {
        assert state.monsterList.size() == 1 && state.monsterList.get(0) instanceof SimpleLagavulin;
        ArrayList<AbstractCard> drawPile = AbstractDungeon.player.drawPile.group;
        boolean hasDemonForm = false;
        boolean hasInflame = false;
        boolean hasMetallicize = false;
        for (AbstractCard card : drawPile) {
            hasDemonForm |= card.cardID.equals(DemonForm.ID);
            hasInflame |= card.cardID.equals(Inflame.ID);
            hasMetallicize |= card.cardID.equals(Metallicize.ID);
        }

        SimpleLagavulin lagavulin = (SimpleLagavulin) state.monsterList.get(0);
        SimpleLagavulin.MODE mode = lagavulin.getMode();
        int healthMissing = lagavulin.maxHealth - lagavulin.health;
        int vulnerable = lagavulin.vulnerable;
        int weak = lagavulin.weak;

        BasicStateEvaluator evaluator = new BasicStateEvaluator();
        switch (mode) {
            case SLEEP_ONE:
            case SLEEP_TWO:
                // prefer not damaging Lagavulin if we have powers we can draw
                if (hasDemonForm || hasInflame || hasMetallicize) {
                    evaluator.TMHw = 50;
                } else {
                    // otherwise, only damage Lagavulin if we can deal a solid amount this turn
                    int vulnerableBonus = Math.max(0, vulnerable - 1) * 8;
                    int weakBonus = Math.max(0, weak - 1) * 5;
                    if (vulnerableBonus + weakBonus + healthMissing <= 15) {
                        evaluator.TMHw = 50;
                    }
                }
                break;
            case SLEEP_THREE:
            case AWAKE:
                evaluator.TMHw = -1.0 / 2;
        }
        return evaluator.evaluate(state);
    }

    private double evalStateSlimeBoss(CombatSimulator state) {
        BasicStateEvaluator evaluator = new BasicStateEvaluator();

        if (state.monsterList.size() == 1 && state.monsterList.get(0) instanceof SimpleSlimeBoss) {
            SimpleSlimeBoss enemy = (SimpleSlimeBoss) state.monsterList.get(0);
            if (enemy.isSplitting()) {
                evaluator.VBw = 0;
                int amtUnderHalf = enemy.maxHealth / 2 - enemy.health;
                if (amtUnderHalf < 10) {
                    return evaluator.evaluate(state) - 10;
                }
            }
        }

        return evaluator.evaluate(state);
    }

    private double evalStateTheGuardian(CombatSimulator state) {
        BasicStateEvaluator evaluator = new BasicStateEvaluator();

        assert state.monsterList.size() == 1 && state.monsterList.get(0) instanceof SimpleTheGuardian;
        SimpleTheGuardian.MODE mode = ((SimpleTheGuardian)state.monsterList.get(0)).getMode();
        if (mode == SimpleTheGuardian.MODE.DEFENSIVE) {
            if (state.player.health < 40) {
                evaluator.TMHw = -1.0 / 10;
            }
        }

        return evaluator.evaluate(state);
    }

}
