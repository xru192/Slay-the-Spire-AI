package newaimod.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CardInfoCommand extends ConsoleCommand {

    public CardInfoCommand() {
        maxExtraTokens = 1;
        minExtraTokens = 1;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens.length != 2) {
            errorMsg();
            return;
        }
        String input = tokens[depth];
        ArrayList<AbstractCard> Cards = AbstractDungeon.player.hand.group;
        try {
            int cardIndex = Integer.parseInt(input) - 1;
            DevConsole.log("Card ID: " + Cards.get(cardIndex).cardID);
            DevConsole.log("Card Damage: " + Cards.get(cardIndex).damage);
            DevConsole.log("Card Block: " + Cards.get(cardIndex).block);
            DevConsole.log("Card Magic number: " + Cards.get(cardIndex).magicNumber);
        } catch (NumberFormatException e) {
            DevConsole.log("Catch block 1");
            errorMsg();
        } catch (IndexOutOfBoundsException e) {
            DevConsole.log("Catch block 2");
            errorMsg();
        }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        int num_cards = AbstractDungeon.player.hand.size();
        for (int i = 1; i <= num_cards; i++) {
            result.add("" + i);
        }

        return result;
    }


    @Override
    public void errorMsg() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* [card #]");
    }
}
