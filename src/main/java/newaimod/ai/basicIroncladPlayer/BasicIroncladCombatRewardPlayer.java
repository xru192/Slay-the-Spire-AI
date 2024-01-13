package newaimod.ai.basicIroncladPlayer;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.rewards.RewardItem;
import communicationmod.ChoiceScreenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BasicIroncladCombatRewardPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladCombatRewardPlayer.class.getName());

    boolean fresh = true;

    int passedRewards = 0;
    int savedRewardCount = -1;


    void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.COMBAT_REWARD) {
            logger.info("Done: choice type not suitable");
            return;
        }

        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        if (choices.isEmpty()) {
            fresh = true;
            passedRewards = 0;
            savedRewardCount = -1;
            ChoiceScreenUtils.pressConfirmButton();
            logger.info("Done: no rewards left");
        }

        if (savedRewardCount == choices.size() && !fresh) {
            logger.info("We have passed a reward");
            passedRewards += 1;
        }

        logger.info("Choosing between: " + choices);

        for (int i = passedRewards; i < choices.size(); ++i) {
            String choice = choices.get(i);
            switch (choice) {
                case "gold":
                case "stolen_gold":
                case "emerald_key":
                case "sapphire_key":
                    choose(i);
                    fresh = true;
                    return;
                case "potion":
                    String potionName = AbstractDungeon.combatRewardScreen.rewards.get(i).potion.name;
                    boolean full = true;
                    for (AbstractPotion potion : AbstractDungeon.player.potions) {
                        if (potion.getClass() == PotionSlot.class) {
                            full = false;
                            break;
                        }
                    }
                    if (!full) {
                        choose(i);
                        return;
                    } else {
                        passedRewards += 1;
                        logger.info("Not taking potion");
                    }
                    break;
                case "relic":
                    String relicName = AbstractDungeon.combatRewardScreen.rewards.get(i).relic.name;
                    choose(i);
                    fresh = true;
                    return;
                case "card":
                    fresh = false;
                    savedRewardCount = choices.size();
                    choose(i);
                    return;
            }
        }

        fresh = true;
        passedRewards = 0;
        savedRewardCount = -1;
        ChoiceScreenUtils.pressConfirmButton();

    }

    /**
     * Make the specified combat reward choice if it is valid.
     *
     * @param i the 0-based index of the choice to make
     */
    public static void choose(int i) {
        try {
            ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
            logger.info("Making choice: " + choices.get(i));
            RewardItem reward = AbstractDungeon.combatRewardScreen.rewards.get(i);
            reward.isDone = true;
        } catch (Exception e) {
            logger.info("Failed to make choice: " + i + ". Error: " + e.getMessage());
        }
    }
}
