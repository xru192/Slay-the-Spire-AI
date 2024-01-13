package newaimod.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import newaimod.NewAIMod;

public class TestCommand extends ConsoleCommand {

    @Override
    protected void execute(String[] strings, int i) {
        String output = "InGame: " +
                NewAIMod.inGame +
                ", InBattle: " +
                NewAIMod.inBattle;

        DevConsole.log(output);
    }
}
