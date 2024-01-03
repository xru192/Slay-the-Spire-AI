package newaimod.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.AI.AutoPlayer;
import newaimod.actions.DoCombatMoveAction;
import newaimod.util.CombatUtils;

public class TestDoCombatMoveActionCommand extends ConsoleCommand {
    @Override
    protected void execute(String[] strings, int i) {
        AbstractMonster weakest = CombatUtils.getWeakestTarget();
        AutoPlayer.CombatMove move = new AutoPlayer.CombatMove(AutoPlayer.CombatMove.TYPE.CARD, 0, weakest);
        AbstractDungeon.actionManager.addToBottom(new DoCombatMoveAction(move));
        DevConsole.log("Added move to queue");
    }
}
