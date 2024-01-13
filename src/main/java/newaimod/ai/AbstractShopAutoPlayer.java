package newaimod.ai;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import communicationmod.ChoiceScreenUtils;
import communicationmod.patches.MerchantPatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class AbstractShopAutoPlayer {
    public static final Logger logger = LogManager.getLogger(AbstractShopAutoPlayer.class.getName());
    private boolean doneShopping = false;

    public final void execute() {
        logger.info("Executing ShopAI");
        ChoiceScreenUtils.ChoiceType type = ChoiceScreenUtils.getCurrentChoiceType();
        if (type != ChoiceScreenUtils.ChoiceType.SHOP_ROOM && type != ChoiceScreenUtils.ChoiceType.SHOP_SCREEN) {
            logger.info("Done: choice type not suitable");
            return;
        }

        if (type == ChoiceScreenUtils.ChoiceType.SHOP_ROOM) {
            if (doneShopping) {
                logger.info("Done: exiting shop");
                ChoiceScreenUtils.pressConfirmButton();
                doneShopping = false;
                return;
            }

            logger.info("Done: opening shop screen");
            openShop();
            doneShopping = false;
            return;
        }

        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        logger.info("Choosing between: " + choices);
        Object purchase = getPurchase();
        if (purchase == null) {
            doneShopping = true;
            closeShop();
            return;
        }

        choose(purchase);
    }

    protected abstract Object getPurchase();


    /**
     * Purchases the specified item if it is valid.
     *
     * @param o the card/relic/potion to purchase ("purge" for purge purchase)
     */
    protected static void choose(Object o) {
        try {
            String choice = "";
            if (o.equals("purge")) {
                choice = "purge";
            } else if (o instanceof AbstractCard) {
                choice = ((AbstractCard) o).name.toLowerCase();
            } else if (o instanceof StoreRelic) {
                choice = ((StoreRelic) o).relic.name.toLowerCase();
            } else if (o instanceof StorePotion) {
                choice = ((StorePotion) o).potion.name.toLowerCase();
            }
            ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
            logger.info("Making choice: " + choice);
            ChoiceScreenUtils.makeShopScreenChoice(choices.indexOf(choice));
        } catch (Exception e) {
            logger.info("Failed to make choice: " + o.toString() + ". Error: " + e.getMessage());
        }
    }

    /**
     * Opens the shop screen, given that the player is in a shop room.
     */
    private static void openShop() {
        logger.info("Opening shop");
        MerchantPatch.visitMerchant = true;
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.currMapNode.room instanceof ShopRoom) {
            Merchant m = ((ShopRoom) AbstractDungeon.currMapNode.room).merchant;
            m.update();
        }
    }

    /**
     * Closes the shop screen if it is open.
     */
    public static void closeShop() {
        logger.info("Closing shop");
        AbstractDungeon.overlayMenu.cancelButton.hb.clicked = true;
        AbstractDungeon.overlayMenu.update();
    }
}
