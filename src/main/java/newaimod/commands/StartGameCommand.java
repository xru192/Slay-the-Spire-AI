package newaimod.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import newaimod.NewAIMod;

import java.util.ArrayList;

/**
 * Command for queueing new runs to start at the home screen. For now, all runs must be Ironclad A0.
 */
public class StartGameCommand extends ConsoleCommand {

    public StartGameCommand() {
        minExtraTokens = 1;
        maxExtraTokens = 1;
    }

    @Override
    protected void execute(String[] strings, int depth) {
        if (strings.length != 2 || !isInteger(strings[1])) {
            errorMsg();
            return;
        }

        int times = Integer.parseInt(strings[1]);
        for (int i = 0; i < times; i++) {
            NewAIMod.enqueueGame(AbstractPlayer.PlayerClass.IRONCLAD, 0);
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        complete = tokens.length == 2 && isInteger(tokens[depth]);
        ArrayList<String> options = new ArrayList<>();
        if (tokens.length == 2 && tokens[1].isEmpty()) {
            options.add("[amt]");
        }
        return options;
    }

    @Override
    public void errorMsg() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* [amt]");
    }

    private static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
