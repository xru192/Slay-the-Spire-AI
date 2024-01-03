package newaimod.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import newaimod.NewAIMod;

public class TestCommand extends ConsoleCommand {


    @Override
    protected void execute(String[] strings, int i) {
        StringBuilder output = new StringBuilder();
        output.append("InGame: ");
        output.append(NewAIMod.inGame);
        output.append(", InBattle: ");
        output.append(NewAIMod.inBattle);

        DevConsole.log(output.toString());
//        DevConsole.log(ChoiceScreenUtils.getCurrentChoiceType().toString());
    }
}
