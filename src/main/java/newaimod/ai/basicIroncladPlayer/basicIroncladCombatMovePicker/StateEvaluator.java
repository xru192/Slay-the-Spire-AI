package newaimod.ai.basicIroncladPlayer.basicIroncladCombatMovePicker;

import newaimod.util.simulator.CombatSimulator;

public interface StateEvaluator {
    double evaluate(CombatSimulator state);
}
