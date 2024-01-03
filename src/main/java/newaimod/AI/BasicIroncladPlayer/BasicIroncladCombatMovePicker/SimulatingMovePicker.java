package newaimod.AI.BasicIroncladPlayer.BasicIroncladCombatMovePicker;

import newaimod.AI.AbstractCombatMovePicker;
import newaimod.AI.AutoPlayer;
import newaimod.util.Simulator.CombatSimulator;

public class SimulatingMovePicker extends AbstractCombatMovePicker {
    @Override
    public AutoPlayer.CombatMove pickMoveDefault() {
        CombatSimulator simulator = new CombatSimulator();
        return null;
    }


}
