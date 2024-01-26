package newaimod.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import newaimod.ai.AutoPlayer;
import newaimod.actions.DoCombatMoveAction;
import newaimod.util.CombatUtils;
import newaimod.util.DungeonInformationManager;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class TestDoCombatMoveActionCommand extends ConsoleCommand {
    @Override
    protected void execute(String[] strings, int i) {
        CombatSimulator currentState = DungeonInformationManager.getInstance().getCurrentState();
        SimpleMonster weakest = CombatUtils.getWeakestTarget(currentState);
        AutoPlayer.CombatMove move = new AutoPlayer.CombatMove(AutoPlayer.CombatMove.TYPE.CARD, 0, weakest);
        AbstractDungeon.actionManager.addToBottom(new DoCombatMoveAction(move));
        DevConsole.log("Added move to queue");
    }
}
