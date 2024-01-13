package newaimod.ai.basicIroncladPlayer;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.CursedKey;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import communicationmod.ChoiceScreenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasicIroncladChestPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladChestPlayer.class.getName());

    void execute() {
        boolean isBoss = false;
        AbstractChest chest = null;
        if (AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss) {
            isBoss = true;
            chest = ((TreasureRoomBoss) AbstractDungeon.getCurrRoom()).chest;
        } else if (AbstractDungeon.getCurrRoom() instanceof TreasureRoom) {
            chest = ((TreasureRoom) AbstractDungeon.getCurrRoom()).chest;
        }

        if (chest == null) {
            logger.error("Done: chest does not exist in this room");
            return;
        }

        if (chest.isOpen) {
            // Screen where boss chest is already open
            if (ChoiceScreenUtils.isConfirmButtonAvailable()) {
                ChoiceScreenUtils.pressConfirmButton();
            }
        } else {
            if (AbstractDungeon.player.hasRelic(CursedKey.ID) && !isBoss) {
                logger.info("Skipping Chest");
                ChoiceScreenUtils.pressConfirmButton();
            } else {
                logger.info("Opening chest");
                ChoiceScreenUtils.makeChestRoomChoice(0);
            }
        }
        logger.info("Done");
    }
}
