package newaimod.AI.BasicIroncladPlayer;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import newaimod.AI.AbstractShopAutoPlayer;

public class BasicIroncladShopPlayer extends AbstractShopAutoPlayer {
    @Override
    protected Object getPurchase() {
        if (AbstractDungeon.shopScreen.purgeAvailable && AbstractDungeon.player.gold >= ShopScreen.actualPurgeCost) {
            return "purge";
        }
        return null;
    }
}
