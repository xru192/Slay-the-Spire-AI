package newaimod.util.simulator.potions.potions;

import com.megacrit.cardcrawl.potions.FearPotion;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.potions.AbstractSimplePotion;

public final class SimpleFearPotion extends AbstractSimplePotion {
    public static final String ID = FearPotion.POTION_ID;
    public static final boolean targetsOne = true;
    public static final boolean requiresCombat = true;

    public SimpleFearPotion() {
        super(null, ID, targetsOne, requiresCombat);
    }

    @Override
    public void use(SimpleMonster target) {
        target.takeVulnerable(3);
    }

    @Override
    public void useWithEffectDoubled(SimpleMonster target) {
        target.takeVulnerable(6);
    }
}
