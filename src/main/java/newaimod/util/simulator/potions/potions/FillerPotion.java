package newaimod.util.simulator.potions.potions;

import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.potions.AbstractSimplePotion;

public final class FillerPotion extends AbstractSimplePotion {
    public static final String ID = "Filler";
    public static final boolean targetsOne = true;
    public static final boolean requiresCombat = true;

    public FillerPotion() {
        super(null, ID, targetsOne, requiresCombat);
    }

    @Override
    public void use(SimpleMonster target) {
        throw new RuntimeException("Cannot use Filler potion.");
    }

    @Override
    public void useWithEffectDoubled(SimpleMonster target) {
        throw new RuntimeException("Cannot use Filler potion.");
    }
}
