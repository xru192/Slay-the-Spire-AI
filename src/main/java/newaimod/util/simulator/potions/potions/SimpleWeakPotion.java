package newaimod.util.simulator.potions.potions;

import com.megacrit.cardcrawl.potions.WeakenPotion;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.potions.AbstractSimplePotion;

public final class SimpleWeakPotion extends AbstractSimplePotion {
    public static final String ID = WeakenPotion.POTION_ID;
    public static final boolean targetsOne = true;
    public static final boolean requiresCombat = true;

    public SimpleWeakPotion() {
        super(null, ID, targetsOne, requiresCombat);
    }

    @Override
    public void use(SimpleMonster target) {
        target.takeWeak(3);
    }

    @Override
    public void useWithEffectDoubled(SimpleMonster target) {
        target.takeWeak(6);
    }
}
