package newaimod.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MonsterInfoCommand extends ConsoleCommand {
    @Override
    protected void execute(String[] strings, int i) {
        DevConsole.log(AbstractDungeon.getCurrRoom().monsters.monsters);
    }
}
