package newaimod.commands;

import basemod.devcommands.ConsoleCommand;
import newaimod.NewAIMod;

import java.util.ArrayList;

public class SettingsCommand extends ConsoleCommand {
    public SettingsCommand() {
        maxExtraTokens = 2;
        minExtraTokens = 1;
    }

    @Override
    protected void execute(String[] strings, int i) {
        if (strings.length == 2) {
            switch (strings[1]) {
                case "enable":
                    NewAIMod.myPropertyManager.enabled = true;
                    break;
                case "disable":
                    NewAIMod.myPropertyManager.enabled = false;
                    break;
                case "slow":
                    NewAIMod.WAIT = 60;
                    break;
                case "fast":
                    NewAIMod.WAIT = 5;
                    break;
                default:
                    errorMsg();
            }
            return;
        }

        errorMsg();
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        if (tokens.length == 1) {
            result.add("enable");
            result.add("disable");
            result.add("slow");
            result.add("fast");
        }

        if (tokens[depth].equals("enable") || tokens[depth].equals("disable") || tokens[depth].equals("slow") || tokens[depth].equals("fast")) {
            complete = true;
        }

        return result;
    }
}
